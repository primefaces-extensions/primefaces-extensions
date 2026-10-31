/**
 * PrimeFaces Extensions LightSwitch Widget.
 *
 * The PrimeFaces 17 design token themes support light and dark mode, which is selected by the CSS `color-scheme` of
 * the page. This widget switches the `color-scheme` between `light` and `dark` and optionally also changes the
 * theme (the `light` / `dark` options) for applications that use a different theme per mode.
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
        this.selected = this.normalize(cfg.selected);
        if (cfg.automatic) {
            this.automatic();
        } else if (this.selected) {
            this.applyColorScheme(this.selected);
        }
    }

    /**
     * Automatically set the color scheme based on the OS setting.
     */
    automatic() {
        this.changeColorScheme(PrimeFaces.env.getOSPreferredColorScheme());
    }

    /**
     * Change to the light color scheme.
     */
    light() {
        this.changeColorScheme('light');
    }

    /**
     * Change to the dark color scheme.
     */
    dark() {
        this.changeColorScheme('dark');
    }

    /**
     * Toggle between the light and dark color scheme.
     */
    toggle() {
        this.changeColorScheme(this.isDark() ? 'light' : 'dark');
    }

    /**
     * @returns {boolean} `true` if the dark color scheme is currently active.
     */
    isDark() {
        return (this.selected || PrimeFaces.env.getThemeContrast()) === 'dark';
    }

    /**
     * Change the color scheme to the one provided, if it is not the selected one, and sends it to the server side.
     * @param {string} scheme `light` or `dark`.
     */
    changeColorScheme(scheme) {
        var $this = this;
        scheme = this.normalize(scheme);
        if (!scheme) {
            return;
        }

        // apply immediately, also when it is already selected (e.g. after a full page load)
        this.applyColorScheme(scheme);

        if (scheme !== this.selected) {
            this.selected = scheme;
            var options = {params: [{name: this.id + '_scheme', value: scheme}]};
            if (this.hasBehavior('switch')) {
                this.callBehavior('switch', options);
            } else {
                options.source = this.id;
                options.process = this.id;
                options.update = this.id;
                options.formId = this.formId;
                PrimeFaces.ajax.Request.handle(options);
            }
        }
    }

    /**
     * Applies the color scheme on the page and changes the theme if one is configured for the scheme.
     * @param {string} scheme `light` or `dark`.
     */
    applyColorScheme(scheme) {
        document.documentElement.style.colorScheme = scheme;
        var theme = scheme === 'dark' ? this.cfg.dark : this.cfg.light;
        if (theme && theme !== PrimeFaces.getTheme()) {
            PrimeFacesExt.changeTheme(theme);
        }
    }

    /**
     * @param {string} scheme the scheme to normalize.
     * @returns {string|undefined} `light`, `dark` or `undefined` for anything else.
     */
    normalize(scheme) {
        if (scheme === 'light' || scheme === 'dark') {
            return scheme;
        }
        return undefined;
    }

    // kept for backwards compatibility with PrimeFaces Extensions < 17
    /**
     * @deprecated use {@link changeColorScheme}
     * @param {string} scheme `light` or `dark`.
     */
    changeTheme(scheme) {
        this.changeColorScheme(scheme);
    }

};
