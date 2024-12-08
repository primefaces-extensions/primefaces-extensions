/**
 * PrimeFaces Extensions Counter Widget
 *
 * @since 8.0.1
 */
PrimeFaces.widget.ExtCounter = class extends PrimeFaces.widget.BaseWidget {

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.widget.BaseWidget.cfg} cfg
     */
    init(cfg) {

        super.init(cfg);
        this.id = cfg.id;
        this.cfg = cfg;

        this.end = cfg.end;
        this.autoStart = cfg.autoStart;
        this.onEnd = cfg.onend;
        this.onStart = cfg.onstart;
        this.options = {
            startVal: cfg.start || 0,
            decimalPlaces: cfg.decimals || 0,
            duration: cfg.duration || 2,
            useEasing: cfg.useEasing === true,
            useGrouping: cfg.useGrouping === true,
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
    }

    /**
     * Starts the counter.
     */
    startCounter() {
        var $this = this;

        if (!this.counter.error) {
            if (this.onStart) {
                this.onStart();
            }

            this.counter.start(function () {
                $this.endCounter()
            });

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
    }

    /**
     * Ends the counter.
     */
    endCounter() {
        var $this = this;

        if (this.onEnd) {
            this.onEnd();
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
    }

    /**
     * Pause and Resume toggle.
     */
    pauseResume() {
        this.counter.pauseResume();
    }

    /**
     * Reset the counter.
     */
    reset() {
        this.counter.reset();
    }

};