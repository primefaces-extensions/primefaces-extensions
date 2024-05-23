/**
 * PrimeFaces OpenStreet Maps Widget
 */
PrimeFaces.widget.OSMap = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

       	this.cfg.tile.addTo( this.cfg.map ); 

        if(this.cfg.markers) {
            this.configureMarkers();
        }
    },

    configureMarkers: function() {
        var _self = this;
  
        var myIcon = L.icon({ iconUrl: this.cfg.iconUrl, shadowUrl: this.cfg.shadowUrl, iconSize: [25, 41], iconAnchor: [12, 41] });

        for(var i=0; i < this.cfg.markers.length; i++) {
            var marker = this.cfg.markers[i];
            marker.setIcon( myIcon );
            marker.addTo( this.cfg.map );
        }
    },

});
