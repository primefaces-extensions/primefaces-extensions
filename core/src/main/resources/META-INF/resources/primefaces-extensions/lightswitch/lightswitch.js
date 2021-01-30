/**
 * PrimeFaces Extensions LightSwitch Widget.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
PrimeFaces.widget.ExtLightSwitch = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.selected = cfg.selected;
        this.light = cfg.light;
        this.dark = cfg.dark;
        this.automatic();
    },

    /**
     * Automatically set the theme based on the OS setting and sends the theme to the server side if it has changed.
     */
    automatic : function() {
        var theme = (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)
            ? this.dark : this.light;
        PrimeFaces.changeTheme(theme);
        if (theme != this.selected) {
            var options = { params: [ {name: this.id + "_theme", value: theme} ] };
            options.source = this.id;
            options.process = this.id;
            options.update = this.id;
            options.formId = this.cfg.formId;
            PrimeFaces.ajax.Request.handle(options);
        }
    },

});
