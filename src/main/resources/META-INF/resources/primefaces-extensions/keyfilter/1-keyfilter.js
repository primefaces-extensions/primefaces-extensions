/**
 * PrimeFaces Extensions KeyFilter Widget.
 *
 * @author Thomas Andraschko
 * @constructor
 */
PrimeFacesExt.widget.KeyFilter = function(cfg) {
	this.cfg = cfg;
    var target = $(this.cfg.target);

    if (target.is(':input')) {
    	this.applyKeyFilter(target);
    } else {
    	var nestedInput = $(':not(:submit):not(:button):input:visible:enabled:first', target);
    	this.applyKeyFilter(nestedInput);
    }
}

/**
 * Applies the keyFilter to the given jQuery selector object.
 * 
 * @author Thomas Andraschko
 * @param {object} input A jQuery selector object.
 * @private
 */
PrimeFacesExt.widget.KeyFilter.prototype.applyKeyFilter = function(input) {
	if (this.cfg.regEx) {
		input.keyfilter(this.cfg.regEx);
	} else if (this.cfg.testFunction) {
		input.keyfilter(this.cfg.testFunction);
	} else if (this.cfg.mask) {
		input.keyfilter($.fn.keyfilter.defaults.masks[this.cfg.mask]);
	}
}
