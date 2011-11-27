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
		this.instance = input.keyfilter(this.cfg.regEx);
	} else if (this.cfg.testFunction) {
		this.instance = input.keyfilter(this.cfg.testFunction)
	} else if (this.cfg.mask) {
		input.addClass('mask-' + this.cfg.mask);
		this.instance = input.keyfilter();
	}
}
