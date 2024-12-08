/**
 * PrimeFaces Extensions Legend Widget.
 * 
 * @author Melloware info@melloware.com
 * @since 7.1
 */
PrimeFaces.widget.ExtLegend = class extends PrimeFaces.widget.BaseWidget {

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init(cfg) {
        super.init(cfg);
        this.id = cfg.id;
        this.cfg = cfg;
    }

    /**
     * Show the legend with optional animation duration.
     * 
     * @param [duration] Durations are given in milliseconds; higher values indicate slower animations, not faster ones. 
     *                 The strings 'fast' and 'slow' can be supplied to indicate durations of 200 and 600 milliseconds, 
     *                 respectively.
     */
    show(duration) {
        this.jq.show(duration);
    }
    
    /**
     * Hide the legend with optional animation duration.
     * 
     * @param [duration] Durations are given in milliseconds; higher values indicate slower animations, not faster ones. 
     *                 The strings 'fast' and 'slow' can be supplied to indicate durations of 200 and 600 milliseconds, 
     *                 respectively.
     */
    hide(duration) {
        this.jq.hide(duration);
    }
};


