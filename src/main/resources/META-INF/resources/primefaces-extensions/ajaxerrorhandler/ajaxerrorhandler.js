PrimeFacesExt.getAjaxErrorHandlerInstance = function() {
	if (!PrimeFacesExt.AJAX_ERROR_HANDLER_INSTANCE) {
		var instance = new PrimeFacesExt.widget.AjaxErrorHandler();

		//  INIT IS CALLED AUTOMATICALLY ... instance.init();
		
		PrimeFacesExt.AJAX_ERROR_HANDLER_INSTANCE = instance;
	}

	return PrimeFacesExt.AJAX_ERROR_HANDLER_INSTANCE;
}

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

		// backup original AjaxResponse function ...
		var backupAjaxResponse = PrimeFaces.ajax.AjaxResponse;

		PrimeFaces.ajax.AjaxResponse = function() {
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

			return backupAjaxResponse.apply(this, arguments);
		};
	},
	
	isVisible : function() {
		return this.popupWindow && this.popupWindow.isVisible();
	},
	
	hide : function () {
		if (this.dialogWidget) {
			this.dialogWidget.hide();
			this.dialogWidget = null;

			this.dialog.remove();
		}
	},
	
	show : function(errorData) {
		this.hide();

		//create required html
		this.dialog = $('<div id="ajaxErrorHandlerDialog" class="ui-dialog ui-widget ui-widget-content ui-overlay-hidden ui-corner-all ui-shadow pe-ajax-error-handler" style="width: auto; height: auto;"></div>');

		//append to DOM
		$('body').append(this.dialog);

		//custom content?
		if (errorData.updateCustomContent) {
			this.dialog.append(errorData.updateCustomContent);
		} else {
			//create required html
			var dialogHeader = $('<div class="ui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top"></div>');
			var dialogHeaderText = $('<span class="ui-dialog-title"></span>');
			var dialogContent = $('<div class="ui-dialog-content ui-widget-content" style="height: auto;"></div>');
			var dialogButtonPane = $('<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix"></div>')
			
			//append to DOM
			dialogHeader.append(dialogHeaderText);

			this.dialog.append(dialogHeader);
			this.dialog.append(dialogContent);
			this.dialog.append(dialogButtonPane);

			//setup button
			var dialogButton = $('<button class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only"><span class="ui-button-text">' + errorData['button'] + '</span></button>');
			var buttonOnclickFunction = errorData['buttonOnclick'];
			dialogButton.click(function() {
				buttonOnclickFunction.call(this);
			});
			
			dialogButtonPane.append(dialogButton);

			//add title
			if (errorData.updateTitle) {
				dialogHeaderText.append(errorData.updateTitle);
			} else {
				dialogHeaderText.append(errorData.title);
			}

			//add body
			if (errorData.updateBody) {
				dialogContent.append(errorData.updateBody);
			} else {
				dialogContent.append(errorData.body);
			}
		}

		//create Dialog widget
		this.dialogWidget = new PrimeFaces.widget.Dialog({
			id : 'ajaxErrorHandlerDialog',
			resizable : true,
			draggable : true,
			modal : true
		});

		this.dialogWidget.show();
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
