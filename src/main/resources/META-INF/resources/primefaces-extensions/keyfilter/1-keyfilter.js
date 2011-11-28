PrimeFacesExt.widget.KeyFilter = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
	
    var target = $(PrimeFaces.escapeClientId(this.cfg.target));

    if (target.is(':input')) {
    	this.applyKeyFilter(target);
    } else {
    	var nestedInput = $(':not(:submit):not(:button):input:visible:enabled:first', target);
    	this.applyKeyFilter(nestedInput);
    }
}

PrimeFacesExt.widget.KeyFilter.prototype.applyKeyFilter = function(input) {
	if (this.cfg.regEx) {
		input.keyfilter(this.cfg.regEx);
	} else if (this.cfg.testFunction) {
		input.keyfilter(this.cfg.testFunction);
	} else if (this.cfg.mask) {
		input.keyfilter($.fn.keyfilter.defaults.masks[this.cfg.mask]);
	}
}
