/**
 * @namespace The PrimeFaces Extensions root namespace.
 */
PrimeFacesExt = {

    /**
     * Configures component specific localized text by given widget name and
     * locale in configuration object.
     *
     * @author Oleg Varaksin
     * @param {string}
     *        widgetName The name of the widget. For example: 'TimePicker'.
     * @param {object}
     *        cfg Configuration object as key, value pair. This object should
     *        keep current locale in cfg.locale.
     * @returns {object} cfg Configuration object with updated localized text (if
     *          any text to given locale were found).
     */
    configureLocale: function (widgetName, cfg) {
        if (PrimeFacesExt.locales && PrimeFacesExt.locales[widgetName] && cfg.locale) {
            var localeSettings = PrimeFacesExt.locales[widgetName][cfg.locale];
            if (localeSettings) {
                for (var setting in localeSettings) {
                    if (localeSettings.hasOwnProperty(setting)) {
                        cfg[setting] = localeSettings[setting];
                    }
                }
            }
        }

        return cfg;
    },

    /**
     * The name of the PrimeFaces Extensions resource library.
     *
     * @author Thomas Andraschko
     * @type {string}
     * @constant
     */
    RESOURCE_LIBRARY: 'primefaces-extensions',

    VERSION: '${project.version}'
};

/**
 * @namespace Namespace for behaviors.
 */
PrimeFacesExt.behavior = {};

/**
 * @namespace Namespace for widgets.
 */
PrimeFacesExt.widget = {};

/**
 * @namespace Namespace for localization.
 */
PrimeFacesExt.locales = {};

/**
 * @namespace Namespaces for components with localized text.
 */
PrimeFacesExt.locales.TimeAgo = {};
PrimeFacesExt.locales.TimePicker = {};

/**
 * JavaScript behavior.
 *
 * @author Thomas Andraschko
 * @constructor
 */
PrimeFacesExt.behavior.Javascript = function (cfg, ext) {

    var params = null;
    if (ext) {
        params = ext.params;
    }

    return cfg.execute.call(this, cfg.source, cfg.event, params, ext);
};
