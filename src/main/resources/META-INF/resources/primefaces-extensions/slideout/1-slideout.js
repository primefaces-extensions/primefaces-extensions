/**
 * PrimeFaces Extensions SlideOut Widget.
 * 
 * @author Melloware info@melloware.com
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
        // make a copy of the cfg
        var opts={};
        Object.keys(cfg).forEach((key) => opts[key] = cfg[key]);

        // create the slideout
        this.jq.tabSlideOut(opts);
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
    },

});
