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

        let $this = this;

        // Add an event listener to listen for changes in localStorage
        window.addEventListener('storage', function(event) {
            if (event.key === $this.cfg.storageKey) {
                // reset the timers when the value changes
                $this.jsfAjaxEventCallback();
            }
        });
    },

    registerAjaxCallbacks() {
        let $this = this;

        // bind to JSF (f:ajax) events
        if (window.jsf && jsf.ajax) {
            jsf.ajax.addOnEvent(() => {
                $this.jsfAjaxEventCallback();
                $this.notifyOtherWindows();
            });
        }

        // PrimeFaces does its own client-side lifecycle.
        $(document).on('pfAjaxComplete', () => {
            $this.jsfAjaxEventCallback();
            $this.notifyOtherWindows();
        });
    },

    configureTimer: function () {
        let $this = this;
        let maxInactiveInterval = this.cfg.max_inactive_interval;
        let reactionPeriod = this.cfg.reactionPeriod;

        this.cancelTimers();

        if (maxInactiveInterval !== undefined) {
            let timeout = (Number(maxInactiveInterval) - reactionPeriod) * 1000;

            if (timeout > 0) {
                this.timer = window.setTimeout(() => {
                    $this.timeoutCallback();
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
        let $this = this;
        this.expiredTimer = window.setTimeout(() => {
            $this.expiredTimeoutCallback();
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