/**
 * PrimeFaces OpenStreet Maps Widget
 */
PrimeFaces.widget.OSMap = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

       	this.cfg.tile.addTo( this.cfg.map ); 
    },

});
