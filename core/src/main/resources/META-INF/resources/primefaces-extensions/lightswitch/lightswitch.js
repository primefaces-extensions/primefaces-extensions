/**
 * PrimeFaces Extensions LightSwitch Widget.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
PrimeFaces.widget.ExtLightSwitch = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object}
     *        cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.formId = $(PrimeFaces.escapeClientId(this.cfg.parent)).closest('form').attr('id');
        this.selected = cfg.selected;
        if (cfg.automatic) {
            this.automatic();
        }
    }

    /**
     * Automatically set the theme based on the OS setting.
     */
    automatic() {
        this.changeTheme(PrimeFaces.env.preferredColorSchemeDark ? this.cfg.dark : this.cfg.light);
    }

    /**
     * Change to the light theme.
     */
    light() {
        this.changeTheme(this.cfg.light);
    }

    /**
     * Change to the dark theme.
     */
    dark() {
        this.changeTheme(this.cfg.dark);
    }

    /**
     * Toggle between the light and dark theme.
     */
    toggle() {
        this.changeTheme(this.selected === this.cfg.dark ? this.cfg.light : this.cfg.dark);
    }

    /**
     * Change the theme to the one provided, if it is not the selected theme, and sends the theme to the server side.
     * @param {string} theme to change to.
     */
    changeTheme(theme) {
        var $this = this;
        if (theme !== this.selected) {
            this.selected = theme;
            var options = {params: [{name: this.id + '_theme', value: theme}]};
            if (this.hasBehavior('switch')) {
                this.callBehavior('switch', options);
                PrimeFacesExt.changeTheme(theme);
            } else {
                options.source = this.id;
                options.process = this.id;
                options.update = this.id;
                options.formId = this.formId;
                options.oncomplete = function () {
                    PrimeFacesExt.changeTheme(theme);
                }
                PrimeFaces.ajax.Request.handle(options);
            }
        }
    }

};
