/**
 * PrimeFaces Extensions Social Media Widget.
 * 
 * @author Melloware mellowaredev@gmail.com
 * @since 6.2
 */
PrimeFaces.widget.ExtSocial = PrimeFaces.widget.BaseWidget.extend({

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

        // create the Social Media
        this.jq.jsSocials('destroy').jsSocials(opts);
    },

    update : function() {
        if (this.jq) {
            this.jq.jsSocials('refresh');
        }
    },

    destroy : function() {
        if (this.jq) {
            this.jq.jsSocials('destroy');
        }
    }
});
