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

        this.buttons = this.jq.children('div:not(.ui-state-disabled)').children();
        this.inputs = this.jq.find(':radio:not(:disabled)');
        console.log("buttons length is: " + this.buttons.length);
        console.log("inputs length is: " + this.inputs.length);

        this.input = $(this.jqId + '_fuzzysearch-search-input');
        this.results = $(this.jqId + '_fuzzysearch-search-results');
//        this.keys = JSON.parse(this.cfg.keys); // TODO not sure how to implement (https://github.com/farzher/fuzzysort)
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

        this.items.on('click', function (e) {
            var item = $(this);
            console.log(item);

            if ($this.hasBehavior('select')) {
                var ext = {
                    params: [
                        {name: this.id + '_itemSelect', value: item}
                    ]
                };

                $this.callBehavior('select', ext);
            }
        });
    },

    search: function (query) {
        var $this = this;

        var resultStyle = this.cfg.resultStyle || '';
        var resultStyleClass = this.cfg.resultStyleClass || '';

        var itemDisplayMarkup = '';
        if (query) { // when any input entered
            $.each(fuzzysearch.go(query, $this.datasource, {keys: ['label']}), function (index, element) {
                itemDisplayMarkup += '<div';
                itemDisplayMarkup += (resultStyle === '' ? '' : ' style="' + resultStyle + '"');
                itemDisplayMarkup += ' class="ui-fuzzysearch-item';
                itemDisplayMarkup += (resultStyleClass === '' ? '' : ' ' + resultStyleClass) + '">';
                itemDisplayMarkup += element.obj.label;
                itemDisplayMarkup += '</div>';
            });
        } else if (this.listItemsAtTheBeginning) { // when there is no input
            $.each($this.datasource, function (index, element) {
                itemDisplayMarkup += '<div';
                itemDisplayMarkup += (resultStyle === '' ? '' : ' style="' + resultStyle + '"');
                itemDisplayMarkup += ' class="ui-fuzzysearch-item';
                itemDisplayMarkup += (resultStyleClass === '' ? '' : ' ' + resultStyleClass) + '">';
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