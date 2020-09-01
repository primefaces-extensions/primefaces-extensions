/**
 * Resolves the resources for the CKEditor.
 *
 * @param {string} resource The requested resource from CKEditor.
 * @returns {string} The faces resource URL.
 */
CKEDITOR_GETURL = function (resource) {
    var facesResource;

    // GitHub #545 IE11 support
    if (PrimeFaces.env.isIE()) {
        if (!String.prototype.startsWith) {
            String.prototype.startsWith = function (searchString, position) {
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
                    appendedResource = appendedResource.replace(/&\w+=\d+(?:\.\d+)*/, '');
                }

                var resourceIdentiferPosition = facesResource.indexOf(PrimeFaces.RESOURCE_IDENTIFIER);

                if (PrimeFacesExt.isExtensionMapping()) {
                    var extension = '.' + PrimeFacesExt.getResourceUrlExtension();
                    var extensionMappingPosition = facesResource.lastIndexOf(extension);
                    if (extensionMappingPosition === -1) {
                        extensionMappingPosition = facesResource.lastIndexOf('.xhtml');
                        if (extensionMappingPosition === -1) {
                            extensionMappingPosition = facesResource.lastIndexOf('.jsf');
                        }
                    }

                    if (extensionMappingPosition === -1) {
                        console.error('Could not find .jsf or .xhtml extension!');
                    }

                    //extract resource
                    var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFaces.RESOURCE_IDENTIFIER.length, extensionMappingPosition);

                    facesResource = PrimeFaces.resources.getFacesResource(extractedResource + appendedResource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
                } else {
                    var questionMarkPosition = facesResource.indexOf('?');

                    //extract resource
                    var extractedResource = facesResource.substring(resourceIdentiferPosition + PrimeFaces.RESOURCE_IDENTIFIER.length, questionMarkPosition);

                    facesResource = PrimeFaces.resources.getFacesResource(extractedResource + appendedResource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
                }
            } else {
                facesResource = resource;
            }
        } else {
            if (resource.indexOf(PrimeFaces.RESOURCE_IDENTIFIER) === -1) {
                facesResource = PrimeFaces.resources.getFacesResource('ckeditor/' + resource, PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);
            } else {
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
    init: function (cfg) {
        this._super(cfg);

        this.instance = null;
        this.initializing = false;
        this.editorState = this.editorState || {
            scrollTop: 0,
            scrollLeft: 0,
            mode: 'wysiwyg'
        };

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
                this.options.toolbar = PrimeFaces.csp.evalResult(this.cfg.toolbar);
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
            var ckEditorScriptURI = PrimeFaces.resources.getFacesResource('/ckeditor/ckeditor.js',
                PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);

            var jQueryAdapterScriptURI = PrimeFaces.resources.getFacesResource('/ckeditor/adapters/jquery.js',
                PrimeFacesExt.RESOURCE_LIBRARY, PrimeFacesExt.VERSION);

            // load ckeditor
            PrimeFacesExt.getScript(ckEditorScriptURI, $.proxy(function (data, textStatus) {

                // load jquery adapter
                PrimeFacesExt.getScript(jQueryAdapterScriptURI, $.proxy(function (data, textStatus) {

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
    _render: function () {
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
            this.jq.ckeditor($.proxy(function () {
                this.initialized();
            }, this), this.options);

            if (CKEDITOR.instances[this.id]) {
                var thisConfig = CKEDITOR.instances[this.id].config;
                // Issue #414 enable/disable ACF
                thisConfig.allowedContent = !this.cfg.advancedContentFilter;
                // Issue #415: set readOnly attribute to the config file
                thisConfig.readOnly = this.cfg.readOnly;
                // Issue: #763: whether to use browser spell check
                thisConfig.disableNativeSpellChecker = this.cfg.disableNativeSpellChecker;
                // Issue #779: enter/shift enter Mode
                thisConfig.enterMode = PrimeFaces.csp.evalResult(this.cfg.enterMode);
                thisConfig.shiftEnterMode = PrimeFaces.csp.evalResult(this.cfg.shiftEnterMode);
                // Issue #151: font and font size
                if (this.cfg.font) {
                    thisConfig.font_defaultLabel = this.cfg.font;
                    thisConfig.fontSize_defaultLabel = this.cfg.fontSize;
                }
            }
            PrimeFaces.info('Finished Rendering CKEditor: ' + this.id);
        }
    },

    /**
     * Overwrites the save button.
     *
     * @private
     */
    overwriteSaveButton: function () {
        //overwrite save button
        CKEDITOR.plugins.registered['save'] = {
            init: function (editor) {

                //get widget
                var widget = PF(editor.config.widgetVar);
                var command = editor.addCommand('save', {
                    modes: {wysiwyg: 1, source: 1},
                    exec: function (editor) {
                        widget.callBehavior('save');
                    }
                });

                editor.ui.addButton('Save', {label: editor.lang.save.toolbar, command: 'save', title: editor.lang.save.toolbar});
            }
        };
    },

    /**
     * This method will be called when the CKEditor was initialized.
     *
     * @private
     */
    initialized: function () {
        PrimeFaces.info("Initialized: " + this.id);
        //get instance
        this.instance = this.jq.ckeditorGet();

        //fire initialize event
        this.fireEvent('initialize');

        //GitHub #501 blur immediately
        //CKEDITOR.focusManager._.blurDelay = 0;

        // Restore scroll position and mode after an AJAX update, and bind events again
        this.restoreMode();
        this.restoreScrollPosition();
        this.bindEditableEvents();

        //register blur and focus event
        this.instance.on('blur', $.proxy(function () {
            this.fireEvent('blur');
        }, this));
        this.instance.on('focus', $.proxy(function () {
            this.fireEvent('focus');
        }, this));

        //changes to WYSIWYG mode
        this.instance.on('contentDom', $.proxy(function () {
            this.bindEditableEvents();
            this.fireEvent('wysiwygMode');
        }, this));

        //changes to source mode
        this.instance.on('mode', $.proxy(function (event) {
            this.saveMode();
            this.saveScrollPosition();
            if (this.instance.mode != 'source') {
                return;
            }
            this.bindEditableEvents();
            this.fireEvent('sourceMode');
        }, this));

        //check dirty- and changed events
        this.isDirtyEventDefined = this.hasBehavior('dirty');
        this.isChangeEventDefined = this.hasBehavior('change');

        this.instance.on('blur', $.proxy(function () {
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
    destroyOnUpdate: function (instance) {
        if (instance) {
            instance.fire('beforeDestroy');

            if (instance.filter) {
                instance.filter.destroy();
                delete instance.filter;
            }

            delete instance.activeFilter;

            instance.status = 'destroyed';

            instance.fire('destroy');

            // Plug off all listeners to prevent any further events firing.
            instance.removeAllListeners();

            CKEDITOR.remove(instance);
            CKEDITOR.fire('instanceDestroyed', null, instance);
        }
    },

    /**
     * This method checks if this editor instance is dirty (content has changed)
     * and fires a dirty event if it was not fired since last entering the editor.
     *
     * @private
     */
    checkDirty: function () {
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
    checkChange: function () {
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
    fireEvent: function (eventName) {
        this.callBehavior(eventName);
    },

    /**
     * Destroys the CKEditor instance.
     */
    destroy: function () {
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
     * Saves the current scroll position of the editor after a scroll event. Used to restore the position after an
     * AJAX update.
     * @private
     */
    saveScrollPosition: function () {
        if (this.instance) {
            if (this.instance.mode === 'source') {
                // Source mode -> use scroll position of the textarea element
                var editable = this.instance.editable();
                if (editable && editable.$) {
                    this.editorState.scrollTop = editable.$.scrollTop;
                    this.editorState.scrollLeft = editable.$.scrollLeft;
                }
            } else {
                // WYSIWYG mode -> use scroll position of html element of the iframe
                if (this.instance.document && this.instance.document.$ && this.instance.document.$.documentElement) {
                    this.editorState.scrollTop = this.instance.document.$.documentElement.scrollTop;
                    this.editorState.scrollLeft = this.instance.document.$.documentElement.scrollLeft;
                }
            }
        }
    },

    /**
     * Restores the saved scroll position of the editor after an AJAX update.
     * @private
     */
    restoreScrollPosition: function () {
        if (this.instance) {
            if (this.instance.mode === 'source') {
                // Source mode -> use scroll position of the textarea element
                var editable = this.instance.editable();
                if (editable && editable.$) {
                    if (typeof this.editorState.scrollTop === "number") {
                        editable.$.scrollTop = this.editorState.scrollTop;
                    }
                    if (typeof this.editorState.scrollLeft === "number") {
                        editable.$.scrollLeft = this.editorState.scrollLeft;
                    }
                }
            } else {
                // WYSIWYG mode -> use scroll position of html element of the iframe
                if (this.instance.document && this.instance.document.$ && this.instance.document.$.documentElement) {
                    if (typeof this.editorState.scrollTop === "number") {
                        this.instance.document.$.documentElement.scrollTop = this.editorState.scrollTop;
                    }
                    if (typeof this.editorState.scrollLeft === "number") {
                        this.instance.document.$.documentElement.scrollLeft = this.editorState.scrollLeft;
                    }
                }
            }
        }
    },

    /**
     * Saves whether the editor is currently is source or WYSIWYG mode.
     * @private
     */
    saveMode: function () {
        if (this.instance) {
            this.editorState.mode = this.instance.mode === "source" ? "source" : "wysiwyg";
        }
    },

    /**
     * Restores the editor mode after an AJAX update.
     * @private
     */
    restoreMode: function () {
        // Editor starts out in WYSIWYG mode, only switch if it was in source mode
        if (this.instance && this.editorState.mode === 'source') {
            this.instance.setMode('source');
        }
    },

    /**
     * Registers the event listeners for the CKEditor `editable` instance. The editable changes when the mode changes,
     * so they need to be registered again.
     * @private
     */
    bindEditableEvents: function () {
        if (this.instance && this.instance.editable()) {
            var editable = this.instance.editable();
            editable.attachListener(editable, 'cut', $.proxy(function (event) {
                this.checkChange();
                this.checkDirty();
            }, this));
            editable.attachListener(editable, 'paste', $.proxy(function (event) {
                this.checkChange();
                this.checkDirty();
            }, this));
            editable.attachListener(editable, 'keyup', $.proxy(function (event) {
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

            if (this.scrollListener) {
                this.scrollListener.removeListener();
                this.scrollListener = undefined;
            }
            if (this.instance.mode === 'source') {
                this.scrollListener = editable.attachListener(editable, 'scroll', $.proxy(function () {
                    this.saveScrollPosition();
                }, this));
            } else {
                if (editable.getDocument()) {
                    this.scrollListener = editable.attachListener(editable.getDocument(), 'scroll', $.proxy(function () {
                        this.saveScrollPosition();
                    }, this));
                }
            }
        }
    },

    /**
     * Checks if the editor is in dirty state.
     */
    isDirty: function () {
        if (!this.instance) {
            return false;
        }

        return this.instance.checkDirty();
    },

    /**
     * Sets readOnly to the CKEditor.
     */
    setReadOnly: function (readOnly) {
        this.instance.setReadOnly(readOnly !== false);
    },

    /**
     * Checks if the CKEditor is readOnly.
     */
    isReadOnly: function () {
        return this.instance.readOnly;
    },

    /**
     * Indicates that the editor instance has focus.
     */
    hasFocus: function () {
        return this.instance.focusManager.hasFocus;
    },

    /**
     * Returns the CKEditor instance.
     */
    getEditorInstance: function () {
        return this.instance;
    }
});
