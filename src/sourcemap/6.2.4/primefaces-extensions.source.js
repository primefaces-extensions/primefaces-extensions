/**
 * @namespace The PrimeFaces Extensions root namespace.
 */
PrimeFacesExt = {

    /**
     * Checks if the FacesServlet is mapped with extension mapping. For example:
     * .jsf/.xhtml.
     * 
     * @author Thomas Andraschko
     * @returns {boolean} If mapped with extension mapping.
     */
    isExtensionMapping : function() {
        if (!PrimeFacesExt.IS_EXTENSION_MAPPING) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();
            var primeFacesExtensionsScript = 'primefaces-extensions.js';

            PrimeFacesExt.IS_EXTENSION_MAPPING = scriptURI.charAt(scriptURI.indexOf(primeFacesExtensionsScript)
                    + primeFacesExtensionsScript.length) === '.';
        }

        return PrimeFacesExt.IS_EXTENSION_MAPPING;
    },

    /**
     * Gets the URL extensions of current included resources. For example: jsf
     * or xhtml. This should only be used if extensions mapping is used.
     * 
     * @returns {string} The URL extension.
     */
    getResourceUrlExtension : function() {
        if (!PrimeFacesExt.RESOURCE_URL_EXTENSION) {
            var scriptURI = PrimeFacesExt.getPrimeFacesExtensionsScriptURI();

            PrimeFacesExt.RESOURCE_URL_EXTENSION = RegExp('primefaces-extensions.js.([^?]*)').exec(scriptURI)[1];
        }

        return PrimeFacesExt.RESOURCE_URL_EXTENSION;
    },

    /**
     * Gets the resource URI of the current included primefaces-extensions.js.
     * 
     * @author Thomas Andraschko
     * @returns {string} The resource URI.
     */
    getPrimeFacesExtensionsScriptURI : function() {
        if (!PrimeFacesExt.SCRIPT_URI) {
            PrimeFacesExt.SCRIPT_URI = $('script[src*="/' + PrimeFaces.RESOURCE_IDENTIFIER + '/primefaces-extensions.js"]').attr('src');
            if (!PrimeFacesExt.SCRIPT_URI) {
                PrimeFacesExt.SCRIPT_URI = $('script[src*="' + PrimeFaces.RESOURCE_IDENTIFIER + '=primefaces-extensions.js"]').attr('src');
            }
        }

        return PrimeFacesExt.SCRIPT_URI;
    },

    /**
     * Configures component specific localized text by given widget name and
     * locale in configuration object.
     * 
     * @author Oleg Varaksin
     * @param {string}
     *        widgetName The name of the widget. For example: 'TimePicker'.
     * @param {object}
     *        cfg Configuration object as key, value pair. This object should
     *        keep current locale in cfg.locale.
     * @returns {object} cfg Configuration object with updated localized text
     *          (if any text to given locale were found).
     */
    configureLocale : function(widgetName, cfg) {
        if (PrimeFacesExt.locales && PrimeFacesExt.locales[widgetName] && cfg.locale) {
            var localeSettings = PrimeFacesExt.locales[widgetName][cfg.locale];
            if (localeSettings) {
                for ( var setting in localeSettings) {
                    if (localeSettings.hasOwnProperty(setting)) {
                        cfg[setting] = localeSettings[setting];
                    }
                }
            }
        }

        return cfg;
    },

    /**
     * This function need to be invoked after PrimeFaces changeTheme. It's used
     * to sync canvas and svg components to the current theme.
     * 
     * @author f.strazzullo
     */
    changeTheme : function(newValue) {
        $(document).trigger("PrimeFacesExt.themeChanged", newValue);
    },

    /**
     * The name of the PrimeFaces Extensions resource library.
     * 
     * @author Thomas Andraschko
     * @type {string}
     * @constant
     */
    RESOURCE_LIBRARY : 'primefaces-extensions',

    VERSION : '6.2.4'
};

/**
 * @namespace Namespace for behaviors.
 */
PrimeFacesExt.behavior = {};

/**
 * @namespace Namespace for widgets.
 */
PrimeFacesExt.widget = {};

/**
 * @namespace Namespace for localization.
 */
