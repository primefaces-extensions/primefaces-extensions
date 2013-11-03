/**
 * PrimeFaces Extensions FluidGrid Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.FluidGrid = PrimeFaces.widget.DeferredWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;
        
        this.cfg.opts.itemSelector = ".pe-fluidgrid-item";
        this.cfg.opts.isInitLayout = false;
        
        if (this.cfg.opts.stamp) {
            this.cfg.opts.stamp = PrimeFaces.Expressions.resolveComponentsAsSelector(this.cfg.opts.stamp).get();
        }
        
        this.renderDeferred();
    },

    /**
     * Creates this widget with all initialization steps.
     */
    _render: function () {
        this.$container = $(PrimeFaces.escapeClientId(this.id));
               
        if (this.cfg.opts.hasImages) {
            // initialize plugin after all images have been loaded
            this.$container.imagesLoaded($.proxy(function() {
                this.$container.masonry(this.cfg.opts);
                
                // bind events
                this.bindEvents();
                
                // trigger layout manually
                this.$container.masonry();
            }, this));
        } else {
            this.$container.masonry(this.cfg.opts);
            
            // bind events
            this.bindEvents();
            
            // trigger layout manually
            this.$container.masonry();
        }
    },
    
    /**
     * Binds events.
     */
    bindEvents: function () {
        if (this.getBehavior("layoutComplete")) {
            this.$container.masonry('off', 'layoutComplete');
            this.$container.masonry('on', 'layoutComplete', $.proxy(function() {
                var behavior = this.getBehavior("layoutComplete");
                var ext = {
                    params:[]
                };
    
                behavior.call(this, null, ext);
            }, this));
        }
    },
    
    /**
     * Gets behavior callback by name or null.
     * 
     * @param name behavior name
     * @return {Function}
     */
    getBehavior: function (name) {
        return this.cfg.behaviors ? this.cfg.behaviors[name] : null;
    },

    addItems: function (elements) {
        this.$container.masonry( 'addItems', elements);
    },
    
    appended: function (elements) {
        this.$container.masonry( 'appended', elements);
    },
    
    prepended: function (elements) {
        this.$container.masonry( 'prepended', elements);
    },    

    bindResize: function () {
        this.$container.masonry('bindResize');
    },

    unbindResize: function () {
        this.$container.masonry('unbindResize');
    },
    
    destroy: function () {
        this.$container.masonry('destroy');
    },
    
    getItemElements: function () {
        return this.$container.masonry('getItemElements');
    },
    
    hide: function () {
        this.$container.masonry('hide');
    },
    
    layout: function () {
        this.$container.masonry();
    },
    
    layoutItems: function (items, isStill) {
        this.$container.masonry('layoutItems', items, isStill);
    },
    
    reloadItems: function () {
        this.$container.masonry('reloadItems');
    },
    
    remove: function (elements) {
        this.$container.masonry('remove', elements);
    },
    
    reveal: function (items) {
        this.$container.masonry('reveal', items);
    },
    
    stamp: function (elements) {
        this.$container.masonry('stamp', elements);
    },
    
    unstamp: function (elements) {
        this.$container.masonry('unstamp', elements);
    }
});
