PrimeFacesExt = {
    /**
     * Gets the extension of the current request URL.
     *
     * @return {string} The URL extensions.
     */
	getRequestUrlExtension : function() {
		return PrimeFacesExt.getUrlExtension(location.href);
	},	
		
    /**
     * Gets the extension of the given URL.
     *
     * @return {string} The URL extensions.
     */
	getUrlExtension : function(url) {
		return (url = url.substr(1 + url.lastIndexOf("/")).split('?')[0]).substr(url.lastIndexOf("."));
	},

    /**
     * Builds a resource URL for given parameters.
     * 
     * @param {string} name The name of the resource. For example: /core/core.js
     * @param {string} library The library of the resource. For example: primefaces-extensions
     * @param {string} version The version of the library. For example: 0.2.0-SNAPSHOT
     * 
     * @return {string} The resource URL.
     */
	getFacesResource : function(name, library, version) {
		var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
        
		scriptURI = scriptURI.replace('/primefaces-extensions.js', name);
		scriptURI = scriptURI.replace('ln=' + PrimeFacesExt.RESOURCE_LIBRARY, 'ln=' + library);

		var extractedVersion = RegExp('[?&]v=([^&]*)').exec(scriptURI)[1];
		if (version) {
			scriptURI = scriptURI.replace('v=' + extractedVersion, 'v=' + version);
		} else {
			scriptURI = scriptURI.replace('v=' + extractedVersion, '');
		}

        return location.protocol + '//' + location.host + scriptURI;
	},

    /**
     * Gets the version of the current PrimeFaces Extensions library.
     *
     * @return {string} The PrimeFaces Extensions version.
     */
	getPrimeFacesExtensionsVersion : function() {
		if (!PrimeFacesExt.VERSION) {
			var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
			PrimeFacesExt.VERSION = RegExp('[?&]v=([^&]*)').exec(scriptURI)[1];
		}

		return PrimeFacesExt.VERSION;
	},

    /**
     * Builds a resource URL for a PrimeFaces Extensions resource.
     * 
     * @param {string} name The name of the resource. For example: /core/core.js
     * 
     * @return {string} The resource URL.
     */
	getPrimeFacesExtensionsResource : function(name) {
		return PrimeFacesExt.getFacesResource(
				name,
				PrimeFacesExt.RESOURCE_LIBRARY,
				PrimeFacesExt.getPrimeFacesExtensionsVersion());
	},

    /**
     * Checks if the FacesServlet is mapped with extension mapping. For example: .jsf/.xhtml.
     *
     * @return {boolean} If mapped with extension mapping.
     */
	isExtensionMapping : function() {
		if (!PrimeFacesExt.IS_EXTENSION_MAPPING) {
			var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
			var primeFacesExtensionsScript = 'primefaces-extensions.js';

			PrimeFacesExt.IS_EXTENSION_MAPPING = scriptURI.charAt(scriptURI.indexOf(primeFacesExtensionsScript) + primeFacesExtensionsScript.length) === '.';
		}

		return PrimeFacesExt.IS_EXTENSION_MAPPING;
	},

    /**
     * Gets the resource URI of the current included primefaces-extensions.js.
     *
     * @return {string} The resource URI.
     * @protected
     */
	getPrimeFacesExtensionsScriptURI : function() {
		if (!PrimeFacesExt.SCRIPT_URI) {
			PrimeFacesExt.SCRIPT_URI = $('script[src*="' + PrimeFacesExt.RESOURCE_IDENTIFIER + '/primefaces-extensions.js"]').attr('src');
		}

		return PrimeFacesExt.SCRIPT_URI;
	},

	/**
	 * Load a JavaScript file from the server using a GET HTTP request, then execute it.
	 * 
	 * @param {string} url A string containing the URL to which the request is sent.
	 * @param {function} callback A callback function that is executed if the request succeeds.
	 * @param {string} cache Appends a unique timestamp if false.
	 */
	getScript : function(url, callback, cache) {
		$.ajax({
				type: "GET",
				url: url,
				success: callback,
				dataType: "script",
				cache: cache
		});
	},

	/**
	 * The JSF resource identifier.
	 *
	 * @type {string}
	 * @constant
	 */
	RESOURCE_IDENTIFIER : '/javax.faces.resource',

	/**
	 * The name of the PrimeFaces Extensions resource library.
	 *
	 * @type {string}
	 * @constant
	 */
	RESOURCE_LIBRARY : 'primefaces-extensions'
};

PrimeFacesExt.behavior = {};
PrimeFacesExt.widget = {};

PrimeFacesExt.behavior.Javascript = function(cfg, ext) {
	return cfg.execute.call(this, cfg.source, cfg.event, cfg.params, ext);
}
