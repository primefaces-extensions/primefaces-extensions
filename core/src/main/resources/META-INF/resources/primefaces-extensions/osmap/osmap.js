/**
 * PrimeFaces OpenStreetMap Widget
 */
PrimeFaces.widget.OSMap = PrimeFaces.widget.BaseWidget.extend({

    init: function(cfg) {
        this._super(cfg);

        this.cfg.tile.addTo( this.cfg.map );

        //conf markers
        if(this.cfg.markers) {
            this.configureMarkers();
        }

        //add polylines
        if(this.cfg.polylines) {
            this.configurePolylines();
        }

        //add polygons
        if(this.cfg.polygons) {
            this.configurePolygons();
        }

        //add circles
        if(this.cfg.circles) {
            this.configureCircles();
        }

        //add rectangles
        if(this.cfg.rectangles) {
            this.configureRectangles();
        }

        //general map events
        this.configureEventListeners();
    },

    configureMarkers: function() {
        var _self = this;

        var iconUrl = PrimeFaces.resources.getFacesResource('leaflet/images/marker-icon.png',
		PrimeFacesExt.RESOURCE_LIBRARY,
		PrimeFacesExt.VERSION);

        var shadowUrl = PrimeFaces.resources.getFacesResource('leaflet/images/marker-shadow.png',
		PrimeFacesExt.RESOURCE_LIBRARY,
		PrimeFacesExt.VERSION);

        var myIcon = L.icon({ iconUrl: iconUrl, shadowUrl: shadowUrl, iconSize: [25, 41], iconAnchor: [12, 41] });

        for(var i=0; i < this.cfg.markers.length; i++) {
            var marker = this.cfg.markers[i];
            //console.log( marker.options.customId + " " + marker.options.icon );
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
               _self.fireMarkerDragEvent(event, this);
           });
        }
    },

    /**
     * Calls the behavior for when a marker was dragged.
     * @private
     * @param {MapMouseEvent | IconMouseEvent} event Event that occurred.
     * @param {MarkerOptions} marker The marker that was dragged.
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
     * Adds the overlay for a polyline shape.
     * @private
     */
    configurePolylines: function() {
        this.addOverlays(this.cfg.polylines);
    },

    /**
     * Adds the overlay for a circle shape.
     * @private
     */
    configureCircles: function() {
        this.addOverlays(this.cfg.circles);
    },

    /**
     * Adds the overlay for a rectangular shape.
     * @private
     */
    configureRectangles: function() {
        this.addOverlays(this.cfg.rectangles);
    },

    /**
     * Adds the overlay for a polygonal shape.
     * @private
     */
    configurePolygons: function() {
        this.addOverlays(this.cfg.polygons);
    },

    /**
     * Triggers the behavior for when an overlay shape was selected.
     * @private
     * @param {MapMouseEvent | IconMouseEvent} event The event that occurred.
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

    configureEventListeners: function() {
        var _self = this;

        //behaviors
        this.configureStateChangeListener();
        this.configurePointSelectListener();
    },

    configureStateChangeListener: function() {
        var _self = this,

        onStateChange = function(event) {
            _self.fireStateChangeEvent(event);
        };

        this.cfg.map.on('zoomend', onStateChange);
        this.cfg.map.on('moveend', onStateChange);
    },

    /**
     * Triggers the behavior for when the state of this map has changed.
     * @private
     * @param {never} event The event that triggered the state change.
     */
    fireStateChangeEvent: function(event) {
        if(this.hasBehavior('stateChange')) {
            var bounds = this.cfg.map.getBounds();

            var ext = {
                params: [
                    {name: this.id + '_northeast', value: bounds.getNorthEast().lat + ',' + bounds.getNorthEast().lng},
                    {name: this.id + '_southwest', value: bounds.getSouthWest().lat + ',' + bounds.getSouthWest().lng},
                    {name: this.id + '_center', value: bounds.getCenter().lat + ',' + bounds.getCenter().lng},
                    {name: this.id + '_zoom', value: this.cfg.map.getZoom()}
                ]
            };


            this.callBehavior('stateChange', ext);
        }
    },

    /**
     * Sets up the event listeners for when a point on the map was selected.
     * @private
     */
    configurePointSelectListener: function() {
        var _self = this;

        this.cfg.map.on('click', function(event) {
            _self.firePointSelectEvent(event, 1);
        });

        this.cfg.map.on('dblclick', function(event) {
            _self.firePointSelectEvent(event, 2);
        });

    },

    /**
     * Triggers the behavior for when a point on the map was selected.
     * @private
     * @param {MapMouseEvent | IconMouseEvent} event The event that triggered the point selection.
     * @param {number} clickCount whether it was single or double click
     */
    firePointSelectEvent: function(event, clickCount) {
        var ext = {
                params: [
                    {name: this.id + '_pointLatLng', value: event.latlng.lat + ',' + event.latlng.lng}
                ]
            };
        
        if (clickCount === 1 && this.hasBehavior('pointSelect')) {
            this.callBehavior('pointSelect', ext);
        }
        if (clickCount === 2 && this.hasBehavior('pointDblSelect')) {
            this.callBehavior('pointDblSelect', ext);
        }
    },

    /**
     * Adds all overlay shapes (circle, polyline, or polygon) to this map.
     * @param {PrimeFaces.widget.GMap.Overlay[]} overlays A list of overlay shapes to add to this map.
     */
    addOverlays: function(overlays) {
        var _self = this;

        $.each(overlays, function(index, item){
            //console.log( index + " " + item );
            item.addTo( _self.cfg.map );

            //bind overlay click event
            item.on('click', function(event) {
                _self.fireOverlaySelectEvent(event, item.options, 1);
            });
            
            item.on('dblclick', function(event) {
                _self.fireOverlaySelectEvent(event, item.options, 2);
            });
        })
    },

});
