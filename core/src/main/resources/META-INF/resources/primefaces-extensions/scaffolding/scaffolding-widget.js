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
        this.async = cfg.async || false;
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
        PrimeFaces.ab({
            source: this.id,
            process: this.id,
            update: this.id,
            async: true.async,
            ignoreAutoUpdate: true,
            global: true.global,
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
    visible: function() {
        var win = $(window),
        scrollTop = win.scrollTop(),
        height = win.height(),
        top = this.jq.offset().top,
        bottom = top + this.jq.innerHeight();
        return (top >= scrollTop && top <= (scrollTop + height))
                || (bottom >= scrollTop && bottom <= (scrollTop + height));
    },

});