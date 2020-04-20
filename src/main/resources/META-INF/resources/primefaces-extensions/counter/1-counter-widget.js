/**
 * PrimeFaces Extensions Counter Widget
 * 
 */
PrimeFaces.widget.ExtCounter = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
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

        this.counter = new Counter(this.id, this.end, this.options);

        if (this.autoStart) {
            this.start();
        }

    },

    start: function () {
        var $this = this;

        if (!$this.counter.error) {
            if ($this.oncountercomplete) {
                this.callBehavior('end');
            } else {
                this.callBehavior('start');
            }
        } else {
            console.error($this.counter.error);
        }
    },

    pauseResume: function () {
        var $this = this;

        $this.counter.pauseResume();
    },

    reset: function () {
        var $this = this;

        $this.counter.reset();
    }

});
