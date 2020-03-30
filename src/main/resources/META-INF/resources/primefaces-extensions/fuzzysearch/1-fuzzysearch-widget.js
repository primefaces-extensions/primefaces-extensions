/**
 * PrimeFaces Fuzzy Search Widget
 */
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
        this.resultStyle = cfg.resultStyle;
        this.resultStyleClass = cfg.resultStyleClass;
        this.items = this.results.children();

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

        this.items.on('click', function (e) {
            // client behaviors
            if ($this.cfg.behaviors) {
                var onSelectBehavior = $this.cfg.behaviors['select'];
                //Call user onSelect callback
                if (onSelectBehavior) {
                    var result = onSelectBehavior.call(this, e);
                    if (result === false)
                        return false;
                }
            }
        });
    },

    search: function (query) {
        var $this = this;

        var resultStyle = this.cfg.resultStyle || '';
        var resultStyleClass = this.cfg.resultStyleClass || '';

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
    },

    disable: function () {
        this.input.prop('disabled', true).addClass('ui-state-disabled');
    },

    enable: function () {
        this.input.prop('disabled', false).removeClass('ui-state-disabled');
    }

});