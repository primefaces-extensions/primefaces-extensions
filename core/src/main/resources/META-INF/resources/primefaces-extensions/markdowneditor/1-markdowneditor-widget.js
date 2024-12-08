/**
 * __PrimeFaces MarkdownEditor Widget__
 *
 * MarkdownEditor is an extension to standard inputTextarea with autoComplete, autoResize, remaining characters counter
 * and theming features.
 *
 * @prop {object} editor Control handle for the underlying Markdown Editor
 * @prop {any | jQuery | HTMLElement} container Container for the DIV of EasyMDE.
 *
 * @interface {PrimeFaces.widget.MarkdownEditorCfg} cfg The configuration for the {@link  MarkdownEditor| MarkdownEditor widget}.
 * You can access this configuration via {@link PrimeFaces.widget.BaseWidget.cfg|BaseWidget.cfg}. Please note that this
 * configuration is usually meant to be read-only and should not be modified.
 * @extends {PrimeFaces.widget.DeferredWidgetCfg} cfg
 *
 * @prop {boolean} cfg.direction Right To Left handling.
 */
PrimeFaces.widget.ExtMarkdownEditor = class extends PrimeFaces.widget.BaseWidget {

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    init(cfg) {
        super.init(cfg);
        this.textArea = this.jq[0];
        this.bindOnRemove();
        this._render();
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
     * @include
     * @override
     * @protected
     * @inheritdoc
     */
    _render() {
        let $this = this;
        // user extension to configure markdown editor
        let extender = this.cfg.extender;
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        const options = this.cfg;
        options.element = this.textArea;
        options.forceSync = true;
        options.spellchecker = false; // prevent CSP issues https://github.com/Ionaru/easy-markdown-editor/issues/546

        if (this.cfg.toolbar) {
            if (this.cfg.toolbar === "false") {
                options.toolbar = false;
            } else {
                options.toolbar = this.cfg.toolbar.split(',').map(function (item) {
                    return item.trim();
                });
            }
        } else {
            // default toolbar if none given
            options.toolbar = ["bold", "italic", "strikethrough", "|", "heading-1", "heading-2", "heading-3",
                "|", "code", "quote", "unordered-list", "ordered-list",
                "|", "clean-block", "link", "image", "table", "horizontal-rule",
                "|", "preview", "side-by-side", "fullscreen", "|", "guide"];
        }
        this.editor = new EasyMDE(options);

        // pass all AJAX behaviors to the editor
        for (let key in this.cfg.behaviors) {
            if (this.cfg.behaviors.hasOwnProperty(key)) {
                this.editor.codemirror.on(key, () => {
                    $this.callBehavior(key);
                });
            }
        }

        switch (this.cfg.mode) {
            case 'sidebyside':
                this.editor.toggleSideBySide();
                break;
            case 'preview':
                this.editor.togglePreview();
                break;
            case 'fullscreen':
                this.editor.toggleFullScreen();
                break;
        }

        // apply classes from textarea to easy MDE
        this.applyStyles();
    }

    /**
     * Clean up this widget and remove elements from DOM.
     * @private
     */
    _remove() {
        if (this.editor) {
            this.editor.toTextArea();
            this.editor = null;
        }
    }

    /**
     * When the underlying textArea is removed by an AJAX refresh we must clean up the editor.
     * Clean up this widget and remove events from the DOM.
     */
    bindOnRemove() {
        // when the underlying textArea is removed by an AJAX refresh we must clean up the editor.
        let $this = this;
        this.jq.off('remove.markdown').on('remove.markdown', function () {
            $this.destroy();
        });
    }

    /**
     * Apply classes from textarea to easy MDE.
     */
    applyStyles() {
        this.container = this.jq.next('.EasyMDEContainer');
        if (this.container.length > 0) {
            let classes = this.jq.attr('class');
            this.container.addClass(classes);
        }
    }

    /**
     * Clears the entire text of the editor.
     */
    clear() {
        this.setValue('');
    }

    /**
     * Sets the value of the editor.
     * @param {string} value the value to set
     */
    setValue(value) {
        if (this.editor) {
            this.editor.value(value);
        }
    }

    /**
     * Gets the value of the editor.
     * @return {string} the value of the editor
     */
    getValue() {
        if (this.editor) {
            return this.editor.value();
        }
    }

    /**
     * Disables this input so that the user cannot enter a value anymore.
     */
    disable() {
        PrimeFaces.utils.disableInputWidget(this.container, this.jq);
    }

    /**
     * Enables this input so that the user can enter a value.
     */
    enable() {
        PrimeFaces.utils.enableInputWidget(this.container, this.jq);
    }
};