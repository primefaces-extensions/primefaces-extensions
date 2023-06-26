/**
 * PrimeFaces Extension Timer Widget
 *
 * @author f.strazzullo
 */
PrimeFaces.widget.ExtTimer = PrimeFaces.widget.BaseWidget.extend({

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.forward = cfg.forward;
        this.locale = cfg.locale;
        this.originalTimeout = cfg.timeout;
        this.currentTimeout = this.forward ? 0 : cfg.timeout;

        if (cfg.autoStart) {
            this.start();
        }

        this.print();
    },

    /**
     * Calculate the current time in seconds.
     * @private
     * @returns {number} the number of seconds in the current time
     */
    currentTimeInSecs: function () {
        return Math.floor(new Date().getTime() / 1000);
    },

    /**
     * Prints the current step to the UI.
     * @private
     */
    print: function () {
        var value = this.currentTimeout;

        if (this.cfg.formatFunction) {
            value = this.cfg.formatFunction(value);
        } else if (this.cfg.format) {

            var format = this.cfg.format;

            if ("percentage" === format) {
                value = ((this.currentTimeout * 100) / this.originalTimeout) + "%";
            } else if ("human" === format) {
                value = moment.duration(this.currentTimeout, 'seconds').locale(this.locale).humanize();
            } else {
                value = moment.utc(moment.duration(this.currentTimeout, 'seconds').locale(this.locale).asMilliseconds()).format(format);
            }
        }

        this.jq.html(value);
    },

    /**
     * Perform the increment of the second
     * @private
     */
    doStep: function () {
        var currentSeconds = this.currentTimeInSecs(),
            seconds = currentSeconds - this.prevTime;
        this.prevTime = currentSeconds;
        this.currentTimeout += this.forward ? seconds : (0 - seconds);
        this.print();
        if (this.cfg.ontimerstep) {
            this.cfg.ontimerstep({
                current: this.currentTimeout,
                total: this.originalTimeout
            });
        }
    },

    /**
     * Start the timer.
     */
    start: function () {
        var $this = this;
        this.prevTime = this.currentTimeInSecs();

        // GitHub #1047 stop timer if timer was originally set to 0
        if ($this.originalTimeout <= 0) {
            $this.stop(false, true);
            return;
        }

        if (!this.intervalId) {
            this.intervalId = PrimeFacesExt.timers.setInterval(function () {
                $this.doStep();

                var end = $this.forward ? $this.currentTimeout >= $this.originalTimeout : $this.currentTimeout <= 0;
                if (end) {
                    $this.stop(false, end);
                }
            }, $this.cfg.interval);
        }
    },

    /**
     * Pause the timer.
     */
    pause: function () {
        if (this.intervalId) {
            PrimeFacesExt.timers.clearInterval(this.intervalId);
            this.intervalId = null;
        }
    },

    /**
     * Stop the timer.
     *
     * @param {boolean} silent true if you don't want to fire any AJAX events
     * @param {boolean} end true if you want to check the singleRun before stopping
     */
    stop: function (silent, end) {
        if (!silent && this.cfg.listener) {
            this.cfg.listener();
        }
        if (this.cfg.ontimercomplete) {
            this.cfg.ontimercomplete();
        }
        var singleRun = end === true && this.cfg.singleRun;
        if (!end || singleRun) {
            this.pause();
        }
        if (!end || !singleRun) {
            this.currentTimeout = this.forward ? 0 : this.originalTimeout;
            this.print();
        }
    },

    /**
     * Restart the timer back at its original value.
     *
     * @param {boolean} silent true if you don't want to fire any AJAX events
     */
    restart: function (silent) {
        this.stop(silent, false);
        this.start();
    }

});