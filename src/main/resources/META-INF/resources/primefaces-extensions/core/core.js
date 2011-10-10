PrimeFacesExt = {
	getUrlExtension : function(url) {
		return (url = url.substr(1 + url.lastIndexOf("/")).split('?')[0]).substr(url.lastIndexOf("."));
	},

	RESOURCE_IDENTIFIER : '/javax.faces.resource',
	RESOURCE_LIBRARY : 'primefaces-extensions',
	LIBRARY_VERSION : '0.2-SNAPSHOT'
};

PrimeFacesExt.behavior = {};
PrimeFacesExt.widget = {};

PrimeFacesExt.behavior.Javascript = function(cfg, ext) {
	return cfg.execute.call(this, cfg.source, cfg.event, cfg.params, ext);
}
