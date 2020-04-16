/**
 * PrimeFaces FuzzySearch Widget
 */
PrimeFaces.widget.FuzzySearch = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);

        this.keys = ['label']; //JSON.parse(this.cfg.keys); // TODO i am not sure how to implement it without using FuzzySearchKey
        this.datasource = JSON.parse(this.cfg.datasource);
        this.input = $(this.jqId + '_fuzzysearch-search-input');
        this.results = $(this.jqId + '_fuzzysearch-search-results');
//        this.buttons = this.jq.children('div:not(.ui-state-disabled)');
        this.buttons = this.results.children();
        this.inputs = this.jq.find(':radio:not(:disabled)');
        this.cfg.unselectable = this.cfg.unselectable === false ? false : true;

        //Visual effects
        PrimeFaces.skinInput(this.input);

        this.bindEvents();

        //pfs metadata
        this.inputs.data(PrimeFaces.CLIENT_ID_DATA, this.id);
    },

    bindEvents: function () {
        var $this = this;

        this.input.on('input', function (e) {
            $this.search($this.input.val());
        });

        this.bindEventsForButtons(this.buttons);
    },

    bindEventsForButtons: function (buttons) {
        var $this = this;

        buttons
                .on('mouseover', function () {
                    var button = $(this);
                    button.addClass('ui-state-hover');
                })
                .on('mouseout', function () {
                    $(this).removeClass('ui-state-hover');
                })
                .on('click', function () {
                    var button = $(this),
                            radio = button.children(':radio');

                    if (button.hasClass('ui-state-active') || radio.prop('checked')) {
                        $this.unselect(button);
                    } else {
                        $this.select(button);
                    }
                });

        /* For keyboard accessibility */
        buttons
                .on('focus.fuzzySearch', function () {
                    var button = $(this);
                    button.addClass('ui-state-focus');
                })
                .on('blur.fuzzySearch', function () {
                    var button = $(this);
                    button.removeClass('ui-state-focus');
                })
                .on('keydown.fuzzySearch', function (e) {
                    var keyCode = $.ui.keyCode,
                            key = e.which;

                    if (key === keyCode.SPACE || key === keyCode.ENTER) {
                        var button = $(this),
                                radio = button.children(':radio');

                        if (radio.prop('checked')) {
                            $this.unselect(button);
                        } else {
                            $this.select(button);
                        }
                        e.preventDefault();
                    }
                });
    },

    search: function (query) {
        var $this = this;

        var resultStyle = this.cfg.resultStyle || '';
        var resultStyleClass = this.cfg.resultStyleClass || '';
        var listItemsAtTheBeginning = this.cfg.listItemsAtTheBeginning;

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
        } else if (listItemsAtTheBeginning) { // when there is no input
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
        this.bindEventsForButtons($this.results.children());
    },

    select: function (button) {
        this.buttons.filter('.ui-state-active').removeClass('ui-state-active ui-state-hover').children(':radio').prop('checked', false);

        button.addClass('ui-state-active').children(':radio').prop('checked', true);

        this.triggerChange(button);
    },

    unselect: function (button) {
        if (this.cfg.unselectable) {
            button.removeClass('ui-state-active ui-state-hover').children(':radio').prop('checked', false).change();

            this.triggerChange(button);
        }
    },

    triggerChange: function (button) {
        var itemValue = button.attr('data-item-value');
        this.input.val(itemValue);

        if (this.cfg.change) {
            this.cfg.change.call(this);
        }

        var ext = {
            params: [
                {name: this.id + '_change', value: itemValue}
            ]
        };

        this.callBehavior('change', ext);
    },

    disable: function () {
        this.buttons.removeClass('ui-state-hover ui-state-focus ui-state-active')
                .addClass('ui-state-disabled').attr('disabled', 'disabled');
    },

    enable: function () {
        this.buttons.removeClass('ui-state-disabled').removeAttr('disabled');
    }

});
