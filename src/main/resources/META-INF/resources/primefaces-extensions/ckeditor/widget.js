/**
 * Resolves the resources for the CKEditor.
 * 
 * @param {string} resource The requested resource from CKEditor.
 * 
 * @return {string} The faces resource URL.
 */
CKEDITOR_GETURL = function(resource) {
	var facesResource;
	
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
				var extensionMappingPosition = facesResource.indexOf(PrimeFacesExt.getRequestUrlExtension());

				//extract resource
				var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFacesExt.RESOURCE_IDENTIFIER.length, extensionMappingPosition);

				facesResource = PrimeFacesExt.getPrimeFacesExtensionsResource(extractedResource + appendedResource);
			} else {
				var questionMarkPosition = facesResource.indexOf('?');

				//extract resource
				var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFacesExt.RESOURCE_IDENTIFIER.length, questionMarkPosition);

				facesResource = PrimeFacesExt.getPrimeFacesExtensionsResource(extractedResource + appendedResource);
			}
		} else {
			facesResource = resource;
		}
	} else {		
		facesResource = PrimeFacesExt.getPrimeFacesExtensionsResource('/ckeditor/' + resource);
	}
	
	return facesResource;
};

/**
 * PrimeFaces Extensions CKEditor Widget
 * 
 * @constructor
 */
PrimeFacesExt.widget.CKEditor = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
	this.jqId = PrimeFaces.escapeClientId(this.id);
	this.jq = $(this.jqId);

	this.dirtyEventDefined = this.cfg.behaviors && this.cfg.behaviors['dirty'];
	this.changeEventDefined = this.cfg.behaviors && this.cfg.behaviors['change'];
    this.dirtyState = false;

	this.options = {};
	//add widget to ckeditor config, this is required for the save event
	this.options.widget = this;

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
		this.options.toolbar = this.cfg.toolbar;
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

	//check if ckeditor is already included
	if (typeof(CKEDITOR) == 'undefined') {
		var _self = this;
		//load ckeditor
		PrimeFacesExt.getScript(PrimeFacesExt.getPrimeFacesExtensionsResource('/ckeditor/ckeditor.js'), function(data, textStatus) {
			//load jquery adapter
			PrimeFacesExt.getScript(PrimeFacesExt.getPrimeFacesExtensionsResource('/ckeditor/adapters/jquery.js'), function(data, textStatus) {
				_self.overwriteSaveButton();
				_self.initialize();
			}, true);
		}, true);
	} else {
		this.initialize();
	}
}

PrimeFaces.extend(PrimeFacesExt.widget.CKEditor, PrimeFaces.widget.BaseWidget);

