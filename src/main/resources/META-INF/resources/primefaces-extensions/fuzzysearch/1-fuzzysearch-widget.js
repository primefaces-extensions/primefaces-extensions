PrimeFaces.widget.FuzzySearch = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);

//        this.id = this.cfg.id;
//        this.jqId = PrimeFaces.escapeClientId(this.id);
//        this.input = $(PrimeFaces.escapeClientId(this.id + "_fuzzysearch-search-input"));
//        this.results = $(PrimeFaces.escapeClientId(this.id + "_fuzzysearch-search-results"));
        this.input = $(this.jqId + '_fuzzysearch-search-input');
        this.results = $(this.jqId + '_fuzzysearch-search-results');
        this.panel = this.jq.children();
        this.keys = JSON.parse(cfg.keys);
        this.datasource = JSON.parse(cfg.value);

        //Visual effects
        PrimeFaces.skinInput(this.input);

        this.bindKeyEvents();

        var $this = this;
//        console.log($this);
    },

    bindKeyEvents: function () {
        var $this = this;

        this.input.on('input', function (e) {
            $this.search($this.input.val());
        });
    },

    search: function (query) {
//        console.log(query);
//        console.log(this.panel.get(1).innerHTML);
        var $this = this;
        $this.results.empty();
        if ($this.input.val()) { // when any input entered
            $.each(fuzzysearch.go($this.input.val(), $this.datasource, {keys: $this.keys}), function (index, value) {
                $this.results.append('<div><a href="' + value.obj.fileName + '">' + value.obj.name + '</a></div>'); // TODO how to get each row item format from renderChildren
            });
        } else { // when there is no input
            $.each($this.datasource, function (index, value) {
                $this.results.append('<div><a href="' + value.fileName + '">' + value.name + '</a></div>'); // TODO how to get each row item format from renderChildren
            });
        }
    }



});