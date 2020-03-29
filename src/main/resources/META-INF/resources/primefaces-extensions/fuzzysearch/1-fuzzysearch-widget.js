PrimeFaces.widget.FuzzySearch = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);

        this.input = $(this.jqId + '_fuzzysearch-search-input');
        this.results = $(this.jqId + '_fuzzysearch-search-results');
        this.keys = JSON.parse(cfg.keys);
        this.datasource = JSON.parse(cfg.value);

        //Visual effects
        PrimeFaces.skinInput(this.input);

        this.bindKeyEvents();
        this.bindDynamicEvents();
    },

    bindKeyEvents: function () {
        var $this = this;

        this.input.on('input', function (e) {
            $this.search($this.input.val());
        });
    },

    bindDynamicEvents: function () {
        var $this = this;

        $this.results.on('click', function (e) {
            if ($this.cfg.onSelect) {
                $this.cfg.onSelect.call(e);
            }
        });
    },

    search: function (query) {
        var $this = this;

        var resultStyle = $this.jq.attr('data-result-style') || '';
        var resultStyleClass = $this.jq.attr('data-result-style-class') || '';

        var itemDisplayMarkup = '';
        if (query) { // when any input entered
            $.each(fuzzysearch.go(query, $this.datasource, {keys: $this.keys}), function (index, value) {
                itemDisplayMarkup += '<div';
                itemDisplayMarkup += (resultStyle === '' ? '' : ' style="' + resultStyle + '"');
                itemDisplayMarkup += ' class="ui-fuzzysearch-item';
                itemDisplayMarkup += (resultStyleClass === '' ? '' : ' ' + resultStyleClass) + '">';
                itemDisplayMarkup += value.obj.name;
                itemDisplayMarkup += '</div>';
            });
        } else { // when there is no input
            $.each($this.datasource, function (index, value) {
                itemDisplayMarkup += '<div';
                itemDisplayMarkup += (resultStyle === '' ? '' : ' style="' + resultStyle + '"');
                itemDisplayMarkup += ' class="ui-fuzzysearch-item';
                itemDisplayMarkup += (resultStyleClass === '' ? '' : ' ' + resultStyleClass) + '">';
                itemDisplayMarkup += value.name;
                itemDisplayMarkup += '</div>';
            });
        }

        $this.results.empty();
        $this.results.append(itemDisplayMarkup);
    }

});