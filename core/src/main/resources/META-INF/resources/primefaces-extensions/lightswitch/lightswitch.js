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
        if (cfg.automatic) {
            this.automatic();
        }
    },

    /**
     * Automatically set the theme based on the OS setting.
     */
    automatic : function() {
        this.changeTheme((window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)
            ? this.cfg.dark : this.cfg.light);
    },

    /**
     * Change to the light theme.
     */
    light : function() {
        this.changeTheme(this.cfg.light);
    },

    /**
     * Change to the dark theme.
     */
    dark : function() {
        this.changeTheme(this.cfg.dark);
    },

    /**
     * Toggle between the light and dark theme.
     */
    toggle : function() {
        this.changeTheme(this.selected === this.cfg.dark ? this.cfg.light : this.cfg.dark);
    },

    /**
     * Change the theme to the one provided, if it is not the selected theme, and sends the theme to the server side.
     * @param {string} theme to change to.
     */
    changeTheme : function(theme) {
        if (theme !== this.selected) {
            var options = { params: [ {name: this.id + "_theme", value: theme} ] };
            if (this.hasBehavior('switch')) {
              this.callBehavior('switch', options);
              PrimeFaces.changeTheme(theme);
            }
            else {
              options.source = this.id;
              options.process = this.id;
              options.update = this.id;
              options.formId = this.cfg.formId;
              PrimeFaces.ajax.Request.handle(options);
            }
            this.selected = theme;
        }
    },

});
