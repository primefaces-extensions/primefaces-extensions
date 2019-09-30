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
         var scriptURI = PrimeFacesExt.getResourceScriptURI();
         var scriptName = PrimeFacesExt.getResourceScriptName(scriptURI);

         PrimeFacesExt.IS_EXTENSION_MAPPING = scriptURI.charAt(scriptURI.indexOf(scriptName) + scriptName.length) === '.';
      }

      return PrimeFacesExt.IS_EXTENSION_MAPPING;
   },

   /**
    * Gets the URL extensions of current included resources. For example: jsf or
    * xhtml. This should only be used if extensions mapping is used.
    *
    * @returns {string} The URL extension.
    */
   getResourceUrlExtension : function() {
      if (!PrimeFacesExt.RESOURCE_URL_EXTENSION) {
         var scriptURI = PrimeFacesExt.getResourceScriptURI();
         var scriptName = PrimeFacesExt.getResourceScriptName(scriptURI);
         PrimeFacesExt.RESOURCE_URL_EXTENSION = RegExp(scriptName + '.([^?]*)').exec(scriptURI)[1];
      }

      return PrimeFacesExt.RESOURCE_URL_EXTENSION;
   },

   /**
    * For a URI parses out the name of the script like primefaces-extensions.js
    *
    * @param the
    *        URI of the script
    * @returns {string} The script name.
    */
   getResourceScriptName : function(scriptURI) {
      if (!PrimeFacesExt.SCRIPT_NAME) {
         // find script...normal is '/core.js' and portlets are '=core.js'
         var scriptRegex = new RegExp('\/?' + PrimeFaces.RESOURCE_IDENTIFIER + '(\/|=)(.*?)\.js');

         // find script to replace e.g. 'core.js'
         PrimeFacesExt.SCRIPT_NAME = scriptRegex.exec(scriptURI)[2] + '.js';
      }

      return PrimeFacesExt.SCRIPT_NAME;
   },

   /**
    * Gets the resource URI of any Javascript JS file served as a JSF resource.
    *
    * @author Thomas Andraschko
    * @returns {string} The resource URI.
    */
   getResourceScriptURI : function() {
      if (!PrimeFacesExt.SCRIPT_URI) {
         // GitHub #601 maybe using OmniFaces CombinedResourceHandler
         PrimeFacesExt.SCRIPT_URI =
            $('script[src*="/' + PrimeFaces.RESOURCE_IDENTIFIER + '/"]').first().attr('src');

         // portlet
         if (!PrimeFacesExt.SCRIPT_URI) {
            PrimeFacesExt.SCRIPT_URI =
               $('script[src*="' + PrimeFaces.RESOURCE_IDENTIFIER + '="]').first().attr('src');
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
    * @returns {object} cfg Configuration object with updated localized text (if
    *          any text to given locale were found).
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

   getScript: function(url, callback) {
       $.ajax({
           type: "GET",
           url: url,
           success: callback,
           dataType: "script",
           cache: true,
           async: true
       });
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
PrimeFacesExt.locales.TimeAgo = {};
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
