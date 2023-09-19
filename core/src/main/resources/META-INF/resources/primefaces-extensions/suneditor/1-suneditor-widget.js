/**
 * PrimeFaces SunEditor component
 * API reference http://suneditor.com/sample/html/out/document-editor.html
 *
 * @author Matthieu Valente
 */
PrimeFaces.widget.ExtSunEditor = PrimeFaces.widget.DeferredWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.input = $(this.jqId);
        this.disabled = (cfg.disabled === undefined) ? false : cfg.disabled;

        if (this.disabled) {
            this.input.attr("disabled", "disabled");
            this.jq.addClass("ui-state-disabled");
            this.cfg.readOnly = true;
        }

        if (this.cfg.toolbar) {
            this.cfg.buttonList = JSON.parse(this.cfg.toolbar.replace(/'/g, "\""));
            this.cfg.toolbar = null;
        }

        // user extension to configure handsontable
        const extender = this.cfg.extender
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        this.renderDeferred();
    },

    /**
     * Initializes the SunEditor instance. This method will be called when the
     * resources for the SunEditor are loaded.
     *
     * @private
     */
    _render: function () {
        const $this = this;

        //initialize
        this.getLanguage();
        this.editor = SUNEDITOR.create(this.id, this.cfg);
        this.editor.onload = function (core, reload) {
            $this.callBehavior('initialize');
        }
        if (this.cfg.readOnly) {
            this.editor.readOnly(true);
        }
        if (this.disabled) {
            this.editor.disable();
        }

        //update input on change
        this.editor.onChange = function (contents, core) {
            $this.input.val([].map.call(core.context.element.wysiwyg.children, function (node) {
                return node.outerHTML || "";
            }).join(""));
            $this.callBehavior('change');
        };

        this.editor.onScroll = function (contents, core) {
            $this.callBehavior('scroll')
        };
        this.editor.onMouseDown = function (contents, core) {
            $this.callBehavior('mousedown')
        };
        this.editor.onClick = function (e, core) {
            $this.callBehavior('click')
        }
        this.editor.onInput = function (e, core) {
            $this.callBehavior('input')
        }
        this.editor.onKeyDown = function (e, core) {
            $this.callBehavior('keydown')
        }
        this.editor.onKeyUp = function (e, core) {
            $this.callBehavior('keyup')
        }
        this.editor.onFocus = function (e, core) {
            $this.callBehavior('focus')
        }
        this.editor.onBlur = function (e, core) {
            $this.callBehavior('blur')
        }
        this.editor.onPaste = function (e, cleanData, maxCharCount, core) {
            $this.callBehavior('paste')
        }
        this.editor.onCopy = function (e, clipboardData, core) {
            $this.callBehavior('copy')
        }
        this.editor.onCut = function (e, clipboardData, core) {
            $this.callBehavior('cut')
        }
        this.editor.onDrop = function (e, cleanData, maxCharCount, core) {
            $this.callBehavior('drop')
        }
        this.editor.onSave = function (contents, core) {
            $this.callBehavior('save')
        };

        // check if being used in dialog
        this.setupDialogSupport();
    },

    // @override
    refresh: function (cfg) {
        if (this.editor) {
            this.editor.destroy();
            this.editor = null;
        }
        this._super(cfg);
    },

    // @override
    destroy: function () {
        this._super();
        if (this.editor) {
            this.editor.destroy();
            this.editor = null;
        }
    },

    /**
     * Sets up support for using the editor within an overlay dialog.
     * @private
     */
    setupDialogSupport: function() {
        const dlg = this.input[0].closest('.ui-dialog');
        if (dlg) {
            const dialog = $(dlg);
            dialog.find('.sun-editor .se-toolbar').zIndex(9999);
            dialog.find('.sun-editor .se-wrapper').zIndex(9998);
        }
    },

    /**
     * Calculates the correct language or defaults to English if not found.
     */
    getLanguage: function () {
        let localeKey = this.cfg.locale ? this.cfg.locale : PrimeFaces.settings.locale;
        let language = 'en';
        if (localeKey && window.SUNEDITOR_LANG) {
            localeKey = localeKey.toLowerCase();
            language = window.SUNEDITOR_LANG[localeKey];
            // try and strip specific language from nl_BE to just nl
            let splitLocale = localeKey.split('_');
            if (!language) {
                language = window.SUNEDITOR_LANG[splitLocale[0]];
            }
            // try and strip specific language from nl_BE to just BE
            if (!language && splitLocale.length > 1) {
                language = window.SUNEDITOR_LANG[splitLocale[1]];
            }
        }
        // if all else fails default to US English
        if (!language) {
            language = window.SUNEDITOR_LANG['en'];
        }
        this.cfg.lang = language;
    },

    /**
     * Clears the entire text of the editor.
     */
    clear: function () {
        if (this.editor) {
            this.editor.setContents('');
        }
    },

    /**
     * Enables this text editor so that text can be entered.
     */
    enable: function () {
        if (this.editor) {
            this.editor.enable();
        }
        PrimeFaces.utils.enableInputWidget(this.jq, this.input);
        this.disabled = false;
    },

    /**
     * Disables this text editor so that no text can be entered or removed.
     */
    disable: function () {
        if (this.editor) {
            this.editor.disable();
        }
        PrimeFaces.utils.disableInputWidget(this.jq, this.input);
        this.disabled = true;
    }
});