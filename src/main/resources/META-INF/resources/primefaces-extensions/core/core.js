PrimeFacesExt = {
	getUrlExtension : function(url) {
		return (url = url.substr(1 + url.lastIndexOf("/")).split('?')[0]).substr(url.lastIndexOf("."));
	},

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
	
	getPrimeFacesExtensionsVersion : function() {
		var scriptURI = PrimeFacesExt.getCoreJsResourceURI();
		return RegExp('[?&]v=([^&]*)').exec(scriptURI)[1];
	},

	getPrimeFacesExtensionsResource : function(name) {
		return PrimeFacesExt.getFacesResource(
				name,
				PrimeFacesExt.RESOURCE_LIBRARY,
				PrimeFacesExt.getPrimeFacesExtensionsVersion());
	},
	
	isExtensionMapping : function() {
		var scriptURI = PrimeFacesExt.getCoreJsResourceURI();
		var coreJs = 'core.js';
		
		return scriptURI.charAt(scriptURI.indexOf(coreJs) + coreJs.length) === '.';
	},
	
	getExtensionMapping : function() {
		var scriptURI = PrimeFacesExt.getCoreJsResourceURI();
		
		var coreJs = 'core.js';
		var coreJsPosition = scriptURI.indexOf(coreJs);
		var questionMarkPosition = scriptURI.indexOf('?');

		return scriptURI.substring(coreJsPosition + coreJs.length, questionMarkPosition);
	},
	
	getCoreJsResourceURI : function() {
		return $('script[src*="' + PrimeFacesExt.RESOURCE_IDENTIFIER + '/core/core.js"]').attr('src');
	},
	
	RESOURCE_IDENTIFIER : '/javax.faces.resource',
	RESOURCE_LIBRARY : 'primefaces-extensions'
};

PrimeFacesExt.behavior = {};
PrimeFacesExt.widget = {};

PrimeFacesExt.behavior.Javascript = function(cfg, ext) {
	return cfg.execute.call(this, cfg.source, cfg.event, cfg.params, ext);
}
