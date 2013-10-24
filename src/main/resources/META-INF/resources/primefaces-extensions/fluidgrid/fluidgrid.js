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
        // TODO
    },

    addItems: function (elements) {
        //$container.masonry( 'addItems', elements);
    },
    
    appended: function (elements) {
        //$container.masonry( 'appended', elements);
    },
    
    prepended: function (elements) {
        //$container.masonry( 'prepended', elements);
    },    

    bindResize: function () {
        //$container.masonry('bindResize');
    },

    unbindResize: function () {
        //$container.masonry('unbindResize');
    },
    
    destroy: function () {
        //$container.masonry('destroy');
    },
    
    getItemElements: function () {
        //return $container.masonry('getItemElements');
    },
    
    hide: function () {
        //$container.masonry('hide');
    },
    
    layout: function () {
        //$container.masonry();
    },
    
    layoutItems: function (items, isStill) {
        //$container.masonry('layoutItems', items, isStill);
    },
    
    reloadItems: function () {
        //$container.masonry('reloadItems');
    },
    
    remove: function (elements) {
        //$container.masonry( 'remove', elements);
    },
    
    reveal: function (items) {
        //$container.masonry( 'reveal', items);
    },
    
    stamp: function (elements) {
        //$container.masonry( 'stamp', elements);
    },
    
    unstamp: function (elements) {
        //$container.masonry( 'unstamp', elements);
    }
});
