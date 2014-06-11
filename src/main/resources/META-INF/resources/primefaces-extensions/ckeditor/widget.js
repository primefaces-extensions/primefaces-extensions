/**
 * Resolves the resources for the CKEditor.
 * 
 * @param {string} resource The requested resource from CKEditor.
 * @returns {string} The faces resource URL.
 */
CKEDITOR_GETURL = function(resource) {
	var facesResource;

	//do not resolve
	if (resource.indexOf('?resolve=false') !== -1) {
		facesResource = resource.replace('?resolve=false', '');
	} else {
		//already wrapped?
		var libraryVersionIndex = resource.indexOf('v=' + PrimeFacesExt.getPrimeFacesExtensionsVersion());
		if (libraryVersionIndex !== -1) {
			//look for appended resource
			var appendedResource = resource.substring(libraryVersionIndex + ('v=' + PrimeFacesExt.getPrimeFacesExtensionsVersion()).length);
	
			if (appendedResource.length > 0) {
				//remove append resource from url
				facesResource = resource.substring(0, resource.length - appendedResource.length);
	
				var resourceIdentiferPosition = facesResource.indexOf(PrimeFacesExt.RESOURCE_IDENTIFIER);
				
				if (PrimeFacesExt.isExtensionMapping()) {
					var extensionMappingPosition = facesResource.lastIndexOf('.' + PrimeFacesExt.getResourceUrlExtension());
	
					//extract resource
					var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFacesExt.RESOURCE_IDENTIFIER.length, extensionMappingPosition);
	
					facesResource = PrimeFacesExt.getPrimeFacesExtensionsCompressedResource(extractedResource + appendedResource);
				} else {
					var questionMarkPosition = facesResource.indexOf('?');
	
					//extract resource
					var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFacesExt.RESOURCE_IDENTIFIER.length, questionMarkPosition);
	
					facesResource = PrimeFacesExt.getPrimeFacesExtensionsCompressedResource(extractedResource + appendedResource);
				}
			} else {
				facesResource = resource;
			}
		} else {
			facesResource = PrimeFacesExt.getPrimeFacesExtensionsCompressedResource('/ckeditor/' + resource);
		}
	}
	
	return facesResource;
};

/**
 * PrimeFaces Extensions CKEditor Widget.
 * 
 * @author Thomas Andraschko
 */
