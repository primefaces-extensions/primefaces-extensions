PrimeFacesExt.widget.KeyFilter = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
	
    var target = $(PrimeFaces.escapeClientId(this.cfg.target));

    if (target.is(':input')) {
    	if (this.cfg.regEx) {
    		this.instance = target.keyfilter(this.cfg.regEx);
    	}
    } else {
    	var nestedInput = $(':not(:submit):not(:button):input:visible:enabled:first', target);
    	if (this.cfg.regEx) {
    		this.instance = nestedInput.keyfilter(this.cfg.regEx);
    	}
    }
}
