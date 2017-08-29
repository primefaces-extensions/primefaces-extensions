/**
 * @namespace The PrimeFaces Extensions root namespace.
 */
PrimeFacesExt = {

    /**
     * Checks if the FacesServlet is mapped with extension mapping. For example:
     * .jsf/.xhtml.
     * 
     * @author Thomas Andraschko
     * @returns {boolean} If mapped with extension mapping.
     */
    isExtensionMapping : function() {
        if (!PrimeFacesExt.IS_EXTENSION_MAPPING) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
            var primeFacesExtensionsScript = 'primefaces-extensions.js';

            PrimeFacesExt.IS_EXTENSION_MAPPING = scriptURI.charAt(scriptURI.indexOf(primeFacesExtensionsScript)
                    + primeFacesExtensionsScript.length) === '.';
        }

        return PrimeFacesExt.IS_EXTENSION_MAPPING;
    },

    /**
     * Gets the URL extensions of current included resources. For example: jsf
     * or xhtml. This should only be used if extensions mapping is used.
     * 
     * @returns {string} The URL extension.
     */
    getResourceUrlExtension : function() {
        if (!PrimeFacesExt.RESOURCE_URL_EXTENSION) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();

            PrimeFacesExt.RESOURCE_URL_EXTENSION = RegExp('primefaces-extensions.js.([^?]*)').exec(scriptURI)[1];
        }

        return PrimeFacesExt.RESOURCE_URL_EXTENSION;
    },

    /**
     * Gets the resource URI of the current included primefaces-extensions.js.
     * 
     * @author Thomas Andraschko
     * @returns {string} The resource URI.
     */
    getPrimeFacesExtensionsScriptURI : function() {
        if (!PrimeFacesExt.SCRIPT_URI) {
            PrimeFacesExt.SCRIPT_URI = $('script[src*="/' + PrimeFaces.RESOURCE_IDENTIFIER + '/primefaces-extensions.js"]').attr('src');
            if (!PrimeFacesExt.SCRIPT_URI) {
                PrimeFacesExt.SCRIPT_URI = $('script[src*="' + PrimeFaces.RESOURCE_IDENTIFIER + '=primefaces-extensions.js"]').attr('src');
            }
        }

        return PrimeFacesExt.SCRIPT_URI;
    },

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
     * @returns {object} cfg Configuration object with updated localized text
     *          (if any text to given locale were found).
     */
    configureLocale : function(widgetName, cfg) {
        if (PrimeFacesExt.locales && PrimeFacesExt.locales[widgetName] && cfg.locale) {
            var localeSettings = PrimeFacesExt.locales[widgetName][cfg.locale];
            if (localeSettings) {
                for ( var setting in localeSettings) {
                    if (localeSettings.hasOwnProperty(setting)) {
                        cfg[setting] = localeSettings[setting];
                    }
                }
            }
        }

        return cfg;
    },

    /**
     * This function need to be invoked after PrimeFaces changeTheme. It's used
     * to sync canvas and svg components to the current theme.
     * 
     * @author f.strazzullo
     */
    changeTheme : function(newValue) {
        $(document).trigger("PrimeFacesExt.themeChanged", newValue);
    },

    /**
     * The name of the PrimeFaces Extensions resource library.
     * 
     * @author Thomas Andraschko
     * @type {string}
     * @constant
     */
    RESOURCE_LIBRARY : 'primefaces-extensions',

    VERSION : '${project.version}'
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
PrimeFacesExt.locales.TimePicker = {};

/**
 * JavaScript behavior.
 * 
 * @author Thomas Andraschko
 * @constructor
 */
PrimeFacesExt.behavior.Javascript = function(cfg, ext) {

    var params = null;
    if (ext) {
        params = ext.params;
    }

    return cfg.execute.call(this, cfg.source, cfg.event, params, ext);
};

/**
 * Hack to allow the PrimeFacesExt changeTheme to automatically invoked on every
 * theme change
 * 
 * @author f.strazzullo
 */
(function(window) {

    var originalChangeTheme = PrimeFaces.changeTheme;

    PrimeFaces.changeTheme = function(newValue) {
        originalChangeTheme(newValue);
        PrimeFacesExt.changeTheme(newValue);
    }

})(window);
