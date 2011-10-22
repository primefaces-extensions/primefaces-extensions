/**
 * PrimeFaces Extensions ImageAreaSelect Widget.
 *
 * @constructor
 */
PrimeFacesExt.widget.ImageAreaSelect = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;

	this.options = {};
	this.options.instance = true,
	this.options.classPrefix = 'ui-imgageareaselect';

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
	if (this.cfg.parent) {
		this.options.parent = $(PrimeFaces.escapeClientId(this.cfg.parent));
	}
	if (this.cfg.keyboardSupport) {
		this.options.keys = this.cfg.keyboardSupport;
	}

	this.bindSelectCallback();

	this.instance = $(PrimeFaces.escapeClientId(this.cfg.target)).imgAreaSelect(this.options);
}

/**
 * Binds the select callback.
 *
 * @protected
 */
PrimeFacesExt.widget.ImageAreaSelect.prototype.bindSelectCallback = function() {
	if (this.cfg.behaviors) {
		var selectCallback = this.cfg.behaviors['select'];
	    if (selectCallback) {
	    	var _self = this;
	   
			this.options.onSelectEnd = function (img, selection) {
		    	var ext = {
		    			params: {}
		    	};
		
		    	ext.params[_self.id + '_x1'] = selection.x1;
		    	ext.params[_self.id + '_x2'] = selection.x2;
		    	ext.params[_self.id + '_y1'] = selection.y1;
		    	ext.params[_self.id + '_y2'] = selection.y2;
		    	ext.params[_self.id + '_width'] = selection.width;
		    	ext.params[_self.id + '_height'] = selection.height;
		    	ext.params[_self.id + '_imgSrc'] = img.src;
		    	ext.params[_self.id + '_imgHeight'] = img.height;
		    	ext.params[_self.id + '_imgWidth'] = img.width;

		    	selectCallback.call(_self, null, ext);
		    }
	    }
	}
}

/**
 * Updates the widget with the given options.
 * 
 * @param {array} options The options for the widget.
 */
PrimeFacesExt.widget.ImageAreaSelect.prototype.update = function(options) {
	this.instance.setOptions(options);
	this.instance.update();
}

/**
 * Reloads the widget.
 */
PrimeFacesExt.widget.ImageAreaSelect.prototype.reload = function() {
	this.update({remove: true});
	this.instance = $(PrimeFaces.escapeClientId(this.cfg.target)).imgAreaSelect(this.options);
}
