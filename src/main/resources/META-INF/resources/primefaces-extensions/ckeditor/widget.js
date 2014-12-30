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
            if (resource.indexOf(PrimeFacesExt.RESOURCE_IDENTIFIER) === -1) {
                facesResource = PrimeFacesExt.getPrimeFacesExtensionsCompressedResource('/ckeditor/' + resource);
            }
            else {
                facesResource = resource;
            }
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
	
        //check dirty- and changed events
        this.isDirtyEventDefined = (this.cfg.behaviors && this.cfg.behaviors['dirty']) ? true : false;
        this.isChangeEventDefined = (this.cfg.behaviors && this.cfg.behaviors['change']) ? true : false;
    
        var editable = this.instance.editable();
        editable.attachListener(editable, 'cut', $.proxy(function(event) {
                this.checkChange();
                this.checkDirty();
        }, this));
        editable.attachListener(editable, 'paste', $.proxy(function(event) {
                this.checkChange();
                this.checkDirty();                
        }, this));
        editable.attachListener(editable, 'keydown', $.proxy(function(event) {
                // do not capture ctrl and meta keys
                if (event.data.$.ctrlKey || event.data.$.metaKey) {
                        return;
                }

                // filter movement keys and related
                var keyCode = event.data.$.keyCode;
                if (keyCode == 8 || keyCode == 13 || keyCode == 32
                                || (keyCode >= 46 && keyCode <= 90)
                                || (keyCode >= 96 && keyCode <= 111)
                                || (keyCode >= 186 && keyCode <= 222)) {
                        this.checkChange();
                        this.checkDirty();
                }
        }, this));

        this.instance.on('blur', $.proxy(function() {
                // this.checkChange(); // editor has built in 'change on blur' check
                this.checkDirty();
                var editor = this.getEditorInstance();
                editor.dirtyFired = false;
        }, this));
    },

    /**
     * This method checks if this editor instance is dirty (content has changed)
     * and fires a dirty event if it was not fired since last entering the editor.
     *
     * @private
     */
    checkDirty : function() {
	if (this.isDirtyEventDefined) {
		var editor = this.getEditorInstance();
		if (!editor.dirtyFired && editor.checkDirty()) { // checkDirty means isDirty
			// fires the dirty event only once!
			this.fireEvent('dirty');
			editor.dirtyFired = true;
		}
	}
    },

    /**
     * This method checks if this editor instance is change (content has changed)
     * and fires a cahnge event if so.
     *
     * @private
     */
    checkChange : function() {
	if (this.isChangeEventDefined) {
            this.fireEvent('change');
	}
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
                try {
                    this.instance.destroy(true);
                } catch (err) {
                    if (window.console && console.log) {
                        console.log('CKEditor throwed a error while destroying the old instance: ' + err);
                    }
                }
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
