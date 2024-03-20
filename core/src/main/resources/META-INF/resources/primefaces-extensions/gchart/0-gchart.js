/**
 * Primefaces Extension GChart Widget
 * 
 * @author f.strazzullo
 * @author Melloware
 */
PrimeFaces.widget.ExtGChart = PrimeFaces.widget.BaseWidget.extend({
    init : function(cfg) {

        var that = this;

        this._super(cfg);
        this.chart = cfg.chart ? JSON.parse(cfg.chart) : {
            data : [],
            options : {},
            type : ""
        };
        this.data = this.chart.data;
        this.type = this.chart.type
        this.height = cfg.height;
        this.width = cfg.width;
        this.title = cfg.title;
        this.apiKey = cfg.apiKey;
        this.language = cfg.language;
        this.options = this.chart.options;
        this.input = jQuery(this.jqId + "_hidden");

        google.charts.load('current', {
            packages : [ 'corechart', 'geochart', 'orgchart', 'gantt' ],
            language: this.language,
            // Note: you will need to get a mapsApiKey for your project.
            // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
            mapsApiKey: this.apiKey
        });

        jQuery(document).ready(function() {
            google.charts.setOnLoadCallback(function() {
                that.draw();
            });
        });

        // user extension to configure gchart
        var extender = this.cfg.extender;
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }
    },

    draw : function() {

        var dataTable = google.visualization.arrayToDataTable(this.data);

        var that = this;

        this.options.title = this.title;
        this.options.width = parseInt(this.width, 10);
        this.options.height = parseInt(this.height, 10);

        this.wrapper = new google.visualization.ChartWrapper({
            chartType : this.type,
            dataTable : dataTable,
            options : this.options,
            containerId : this.id
        });

        if (this.cfg.behaviors && this.cfg.behaviors.select) {
            google.visualization.events.addListener(this.wrapper, 'select', function(e) {
                //console.log(that.wrapper.getChart().getSelection());
                jQuery(that.jqId + "_hidden").val(JSON.stringify(that.wrapper.getChart().getSelection()));
                that.cfg.behaviors.select.call(jQuery(that.jqId + "_hidden"));
            });
        }

        this.wrapper.draw();

    }

});
