/**
 * PrimeFaces Extensions Counter Widget
 */
PrimeFaces.widget.ExtCounter = PrimeFaces.widget.BaseWidget.extend({

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.widget.BaseWidget.cfg} cfg
     */
    init: function (cfg) {

        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;

        this.end = cfg.end;
        this.autoStart = cfg.autoStart;
        this.oncountercomplete = cfg.oncountercomplete;
        this.options = {
            startVal: cfg.start || 0,
            decimalPlaces: cfg.decimals || 0,
            duration: cfg.duration || 2,
            useEasing: cfg.useEasing || true,
            useGrouping: cfg.useGrouping || true,
            smartEasingThreshold: cfg.smartEasingThreshold || 999,
            smartEasingAmount: cfg.smartEasingAmount || 333,
            separator: cfg.separator || ',',
            decimal: cfg.decimal || '.',
            prefix: cfg.prefix || '',
            suffix: cfg.suffix || ''
        };

        this.counter = new CountUp(this.id, this.end, this.options);

        if (this.autoStart) {
            this.startCounter();
        }
    },

    /**
     * Starts the counter.
     */
    startCounter: function () {
        var $this = this;

        if (!this.counter.error) {
            this.counter.start(() => $this.endCounter());

            if (this.hasBehavior('end')) {
                var options = {
                    params: [{
                        name: $this.id + '_value',
                        value: $this.counter.startVal
                    }]
                };
                this.callBehavior('start', options);
            }
        } else {
            PrimeFaces.error($this.counter.error);
        }
    },

    /**
     * Ends the counter.
     */
    endCounter: function () {
        var $this = this;

        if (this.oncountercomplete) {
            this.oncountercomplete();
        }

        if (this.hasBehavior('end')) {
            var options = {
                params: [{
                    name: $this.id + '_value',
                    value: $this.counter.endVal
                }]
            };
            this.callBehavior('end', options);
        }
    },

    /**
     * Pause and Resume toggle.
     */
    pauseResume: function () {
        this.counter.pauseResume();
    },

    /**
     * Reset the counter.
     */
    reset: function () {
        this.counter.reset();
    }

});
