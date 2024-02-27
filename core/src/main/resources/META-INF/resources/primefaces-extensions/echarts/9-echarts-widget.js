/**
 * __PrimeFaces EChart Widget__
 *
 * Apache ECharts based components are a modern replacement for the older `<p:chart>` component. Each chart component has its
 * own model api that defines the data and the options to customize the graph.
 *
 * You can also define an extender function. The extender function allows access to the underlying
 * [echarts.js](https://echarts.apache.org/) API using the `setExtender` method of the model. You need to define
 * a global function and set it on the model, see the user guide for more details. The required typing of that function
 * is given by `PrimeFaces.widget.ExtEChart.ChartExtender`.
 */
PrimeFaces.widget.ExtEChart = PrimeFaces.widget.DeferredWidget.extend({

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    init: function (cfg) {
        this._super(cfg);

        // user extension to configure chart
        let extender = this.cfg.extender;
        if (extender) {
            if (typeof extender === "function") {
                extender.call(this);
            } else {
                PrimeFaces.error("Extender value is not a javascript function!");
            }
        }

        this.renderDeferred();
    },

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    refresh: function (cfg) {
        if (this.chart) {
            this.unbindWindowResizeListener();
            this.chart.dispose();
        }

        this._super(cfg);
    },

    /**
     * @override
     * @inheritdoc
     */
    destroy: function () {
        this._super();

        if (this.chart) {
            this.unbindWindowResizeListener();
            this.chart.dispose();
        }
    },

    /**
     * @include
     * @override
     * @protected
     * @inheritdoc
     */
    _render: function () {
        // configure theme
        let theme = this.cfg.theme || PrimeFaces.env.getThemeContrast();

        // configure options
        let options = this.cfg.config;
        options.aria = {show: true};

        // initialize the chart
        this.chart = echarts.init(document.getElementById(this.id), theme);
        this.chart.setOption(options);

        this.bindWindowResizeListener();
        this.bindItemSelect();
    },

    /**
     * Sets up the window resize listener to make the chart responsive.
     * @private
     */
    bindWindowResizeListener: function() {
        let $this = this;
        $(window).on('resize.' + this.id, function() {
            $this.chart.resize();
        });
    },

    /**
     * Tears down the window resize listener.
     * @private
     */
    unbindWindowResizeListener: function() {
        $(window).off('resize.' + this.id);
    },

    /**
     * Setups the event listeners required by this widget when an item (data point) in the chart is selected.
     * @private
     */
    bindItemSelect: function () {
        if (!this.hasBehavior('itemSelect')) {
            return;
        }

        let $this = this;
        this.chart.on("click", function (event) {
            let params = [];
            for (const key in event) {
                if (Object.hasOwnProperty.call(event, key)) {
                    const value = event[key];
                    if (typeof value !== 'object' && typeof value !== 'function') {
                        params.push({
                            name: key,
                            value: value
                        });
                    }
                }
            }
            $this.callBehavior("itemSelect", {params});
        });
    },

    /**
     * Return this chart as an image with a data source URL (`<img src="data:url" />`)
     * @return {HTMLImageElement} The content of this chart as an HTML IMAGE.
     */
    exportAsImage: function() {
        let img = new Image();
        img.src = this.chart.getDataURL();
        return img;
    },

    /**
     * Send this chart to the printer.
     */
    print: function() {
        // Create a new image element
        let img = `<html><head><script>function s1(){setTimeout('s2()',10);}function s2(){window.print();window.close()}</script></head><body onload='s1()'><img src='${this.chart.getDataURL()}'/></body></html>`;
        let pwa = window.open("about:blank", "_new");
        pwa.document.open();
        pwa.document.write(img);
        pwa.document.close();
    },
});