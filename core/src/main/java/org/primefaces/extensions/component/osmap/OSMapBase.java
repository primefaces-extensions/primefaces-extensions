/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.osmap;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.MapModel;

/**
 * CDK base for the OSMap (OpenStreetMap) component.
 *
 * @since 10.0.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "overlaySelect", event = OverlaySelectEvent.class, description = "Fires when an overlay is selected."),
            @FacesBehaviorEvent(name = "overlayDblSelect", event = OverlaySelectEvent.class, description = "Fires when an overlay is double-selected."),
            @FacesBehaviorEvent(name = "stateChange", event = StateChangeEvent.class, description = "Fires when the map view state changes."),
            @FacesBehaviorEvent(name = "pointSelect", event = PointSelectEvent.class, description = "Fires when a point is selected.", defaultEvent = true),
            @FacesBehaviorEvent(name = "pointDblSelect", event = PointSelectEvent.class, description = "Fires when a point is double-selected."),
            @FacesBehaviorEvent(name = "markerDrag", event = MarkerDragEvent.class, description = "Fires when a marker is dragged.")
})
public abstract class OSMapBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.OSMapRenderer";

    public OSMapBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Client-side widget variable name.")
    public abstract String getWidgetVar();

    @Property(description = "Map model with markers, polylines, polygons, etc.")
    public abstract MapModel getModel();

    @Property(description = "Center of the map as 'lat,lng'.")
    public abstract String getCenter();

    @Property(description = "Initial zoom level.", defaultValue = "8")
    public abstract int getZoom();

    @Property(description = "Whether zoom control is shown.", defaultValue = "true")
    public abstract boolean isZoomControl();

    @Property(description = "Attribution text for the tile layer.")
    public abstract String getAttribution();

    @Property(description = "URL template for map tiles.")
    public abstract String getTileUrl();

    @Property(description = "Whether the map is draggable.", defaultValue = "true")
    public abstract boolean isDraggable();

    @Property(description = "Client-side script to run when a point is clicked.")
    public abstract String getOnPointClick();

    @Property(description = "Whether scroll wheel zoom is enabled.", defaultValue = "true")
    public abstract boolean isScrollWheel();

    @Property(description = "Whether full screen control is shown.", defaultValue = "true")
    public abstract boolean isFullScreen();

    @Property(description = "Whether loading control is shown.", defaultValue = "true")
    public abstract boolean isLoadingControl();
}
