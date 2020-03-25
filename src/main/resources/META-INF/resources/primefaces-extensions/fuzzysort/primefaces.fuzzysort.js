PrimeFaces.widget.Fuzzysort = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this.cfg = cfg;
        this.id = this.cfg.id;
        this.jqId = PrimeFaces.escapeClientId(this.id);
//        this.jq = $(PrimeFaces.escapeClientId(this.cfg.target));

        this.input = $(PrimeFaces.escapeClientId(this.cfg.id + "_fuzzysort-search-box"));
        this.results = $(PrimeFaces.escapeClientId(this.cfg.id + "_fuzzysort-search-results"));
        this.datasource = JSON.parse(this.cfg.value);
        
        //Visual effects
        PrimeFaces.skinInput(this.input);

        var $this = this;
//        console.log($this);

        jQuery(document).ready(function () {
            $this.search();
        });

        // Run a search on input change
        $this.input.on('input', $this.search);
    },

    search: function () {
        this.results.empty();
        this.results.append('<ul>');
        if (this.input.val()) { // when any input entered
            $.each(fuzzysort.go(this.input.val(), this.datasource, {keys: ['name']}), function (index, value) { // TODO how to implement search keys using a new attribute
                this.results.append('<li><a href="' + value.obj.fileName + '">' + value.obj.name + '</a></li>'); // TODO how to get each row item format from renderChildren
            });
        } else {// when there is no any input
            $.each(this.datasource, function (index, value) {
                this.results.append('<li><a href="' + value.fileName + '">' + value.name + '</a></li>'); // TODO how to get each row item format from renderChildren
            });
        }
        this.results.append('</ul>');
    }

});