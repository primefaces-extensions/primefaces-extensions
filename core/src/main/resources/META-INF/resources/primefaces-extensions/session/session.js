/**
 * PrimeFaces Extensions Session Widget.
 *
 * @author Frank Cornelis
 * @since 12.0
 */

PrimeFaces.widget.Session = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        this.configureTimer();
        this.registerAjaxCallbacks();
    },

    registerAjaxCallbacks() {
        let _this = this;
        jsf.ajax.addOnEvent(() => {
            _this.jsfAjaxEventCallback();
        });
        // PrimeFaces does its own client-side lifecycle.
        let doc = $(document);
        doc.on('pfAjaxComplete', () => {
            _this.jsfAjaxEventCallback();
        });
    },

    refresh: function (cfg) {
        this.cancelTimers();
        this._super(cfg);
    },

    configureTimer() {
        this.cancelTimers();
        if (typeof this.cfg.max_inactive_interval !== "undefined") {
            let timeout = (Number(this.cfg.max_inactive_interval) - this.cfg.reactionPeriod) * 1000;
            if (timeout > 0) {
                let _this = this;
                console.log("setting timeout for: " + timeout + " ms.");
                this.timer = window.setTimeout(() => {
                    _this.timeoutCallback();
                }, timeout);
            }
        }
    },

    timeoutCallback() {
        this.timer = null;
        eval(this.cfg.onexpire);
        let _this = this;
        this.expiredTimer = window.setTimeout(() => {
            _this.expiredTimeoutCallback();
        }, this.cfg.reactionPeriod * 1000);
    },

    expiredTimeoutCallback() {
        this.expiredTimer = null;
        eval(this.cfg.onexpired);
    },

    jsfAjaxEventCallback() {
        this.configureTimer();
    },

    cancelTimers() {
        if (this.timer) {
            window.clearTimeout(this.timer);
            this.timer = null;
        }
        if (this.expiredTimer) {
            window.clearTimeout(this.expiredTimer);
            this.expiredTimer = null;
        }
    }
});
