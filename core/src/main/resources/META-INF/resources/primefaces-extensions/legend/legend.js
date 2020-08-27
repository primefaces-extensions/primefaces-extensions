/**
 * PrimeFaces Extensions Legend Widget.
 * 
 * @author Melloware info@melloware.com
 * @since 7.1
 */
PrimeFaces.widget.ExtLegend = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
    },

    /**
     * Show the legend with optional animation duration.
     * 
     * @param [duration] Durations are given in milliseconds; higher values indicate slower animations, not faster ones. 
     *                 The strings 'fast' and 'slow' can be supplied to indicate durations of 200 and 600 milliseconds, 
     *                 respectively.
     */
    show: function(duration) {
        this.jq.show(duration);
    },
    
    /**
     * Hide the legend with optional animation duration.
     * 
     * @param [duration] Durations are given in milliseconds; higher values indicate slower animations, not faster ones. 
     *                 The strings 'fast' and 'slow' can be supplied to indicate durations of 200 and 600 milliseconds, 
     *                 respectively.
     */
    hide: function(duration) {
        this.jq.hide(duration);
    },
});


