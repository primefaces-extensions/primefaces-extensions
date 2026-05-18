/**
 * PrimeFaces SunEditor component
 * API reference http://suneditor.com/sample/html/out/document-editor.html
 *
 * @author Matthieu Valente
 */
PrimeFaces.widget.ExtSunEditor = class extends PrimeFaces.widget.DeferredWidget {

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.input = $(this.jqId);
        this.disabled = (cfg.disabled === undefined) ? false : cfg.disabled;
        this.cfg.strictMode = (cfg.strictMode === undefined) ? true : cfg.strictMode;
        this.cfg.textDirection = this.cfg.rtl ? "rtl" : "ltr";
        this.cfg.plugins = this.cfg.plugins || SUNEDITOR.plugins;
        this.cfg.v2Migration = true;

        this.cfg.exportPDF = {
            apiUrl: this.cfg.exportPDFUrl
                || (PrimeFaces.settings.contextPath + '/api/suneditor/export-pdf')
        };

        if (this.disabled) {
            this.input.attr("disabled", "disabled");
            this.jq.addClass("ui-state-disabled");
            this.cfg.readOnly = true;
        }

        if (this.cfg.toolbar) {
            this.cfg.buttonList = JSON.parse(this.cfg.toolbar.replace(/'/g, "\""));
            this.cfg.buttonList = this.normalizeButtonList(this.cfg.buttonList);
            this.cfg.toolbar = null;
        }

        // user extension to configure SunEditor
        const extender = this.cfg.extender
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        this.cfg.theme = this.resolveTheme(this.cfg.theme);
        this.cfg.events = this.createEvents(this.cfg.events);
        this.renderDeferred();
    }

    /**
     * Initializes the SunEditor instance. This method will be called when the
     * resources for the SunEditor are loaded.
     *
     * @private
     */
    _render() {
        // calculate the language to use
        this.getLanguage();

        // initialize
        this.editor = SUNEDITOR.create(this.input[0], this.cfg);
        if (this.cfg.readOnly) {
            this.editor.$.ui.readOnly(true);
        }
        if (this.disabled) {
            this.editor.$.ui.disable();
        }

        // check if being used in a dialog/sidebar
        this.setupDialogSupport();
      
        // #1845: add marker interface so DefaultCommand is ignored as detected as PF TextEditor
        $('.sun-editor .se-wrapper-wysiwyg').addClass('ql-editor');
    }

    createEvents(events) {
        const $this = this;
        events = events || {};
        const bind = function (name, behavior) {
            const callback = events[name];
            events[name] = function (params) {
                const result = typeof callback === "function" ? callback.call(this, params) : undefined;
                behavior();
                return result;
            };
        };

        bind("onload", function () {
            $this.callBehavior('initialize');
        });
        bind("onChange", function () {
            $this.syncInput();
            $this.callBehavior('change');
        });
        bind("onSave", function () {
            $this.syncInput();
            $this.callBehavior('save');
        });

        [
            ["onScroll", "scroll"],
            ["onMouseDown", "mousedown"],
            ["onClick", "click"],
            ["onInput", "input"],
            ["onKeyDown", "keydown"],
            ["onKeyUp", "keyup"],
            ["onFocus", "focus"],
            ["onBlur", "blur"],
            ["onPaste", "paste"],
            ["onCopy", "copy"],
            ["onCut", "cut"],
            ["onDrop", "drop"]
        ].forEach(function (entry) {
            bind(entry[0], function () {
                $this.callBehavior(entry[1]);
            });
        });

        return events;
    }

    syncInput() {
        if (this.editor) {
            const wysiwyg = this.editor.$.frameContext.get('wysiwyg');
            this.input.val([].map.call(wysiwyg.children, function (node) {
                return node.outerHTML || "";
            }).join(""));
        }
    }

    normalizeButtonList(buttonList) {
        const aliases = {
            "formatBlock": "blockStyle",
            "hiliteColor": "backgroundColor",
            "horizontalRule": "hr"
        };
        const normalize = function (button) {
            return Array.isArray(button) ? button.map(normalize) : aliases[button] || button;
        };

        return buttonList.map(normalize);
    }

    resolveTheme(theme) {
        theme = (theme || "auto").toLowerCase();
        if (theme === "default" || theme === "light") {
            return "";
        }
        return theme === "auto" ? (this.isDarkTheme() ? "dark" : "") : theme;
    }

    isDarkTheme() {
        return PrimeFaces.env.getThemeContrast() === 'dark';
    }

    // @override
    refresh(cfg) {
        this._remove();
        super.refresh(cfg);
    }

    // @override
    destroy() {
        super.destroy();
        this._remove();
    }

    /**
     * Clean up this widget and remove elements from DOM.
     * @private
     */
    _remove() {
        if (this.editor) {
            this.editor.destroy();
            this.editor = null;
        }
    }

    /**
     * Sets up support for using the editor within an overlay dialog.
     * @private
     */
    setupDialogSupport() {
        const dlg = this.input[0].closest('.ui-dialog, .ui-sidebar');
        if (dlg) {
            const dialog = $(dlg);
            dialog.find('.sun-editor .se-toolbar').zIndex(9999);
            dialog.find('.sun-editor .se-controller').zIndex(9999);
            dialog.find('.sun-editor .se-line-breaker').zIndex(9999);
            dialog.find('.sun-editor .se-line-breaker-component').zIndex(9999);
            dialog.find('.sun-editor .se-wrapper').zIndex(9998);
        }
    }

    /**
     * Calculates the correct language or defaults to English if not found.
     */
    getLanguage() {
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
        // if all else fails, default to US English
        if (!language) {
            language = window.SUNEDITOR_LANG['en'];
        }
        this.cfg.lang = language;
    }

    /**
     * Clears the entire text of the editor.
     */
    clear() {
        if (this.editor) {
            this.editor.$.frameContext.get('wysiwyg').innerHTML = '';
            this.syncInput();
        }
    }

    /**
     * Enables this text editor so that text can be entered.
     */
    enable() {
        if (this.editor) {
            this.editor.$.ui.enable();
        }
        PrimeFaces.utils.enableInputWidget(this.jq, this.input);
        this.disabled = false;
    }

    /**
     * Disables this text editor so that no text can be entered or removed.
     */
    disable() {
        if (this.editor) {
            this.editor.$.ui.disable();
        }
        PrimeFaces.utils.disableInputWidget(this.jq, this.input);
        this.disabled = true;
    }
};
