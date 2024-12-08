/**
 * PrimeFaces Extensions BlockUI Widget.
 */
PrimeFaces.widget.ExtBlockUI = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);

        this.source = cfg.source;
        this.target = cfg.target;
        this.contentId = cfg.content;
        this.contentExtern = cfg.contentExtern;
        this.namingContSep = cfg.namingContSep;
        this.eventRegEx = cfg.regEx;
        this.css = cfg.css;
        this.overlayCSS = cfg.overlayCSS;
        this.timeout = cfg.timeout;
        this.centerX = cfg.centerX;
        this.centerY = cfg.centerY;
        this.fadeIn = cfg.fadeIn;
        this.fadeOut = cfg.fadeOut;
        this.showOverlay = cfg.showOverlay;
        this.focusInput = cfg.focusInput;

        if (cfg.autoShow) {
            this.setupAjaxHandlers();
        }

        // global settings
        $.blockUI.defaults.theme = true;
        $.blockUI.defaults.ignoreIfBlocked = true;
    }

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    refresh(cfg) {
        this._remove();
        super.refresh(cfg);
    }

    /**
     * @override
     * @inheritdoc
     */
    destroy() {
        super.destroy();
        this._remove();
    }

    /**
     * Clean up this widget and remove elements from DOM.
     * @private
     */
    _remove() {
        this.unblock();
        $(document).off('pfAjaxSend.' + this.id + ' pfAjaxUpdated.' + this.id + ' pfAjaxComplete.' + this.id);
    }

    /* public access */
    setupAjaxHandlers() {
        let $this = this;
        let $document = $(document);

        $document.on('pfAjaxSend.' + this.id, function(e, xhr, settings) {
            // get IDs of the sources
            let source = PrimeFaces.expressions.SearchExpressionFacade.resolveComponents(this.jq, $this.source);

            // first, check if event should be handled
            if ($this.isAppropriateEvent(source, settings)) {
                $this.block();
            }
        });

        $document.on('pfAjaxComplete.' + this.id, function(e, xhr, settings) {
            // get IDs of the sources
            let source = PrimeFaces.expressions.SearchExpressionFacade.resolveComponents(this.jq, $this.source);

            // first, check if event should be handled
            if ($this.isAppropriateEvent(source, settings)) {
                $this.unblock();
            }
        });
    }

    block() {
        let opt;

        if (this.target) {
            let targetEl = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, this.target);

            // second, check if the target element has been found
            if (targetEl.length > 0) {
                // block the target element
                opt = this.getOptions();
                if (opt) {
                    targetEl.block(opt);
                } else {
                    targetEl.block();
                }

                // get the current counter
                let blocksCount = targetEl.data("blockUI.blocksCount");
                if (typeof blocksCount === 'undefined') {
                    blocksCount = 0;
                }

                // increase the counter
                targetEl.data("blockUI.blocksCount", blocksCount + 1);
                targetEl.attr('aria-busy', true);
            }
        } else {
            // block the entire page
            opt = this.getOptions();
            if (opt) {
                $.blockUI(opt);
            } else {
                $.blockUI();
            }
        }
    }

    unblock() {
        if (this.target) {
            let targetEl = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, this.target);

            // second, check if the target element has been found
            if (targetEl.length > 0) {
                // get the current counter
                let blocksCount = targetEl.data("blockUI.blocksCount");

                // check the counter
                if (typeof blocksCount !== 'undefined') {
                    if (blocksCount == 1) {
                        // unblock the target element and reset the counter
                        targetEl.unblock();
                        targetEl.data("blockUI.blocksCount", 0);
                        targetEl.attr('aria-busy', false);
                    } else if (blocksCount > 1) {
                        // only decrease the counter
                        targetEl.data("blockUI.blocksCount", blocksCount - 1);
                    }
                }
            }
        } else {
            $.unblockUI();
        }
    }

    /* private access */

    getOptions() {
        let opt = null;

        if (this.contentId != null) {
            opt = {};
            if (this.contentExtern) {
                opt.message = $(this.contentId).clone().show().wrap('<div>').parent().html();
            } else {
                opt.message = $(this.contentId).html();
            }

            if (this.css) {
                opt.themedCSS = this.css;
            }

            if (this.overlayCSS) {
                opt.overlayCSS = this.overlayCSS;
            }

            if (this.timeout) {
                opt.timeout = this.timeout;
            }

            opt.centerX = this.centerX;
            opt.centerY = this.centerY;
            opt.fadeIn = this.fadeIn;
            opt.fadeOut = this.fadeOut;
            opt.showOverlay = this.showOverlay;
            opt.focusInput = this.focusInput;
        }

        return opt;
    }

    isAppropriateEvent(source, settings) {
        if (typeof settings === 'undefined' || settings == null || settings.source == null ||
            typeof settings.data === 'undefined' || settings.data == null) {
            return false;
        }

        // check if settings.source is an object and extract id if yes.
        let sourceId;
        if (Object.prototype.toString.call(settings.source) === "[object String]") {
            sourceId = settings.source;
        } else {
            let idx = settings.source.id.lastIndexOf(this.namingContSep);
            if (idx == -1) {
                sourceId = settings.source.id;
            } else {
                sourceId = settings.source.id.substring(0, idx);
            }
        }

        if ($.inArray(sourceId, source) == -1) {
            return false;
        }

        // split options around ampersands
        let params = settings.data.split(/&/g);

        // loop over the ajax options and try to match events
        for (let i = 0; i < params.length; i++) {
            if (this.eventRegEx.test(params[i])) {
                return true;
            }
        }

        return false;
    }
};