/**
 * PrimeFaces Extensions ImageRotateAndResize Widget.
 *
 * @author Thomas Andraschko
 */
PrimeFacesExt.widget.ImageRotateAndResize = PrimeFaces.widget.BaseWidget.extend({
	
	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
		this.id = cfg.id;
		this.cfg = cfg;

		this.initialized = false;
		
		PrimeFacesExt.removeWidgetScript(this.id);
	},

	/**
	 * Initializes the settings.
	 *
	 * @private
	 */
	initializeLazy : function() {
		if (!this.initialized) {
			this.target = $(this.cfg.target)[0];
			this.imageSrc = this.target.src;
			this.imageWidth = this.target.width;
			this.imageHeight = this.target.height;
			this.degree = 0;
			this.newImageWidth = this.target.width;
			this.newImageHeight = this.target.height;
			this.initialized = true;
		}
	},

	/**
	 * Reloads the widget.
	 */
	reload : function() {
		this.initialized = false;
		this.initializeLazy();
	},

	/**
	 * Rotates the image to the left direction.
	 * 
	 * @param {number} degree The degree.
	 */
	rotateLeft : function(degree) {
		this.initializeLazy();
	
		if ((this.degree - degree) <= 0) {
			this.degree = 360 - ((this.degree - degree) * -1);
		}
		else {
			this.degree -= degree;
		}
	
		this.redrawImage(false, true);
	},

	/**
	 * Rotates the image to the right direction.
	 * 
	 * @param {number} degree The degree.
	 */
	rotateRight : function(degree) {
		this.initializeLazy();
	
		if ((this.degree + degree) >= 360) {
			this.degree = (this.degree + degree) - 360;
		}
		else {
			this.degree += degree;
		}
	
		this.redrawImage(false, true);
	},

	/**
	 * Resizes the image to the given width and height.
	 * 
	 * @param {number} width The new width of the image.
	 * @param {number} height The new height of the image.
	 */
	resize : function(width, height) {
		this.initializeLazy();
	
		this.newImageWidth = width;
		this.newImageHeight = height;
	
		this.redrawImage(true, false);
	},

	/**
	 * Scales the image with the given factor.
	 * 
	 * @param {number} scaleFactor The scale factor. For example: 1.1 = scales the image to 110% size.
	 */
	scale : function(scaleFactor) {
		this.initializeLazy();
	
		this.newImageWidth = this.newImageWidth * scaleFactor;
		this.newImageHeight = this.newImageHeight * scaleFactor;
	
		this.redrawImage(true, false);
	},

	/**
	 * Restores the default image.
	 * It also fires the rotate and resize event with the default values.
	 */
	restoreDefaults : function() {
		this.initializeLazy();
	
		this.newImageWidth = this.imageWidth;
		this.newImageHeight = this.imageHeight;
		this.degree = 0;
	
		this.redrawImage(true, true);
	},

	/**
	 * Redraws the image.
	 * 
	 * @param {fireResizeEvent} fireResizeEvent If the resize event should be fired.
	 * @param {fireRotateEvent} fireRotateEvent If the rotate event should be fired.
	 * @private
	 */
	redrawImage : function(fireResizeEvent, fireRotateEvent) {

		var rotation;
		if (this.degree >= 0) {
			rotation = Math.PI * this.degree / 180;
		} else {
			rotation = Math.PI * (360 + this.degree) / 180;
		}
	
		var cos = Math.cos(rotation);
		var sin = Math.sin(rotation);
	
		//check for < IE9, otherwise use canvas
		if ($.browser.msie && parseInt($.browser.version) <= 8) {
			//create new image
			var image = document.createElement('img');

			image.onload = $.proxy(function() {
				//set new size
				image.height = this.newImageHeight;
				image.width = this.newImageWidth;
		
				//apply rotation for IE
				image.style.filter = "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (sin * -1) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')";	
				
				//replace old image with new generated one
				image.id = this.target.id;
				this.target.parentNode.replaceChild(image, this.target);
				this.target = image;
				
				if (fireResizeEvent) {
					this.fireResizeEvent();
				}
				if (fireRotateEvent) {
					this.fireRotateEvent();
				}
				
			}, this);
			
			image.src = this.imageSrc;
			
		} else {
			//create canvas instead of img
			var canvas = document.createElement('canvas');
	
			//new image with new size
			var newImage = new Image();

			newImage.onload = $.proxy(function() {
				//rotate
				canvas.style.width = canvas.width = Math.abs(cos * newImage.width) + Math.abs(sin * newImage.height);
				canvas.style.height = canvas.height = Math.abs(cos * newImage.height) + Math.abs(sin * newImage.width);
		
				var context = canvas.getContext('2d');
				context.save();
				
				if (rotation <= Math.PI/2) {
					context.translate(sin * newImage.height, 0);
				} else if (rotation <= Math.PI) {
					context.translate(canvas.width, (cos * -1) * newImage.height);
				} else if (rotation <= 1.5 * Math.PI) {
					context.translate((cos * -1) * newImage.width, canvas.height);
				} else {
					context.translate(0, (sin * -1) * newImage.width);
				}
		
				context.rotate(rotation);
				context.drawImage(newImage, 0, 0, newImage.width, newImage.height);
				context.restore();	
		
				//replace image with canvas and set src attribute
				canvas.id = this.target.id;
				canvas.src = this.target.src;
				this.target.parentNode.replaceChild(canvas, this.target);
				this.target = canvas;
				
				if (fireResizeEvent) {
					this.fireResizeEvent();
				}
				if (fireRotateEvent) {
					this.fireRotateEvent();
				}
				
			}, this);

			newImage.src = this.imageSrc;
			newImage.width = this.newImageWidth;
			newImage.height = this.newImageHeight;
		}
	},

	/**
	 * Fires the rotate event.
	 *
	 * @private
	 */
	fireRotateEvent : function() {
		if (this.cfg.behaviors) {
			var callback = this.cfg.behaviors['rotate'];
		    if (callback) {
		    	var ext = {
		    			params: [
		    			         { name: this.id + '_degree', value: this.degree }
		    			]
		    	};

		    	callback.call(this, null, ext);
		    }
		}
	},

	/**
	 * Fires the resize event.
	 *
	 * @private
	 */
	fireResizeEvent : function() {
		if (this.cfg.behaviors) {
			var callback = this.cfg.behaviors['resize'];
		    if (callback) {
		    	var ext = {
		    			params: [
		    			         { name: this.id + '_width', value: this.newImageWidth },
		    			         { name: this.id + '_height', value: this.newImageHeight }
		    			]
		    	};

		    	callback.call(this, null, ext);
		    }
		}
	}
});
