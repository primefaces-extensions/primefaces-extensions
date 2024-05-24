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

            marker.on('click', function(event) {
               _self.fireOverlaySelectEvent(event, this.options, 1);
            });
        }
    },

    /**
     * Triggers the behavior for when an overlay shape was selected.
     * @private
     * @param {google.maps.MapMouseEvent | google.maps.IconMouseEvent} event The event that occurred.
     * @param {PrimeFaces.widget.GMap.Overlay} overlay The shape that was selected.
     * @param {number} clickCount whether it was single or double click
     */
    fireOverlaySelectEvent: function(event, overlay, clickCount) {
        this.selectedOverlay = overlay;
        console.log( 'fireOverlaySelectEvent: ' + overlay.customId );
        console.log( 'fireOverlaySelectEvent: ' + this.id ); 
        
        var ext = {
                params: [
                    {name: this.id + '_overlayId', value: overlay.customId}
                ]
            };

        if (clickCount === 1 && this.hasBehavior('overlaySelect')) {
            this.callBehavior('overlaySelect', ext);
        }
        if (clickCount === 2 && this.hasBehavior('overlayDblSelect')) {
            this.callBehavior('overlayDblSelect', ext);
        }
    },

});
