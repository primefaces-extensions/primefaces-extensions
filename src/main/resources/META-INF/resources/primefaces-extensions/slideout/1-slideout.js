/**
 * PrimeFaces Extensions SlideOut Widget.
 * 
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
PrimeFaces.widget.ExtSlideOut = PrimeFaces.widget.BaseWidget.extend({

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
        
        this._applySlideOut(this.cfg);
    },

    /**
     * Applies the SlideOut to the given jQuery selector object.
     * 
     * @param {object}
     *        cfg The widget configuration.
     * @private
     */
    _applySlideOut : function(cfg) {
        var $this = this;
        // make a copy of the configuration
        var opts = $.extend(true, {}, cfg);
        
        opts.tabHandle = PrimeFaces.escapeClientId(opts.tabHandle);

        // create the slideout
        $this.jq.tabSlideOut(opts);
        
        // bind "open", "close" events
        this._bindEvents();
    },
    
    /**
     * Binds all events to p:ajax events
     * 
     * @private
     */
    _bindEvents : function() {
        var $this = this;
        
        this.jq.on("slideouttabopen",function () {
            var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['open'] : null;
            if (behavior) {
                var options = {};
                behavior.call($this, options);
            }
        }).on("slideouttabclose",function () {
            var behavior = $this.cfg.behaviors ? $this.cfg.behaviors['close'] : null;
            if (behavior) {
                var options = {};
                behavior.call($this, options);
            }
        });
    },

    /**
     * Open the tab.
     */
    open : function() {
        this.jq.tabSlideOut('open');
    },

    /**
     * Close the tab.
     */
    close : function() {
        this.jq.tabSlideOut('close');
    },
    
    /**
     * Toggle the tab open or closed.
     */
    toggle : function() {
        this.jq.tabSlideOut('toggle'); 
    },
    
    /**
     * Bounce the tab using an animation.
     */
    bounce : function() {
        this.jq.tabSlideOut('bounce'); 
    },
    
    /**
     * True if tab is currently open, false if closed.
     */
    isOpen : function() {
        return this.jq.tabSlideOut('isOpen');
    }

});
