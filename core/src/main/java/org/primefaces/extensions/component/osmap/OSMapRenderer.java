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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.model.map.*;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(rendererType = OSMapBase.DEFAULT_RENDERER, componentFamily = OSMapBase.COMPONENT_FAMILY)
public class OSMapRenderer extends CoreRenderer<OSMap> {

    @Override
    public void decode(FacesContext context, OSMap component) {
        super.decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext facesContext, OSMap component) throws IOException {
        encodeMarkup(facesContext, component);
        encodeScript(facesContext, component);
    }

    protected void encodeMarkup(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId + "_map", null);
        if (component.getStyle() != null) {
            writer.writeAttribute("style", component.getStyle(), null);
        }
        if (component.getStyleClass() != null) {
            writer.writeAttribute("class", component.getStyleClass(), null);
        }

        writer.endElement("div");
    }

    protected void encodeScript(FacesContext context, OSMap component) throws IOException {

        String[] parts = component.getCenter().split(",");

        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("OSMap", component);
        wb.attr("center", component.getCenter());
        wb.nativeAttr("map",
                    "L.map('" + component.getClientId() + "_map', { dragging: " + component.isDraggable() + ", zoomControl: " + component.isZoomControl()
                                + ", scrollWheelZoom: "
                                + component.isScrollWheel() + ", loadingControl: " + component.isLoadingControl() + " } ).setView(['"
                                + parts[0].trim() + "', '"
                                + parts[1].trim() + "'], " + component.getZoom() + ")");

        String tileUrl = "https://tile.openstreetmap.org/{z}/{x}/{y}.png";
        String attribution = "&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a>";

        if (component.getTileUrl() != null) {
            tileUrl = component.getTileUrl();
        }

        if (component.getAttribution() != null) {
            attribution = component.getAttribution();
        }

        wb.nativeAttr("tile", "L.tileLayer('" + tileUrl + "', { attribution: '" + attribution + "' })");

        encodeOverlays(context, component);

        if (component.isFullScreen()) {
            encodeFullScreen(context, component);
        }

        // Client events
        if (component.getOnPointClick() != null) {
            wb.callback("onPointClick", "function(event)", component.getOnPointClick() + ";");
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    protected void encodeFullScreen(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write(",fullScreen: " + component.isFullScreen());
    }

    protected void encodeOverlays(FacesContext context, OSMap component) throws IOException {
        MapModel model = component.getModel();

        // Overlays
        if (model != null) {
            if (!model.getMarkers().isEmpty()) {
                encodeMarkers(context, component);
            }
            if (!model.getPolylines().isEmpty()) {
                encodePolylines(context, component);
            }
            if (!model.getPolygons().isEmpty()) {
                encodePolygons(context, component);
            }
            if (!model.getCircles().isEmpty()) {
                encodeCircles(context, component);
            }
            if (!model.getRectangles().isEmpty()) {
                encodeRectangles(context, component);
            }
        }
    }

    protected void encodeMarkers(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        MapModel model = component.getModel();

        writer.write(",markers:[");

        for (Iterator<Marker> iterator = model.getMarkers().iterator(); iterator.hasNext();) {
            Marker marker = iterator.next();
            encodeMarker(context, marker);

            if (iterator.hasNext()) {
                writer.write(",\n");
            }
        }
        writer.write("]");
    }

    protected void encodeMarker(FacesContext context, Marker marker) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.write("L.marker([");
        writer.write(marker.getLatlng().getLat() + ", " + marker.getLatlng().getLng() + "]");

        writer.write(", {customId:'" + marker.getId() + "'");

        if (marker.getIcon() != null) {
            writer.write(", icon:");
            encodeIcon(context, marker.getIcon());
        }
        if (marker.isDraggable()) {
            writer.write(",draggable: true");
        }

        writer.write("})");
    }

    protected void encodeIcon(FacesContext context, Object icon) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (icon instanceof String) {
            writer.write("'" + icon + "'");
        }
        else {
            throw new FacesException("OSMap marker icon must be String");
        }
    }

    protected void encodePolylines(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        MapModel model = component.getModel();

        writer.write(",polylines:[");

        for (Iterator<Polyline> lines = model.getPolylines().iterator(); lines.hasNext();) {
            Polyline polyline = lines.next();

            writer.write("L.polyline([");

            encodePaths(context, polyline.getPaths());

            writer.write("], {customId:'" + polyline.getId() + "'");

            writer.write(",opacity:" + polyline.getStrokeOpacity());
            writer.write(",weight:" + polyline.getStrokeWeight());

            if (polyline.getStrokeColor() != null) {
                writer.write(",color:'" + polyline.getStrokeColor() + "'");
            }

            writer.write("})");

            if (lines.hasNext()) {
                writer.write(",");
            }
        }

        writer.write("]");
    }

    protected void encodePolygons(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        MapModel model = component.getModel();

        writer.write(",polygons:[");

        for (Iterator<Polygon> polygons = model.getPolygons().iterator(); polygons.hasNext();) {
            Polygon polygon = polygons.next();

            writer.write("L.polygon([");

            encodePaths(context, polygon.getPaths());

            writer.write("], {customId:'" + polygon.getId() + "'");

            writer.write(",opacity:" + polygon.getStrokeOpacity());
            writer.write(",weight:" + polygon.getStrokeWeight());
            writer.write(",fillOpacity:" + polygon.getFillOpacity());

            if (polygon.getStrokeColor() != null) {
                writer.write(",color:'" + polygon.getStrokeColor() + "'");
            }
            if (polygon.getFillColor() != null) {
                writer.write(",fillColor:'" + polygon.getFillColor() + "'");
            }
            writer.write("})");

            if (polygons.hasNext()) {
                writer.write(",");
            }
        }

        writer.write("]");
    }

    protected void encodeCircles(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        MapModel model = component.getModel();

        writer.write(",circles:[");

        for (Iterator<Circle> circles = model.getCircles().iterator(); circles.hasNext();) {
            Circle circle = circles.next();

            writer.write("L.circle([");
            writer.write(circle.getCenter().getLat() + ", " + circle.getCenter().getLng() + "]");

            writer.write(", {customId:'" + circle.getId() + "'");

            writer.write(",radius:" + circle.getRadius());

            writer.write(",opacity:" + circle.getStrokeOpacity());
            writer.write(",weight:" + circle.getStrokeWeight());
            writer.write(",fillOpacity:" + circle.getFillOpacity());

            if (circle.getStrokeColor() != null) {
                writer.write(",color:'" + circle.getStrokeColor() + "'");
            }
            if (circle.getFillColor() != null) {
                writer.write(",fillColor:'" + circle.getFillColor() + "'");
            }

            writer.write("})");

            if (circles.hasNext()) {
                writer.write(",");
            }
        }

        writer.write("]");
    }

    protected void encodeRectangles(FacesContext context, OSMap component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        MapModel model = component.getModel();

        writer.write(",rectangles:[");

        for (Iterator<Rectangle> rectangles = model.getRectangles().iterator(); rectangles.hasNext();) {
            Rectangle rectangle = rectangles.next();

            LatLng ne = rectangle.getBounds().getNorthEast();
            LatLng sw = rectangle.getBounds().getSouthWest();

            writer.write("L.rectangle([");
            writer.write("[" + sw.getLat() + ", " + sw.getLng() + "],[" + ne.getLat() + ", " + ne.getLng() + "]");
            writer.write("]");

            writer.write(", {customId:'" + rectangle.getId() + "'");

            writer.write(",opacity:" + rectangle.getStrokeOpacity());
            writer.write(",weight:" + rectangle.getStrokeWeight());
            writer.write(",fillOpacity:" + rectangle.getFillOpacity());

            if (rectangle.getStrokeColor() != null) {
                writer.write(",color:'" + rectangle.getStrokeColor() + "'");
            }
            if (rectangle.getFillColor() != null) {
                writer.write(",fillColor:'" + rectangle.getFillColor() + "'");
            }

            writer.write("})");

            if (rectangles.hasNext()) {
                writer.write(",");
            }
        }

        writer.write("]");
    }

    protected void encodePaths(FacesContext context, List<LatLng> paths) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        for (Iterator<LatLng> coords = paths.iterator(); coords.hasNext();) {
            LatLng coord = coords.next();

            writer.write("[" + coord.getLat() + ", " + coord.getLng() + "]");

            if (coords.hasNext()) {
                writer.write(",");
            }

        }
    }

    @Override
    public void encodeChildren(FacesContext context, OSMap component) throws IOException {
        // Do Nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
