PrimeFacesExt = {};
PrimeFacesExt.behavior = {};
PrimeFacesExt.widget = {};


PrimeFacesExt.behavior.Javascript = function(cfg, ext) {
	return cfg.execute.call(this, cfg.source, cfg.event, cfg.params, ext);
}
