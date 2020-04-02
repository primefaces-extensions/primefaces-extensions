/**
 * PrimeFaces FuzzySearch Widget
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
        this.keys = ['label']; //JSON.parse(this.cfg.keys); // TODO i am not sure how to implement it without using FuzzySearchKey
        this.datasource = JSON.parse(this.cfg.datasource);
        this.resultStyle = this.cfg.resultStyle;
        this.resultStyleClass = this.cfg.resultStyleClass;
        this.items = this.results.children();
        this.listItemsAtTheBeginning = this.cfg.listItemsAtTheBeginning;

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

        this.items.on('click', function (event) {
            var item = $(this);
            var itemValue = item.attr('data-item-value');

            $this.invokeItemSelectBehavior(event, itemValue);
        });
    },

    invokeItemSelectBehavior: function (event, itemValue) {
        if (this.hasBehavior('itemSelect')) {
            var ext = {
                params: [
                    {name: this.id + '_itemSelect', value: itemValue}
                ]
            };

            this.callBehavior('itemSelect', ext);
        }
    },

    search: function (query) {
        var $this = this;

        var resultStyle = this.cfg.resultStyle || '';
        var resultStyleClass = this.cfg.resultStyleClass || '';

        var itemDisplayMarkup = '';
        if (query) { // when any input entered
            $.each(fuzzysearch.go(query, $this.datasource, {keys: $this.keys}), function (index, element) {
                itemDisplayMarkup += '<div';
                itemDisplayMarkup += (resultStyle === '' ? '' : ' style="' + resultStyle + '"');
                itemDisplayMarkup += ' class="ui-fuzzysearch-item';
                itemDisplayMarkup += (resultStyleClass === '' ? '' : ' ' + resultStyleClass) + '"';
                itemDisplayMarkup += ' data-item-value="' + element.obj.label + '">';
                itemDisplayMarkup += element.obj.label;
                itemDisplayMarkup += '</div>';
            });
        } else if (this.listItemsAtTheBeginning) { // when there is no input
            $.each($this.datasource, function (index, element) {
                itemDisplayMarkup += '<div';
                itemDisplayMarkup += (resultStyle === '' ? '' : ' style="' + resultStyle + '"');
                itemDisplayMarkup += ' class="ui-fuzzysearch-item';
                itemDisplayMarkup += (resultStyleClass === '' ? '' : ' ' + resultStyleClass) + '"';
                itemDisplayMarkup += ' data-item-value="' + element.value.name + '">';
                itemDisplayMarkup += element.value.name;
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