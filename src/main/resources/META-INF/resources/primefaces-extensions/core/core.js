PrimeFacesExt = {
    /**
     * Gets the extension of the current request URL.
     *
     * @return {String} the URL extensions.
     */
	getRequestUrlExtension : function() {
		return PrimeFacesExt.getUrlExtension(location.href);
	},	
		
    /**
     * Gets the extension of the given URL.
     *
     * @return {String} the URL extensions.
     */
	getUrlExtension : function(url) {
		return (url = url.substr(1 + url.lastIndexOf("/")).split('?')[0]).substr(url.lastIndexOf("."));
	},

    /**
     * Builds a resource URL for given parameters.
     * 
     * @param {String} name: the name of the resource. For example: /core/core.js
     * @param {String} library: the library of the resource. For example: primefaces-extensions
     * @param {String} version: the version of the library. For example: 0.2.0-SNAPSHOT
     * 
     * @return {String} the resource URL.
     */
	getFacesResource : function(name, library, version) {
		var scriptURI = PrimeFacesExt.getCoreJsResourceURI();
        
		scriptURI = scriptURI.replace('/core/core.js', name);
		scriptURI = scriptURI.replace('ln=' + PrimeFacesExt.RESOURCE_LIBRARY, 'ln=' + library);

		var extractedVersion = RegExp('[?&]v=([^&]*)').exec(scriptURI)[1];
		if (version) {
			scriptURI = scriptURI.replace('v=' + extractedVersion, 'v=' + version);
		} else {
			scriptURI = scriptURI.replace('v=' + extractedVersion, 'v=');
		}

        return location.protocol + '//' + location.host + scriptURI;
	},

    /**
     * Gets the version if the current PrimeFaces Extensions library.
     *
     * @return {String} the URL extensions.
     */
	getPrimeFacesExtensionsVersion : function() {
		var scriptURI = PrimeFacesExt.getCoreJsResourceURI();
		return RegExp('[?&]v=([^&]*)').exec(scriptURI)[1];
	},

    /**
     * Builds a resource URL for a PrimeFaces Extensions resource.
     * 
     * @param {String} name: the name of the resource. For example: /core/core.js
     * 
     * @return {String} the resource URL.
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
     * @return {boolean} if mapped with extension mapping.
     */
	isExtensionMapping : function() {
		var scriptURI = PrimeFacesExt.getCoreJsResourceURI();
		var coreJs = 'core.js';
		
		return scriptURI.charAt(scriptURI.indexOf(coreJs) + coreJs.length) === '.';
	},

    /**
     * Gets the resource URI of the current included core.js.
     *
     * @return {String} the resource URI.
     */
	getCoreJsResourceURI : function() {
		return $('script[src*="' + PrimeFacesExt.RESOURCE_IDENTIFIER + '/core/core.js"]').attr('src');
	},

	/**
	 * The JSF resource identifier.
	 *
	 * @constant
	 */
	RESOURCE_IDENTIFIER : '/javax.faces.resource',

	/**
	 * The name of the PrimeFaces Extensions resource library.
	 *
	 * @constant
	 */
	RESOURCE_LIBRARY : 'primefaces-extensions'
};

PrimeFacesExt.behavior = {};
PrimeFacesExt.widget = {};

PrimeFacesExt.behavior.Javascript = function(cfg, ext) {
	return cfg.execute.call(this, cfg.source, cfg.event, cfg.params, ext);
}
