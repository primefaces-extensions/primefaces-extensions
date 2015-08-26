/**
 * @namespace The PrimeFaces Extensions root namespace.
 */
PrimeFacesExt = {
    /**
     * Gets the extension of the current request URL.
     *
     * @author Thomas Andraschko
     * @returns {string} The URL extensions.
     */
    getRequestUrlExtension: function () {
        return PrimeFacesExt.getUrlExtension(location.href);
    },

    /**
     * Gets the extension of the given URL.
     *
     * @author Thomas Andraschko
     * @returns {string} The URL extensions.
     */
    getUrlExtension: function (url) {
        return (url = url.substr(1 + url.lastIndexOf("/")).split('?')[0]).substr(url.lastIndexOf("."));
    },

    /**
     * Builds a resource URL for given parameters.
     *
     * @author Thomas Andraschko
     * @param {string} name The name of the resource. For example: /core/core.js
     * @param {string} library The library of the resource. For example: primefaces-extensions
     * @param {string} version The version of the library. For example: 0.2.0-SNAPSHOT
     * @returns {string} The resource URL.
     */
    getFacesResource: function (name, library, version) {
        var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();

        if (name.indexOf('/') === 0)
        {
            name = name.substring(1, name.length);
        }

        scriptURI = scriptURI.replace('primefaces-extensions.js', name);

        if (PrimeFacesExt.useUncompressedResources) {
            scriptURI = scriptURI.replace('ln=' + PrimeFacesExt.RESOURCE_LIBRARY_UNCOMPRESSED, 'ln=' + library);
        } else {
            scriptURI = scriptURI.replace('ln=' + PrimeFacesExt.RESOURCE_LIBRARY, 'ln=' + library);
        }

        var extractedVersion = RegExp('[?&]v=([^&]*)').exec(scriptURI)[1];
        if (version && version.length > 0) {
            scriptURI = scriptURI.replace('v=' + extractedVersion, 'v=' + version);
        } else {
            scriptURI = scriptURI.replace('v=' + extractedVersion, '');
        }

        var prefix = window.location.protocol + '//' + window.location.host;
        return scriptURI.indexOf(prefix) >= 0 ? scriptURI : prefix + scriptURI;
    },

    /**
     * Gets the version of the current PrimeFaces Extensions library.
     *
     * @author Thomas Andraschko
     * @returns {string} The PrimeFaces Extensions version.
     */
    getPrimeFacesExtensionsVersion: function () {
        if (!PrimeFacesExt.VERSION) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
            var result = RegExp('[?&]v=([^&]*)').exec(scriptURI);
            if (result && result.length > 0) {
                PrimeFacesExt.VERSION = result[1];
            } else {
                PrimeFacesExt.VERSION = '';
            }
        }

        return PrimeFacesExt.VERSION;
    },

    /**
     * Builds a resource URL for a PrimeFaces Extensions resource.
     *
     * @author Thomas Andraschko
     * @returns {string} The resource URL.
     */
    getPrimeFacesExtensionsResource: function (name) {
        var resourceLibrary = PrimeFacesExt.RESOURCE_LIBRARY;
        if (PrimeFacesExt.useUncompressedResources) {
            resourceLibrary = PrimeFacesExt.RESOURCE_LIBRARY_UNCOMPRESSED;
        }

        return PrimeFacesExt.getFacesResource(name, resourceLibrary, PrimeFacesExt.getPrimeFacesExtensionsVersion());
    },

    /**
     * Builds a resource URL for a PrimeFaces Extensions Compressed resource.
     *
     * @author Thomas Andraschko
     * @param {string} name The name of the resource. For example: /core/core.js
     * @returns {string} The resource URL.
     */
    getPrimeFacesExtensionsCompressedResource: function (name) {
        return PrimeFacesExt.getFacesResource(name, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.getPrimeFacesExtensionsVersion());
    },

    /**
     * Checks if the FacesServlet is mapped with extension mapping. For example: .jsf/.xhtml.
     *
     * @author Thomas Andraschko
     * @returns {boolean} If mapped with extension mapping.
     */
    isExtensionMapping: function () {
        if (!PrimeFacesExt.IS_EXTENSION_MAPPING) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
            var primeFacesExtensionsScript = 'primefaces-extensions.js';

            PrimeFacesExt.IS_EXTENSION_MAPPING = scriptURI.charAt(scriptURI.indexOf(primeFacesExtensionsScript) + primeFacesExtensionsScript.length) === '.';
        }

        return PrimeFacesExt.IS_EXTENSION_MAPPING;
    },

    /**
     * Gets the URL extensions of current included resources. For example: jsf/xhtml.
     * This should only be used if extensions mapping is used.
     *
     * @author Thomas Andraschko
     * @returns {string} The URL extension.
     */
    getResourceUrlExtension: function () {
        if (!PrimeFacesExt.RESOURCE_URL_EXTENSION) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();

            PrimeFacesExt.RESOURCE_URL_EXTENSION = RegExp('primefaces-extensions.js.([^?]*)').exec(scriptURI)[1];
        }

        return PrimeFacesExt.RESOURCE_URL_EXTENSION;
    },

    /**
     * Checks if the current included scripts are uncompressed.
     *
     * @author Thomas Andraschko
     * @returns {boolean} If uncompresed resources are used.
     */
    useUncompressedResources: function () {
        if (!PrimeFacesExt.USE_UNCOMPRESSED_RESOURCES) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();

            PrimeFacesExt.USE_UNCOMPRESSED_RESOURCES = scriptURI.indexOf(PrimeFacesExt.RESOURCE_LIBRARY_UNCOMPRESSED) !== -1;
        }

        return PrimeFacesExt.USE_UNCOMPRESSED_RESOURCES;
    },

    /**
     * Gets the resource URI of the current included primefaces-extensions.js.
     *
     * @author Thomas Andraschko
     * @returns {string} The resource URI.
     */
    getPrimeFacesExtensionsScriptURI: function () {
        if (!PrimeFacesExt.SCRIPT_URI) {
            PrimeFacesExt.SCRIPT_URI = $('script[src*="/' + PrimeFacesExt.RESOURCE_IDENTIFIER + '/primefaces-extensions.js"]').attr('src');
            if (!PrimeFacesExt.SCRIPT_URI) {
                PrimeFacesExt.SCRIPT_URI = $('script[src*="' + PrimeFacesExt.RESOURCE_IDENTIFIER + '=primefaces-extensions.js"]').attr('src');
            }
        }

        return PrimeFacesExt.SCRIPT_URI;
    },

    /**
     * Creates a widget and load the required resources if not already available.
     * The .js and .css must has the same name as the widget and must be placed inside a directory with also the name.
     * The file and directory names must be completely in lower case.
     * For example: /imageareaselect/imageareaselect.js.
     *
     * @author Thomas Andraschko
     * @param {string} widgetName The name of the widget. For example: ImageAreaSelect.
     * @param {object} widgetVar The variable in the window object for accessing the widget.
     * @param {object} cfg An object with options.
     * @param {boolean} hasStyleSheet If the css file should be loaded as well.
     */
    cw: function (widgetName, widgetVar, cfg, hasStyleSheet) {
        PrimeFacesExt.createWidget(widgetName, widgetVar, cfg, hasStyleSheet);
    },

    /**
     * Creates a widget and load the required resources if not already available.
     * The .js and .css must has the same name as the widget and must be placed inside a directory with also the name.
     * The file and directory names must be completely in lower case.
     * For example: /imageareaselect/imageareaselect.js.
     *
     * @author Thomas Andraschko
     * @param {string} widgetName The name of the widget. For example: ImageAreaSelect.
     * @param {object} widgetVar The variable in the window object for accessing the widget.
     * @param {object} cfg An object with options.
     * @param {boolean} hasStyleSheet If the css file should be loaded as well.
     */
    createWidget: function (widgetName, widgetVar, cfg, hasStyleSheet) {
        if (PrimeFacesExt.widget[widgetName]) {
            PrimeFacesExt.initWidget(widgetName, widgetVar, cfg);
        } else {
            if (hasStyleSheet) {
                var cssResource = PrimeFacesExt.getPrimeFacesExtensionsResource(widgetName.toLowerCase() + '/' + widgetName.toLowerCase() + '.css');
                $('head').append(cssResource);
            }

            var script = PrimeFacesExt.getPrimeFacesExtensionsResource(widgetName.toLowerCase() + '/' + widgetName.toLowerCase() + '.js');

            //load script
            PrimeFaces.getScript(script, function () {
                setTimeout(function () {
                    PrimeFacesExt.initWidget(widgetName, widgetVar, cfg);
                }, 100);
            });
        }
    },

    /**
     * Creates the widget or calls "refresh" if already available.
     *
     * @author Thomas Andraschko
     * @param {string} widgetName The name of the widget. For example: ImageAreaSelect.
     * @param {string} widgetVar The variable in the window object for accessing the widget.
     * @param {object} cfg An object with options.
     */
    initWidget: function (widgetName, widgetVar, cfg) {
        if (PrimeFaces.widgets[widgetVar]) {
            PrimeFaces.widgets[widgetVar].refresh(cfg);
        } else {
            PrimeFaces.widgets[widgetVar] = new PrimeFacesExt.widget[widgetName](cfg);
            if (PrimeFaces.settings.legacyWidgetNamespace) {
                window[widgetVar] = PrimeFaces.widgets[widgetVar];
            }
        }
    },

    /**
     * Configures component specific localized text by given widget name and locale in configuration object.
     *
     * @author Oleg Varaksin
     * @param {string} widgetName The name of the widget. For example: 'TimePicker'.
     * @param {object} cfg Configuration object as key, value pair. This object should keep current locale in cfg.locale.
     * @returns {object} cfg Configuration object with updated localized text (if any text to given locale were found).
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
     * This function need to be invoked after PrimeFaces changeTheme. It's used to sync canvas and svg components to the current theme (pe:analogClock)
     * @author f.strazzullo
     */
    changeTheme: function (newValue) {
        $(document).trigger("PrimeFacesExt.themeChanged", newValue);
    },

    /**
     * The JSF resource identifier.
     *
     * @author Thomas Andraschko
     * @type {string}
     * @constant
     */
    RESOURCE_IDENTIFIER: 'javax.faces.resource',

    /**
     * The name of the PrimeFaces Extensions resource library.
     *
     * @author Thomas Andraschko
     * @type {string}
     * @constant
     */
    RESOURCE_LIBRARY: 'primefaces-extensions',

    /**
     * The name of the PrimeFaces Extensions Uncompressed resource library.
     *
     * @author Thomas Andraschko
     * @type {string}
     * @constant
     */
    RESOURCE_LIBRARY_UNCOMPRESSED: 'primefaces-extensions-uncompressed'
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
PrimeFacesExt.locales.Timeline = {};

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

/**
 * Hack to allow the PrimeFacesExt changeTheme to automatically invoked on every theme change
 * @author f.strazzullo
 */
(function (window) {

    var originalChangeTheme = PrimeFaces.changeTheme;

    PrimeFaces.changeTheme = function (newValue) {
        originalChangeTheme(newValue);
        PrimeFacesExt.changeTheme(newValue);
    }

})(window);
