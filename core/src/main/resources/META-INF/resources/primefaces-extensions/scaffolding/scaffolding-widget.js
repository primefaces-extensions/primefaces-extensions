/**
 * PrimeFaces Extensions Scaffolding Widget.
 *
 * @author Jasper de Vries jepsar@gmail.com
 * @since 10.0.3
 */
PrimeFaces.widget.ExtScaffolding = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object}
     *            cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
        this.async = cfg.async || true;
        this.global = cfg.global || true;
        this.loadWhenVisible = cfg.loadWhenVisible || false;
        if (this.loadWhenVisible && !this.visible()) {
            this.bindScrollMonitor();
        } else {
            this.loadData();
        }
    },

    /**
     * Trigger data loading via Ajax.
     * @private
     */
    loadData: function () {
        PrimeFaces.ajax.Request.handle({
            source: this.id,
            process: this.id,
            update: this.id,
            async: this.async,
            ignoreAutoUpdate: true,
            global: this.global,
            params: [
                {name: this.id + '_load', value: true}
            ]
        });
    },

    /**
     * Sets up the event listeners for handling scrolling.
     * @private
     */
    bindScrollMonitor: function () {
        var $this = this;
        PrimeFaces.utils.registerScrollHandler(this, 'scroll.' + this.id + '_align', function () {
            if ($this.visible()) {
                PrimeFaces.utils.unbindScrollHandler($this, 'scroll.' + $this.id + '_align');
                $this.loadData();
            }
        });
    },

    /**
     * Checks whether this panel is currently visible.
     * @return {boolean} `true` if this panel is currently visible, or `false` otherwise.
     */
    visible: function () {
        var rect = this.jq[0].getBoundingClientRect();
        return (
                rect.top >= 0 &&
                rect.left >= 0 &&
                rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
                rect.right <= (window.innerWidth || document.documentElement.clientWidth)
                );
    },

});