PrimeFacesExt.widget.CKEditor = PrimeFaces.widget.DeferredWidget.extend({
	
	/**
	 * Initializes the widget.
	 * 
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
		this._super(cfg);

	    this.instance = null;
	
		this.options = {};
		//add widget to ckeditor config, this is required for the save event
		this.options.widgetVar = this.cfg.widgetVar;
	
		if (this.cfg.skin) {
			this.options.skin = this.cfg.skin;
		}
		if (this.cfg.width) {
			this.options.width = this.cfg.width;
		}
		if (this.cfg.height) {
			this.options.height = this.cfg.height;
		}
		if (this.cfg.theme) {
			this.options.theme = this.cfg.theme;
		}
		if (this.cfg.toolbar) {
			if (!(this.cfg.toolbar instanceof Array)) {
				this.options.toolbar = eval(this.cfg.toolbar);
			} else {
				this.options.toolbar = this.cfg.toolbar;
			}
		}
		if (this.cfg.readOnly) {
			this.options.readOnly = this.cfg.readOnly;
		}
		if (this.cfg.interfaceColor) {
			this.options.uiColor = this.cfg.interfaceColor;
		}
		if (this.cfg.language) {
			this.options.language = this.cfg.language;
		}
		if (this.cfg.defaultLanguage) {
			this.options.defaultLanguage = this.cfg.defaultLanguage;
		}
		if (this.cfg.contentsCss) {
			this.options.contentsCss = this.cfg.contentsCss;
		}
		if (this.cfg.customConfig) {
			this.options.customConfig = this.cfg.customConfig + "?resolve=false";
		}
	
		//check if ckeditor is already included
		if (!$.fn.ckeditor) {
			var ckEditorScriptURI =
				PrimeFacesExt.getPrimeFacesExtensionsCompressedResource('/ckeditor/ckeditor.js');
			
			var jQueryAdapterScriptURI =
				PrimeFacesExt.getPrimeFacesExtensionsCompressedResource('/ckeditor/adapters/jquery.js');		
	
			//load ckeditor
			PrimeFaces.getScript(ckEditorScriptURI, $.proxy(function(data, textStatus) {
	
				//load jquery adapter
				PrimeFaces.getScript(jQueryAdapterScriptURI, $.proxy(function(data, textStatus) {
	
					this.renderDeferred();

				}, this));
	
			}, this), true);
	
		} else {
			this.renderDeferred();
		}
	},

	/**
	 * Initializes the CKEditor instance.
	 * This method will be called when the resources for the CKEditor are loaded.
	 * 
	 * @private
	 */
	_render : function() {
		if (!this.instance) {
			//overwrite save button
			this.overwriteSaveButton();
	
			//remove old instances if required
			var oldInstance = CKEDITOR.instances[this.id];
			if (oldInstance) {
				try {
					oldInstance.destroy(true);
				} catch (err) {
					if (window.console && console.log) {
						console.log('CKEditor throwed a error while destroying the old instance: ' + err);
					}
				}
			}
	
			//initialize ckeditor after all resources were loaded
			this.jq.ckeditor($.proxy(function() { this.initialized(); }, this), this.options);
		}
	},

	/**
	 * Overwrites the save button.
	 *
	 * @private
	 */
	overwriteSaveButton : function() {
		//overwrite save button
		CKEDITOR.plugins.registered['save'] = {
			init : function(editor) {

				//get widget
				var widget = PF(editor.config.widgetVar);
				var command = editor.addCommand('save', {
					modes : { wysiwyg:1, source:1 },
					exec : function(editor) {
						if (widget.cfg.behaviors) {
							var saveCallback = widget.cfg.behaviors['save'];
						    if (saveCallback) {
						    	var options = {
						    			params: []
						    	};
	
						    	saveCallback.call(widget, options);
						    }
						}
					}
				});

				editor.ui.addButton('Save', {label : editor.lang.save, command : 'save', title : '' });
			}
		};
	},

	/**
	 * This method will be called when the CKEditor was initialized. 
	 *
	 * @private
	 */
	initialized : function() {
		//get instance
		this.instance = this.jq.ckeditorGet();
		
		//fire initialize event
		this.fireEvent('initialize');
		
		//register blur and focus event
	    this.instance.on('blur', $.proxy(function() { this.fireEvent('blur'); }, this));
	    this.instance.on('focus', $.proxy(function() { this.fireEvent('focus'); }, this));

	    //changes to WYSIWYG mode
    	this.instance.on('contentDom', $.proxy(function() {
    		this.fireEvent('wysiwygMode');
      	}, this));
 
    	//changes to source mode
    	this.instance.on('mode', $.proxy(function(event) {
			if (this.instance.mode != 'source') {
				return;    	
			}
			this.fireEvent('sourceMode');
    	}, this));
	},

	/**
	 * This method fires an event if the behavior was defined.
	 *
	 * @param {string} eventName The name of the event.
	 * @private
	 */
	fireEvent : function(eventName) {
		if (this.cfg.behaviors) {
			var callback = this.cfg.behaviors[eventName];
		    if (callback) {
		    	var options = {
		    			params: []
		    	};
	
		    	callback.call(this, options);
		    }
		}
	},

	/**
	 * Destroys the CKEditor instance.
	 */
	destroy : function() {
	    if (this.instance) {
	        this.instance.destroy(true);
	        this.instance = null;
	    }
	
	    this.jq.show();
	},

	/**
	 * Checks if the editor is in dirty state.
	 */
	isDirty : function() {
		if (!this.instance) {
			return false;
		}

		return this.instance.checkDirty();
	},

	/**
	 * Sets readOnly to the CKEditor.
	 */
	setReadOnly : function(readOnly) {
	    this.instance.setReadOnly(readOnly !== false);
	},

	/**
	 * Checks if the CKEditor is readOnly.
	 */
	isReadOnly : function() {
	    return this.instance.readOnly;
	},

	/**
	 * Indicates that the editor instance has focus.
	 */
	hasFocus : function() {
	    return this.instance.focusManager.hasFocus;
	},

	/**
	 * Returns the CKEditor instance.
	 */
	getEditorInstance : function() {
	    return this.instance;
	}
});
