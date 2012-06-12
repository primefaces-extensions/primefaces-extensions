/**
 * PrimeFaces Extensions KeyFilter Widget.
 *
 * @author Thomas Andraschko
 */
PrimeFacesExt.widget.KeyFilter = PrimeFaces.widget.BaseWidget.extend({
	
	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
		this.id = cfg.id;
		this.cfg = cfg;
	    this.target = $(this.cfg.target);

	    if (this.target.is(':input')) {
	    	this.applyKeyFilter(this.target);
	    } else {
	    	var nestedInput = $(':not(:submit):not(:button):input:visible:enabled:first', this.target);
	    	this.applyKeyFilter(nestedInput);
	    }
	    
	    PrimeFacesExt.removeWidgetScript(this.id);
	},

	/**
	 * Applies the keyFilter to the given jQuery selector object.
	 * 
	 * @param {object} input A jQuery selector object.
	 * @private
	 */
	applyKeyFilter : function(input) {
		if (this.cfg.regEx) {
			input.keyfilter(this.cfg.regEx);
		} else if (this.cfg.testFunction) {
			input.keyfilter(this.cfg.testFunction);
		} else if (this.cfg.mask) {
			input.keyfilter($.fn.keyfilter.defaults.masks[this.cfg.mask]);
		}

		//disable paste
		input.bind('paste', function(e) {
			e.preventDefault();
		});
	}
});
