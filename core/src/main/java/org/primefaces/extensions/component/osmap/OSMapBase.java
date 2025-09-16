/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

public abstract class OSMapBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.OSMapRenderer";

    public enum PropertyKeys {

        widgetVar, model, style, styleClass, center, zoom, zoomControl, attribution, tileUrl, draggable, onPointClick, scrollWheel, fullScreen
    }

    public OSMapBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public org.primefaces.model.map.MapModel getModel() {
        return (org.primefaces.model.map.MapModel) getStateHelper().eval(PropertyKeys.model, null);
    }

    public void setModel(org.primefaces.model.map.MapModel model) {
        getStateHelper().put(PropertyKeys.model, model);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getCenter() {
        return (String) getStateHelper().eval(PropertyKeys.center, null);
    }

    public void setCenter(String center) {
        getStateHelper().put(PropertyKeys.center, center);
    }

    public int getZoom() {
        return (Integer) getStateHelper().eval(PropertyKeys.zoom, 8);
    }

    public void setZoom(int zoom) {
        getStateHelper().put(PropertyKeys.zoom, zoom);
    }

    public boolean isZoomControl() {
        return (Boolean) getStateHelper().eval(PropertyKeys.zoomControl, true);
    }

    public void setZoomControl(boolean zoomControl) {
        getStateHelper().put(PropertyKeys.zoomControl, zoomControl);
    }

    public String getAttribution() {
        return (String) getStateHelper().eval(PropertyKeys.attribution, null);
    }

    public void setAttribution(String attribution) {
        getStateHelper().put(PropertyKeys.attribution, attribution);
    }

    public String getTileUrl() {
        return (String) getStateHelper().eval(PropertyKeys.tileUrl, null);
    }

    public void setTileUrl(String tileUrl) {
        getStateHelper().put(PropertyKeys.tileUrl, tileUrl);
    }

    public boolean isDraggable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggable, true);
    }

    public void setDraggable(boolean draggable) {
        getStateHelper().put(PropertyKeys.draggable, draggable);
    }

    public String getOnPointClick() {
        return (String) getStateHelper().eval(PropertyKeys.onPointClick, null);
    }

    public void setOnPointClick(String onPointClick) {
        getStateHelper().put(PropertyKeys.onPointClick, onPointClick);
    }

    public boolean isScrollWheel() {
        return (Boolean) getStateHelper().eval(PropertyKeys.scrollWheel, true);
    }

    public void setScrollWheel(boolean scrollWheel) {
        getStateHelper().put(PropertyKeys.scrollWheel, scrollWheel);
    }

    public boolean isFullScreen() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fullScreen, true);
    }

    public void setFullScreen(boolean fullScreen) {
        getStateHelper().put(PropertyKeys.fullScreen, fullScreen);
    }
}
