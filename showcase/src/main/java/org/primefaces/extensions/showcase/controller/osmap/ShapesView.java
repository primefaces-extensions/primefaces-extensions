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
package org.primefaces.extensions.showcase.controller.osmap;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

@Named
@RequestScoped
public class ShapesView implements Serializable {

    private static final long serialVersionUID = 1L;

    private MapModel<Long> shapeModel;

    @PostConstruct
    public void init() {
        shapeModel = new DefaultMapModel<>();

        // Shared coordinates
        LatLng coord1 = new LatLng(36.879466, 30.667648);
        LatLng coord2 = new LatLng(36.883707, 30.689216);
        LatLng coord3 = new LatLng(36.879703, 30.706707);
        LatLng coord4 = new LatLng(36.885233, 30.702323);

        LatLng coord5 = new LatLng(36.90, 30.667648);
        LatLng coord6 = new LatLng(36.909466, 30.697648);
        LatLng coord7 = new LatLng(36.90, 30.707648);

        LatLng ne = new LatLng(36.899466, 30.667648);
        LatLng sw = new LatLng(36.890233, 30.707648);

        // Polyline
        Polyline<Long> polyline = new Polyline<>();
        polyline.setData(1L);
        polyline.getPaths().add(coord1);
        polyline.getPaths().add(coord2);
        polyline.getPaths().add(coord3);
        polyline.getPaths().add(coord4);

        polyline.setStrokeWeight(10);
        polyline.setStrokeColor("#FF9900");
        polyline.setStrokeOpacity(0.7);

        shapeModel.addOverlay(polyline);

        // Circle
        Circle<Long> circle1 = new Circle<>(coord1, 500);
        circle1.setStrokeColor("#d93c3c");
        circle1.setFillColor("#d93c3c");
        circle1.setFillOpacity(0.5);
        circle1.setData(1L);

        Circle<Long> circle2 = new Circle<>(coord4, 300);
        circle2.setStrokeColor("#00ff00");
        circle2.setFillColor("#00ff00");
        circle2.setStrokeOpacity(0.7);
        circle2.setFillOpacity(0.7);
        circle2.setData(2L);

        shapeModel.addOverlay(circle1);
        shapeModel.addOverlay(circle2);

        // Polygon
        Polygon<Long> polygon = new Polygon<>();
        polygon.setData(8L);
        polygon.getPaths().add(coord5);
        polygon.getPaths().add(coord6);
        polygon.getPaths().add(coord7);

        polygon.setStrokeColor("#FF0000");
        polygon.setFillColor("#FF0000");
        polygon.setStrokeOpacity(0.7);
        polygon.setFillOpacity(0.7);

        shapeModel.addOverlay(polygon);

        // Rectangle
        Rectangle<Long> rect = new Rectangle(new LatLngBounds(sw, ne));
        rect.setData(7L);
        rect.setStrokeColor("#0000ff");
        rect.setFillColor("#0000ff");
        rect.setFillOpacity(0.5);
        shapeModel.addOverlay(rect);
    }

    public MapModel<Long> getShapeModel() {
        return shapeModel;
    }
}
