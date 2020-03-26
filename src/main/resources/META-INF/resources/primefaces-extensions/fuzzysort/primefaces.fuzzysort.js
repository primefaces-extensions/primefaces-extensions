PrimeFaces.widget.Fuzzysort = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init: function (cfg) {
        this.cfg = cfg;
        this.id = this.cfg.id;
        this.jqId = PrimeFaces.escapeClientId(this.id);

        this.input = $(PrimeFaces.escapeClientId(this.cfg.id + "_fuzzysort-search-box"));
        this.results = $(PrimeFaces.escapeClientId(this.cfg.id + "_fuzzysort-search-results"));
        this.keys = JSON.parse(this.cfg.keys);
        this.datasource = JSON.parse(this.cfg.value);

        //Visual effects
        PrimeFaces.skinInput(this.input);

        var $this = this;
//        console.log($this);

        // Run a search on input change
        $this.input.on('input', function () {
            $this.results.empty();
            if ($this.input.val()) { // when any input entered
                $.each(fuzzysort.go($this.input.val(), $this.datasource, {keys: $this.keys}), function (index, value) {
                    $this.results.append('<div><a href="' + value.obj.fileName + '">' + value.obj.name + '</a></div>'); // TODO how to get each row item format from renderChildren
                });
            } else { // when there is no input
                $.each($this.datasource, function (index, value) {
                    $this.results.append('<div><a href="' + value.fileName + '">' + value.name + '</a></div>'); // TODO how to get each row item format from renderChildren
                });
            }
        });
    }

});