/**
 * PrimeFaces Extensions ImageAreaSelect Widget.
 *
 * @author Thomas Andraschko
 */
PrimeFacesExt.widget.ImageAreaSelect = PrimeFaces.widget.BaseWidget.extend({
	
	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
		this.id = cfg.id;
		this.cfg = cfg;

		this.options = {};
		this.options.instance = true,
		this.options.classPrefix = 'pe-imgageareaselect';
	
		if (this.cfg.aspectRatio) {
			this.options.aspectRatio = this.cfg.aspectRatio;
		}
		if (this.cfg.autoHide) {
			this.options.autoHide = this.cfg.autoHide;
		}
		if (this.cfg.fadeSpeed) {
			this.options.fadeSpeed = this.cfg.fadeSpeed;
		}
		if (this.cfg.handles) {
			this.options.handles = this.cfg.handles;
		}
		if (this.cfg.hide) {
			this.options.hide = this.cfg.hide;
		}
		if (this.cfg.imageHeight) {
			this.options.imageHeight = this.cfg.imageHeight;
		}
		if (this.cfg.imageWidth) {
			this.options.imageWidth = this.cfg.imageWidth;
		}
		if (this.cfg.movable) {
			this.options.movable = this.cfg.movable;
		}
		if (this.cfg.persistent) {
			this.options.persistent = this.cfg.persistent;
		}
		if (this.cfg.resizable) {
			this.options.resizable = this.cfg.resizable;
		}
		if (this.cfg.show) {
			this.options.show = this.cfg.show;
		}
		if (this.cfg.parentSelector) {
			this.options.parent = this.cfg.parentSelector;
		}
		if (this.cfg.keyboardSupport) {
			this.options.keys = this.cfg.keyboardSupport;
		}
	
		this.bindSelectStartCallback();
		this.bindSelectChangeCallback();
		this.bindSelectEndCallback();
	
		this.instance = PrimeFaces.Expressions.resolveComponentsAsSelector(this.cfg.target).imgAreaSelect(this.options);
		
		this.removeScriptElement(this.id);
	},

	/**
	 * Binds the selectEnd callback.
	 *
	 * @private
	 */
	bindSelectEndCallback : function() {
		if (this.cfg.behaviors) {
			var selectEndCallback = this.cfg.behaviors['selectEnd'];
		    if (selectEndCallback) {
				this.options.onSelectEnd = $.proxy(function(img, selection) {
			    	var ext = {};
	
			    	this.fillSelectEventsParameter(img, selection, ext);
	
			    	selectEndCallback.call(this, null, ext);
			    }, this);
		    }
		}
	},

	/**
	 * Binds the selectStart callback.
	 *
	 * @private
	 */
	bindSelectStartCallback : function() {
		if (this.cfg.behaviors) {
			var selectStartCallback = this.cfg.behaviors['selectStart'];
		    if (selectStartCallback) {
				this.options.onSelectStart = $.proxy(function(img, selection) {
			    	var ext = {};
			
			    	this.fillSelectEventsParameter(img, selection, ext);
	
			    	selectStartCallback.call(this, null, ext);
				}, this);
		    }
		}
	},

	/**
	 * Binds the selectChange callback.
	 *
	 * @private
	 */
	bindSelectChangeCallback : function() {
		if (this.cfg.behaviors) {
			var selectChangeCallback = this.cfg.behaviors['selectChange'];
		    if (selectChangeCallback) {
				this.options.onSelectChange = $.proxy(function(img, selection) {
			    	var ext = {};
			
			    	this.fillSelectEventsParameter(img, selection, ext);
	
			    	selectChangeCallback.call(this, null, ext);
				}, this);
		    }
		}
	},

	/**
	 * Fills the required parameters.
	 *
	 * @param {object} img The img object from the imgareaselect plugin.
	 * @param {object} selection The selection object from the imgareaselect plugin.
	 * @param {object} ext The AJAX extensions object.
	 * @private
	 */
	fillSelectEventsParameter : function(img, selection, ext) {
		ext.params = [
		              { name: this.id + '_x1', value: selection.x1 },
		              { name: this.id + '_x2', value: selection.x2 },
		              { name: this.id + '_y1', value: selection.y1 },
		              { name: this.id + '_y2', value: selection.y2 },
		              { name: this.id + '_width', value: selection.width },
		              { name: this.id + '_height', value: selection.height },
		              { name: this.id + '_imgSrc', value: img.src },
		              { name: this.id + '_imgHeight', value: img.height },
		              { name: this.id + '_imgWidth', value: img.width }
        ];
	},

	/**
	 * Updates the widget.
	 */
	update : function() {
		this.instance.update();
	},

	/**
	 * Reloads the widget.
	 */
	reload : function() {
		this.setOptions({remove: true});
		this.update();
		this.instance = PrimeFaces.Expressions.resolveComponentsAsSelector(this.cfg.target).imgAreaSelect(this.options);
	},

	/**
	 * Cancel the current selection.
	 * This method hides the selection/outer area, 
	 * so no call to update() is necessary (as opposed to setSelection()). 
	 */
	cancelSelection : function(options) {
		this.instance.cancelSelection();
	},

	/**
	 * Get the current selection.
	 *
	 * @returns {object} An object containing the selection.
	 */
	getSelection : function() {
		return this.instance.getSelection();
	},

	/**
	 * Set the current selection.
	 * This method only sets the internal representation of selection in the plugin instance, 
	 * it does not update the visual interface. 
	 * If you want the new selection to be shown, call update() right after setSelection(). 
	 * Also make sure that the show option is set to true. 
	 *
	 * @param {int} x1 X coordinate of the top left corner of the selection area.
	 * @param {int} y1 Y coordinate of the top left corner of the selection area.
	 * @param {int} x2 X coordinate of the bottom right corner of the selection area.
	 * @param {int} y2 Y coordinate of the bottom right corner of the selection area.
	 */
	setSelection : function(x1, y1, x2, y2) {
		this.instance.setSelection(x1, y1, x2, y2);
	},

	/**
	 * Get current options.
	 *
	 * @returns {object} An object containing the set of options currently in use.
	 */
	getOptions : function() {
		return this.instance.getOptions();
	},

	/**
	 * Set the plugin options.
	 * This method only sets the internal representation of selection in the plugin instance, 
	 * it does not update the visual interface.
	 * If you want the new selection to be shown, call update() right after setSelection(). 
	 * Also make sure that the show option is set to true. 
	 *
	 * @param {object} options The options for the widget.
	 */
	setOptions : function(options) {
		this.instance.setOptions(options);
	}
});
