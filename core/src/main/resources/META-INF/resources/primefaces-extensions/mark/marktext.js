/**
 * PrimeFaces Extensions MarkText Widget
 *
 * @since 15.0.13
 */
PrimeFaces.widget.ExtMarkText = class extends PrimeFaces.widget.BaseWidget {

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.widget.BaseWidget.cfg} cfg
     */
    init(cfg) {
        super.init(cfg);

        this.value = cfg.value;
        this.forValue = cfg.forValue;
        this.caseSensitive = cfg.caseSensitive;
        this.separateWordSearch = cfg.separateWordSearch;
        this.accuracy = cfg.accuracy;

        this.markInstance = null;

        this._bindEvents();
        this._initMark();
    }

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.widget.BaseWidget.cfg} cfg
     */
    refresh(cfg) {
        this._cleanUp();
        super.refresh(cfg);
    }

    /**
     * @override
     * @inheritdoc
     */
    destroy() {
        this._cleanUp();
        super.destroy();
    }

    /**
     * Initializes the mark.js instance.
     * @private
     */
    _initMark() {
        if (this.value && this.forValue) {
            var selector = this.forValue;
            var targetElement = null;
            
            try {
                targetElement = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, selector);
            } catch (e) {
                // Fallback to direct ID selection if SearchExpressionFacade fails
                targetElement = $(PrimeFaces.escapeClientId(selector));
            }
            
            if (targetElement && targetElement.length > 0) {
                var options = {
                    caseSensitive: this.caseSensitive,
                    separateWordSearch: this.separateWordSearch,
                    accuracy: this.accuracy,
                    className: this.cfg.className
                };

                this.markInstance = new Mark(targetElement[0]);
                this.markInstance.mark(this.value, options);
            }
        }
    }

    /**
     * Binds events.
     * @private
     */
    _bindEvents() {
        var $this = this;

        if (this.cfg.behaviors) {
            var markBehavior = this.cfg.behaviors['mark'];
            if (markBehavior) {
                this.jq.on('mark', function(e) {
                    markBehavior.call($this, e);
                });
            }
        }
    }

    /**
     * Cleans up the mark instance.
     * @private
     */
    _cleanUp() {
        if (this.markInstance) {
            this.markInstance.unmark();
            this.markInstance = null;
        }
    }

    /**
     * Updates the mark with new value.
     * @param {string} value The new search value.
     */
    updateMark(value) {
        this.value = value;
        this._cleanUp();
        this._initMark();
    }
};