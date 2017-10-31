/**
 * PrimeFaces Extensions GitHub Widget.
 * 
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
PrimeFaces.widget.ExtGitHub = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     * 
     * @param {object}
     *        cfg The widget configuration.
     */
    init : function(cfg) {
        this._super(cfg);
        this.id = cfg.id;

        // make a copy of the configuration
        var opts = $.extend(true, {}, cfg);

        // create the GitHub
        this.jq.github(opts);
    }
});