/**
 * Initializes the CKEditor instance.
 * This method will be called when the resources for the CKEditor are loaded.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.initialize = function() {
	//remove old instances if required
	var oldInstance = CKEDITOR.instances[this.id];
	if (oldInstance) {
		oldInstance.destroy(true);
		delete oldInstance;
	}

	//initialize ckeditor after all resources were loaded
	this.jq.ckeditor($.proxy(function() { this.initialized(); }, this), this.options);
}

/**
 * Overwrites the save button.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.overwriteSaveButton = function() {
	//overwrite save button
	CKEDITOR.plugins.registered['save'] = {
		init : function(editor) {
			//get widget
			var widget = editor.config.widget;
			var command = editor.addCommand('save', {
				modes : { wysiwyg:1, source:1 },
				exec : function(editor) {
					if (widget.cfg.behaviors) {
						var saveCallback = widget.cfg.behaviors['save'];
					    if (saveCallback) {
					    	var ext = {
					    			params: {}
					    	};

					    	saveCallback.call(widget, null, ext);
					    }
					}
				}
			});
			editor.ui.addButton('Save', {label : editor.lang.save, command : 'save' });
		}
	}
}

/**
 * This method will be called when the CKEditor was initialized. 
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.initialized = function() {
	//get instance
	this.instance = this.jq.ckeditorGet();
	
	//fire initialize event
	this.fireEvent('initialize');
	
	//register blur and focus event
    this.instance.on('blur', $.proxy(function() { this.handleBlur(); }, this));
    this.instance.on('focus', $.proxy(function() { this.fireEvent('focus'); }, this));

    //register callbacks for change event
    if (this.changeEventDefined || this.dirtyEventDefined) {  
    	
    	this.bindChangeEventsForWYSIWYGMode();
    	this.bindCommonChangeEvents();

    	//changes to WYSIWYG mode
    	this.instance.on('contentDom', $.proxy(function() {
    		this.bindChangeEventsForWYSIWYGMode();
    		this.fireEvent('wysiwygMode');
      	}, this));
 
    	//changes to source mode
    	this.instance.on('mode', $.proxy(function(event) {
			if (this.instance.mode != 'source') {
				return;    	
			}
			this.bindChangeEventsForSourceMode();
			this.fireEvent('sourceMode');
    	}, this));
    }

    if (this.dirtyEventDefined && this.cfg.checkDirtyInterval !== 0) {
	    this.dirtyCheckInterval = setInterval($.proxy(this.checkDirtyFromTimer, this), this.cfg.checkDirtyInterval);
    }

    this.postConstruct();
}

/**
 * Binds the common events, which will be used for dirty/change checking.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.bindCommonChangeEvents = function() {
	this.instance.on('paste', $.proxy(this.checkDirty, this));
	this.instance.getCommand('undo').on('afterUndo', $.proxy(this.checkDirty, this));
	this.instance.getCommand('redo').on('afterRedo', $.proxy(this.checkDirty, this));

	this.instance.on('saveSnapshot', $.proxy(function(event) {
		if (!event.data || !event.data.contentOnly) {
			this.checkDirty();
		}
	}, this));

	this.instance.on('afterCommandExec', $.proxy(function(event) {
		if (event.data.name == 'source') {
			return;
		}

		if (event.data.command.canUndo !== false) {
			this.checkDirty();
		}
	}, this));
};

/**
 * Binds the events for the WYSIWYG mode, which will be used for dirty/change checking.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.bindChangeEventsForWYSIWYGMode = function() {
    this.instance.document.on('drop', $.proxy(this.checkDirty, this));
    this.instance.document.getBody().on('drop', $.proxy(this.checkDirty, this));
    this.instance.document.on('keydown', $.proxy(function(event) {
    	//do not capture ctrl and meta keys
    	if (event.data.$.ctrlKey ||event.data.$.metaKey) {
    		return;
    	}

    	//filter movement keys and related
    	var keyCode = event.data.$.keyCode;
    	if (keyCode == 8 || keyCode == 13 || keyCode == 32
    			|| ( keyCode >= 46 && keyCode <= 90)
    			|| ( keyCode >= 96 && keyCode <= 111)
    			|| ( keyCode >= 186 && keyCode <= 222)) {
    		this.checkDirty();
    	}
    }, this));
};

/**
 * Binds the events for the source mode, which will be used for dirty/change checking.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.bindChangeEventsForSourceMode = function() {
	this.instance.textarea.on('drop', $.proxy(this.checkDirty, this));
	this.instance.textarea.on('input', $.proxy(this.checkDirty, this));
	/*
	this.instance.textarea.on('keydown', $.proxy(function(event) {
		//do not capture ctrl and meta keys
		if (!event.data.$.ctrlKey && !event.data.$.metaKey) {
			this.checkDirty();
		}
	}, this));;
	*/
};

/**
 * Sets the dirty state.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.checkDirty = function() {
    if (this.dirtyState || this.instance.checkDirty()) {
        this.instance.resetDirty();

        if (this.dirtyState === false) {
        	this.dirtyState = true;
        }
        
        if (this.dirtyEventDefined) {
        	this.fireEvent('dirty');
        }
    }
};

/**
 * Restores the dirtyState and calls check checkDirty();
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.checkDirtyFromTimer = function() {
	this.dirtyState = false;
	this.checkDirty();
};

/**
 * Handles the blur event and fires the change event if required.
 *
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.handleBlur = function() {
	this.fireEvent('blur');

	if (this.changeEventDefined) {
		if (this.dirtyState) {
		    this.instance.resetDirty();
		    this.fireEvent('change');
		}

		this.dirtyState = false;
	}
};

/**
 * This method fires an event if the behavior was defined.
 *
 * @param {string} eventName The name of the event
 * @protected
 */
PrimeFacesExt.widget.CKEditor.prototype.fireEvent = function(eventName) {
	if (this.cfg.behaviors) {
		var callback = this.cfg.behaviors[eventName];
	    if (callback) {
	    	var ext = {
	    			params: {}
	    	};

	    	callback.call(this, null, ext);
	    }
	}
}
