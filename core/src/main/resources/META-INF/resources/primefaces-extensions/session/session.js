/**
 * PrimeFaces Extensions Session Widget.
 *
 * @author Frank Cornelis
 * @since 12.0.4
 */
PrimeFaces.widget.Session = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        this.registerMultipleWindowSupport();
        this.configureTimer();
        this.registerAjaxCallbacks();
    },

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    refresh: function(cfg) {
        this.cancelTimers();
        this._super(cfg);
    },

    /**
     * @override
     * @inheritdoc
     */
    destroy: function() {
        this._super();
        this.cancelTimers();
    },

    /**
     * If multiple window support is true activity for this app in any browser tab resets the timer.
     */
    registerMultipleWindowSupport() {
        if (!this.cfg.multiWindowSupport) {
            return;
        }

        // create the key and set an initial value
        this.cfg.storageKey = PrimeFaces.createStorageKey("PrimeFaces", 'Session_lastActive', true);
        this.notifyOtherWindows();

        let _this = this;

        // Add an event listener to listen for changes in localStorage
        window.addEventListener('storage', function(event) {
            if (event.key === _this.cfg.storageKey) {
                // reset the timers when the value changes
                _this.jsfAjaxEventCallback();
            }
        });
    },

    registerAjaxCallbacks() {
        let _this = this;
        jsf.ajax.addOnEvent(() => {
            _this.jsfAjaxEventCallback();
            _this.notifyOtherWindows();
        });
        // PrimeFaces does its own client-side lifecycle.
        let doc = $(document);
        doc.on('pfAjaxComplete', () => {
            _this.jsfAjaxEventCallback();
            _this.notifyOtherWindows();
        });
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

    /**
     * Set the local storage value and trigger and event to notify other windows.
     */
    notifyOtherWindows: function() {
        if (!this.cfg.multiWindowSupport) {
            return;
        }
        let key = this.cfg.storageKey;
        let value = Date.now().toString();
        localStorage.setItem(key, value);

        // Trigger a custom event to notify other tabs/windows about the change
        window.dispatchEvent(new StorageEvent('storage', {
            key: key,
            oldValue: null,
            newValue: value,
            storageArea: localStorage,
        }));
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

    /**
     * Cancels all current timers.
     */
    cancelTimers: function () {
        this.deleteExpireTimeout();
        this.deleteReactionTimeout();
    },

    /**
     * Callback when any JSF AJAX event completes resetting the timers.
     * @private
     */
    jsfAjaxEventCallback: function () {
        this.configureTimer();
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