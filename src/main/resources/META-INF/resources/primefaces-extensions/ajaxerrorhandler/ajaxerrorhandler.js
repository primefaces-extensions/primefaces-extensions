PrimeFacesExt.getAjaxErrorHandlerInstance = function() {
	if (!PrimeFacesExt.AJAX_ERROR_HANDLER_INSTANCE) {
		var instance = new PrimeFacesExt.widget.AjaxErrorHandler();

		instance.init();
		
		PrimeFacesExt.AJAX_ERROR_HANDLER_INSTANCE = instance;
	}

	return PrimeFacesExt.AJAX_ERROR_HANDLER_INSTANCE;
}

//TODO replace _self with proxy
//TODO dialog is sometimes not centered
//TODO callback args are null

/**
 * PrimeFaces Extensions AjaxErrorHandler.
 *
 * @author Pavol Slany
 */
PrimeFacesExt.widget.AjaxErrorHandler = PrimeFaces.widget.BaseWidget.extend({
	
	DEFAULT_HOSTNAME : '???unknown???',

	init : function() {
		this.popupWindow = null;
		this.popupWindowRoot = null;
		this.popupWindowMask = null;

		this.hostname = this.DEFAULT_HOSTNAME;
		
		this.defaultSettings = {
			'title': '{error-name}',
			'body': '{error-message}',
			'button': 'Reload',
			'buttonOnclick': function() {
				document.location.href = document.location.href;
			},
			'onerror': function(error, response) {
				
			}
		};
		this.settings = this.defaultSettings;
		this.otherSettings = {};

		this.overwritePrimeFacesAjaxResponse();
	},

	overwritePrimeFacesAjaxResponse : function() {
		var _self = this;

		PrimeFaces.ajax.AjaxResponse = function() {
			// backup original AjaxResponse function ...
			var backupAjaxResponse = PrimeFaces.ajax.AjaxResponse;

			var docPartialUpdate = arguments[0];
			var nodeErrors = docPartialUpdate.getElementsByTagName('error');

			if (nodeErrors && nodeErrors.length && nodeErrors[0].childNodes && nodeErrors[0].childNodes.length) {
				// XML => JSON
				var error = {};

				for (var i=0; i < nodeErrors[0].childNodes.length; i++) {
					var node = nodeErrors[0].childNodes[i];
					var key = node.nodeName;
					var val = node.nodeValue;

					if (node.childNodes && node.childNodes.length) {
						val = node.childNodes[0].nodeValue;
					}

					error[key] = val;
				}

				if (error['error-name']) {
					// findErrorSettings
					var errorSetting = _self.findErrorSettings(error['error-name']);

					//skip dialog if onerror is defined and returns false
					if (errorSetting['onerror']) {
						var onerrorFunction = errorSetting['onerror'];
						if (onerrorFunction.call(this, error, arguments[2]) === false) {
							return true;
						}
					}

					// Copy updates to errorSettings ...
					if (error.updateCustomContent && error.updateCustomContent.substring(-13) == '<exception />') {
						error.updateCustomContent = null;
					}

					if (error.updateTitle && error.updateTitle.substring(-13) == '<exception />') {
						error.updateTitle = null;
					}

					if (error.updateBody && error.updateBody.substring(-13) == '<exception />') {
						error.updateBody = null;
					}

					if (error.updateViewState && error.updateViewState.substring(-13) == '<exception />') {
						error.updateViewState = null;
					}

					errorSetting.updateCustomContent = error.updateCustomContent;
					errorSetting.updateTitle = error.updateTitle;
					errorSetting.updateBody = error.updateBody;
					errorSetting.updateViewState = error.updateViewState;

					var errorData = _self.replaceVariables(errorSetting, error);

					_self.show(errorData);

					return true;
				}
			}

			return backupAjaxResponse.apply(window, arguments);
		};
	},
	
	isVisible : function() {
		return this.popupWindow && this.popupWindow.isVisible();
	},

	recalculatePopupWindowHeight : function() {
		if (this.popupWindowMask == null && this.popupWindow == null) {
			return;
		}

		var height = $(window).outerHeight();
		var width = $(window).outerWidth();
		this.popupWindowMask.css('width', width);
		this.popupWindowMask.css('height', height);

		var winCss = {};
		winCss.left = (width - this.popupWindow.outerWidth()) / 2;
		winCss.top = (height - this.popupWindow.outerHeight()) / 2;

		if (winCss.left < 0) {
			winCss.left=0;
		}

		if (winCss.top < 0) {
			winCss.top=0;
		}

		this.popupWindow.css(winCss);
	},
	
	hide : function () {
		if (this.popupWindowRoot) {
			this.popupWindowRoot.remove();
		}

		this.popupWindowRoot = null;
		this.popupWindowMask = null;
		this.popupWindow = null;
	},
	
	show : function(errorData) {
		this.hide();

		this.popupWindowMask = $('<div class="ui-widget-overlay"></div>').hide();
		this.popupWindow = $('<div class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow ui-overlay-visible"></div>').hide();

		this.popupWindowRoot = $('<div class="pe-ajax-error-handler" style="z-index: 999999999; overflow: visible; position: absolute; left:0px; top: 0px;"></div>').append(this.popupWindow, this.popupWindowMask);
		$('body').append(this.popupWindowRoot);

		if (errorData.updateCustomContent) {
			var elContent = $('<div></div>');
			this.popupWindow.append(elContent);
			elContent.replaceWith(errorData.updateCustomContent);
		} else {
			var htmlContent = '<div class="ui-dialog-content ui-widget-content"></div>';
			var htmlTitle = '<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"></div>';
			var htmlTitleText = '<span class="ui-dialog-title"></span>';

			var button = $('<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"><span class="ui-button-text">' + errorData['button'] + '</span></button>');
			var buttonOnclickFunction = errorData['buttonOnclick'];
			button.click(function() {
				buttonOnclickFunction.call(this);
			});

			var elTitle = $('<a />');
			var elBody = $('<a />');
			this.popupWindow.append($(htmlTitle).append($(htmlTitleText).append(elTitle)));
			this.popupWindow.append(
					$(htmlContent).append($('<div></div>').append(elBody)),
					$('<div class="pe-error-buttons"></div>').append(button));

			// setup draggable
			this.popupWindow.draggable({
				cancel: '.ui-dialog-content, .ui-dialog-titlebar-close',
				handle: '.ui-dialog-titlebar',
				containment : 'document'
			});

			elTitle.replaceWith(errorData.updateTitle || errorData.title);
			elBody.replaceWith(errorData.updateBody || errorData.body);
		}

		this.popupWindowMask.css('zIndex', PrimeFaces.zindex++);
		this.popupWindow.css('zIndex', PrimeFaces.zindex++);

		this.popupWindow.css({'position' : 'fixed',
			'margin-left' : 'auto',
			'margin-right' : 'auto',
			'overflow' : 'hidden'});

		this.popupWindowMask.css({'position' : 'fixed', 'left' : 0, 'top' : 0});

		this.recalculatePopupWindowHeight();
		this.popupWindow.show();
		this.popupWindowMask.show();
		this.recalculatePopupWindowHeight();
	},

	getDefaultErrorTime : function() {
		return new Date().toString();
	},
	
	findErrorSettings : function(name) {
		if (!name) {
			return jQuery.extend({}, this.settings);
		}

		if (!this.otherSettings[name]) {
			jQuery.extend({}, this.settings);
		}

		return jQuery.extend({}, this.settings, this.otherSettings[name]);
	},

	addErrorSettings : function(newSettings) {
		if (!newSettings) {
			return;
		}

		if (!newSettings.type) {
			this.settings = jQuery.extend({}, this.settings, newSettings);
			return;
		}

		var type = this.otherSettings[newSettings.type] || {};

		this.otherSettings[newSettings.type] = jQuery.extend({}, type, newSettings);
	},
	
	replaceAll : function(str, key, val) {
		var newStr;

		while ((newStr = str.replace(key, val)) != str) {
			str = newStr
		};

		return str;
	},
	
	replaceVariables : function(obj, variables) {
		if (!obj) {
			return text;
		}

		variables = jQuery.extend({
			'error-hostname': this.hostname,
			'error-stacktrace': '',
			'error-time': this.getDefaultErrorTime()
		}, variables);

		var returnValue = {};

		jQuery.each(obj, $.proxy(function(key, val) {
			if (typeof(val) == 'string') {
				jQuery.each(variables, $.proxy(function(iVar, iVal) {
					val = this.replaceAll(val, '{' + iVar + '}', iVal);
				}, this));
			}

			returnValue[key] = val;
		}, this));

		return returnValue;
	},
	
	setHostname : function(hostname) {
		this.hostname = hostname;
	}
});

