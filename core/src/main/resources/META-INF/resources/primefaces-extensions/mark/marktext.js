/**
 * PrimeFaces Extensions MarkText Widget
 *
 * @since 16.0.0
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
        this.synonyms = cfg.synonyms ? (typeof cfg.synonyms === 'string' ? JSON.parse(cfg.synonyms) : cfg.synonyms) : {};

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
            var $this = this;
            var selector = this.forValue;
            var targetElement = null;

            try {
                targetElement = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, selector);
            } catch (e) {
                // Fallback to direct ID selection if SearchExpressionFacade fails
                targetElement = $(PrimeFaces.escapeClientId(selector));
            }

            if (targetElement && targetElement.length > 0) {
                var matchedTerms = [];
                var positions = [];

                var options = {
                    caseSensitive: this.caseSensitive,
                    separateWordSearch: this.separateWordSearch,
                    accuracy: this.accuracy,
                    className: this.cfg.className,
                    synonyms: this.synonyms,
                    each: function(element) {
                        var term = $(element).text();
                        if (matchedTerms.indexOf(term) === -1) {
                            matchedTerms.push(term);
                        }

                        var range = document.createRange();
                        range.selectNodeContents(targetElement[0]);
                        range.setEndBefore(element);
                        var start = range.toString().length;

                        var $mark = $(element);
                        var nearestParentWithId = $mark.closest('[id]');
                        var nodeId = nearestParentWithId.length ? nearestParentWithId.attr('id') : null;

                        positions.push({
                            term: term,
                            start: start,
                            length: term.length,
                            element: element.outerHTML,
                            nodeId: nodeId
                        });
                    },
                    done: function(totalMatches) {
                        $this.jq.trigger('marktext.mark', [matchedTerms, positions]);
                    }
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

        this.jq.on('marktext.mark', function(e, matchedTerms, positions) {
            if ($this.hasBehavior('mark')) {
                var ext = {
                    params: [
                        { name: $this.id + '_value', value: $this.value },
                        { name: $this.id + '_matchedTerms', value: JSON.stringify(matchedTerms) },
                        { name: $this.id + '_positions', value: JSON.stringify(positions) }
                    ]
                };

                $this.callBehavior('mark', ext);
            }
        });
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