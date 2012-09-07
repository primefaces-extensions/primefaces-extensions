/**
 * PrimeFaces Extensions Spotlight Widget.
 *
 * @author Pavol Slany
 */
PrimeFacesExt.widget.Spotlight = PrimeFaces.widget.BaseWidget.extend({

	/**
	 * Initializes the widget.
	 *
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
		this.id = cfg.id;
		this.blocked = cfg.blocked;
		this.content = $(PrimeFaces.escapeClientId(this.id));

		PrimeFacesExt.widget.Spotlight.cache = PrimeFacesExt.widget.Spotlight.cache || {};

		////////////////////////
		// Mask
		this.getMask = function () {
			var mask = PrimeFacesExt.widget.Spotlight.cache['PrimeFacesExt.widget.Spotlight.MaskAround:' + this.id];
			if (!mask) {
				mask = new PrimeFacesExt.widget.Spotlight.MaskAround(this.id);
				PrimeFacesExt.widget.Spotlight.cache['PrimeFacesExt.widget.Spotlight.MaskAround:' + this.id] = mask;
			}
			return (this.getMask = function () {
				return mask
			})();
		}

		this.block = function () {
			this.blocked = true;
			// Show MASK
			this.getMask().show();

			// focus area ...
			//this.getFocusArea().activate();
			this.enableModality();
		}
		this.unblock = function () {
			this.blocked = false;
			// Hide MASK
			this.getMask().hide();

			// focus area ...
//			this.getFocusArea().deactivate()
			this.disableModality();
		}

		if (this.blocked) {
			this.block();
		}
		else {
			this.unblock();
		}

		PrimeFacesExt.removeWidgetScript(this.id);
	},

	enableModality: function() {

		//disable tabbing out of modal dialog and stop events from targets outside of dialog
		this.disableModality();

		this.content.bind('keydown.lightspot', function(event) {
			if(event.keyCode !== $.ui.keyCode.TAB) {
				return;
			}

			var tabbables = $(':tabbable', this),
				first = tabbables.filter(':first'),
				last  = tabbables.filter(':last');

			if (event.target === last[0] && !event.shiftKey) {
				first.focus(1);
				return false;
			} else if (event.target === first[0] && event.shiftKey) {
				last.focus(1);
				return false;
			}
		});

		// if focus is out of panel => set focus ...
		var focused = $(':focus', this.content);
		if (!focused || !focused.length)
			$(':tabbable', this.content).filter(':first').focus(1);

	},

	disableModality: function(){
		this.content.unbind('.lightspot');
	},

	block:function () {
		this.block();
	},

	unblock:function () {
		this.unblock();
	}
});

/**
 * @author Pavol Slany
 */
PrimeFacesExt.widget.Spotlight.MaskAround = function (elementId) {
	var maskId = elementId + '_maskAround';

	var destinationOpacity = function () {
		var el = $('<div class="ui-widget-overlay"></div>');
		$('body').append(el);
		var opacity = el.css('opacity');
		el.remove();
		return opacity;
	}();

	var ElementPieceOfMask = function (maskKey, zIndex) {
		var idEl = maskId + maskKey;
		var zIndex = zIndex;
		var elementMustBeVisible = $(PrimeFaces.escapeClientId(idEl)).is(':visible');
		var getMaskElement = function () {
			var maskElement = $(PrimeFaces.escapeClientId(idEl));

			if (!maskElement || !maskElement.length) {
				maskElement = $('<div id="' + idEl + '" />');
				maskElement.css({
//					position: 'fixed',
					position: !$.browser.webkit ? 'fixed' : 'absolute',
					top:0,
					left:0,
					display:'none',
					zIndex:zIndex,
					overflow:'hidden',
					border: 'none'
				});
				maskElement.append($('<div class="ui-widget-overlay" style="position:absolute;"></div>').css('opacity', 1));
				$('body').append(maskElement);

				maskElement.click(function() {
					var content = $(PrimeFaces.escapeClientId(elementId));
					var tabbables = $(':tabbable', content);
					tabbables.filter(':first').focus(1);
				});
			}


			return maskElement;
		}
		var isMaskVisible = function () {
			var maskElement = $(PrimeFaces.escapeClientId(idEl));
			if (!maskElement || !maskElement.length) return false;
			return maskElement.is(":visible");
		}
		var isMaskInBody = function () {
			var maskElement = $(PrimeFaces.escapeClientId(idEl));
			if (!maskElement || !maskElement.length) return false;
			return true;
		}

		var updateVisibility = function () {
			if (elementMustBeVisible) {
				if (!isMaskVisible()) {
					var el = getMaskElement();
					el.css('zIndex', zIndex);
					el.fadeTo("fast", destinationOpacity, updateVisibility);

				}
				return;
			}
			// ...
			if (isMaskVisible()) {
				getMaskElement().fadeOut('fast', function () {
					updateVisibility();
				});
				return;
			}

			if (isMaskInBody()) {
				getMaskElement().remove();
				return;
			}
		}

		return {
			updatePosition:function (x0, y0, x1, y1) {
				// X,Y correction
				if (getMaskElement().css('position')=='fixed') {
					var xx = $(window).scrollLeft();
					var yy = $(window).scrollTop();
					x0 -= xx;
					x1 -= xx;
					y0 -= yy;
					y1 -= yy;
				}

				$('<div class="ui-widget-overlay"></div>')
				var el = getMaskElement();
				el.css({
					left:x0,
					top:y0,
					width:(x1 - x0),
					height:(y1 - y0)
				});

				var maxSize = getMaxSize();
				$(el.children()[0]).css({
					left:(0 - x0),
					top:(0 - y0),
					height:maxSize.height,
					width:maxSize.width
				});
			},
			show:function (zIndexNew) {
				elementMustBeVisible = true;
				zIndex = zIndexNew;
				updateVisibility();
			},
			hide:function () {
				elementMustBeVisible = false;
				updateVisibility();
			}
		};
	};

	var zIndex = ++PrimeFaces.zindex;

	var top = new ElementPieceOfMask('_top', zIndex);
	var left = new ElementPieceOfMask('_left', zIndex);
	var bottom = new ElementPieceOfMask('_bottom', zIndex);
	var right = new ElementPieceOfMask('_right', zIndex);

	var getMaxSize = function () {
		var winWidth = $(window).width();
		var winHeight = $(window).height();
		var docWidth = $(document).width();
		var docHeight = $(document).height();

		var maxWidth = winWidth > docWidth ? winWidth : docWidth;
		var maxHeight = winHeight > docHeight ? winHeight : docHeight;

		return {
			width:maxWidth,
			height:maxHeight
		};
	}

	// check IE8 browser (it works in all BROWSER MODEs and DOCUMENT MODEs)
	var isIE8 = function () {
		if ($.browser.msie) {
			// document.documentMode is since IE8
			// window.performance is since IE9
			if (document.documentMode && !window.performance) return true;
		}
		return false;
	}


	var updateMaskPositions = function () {
		var maxSize = getMaxSize();

		var maxWidth = maxSize.width;
		var maxHeight = maxSize.height;

		// Check PANEL position for MASK
		var el = $(PrimeFaces.escapeClientId(elementId));
		var x0 = el.offset().left;
		var y0 = el.offset().top;
		var x1 = x0 + el.outerWidth();
		var y1 = y0 + el.outerHeight();

		// Correct MASK position before, any parents is with overflow=AUTO|HIDDEN|SCROLL
		var elParent = el.parent();
		while (elParent.length > 0 && elParent[0].tagName != 'HTML') {
			var overflow = elParent.css('overflow');

			if (overflow == 'auto' || overflow == 'hidden' || overflow == 'scroll') {
				// IE BUG - if height is 0 => CSS problem with overflow => ignore it
				if (elParent.height() > 0) {
					var offset = elParent.offset();
					if (x0 < offset.left) x0 = offset.left;
					if (y0 < offset.top) y0 = offset.top;
					if (x1 > offset.left + elParent.outerWidth()) x1 = offset.left + elParent.outerWidth();
					if (y1 > offset.top + elParent.outerHeight()) y1 = offset.top + elParent.outerHeight();
				}
			}

			elParent = elParent.parent();
		}


		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 < x0) x1 = x0;
		if (y1 < y0) y1 = y0;

		if (el.outerHeight() > 0 && y1 - y0 <= 5) {
			try {
				var elFocus = $(PrimeFaces.escapeClientId(elementId) + ' :focusable');
				// Change focus ...
				if (elFocus.length < 2) {
					// If ELEMENT does not exist => create TMP element
					var tmpEl = $('<a href="#"> </a>');
					el.append(tmpEl);
					tmpEl.focus();
					tmpEl.remove();
				}
				else {
					// ELEMENT exists
					$(elFocus[1]).focus();
				}
				// SET FOCUS for first element
				$(elFocus[0]).focus();

			} catch (e) {
			}
		}

		var ie8Corecting = 0;
		// IE8 has bug with layouts => check IE8 (it works in all BROWSER MODE and DOCUMENT MODE)
		if (isIE8()) {
			ie8Corecting = 1;
		}

//		var bodyOffset = {top: $(window).scrollTop(), left: $(window).scrollLeft()};
		var bodyOffset = {top:0, left:0};

		top.updatePosition(
			0 - bodyOffset.left,
			0 - bodyOffset.top,
			maxWidth - bodyOffset.left,
			y0 - bodyOffset.top);
		bottom.updatePosition(
			0 - bodyOffset.left,
			y1 - bodyOffset.top,
			maxWidth - bodyOffset.left,
			maxHeight - bodyOffset.top);
		left.updatePosition(
			0 - bodyOffset.left,
			y0 - bodyOffset.top + ie8Corecting,
			x0 - bodyOffset.left,
			y1 - bodyOffset.top - ie8Corecting);
		right.updatePosition(
			x1 - bodyOffset.left,
			y0 - bodyOffset.top + ie8Corecting,
			maxWidth - bodyOffset.left,
			y1 - bodyOffset.top - ie8Corecting);
	}

	var mustBeShowed = false;
	var resizeTimer = null;
	$(window).bind('resize', function () {
		if (resizeTimer) clearTimeout(resizeTimer);
		resizeTimer = setTimeout(updatePositions, 100);
	});

	var updatePositions = function () {
		if (mustBeShowed === true) {
			updateMaskPositions();
			setTimeout(updatePositions, 150);
		}
	};
	var showAreas = function () {
		mustBeShowed = true;

		updatePositions();

		var zIndex = ++PrimeFaces.zindex;

		top.show(zIndex);
		bottom.show(zIndex);
		left.show(zIndex);
		right.show(zIndex);
	}
	var hideAreas = function () {
		mustBeShowed = false;

		top.hide();
		bottom.hide();
		left.hide();
		right.hide();
	}

	return {
		show:function () {
			showAreas();
		},
		hide:function () {
			hideAreas();
		}
	};
};
