PrimeFaces.Extensions = {};
PrimeFaces.Extensions.behavior = {};
PrimeFaces.Extensions.widget = {};


PrimeFaces.Extensions.behavior.Javascript = function(cfg, ext) {
	return cfg.execute.call(this, cfg.source, cfg.event, cfg.params, ext);
}
