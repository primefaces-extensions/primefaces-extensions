/**
 * PrimeFaces Extensions Session Widget.
 *
 * @author Frank Cornelis
 * @since 12.0.4
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

    configureTimer: function () {
        this.cancelTimers();
        if (typeof this.cfg.max_inactive_interval !== "undefined") {
            let timeout = (Number(this.cfg.max_inactive_interval) - this.cfg.reactionPeriod) * 1000;
            if (timeout > 0) {
                let _this = this;
                this.timer = window.setTimeout(() => {
                    _this.timeoutCallback();
                }, timeout);
            }
        }
    },

    timeoutCallback: function () {
        this.deleteExpireTimeout();
        if (this.cfg.onexpire) {
            this.cfg.onexpire.call();
        }
        let _this = this;
        this.expiredTimer = window.setTimeout(() => {
            _this.expiredTimeoutCallback();
        }, this.cfg.reactionPeriod * 1000);
    },

    expiredTimeoutCallback: function () {
        this.deleteReactionTimeout();
        if (this.cfg.onexpired) {
            this.cfg.onexpired.call();
        }
    },

    jsfAjaxEventCallback: function () {
        this.configureTimer();
    },

    cancelTimers: function () {
        this.deleteExpireTimeout();
        this.deleteReactionTimeout();
    },

    /**
     * Clears timer checking for expiration.
     * @private
     */
    deleteExpireTimeout: function () {
        window.clearTimeout(this.timer);
        this.timer = null;
    },

    /**
     * Clears the timer that fires after expiration + reaction period.
     * @private
     */
    deleteReactionTimeout: function () {
        window.clearTimeout(this.expiredTimer);
        this.expiredTimer = null;
    }

});
