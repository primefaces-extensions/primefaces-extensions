/**
 * PrimeFaces Extensions Calculator Widget.
 * 
 * @author Melloware info@melloware.com
 * @since 6.1
 */
PrimeFaces.widget.ExtCalculator = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 * 
	 * @param {object}
	 *            cfg The widget configuration.
	 */
	init : function(cfg) {
		this._super(cfg);
		this.id = cfg.id;
		this.cfg = cfg;
		this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target);

		if (this.target.is(':input')) {
			this._applyCalculator(this.target, cfg);
		} else {
			var nestedInput = $(':not(:submit):not(:button):input:visible:enabled:first', this.target);
			this._applyCalculator(nestedInput, cfg);
		}
	},

	/**
	 * Applies the calculator to the given jQuery selector object. Delete
	 * previous calculator to support ajax updates and create a new one.
	 * 
	 * @param {object}
	 *            input A jQuery selector object.
	 * @param {object}
	 *            cfg The widget configuration.
	 * @private
	 */
	_applyCalculator : function(input, cfg) {
		this.input = input;
		
		// do not attach calculator if field is readonly
		if (this.input[0].readOnly) {
            return;
        }
		
		var layout = cfg.layout;
		
		if (layout) {
			if (layout === 'standard') {
				// Standard calculator layout with simple operations
				cfg.layout = $.calculator.standardLayout; 
			} else if (layout === 'scientific') {
				// Extended calculator with common scientific operations
				cfg.layout = $.calculator.scientificLayout;
			} else {
			    // custom layout of buttons
			    cfg.layout = layout.split(',');
			}
		}
		
		// make a copy of the cfg
		var opts={};
		Object.keys(cfg).forEach((key) => opts[key] = cfg[key]);
		
		// set language
		if (opts.locale) {
			// using jQuery combine cfg + local into one JSON object
			$.extend(opts, $.calculator.regionalOptions[cfg.locale], cfg);
		}
		
		// register callback for onUse so InputNumber can be set correctly
		opts.onUse = function(value, inst) { 
			var id = inst._input.attr('id');
			if (id) {
				// PF InputNumber must call custom setValue() function
				if (id.endsWith("_input")) {
					id = id.replace("_input", "");
					var widget = PrimeFacesExt.getWidgetVarById(id);
					if (widget) {
						widget.setValue(value);
					} else {
						PrimeFaces.error("Widget not found for id: " + id);
					}
				} else {
					// just set the value in a regular input
					inst._input.val(value);
				}
				
			}
	    }
		
		// create the calculator
		input.calculator('destroy').calculator(opts);
	},
	
	show : function() {
		if (this.input) {
			this.input.calculator('show') // Show the calculator
		}
	},

	hide : function() {
		if (this.input) {
			this.input.calculator('hide') // Hide the calculator
		}
	},

	enable : function() {
		if (this.input) {
			this.input.calculator('enable') // Enable the calculator
		}
	},
	
	disable : function() {
		if (this.input) {
			this.input.calculator('disable') // Disable the calculator
		}
	},
	
	isDisabled : function() {
	    // Is the calculator disabled?
		var disabled = false;
		if (this.input) {
			disabled = this.input.calculator('isDisabled') 
		}
		return disabled;
	},
	
	destroy : function() {
		if (this.input) {
			this.input.calculator('destroy');
		}
	}
});

/**
 * Global calculator defaults
 */
// set language to US English
$.calculator.setDefaults($.calculator.regionalOptions['']);
$.calculator.setDefaults({
	useThemeRoller: true, // True to add ThemeRoller classes
	buttonText: '.', // Display text for trigger button
	duration: 'fast', // Duration of display/closure
	isRTL: false // True if right-to-left language, false if left-to-right
});