PrimeFacesExt.locales = {};

/**
 * @namespace Namespaces for components with localized text.
 */
PrimeFacesExt.locales.TimePicker = {};

/**
 * JavaScript behavior.
 * 
 * @author Thomas Andraschko
 * @constructor
 */
PrimeFacesExt.behavior.Javascript = function(cfg, ext) {

    var params = null;
    if (ext) {
        params = ext.params;
    }

    return cfg.execute.call(this, cfg.source, cfg.event, params, ext);
};

/**
 * Hack to allow the PrimeFacesExt changeTheme to automatically invoked on every
 * theme change
 * 
 * @author f.strazzullo
 */
(function(window) {

    var originalChangeTheme = PrimeFaces.changeTheme;

    PrimeFaces.changeTheme = function(newValue) {
        originalChangeTheme(newValue);
        PrimeFacesExt.changeTheme(newValue);
    }

})(window);
;/**
 * Resolves the resources for the CKEditor.
 *
 * @param {string} resource The requested resource from CKEditor.
 * @returns {string} The faces resource URL.
 */
CKEDITOR_GETURL = function(resource) {
    var facesResource;
    
    // GitHub #545 IE11 support
    if (PrimeFaces.env.isIE()) {
        if (!String.prototype.startsWith) {
            String.prototype.startsWith = function(searchString, position) {
                position = position || 0;
                return this.indexOf(searchString, position) === position;
            };
        }
    }

    //do not resolve
    if (resource.indexOf('?resolve=false') !== -1) {
        facesResource = resource.replace('?resolve=false', '');
    } else {
        //already wrapped?
        var libraryVersion = 'v=' + PrimeFacesExt.VERSION;
        var libraryVersionIndex = resource.indexOf(libraryVersion);
        if (libraryVersionIndex !== -1) {
            //look for appended resource
            var appendedResource = resource.substring(libraryVersionIndex + (libraryVersion).length);

            if (appendedResource.length > 0) {
                //remove append resource from url
                facesResource = resource.substring(0, resource.length - appendedResource.length);
                
                // GitHub ##509 check for URL param
                if (appendedResource.startsWith('&')) {
                    // example: replace &conversationContext=33
                    appendedResource = appendedResource.replace(/&\w+=\d+/, '');
                }

                var resourceIdentiferPosition = facesResource.indexOf(PrimeFaces.RESOURCE_IDENTIFIER);

                if (PrimeFacesExt.isExtensionMapping()) {
                    var extensionMappingPosition = facesResource.lastIndexOf('.' + PrimeFacesExt.getResourceUrlExtension());

                    //extract resource
                    var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFaces.RESOURCE_IDENTIFIER.length, extensionMappingPosition);

                    facesResource = PrimeFaces.getFacesResource(extractedResource + appendedResource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
                } else {
                    var questionMarkPosition = facesResource.indexOf('?');

                    //extract resource
                    var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFaces.RESOURCE_IDENTIFIER.length, questionMarkPosition);

                    facesResource = PrimeFaces.getFacesResource(extractedResource + appendedResource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
                }
            } else {
                facesResource = resource;
            }
        } else {
            if (resource.indexOf(PrimeFaces.RESOURCE_IDENTIFIER) === -1) {
                facesResource = PrimeFaces.getFacesResource('ckeditor/' + resource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
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
PrimeFaces.widget.ExtCKEditor = PrimeFaces.widget.DeferredWidget.extend({

	/**
	 * Initializes the widget.
	 *
	 * @param {object} cfg The widget configuration.
	 */
	init : function(cfg) {
        this._super(cfg);

        this.instance = null;
        this.initializing = false;

        this.options = {};
        // add widget to ckeditor config, this is required for the save event
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

        // check if ckeditor is already included
        if (!$.fn.ckeditor) {
            var ckEditorScriptURI = PrimeFaces.getFacesResource('/ckeditor/ckeditor.js',
                    PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);

            var jQueryAdapterScriptURI = PrimeFaces.getFacesResource('/ckeditor/adapters/jquery.js',
                    PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);

            // load ckeditor
            PrimeFaces.getScript(ckEditorScriptURI, $.proxy(function(data, textStatus) {

                // load jquery adapter
                PrimeFaces.getScript(jQueryAdapterScriptURI, $.proxy(function(data, textStatus) {

                    this.renderDeferred();

                }, this));

            }, this), true);

        } else {
            this.renderDeferred();
        }
	},

	/**
     * Initializes the CKEditor instance. This method will be called when the
     * resources for the CKEditor are loaded.
     * 
     * @private
     */
	_render : function() {
            if (!this.instance && this.initializing === false) {
                this.initializing = true;
                PrimeFaces.info('Rendering CKEditor: ' + this.id);
                // overwrite save button
                this.overwriteSaveButton();

                //remove old instances if required
                var oldInstance = CKEDITOR.instances[this.id];
                if (oldInstance) {
                    try {
                        this.destroyOnUpdate(oldInstance);
                    } catch (err) {
                        if (window.console && console.log) {
                            console.log('CKEditor threw an error while destroying the old instance: ' + err);
                        }
                    }
                }

                //initialize ckeditor after all resources were loaded
                this.jq.ckeditor($.proxy(function() { this.initialized(); }, this), this.options);
                
                if (CKEDITOR.instances[this.id]) {
                    var thisConfig =  CKEDITOR.instances[this.id].config;
                    // Issue #414 enable/disable ACF
                    thisConfig.allowedContent = !this.cfg.advancedContentFilter;
                    // Issue #415: set readOnly attribute to the config file
                    thisConfig.readOnly = this.cfg.readOnly;
                }
                PrimeFaces.info('Finished Rendering CKEditor: ' + this.id);
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

				editor.ui.addButton('Save', {label : editor.lang.save.toolbar, command : 'save', title : editor.lang.save.toolbar });
			}
		};
	},

    /**
     * This method will be called when the CKEditor was initialized.
     *
     * @private
     */
    initialized : function() {
        PrimeFaces.info("Initialized: " + this.id);
        //get instance
        this.instance = this.jq.ckeditorGet();

        //fire initialize event
        this.fireEvent('initialize');

        //GitHub #501 blur immediately
        //CKEDITOR.focusManager._.blurDelay = 0;

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
            this.instance.dirtyFired = false;
        }, this));
        
        // let the widget know we are done initializing
        this.initializing = false;
    },

    /**
     * This method will be called to clean up the old instance on update
     *
     * @private
     */
    destroyOnUpdate: function( instance ) {
        if(instance) {
            instance.fire( 'beforeDestroy' );
    
            if ( instance.filter ) {
                instance.filter.destroy();
                delete instance.filter;
            }
    
            delete instance.activeFilter;
    
            instance.status = 'destroyed';
    
            instance.fire( 'destroy' );
    
            // Plug off all listeners to prevent any further events firing.
            instance.removeAllListeners();
    
            CKEDITOR.remove( instance );
            CKEDITOR.fire( 'instanceDestroyed', null, instance );
        }
    },

    /**
     * This method checks if this editor instance is dirty (content has changed)
     * and fires a dirty event if it was not fired since last entering the editor.
     *
     * @private
     */
    checkDirty : function() {
	if (this.isDirtyEventDefined) {
		if (!this.instance.dirtyFired && this.instance.checkDirty()) { // checkDirty means isDirty
			// fires the dirty event only once!
			this.fireEvent('dirty');
			this.instance.dirtyFired = true;
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
                        console.log('CKEditor threw an error while destroying the old instance: ' + err);
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
;/**
 * PrimeFaces Extensions DynaForm Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFaces.widget.ExtDynaForm = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        if (!cfg.isPostback) {
            this.toggledExtended = false;
        }

        if (cfg.autoSubmit && !PF(cfg.widgetVar)) {
            this.submitForm();
        } else if (cfg.isPostback && this.toggledExtended && this.uuid == cfg.uuid) {
            var rows = this.jq.find("tr.pe-dynaform-extendedrow");
            if (rows.length > 0) {
                if (this.openExtended) {
                    rows.show();
                } else {
                    rows.hide();
                }
            }
        }

        this.uuid = cfg.uuid;
    },

    toggleExtended : function() {
        var rows = this.jq.find("tr.pe-dynaform-extendedrow");
        if (rows.length > 0) {
            rows.toggle();

            this.toggledExtended = true;
            this.openExtended = $(rows[0]).css("display") != "none";
        }
    },

    submitForm : function() {
        this.jq.find(":submit").trigger('click');
    }
});;/**
 * PrimeFaces Extensions ImageRotateAndResize Widget.
 *
 * @author Thomas Andraschko
 */
PrimeFaces.widget.ExtImageRotateAndResize = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init: function (cfg) {
        this.id = cfg.id;
        this.cfg = cfg;

        this.initialized = false;

        this.removeScriptElement(this.id);
    },

    /**
     * Initializes the settings.
     *
     * @private
     */
    initializeLazy: function () {
        if (!this.initialized) {
            this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.cfg.target)[0];
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
    reload: function () {
        this.initialized = false;
        this.initializeLazy();
    },

    /**
     * Rotates the image to the left direction.
     *
     * @param {number} degree The degree.
     */
    rotateLeft: function (degree) {
        this.initializeLazy();

        if ((this.degree - degree) <= 0) {
            this.degree = 360 - ((this.degree - degree) * -1);
        } else {
            this.degree -= degree;
        }

        this.redrawImage(false, true);
    },

    /**
     * Rotates the image to the right direction.
     *
     * @param {number} degree The degree.
     */
    rotateRight: function (degree) {
        this.initializeLazy();

        if ((this.degree + degree) >= 360) {
            this.degree = (this.degree + degree) - 360;
        } else {
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
    resize: function (width, height) {
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
    scale: function (scaleFactor) {
        this.initializeLazy();

        this.newImageWidth = this.newImageWidth * scaleFactor;
        this.newImageHeight = this.newImageHeight * scaleFactor;

        this.redrawImage(true, false);
    },

    /**
     * Restores the default image.
     * It also fires the rotate and resize event with the default values.
     */
    restoreDefaults: function () {
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
    redrawImage: function (fireResizeEvent, fireRotateEvent) {

        var rotation;
        if (this.degree >= 0) {
            rotation = Math.PI * this.degree / 180;
        } else {
            rotation = Math.PI * (360 + this.degree) / 180;
        }

        var cos = Math.cos(rotation);
        var sin = Math.sin(rotation);

        //create canvas instead of img
        var canvas = document.createElement('canvas');

        //new image with new size
        var newImage = new Image();

        newImage.onload = $.proxy(function () {
            //rotate
            canvas.style.width = canvas.width = Math.abs(cos * newImage.width) + Math.abs(sin * newImage.height);
            canvas.style.height = canvas.height = Math.abs(cos * newImage.height) + Math.abs(sin * newImage.width);

            var context = canvas.getContext('2d');
            context.save();

            if (rotation <= Math.PI / 2) {
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

    },

    /**
     * Fires the rotate event.
     *
     * @private
     */
    fireRotateEvent: function () {
        if (this.cfg.behaviors) {
            var callback = this.cfg.behaviors['rotate'];
            if (callback) {
                var options = {
                    params: [
                        {name: this.id + '_degree', value: this.degree}
                    ]
                };

                callback.call(this, options);
            }
        }
    },

    /**
     * Fires the resize event.
     *
     * @private
     */
    fireResizeEvent: function () {
        if (this.cfg.behaviors) {
            var callback = this.cfg.behaviors['resize'];
            if (callback) {
                var options = {
                    params: [
                        {name: this.id + '_width', value: this.newImageWidth},
                        {name: this.id + '_height', value: this.newImageHeight}
                    ]
                };

                callback.call(this, options);
            }
        }
    }
});
;/**
 * PrimeFaces Extensions TriStateManyCheckbox Widget.
 *
 * @author Mauricio Fenoglio
 */
PrimeFaces.widget.ExtTriStateManyCheckbox = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init:function (cfg) {
        this._super(cfg);

        this.outputs = this.jq.find('.ui-chkbox-box:not(.ui-state-disabled)');
        this.inputs = this.jq.find(':text:not(:disabled)');
        this.labels = this.jq.find('label:not(.ui-state-disabled)');
        this.fixedMod = function(number,mod){
            return ((number%mod)+mod)%mod;
        }
        var _self = this;

        this.outputs.mouseover(function () {
            $(this).addClass('ui-state-hover');
        }).mouseout(function () {
            $(this).removeClass('ui-state-hover');
        }).click(function (event) {
            _self.toggle($(this),1);
            if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
        });

        this.labels.click(function (event) {
            var element = $(this), input = $(PrimeFaces.escapeClientId(element.attr('for'))), checkbox = input.parent().next();
            checkbox.click();
            if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
        });

        //adding accesibility
        this.outputs.on('keydown', function(event) {
            switch(event.keyCode){
                case 38:
                    _self.toggle($(this),1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 40:
                    _self.toggle($(this),-1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 39:
                    _self.toggle($(this),1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 37:
                    _self.toggle($(this),-1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
                case 32:
                    _self.toggle($(this),1);
                    if (event.preventDefault) { event.preventDefault(); } else { event.returnValue = false; }
                    break;
            }
        });

        // client behaviors
        if (this.cfg.behaviors) {
            PrimeFaces.attachBehaviors(this.inputs, this.cfg.behaviors);
        }

        // pfs metadata
        this.inputs.data(PrimeFaces.CLIENT_ID_DATA, this.id);
    },

    toggle:function (checkbox,direction) {
        var inputField = checkbox.prev().find(':input');
        if (!checkbox.hasClass('ui-state-disabled')) {
            var oldValue = parseInt(inputField.val());
            var newValue = this.fixedMod((oldValue + direction),3);
            inputField.val(newValue);

            // remove / add def. icon and active classes
            if (newValue == 0) {
                checkbox.removeClass('ui-state-active');
            } else {
                checkbox.addClass('ui-state-active');
            }

            // remove old icon and add the new one
            var iconsClasses = checkbox.data('iconstates');
            checkbox.children().removeClass(iconsClasses[oldValue]).addClass(iconsClasses[newValue]);

            // change title to the new one
            var iconTitles = checkbox.data('titlestates');
            if(iconTitles!=null && iconTitles.length>0){
                checkbox.attr('title', iconTitles[newValue]);
            }

            // fire change event
            inputField.change();
        }
    }
});
;/**
 * Primefaces Extension GChart Widget
 * 
 * @author f.strazzullo
 * @author Melloware
 */
PrimeFaces.widget.ExtGChart = PrimeFaces.widget.BaseWidget.extend({
    init : function(cfg) {

        var that = this;

        this._super(cfg);
        this.chart = cfg.chart ? JSON.parse(cfg.chart) : {
            data : [],
            options : {},
            type : ""
        };
        this.data = this.chart.data;
        this.type = this.chart.type
        this.height = cfg.height;
        this.width = cfg.width;
        this.title = cfg.title;
        this.apiKey = cfg.apiKey;
        this.language = cfg.language;
        this.options = this.chart.options;
        this.input = jQuery(this.jqId + "_hidden");

        google.charts.load('current', {
            packages : [ 'corechart', 'geochart', 'orgchart' ],
            language: this.language,
            // Note: you will need to get a mapsApiKey for your project.
            // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
            mapsApiKey: this.apiKey
        });

        jQuery(document).ready(function() {
            google.charts.setOnLoadCallback(function() {
                that.draw();
            });
        });

    },

    draw : function() {

        var dataTable = google.visualization.arrayToDataTable(this.data);

        var that = this;

        this.options.title = this.title;
        this.options.width = parseInt(this.width, 10);
        this.options.height = parseInt(this.height, 10);

        this.wrapper = new google.visualization.ChartWrapper({
            chartType : this.type,
            dataTable : dataTable,
            options : this.options,
            containerId : this.id
        });

        if (this.cfg.behaviors && this.cfg.behaviors.select) {
            google.visualization.events.addListener(this.wrapper, 'select', function(e) {
                console.log(that.wrapper.getChart().getSelection());
                jQuery(that.jqId + "_hidden").val(JSON.stringify(that.wrapper.getChart().getSelection()));
                that.cfg.behaviors.select.call(jQuery(that.jqId + "_hidden"));
            });
        }

        this.wrapper.draw();

    }

});