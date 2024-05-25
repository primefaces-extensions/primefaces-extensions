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
            console.log( marker.options.customId + " " + marker.options.icon );
            if( typeof marker.options.icon === 'string' || marker.options.icon instanceof String )
            {
                    marker.setIcon( L.icon({ iconUrl: marker.options.icon, shadowUrl: this.cfg.shadowUrl, iconSize: [25, 41], iconAnchor: [12, 41] }) );
            }
            else
            {
		    marker.setIcon( myIcon );
            }
            marker.addTo( this.cfg.map );

            marker.on('click', function(event) {
               _self.fireOverlaySelectEvent(event, this.options, 1);
            });
   
           marker.on('dragend', function() {
               _self.fireMarkerDragEvent(event, marker);
           });
        }
    },

    /**
     * Calls the behavior for when a marker was dragged.
     * @private
     * @param {google.maps.MapMouseEvent | google.maps.IconMouseEvent} event Event that occurred.
     * @param {google.maps.MarkerOptions} marker The marker that was dragged.
     */
    fireMarkerDragEvent: function(event, marker) {
        if(this.hasBehavior('markerDrag')) {
            var ext = {
                params: [
                    {name: this.id + '_markerId', value: marker.options.customId},
                    {name: this.id + '_lat', value: marker.getLatLng().lat},
                    {name: this.id + '_lng', value: marker.getLatLng().lng}
                ]
            };

            this.callBehavior('markerDrag', ext);
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
