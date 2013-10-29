/**
 * PrimeFaces Extensions FluidGrid Widget.
 *
 * @author Oleg Varaksin
 */
PrimeFacesExt.widget.FluidGrid = PrimeFaces.widget.BaseWidget.extend({

    /**
     * Initializes the widget.
     *
     * @param {object} cfg The widget configuration.
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;

        this.renderDeferred();
    },

    /**
     * Creates this widget with all initialization steps.
     */
    _render: function () {
        this.$container = $(PrimeFaces.escapeClientId(this.id));
        
        // initialize Masonry after all images have been loaded  
        this.$container.imagesLoaded($.proxy(function() {
            this.$container.masonry(this.cfg.opts);
        }, this));
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
        this.$container.masonry( 'remove', elements);
    },
    
    reveal: function (items) {
        this.$container.masonry( 'reveal', items);
    },
    
    stamp: function (elements) {
        this.$container.masonry( 'stamp', elements);
    },
    
    unstamp: function (elements) {
        this.$container.masonry( 'unstamp', elements);
    }
});
