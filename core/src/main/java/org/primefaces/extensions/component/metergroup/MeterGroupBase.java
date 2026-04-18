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
package org.primefaces.extensions.component.metergroup;

import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>MeterGroup</code> component.
 *
 * @since 15.0.15
 */
public abstract class MeterGroupBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MeterGroup";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MeterGroupRenderer";

    public enum PropertyKeys {
        value, min, max, orientation, labelPosition, labelOrientation, showLabels, style, styleClass, widgetVar
    }

    public MeterGroupBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(Object value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public double getMin() {
        return (Double) getStateHelper().eval(PropertyKeys.min, 0.0);
    }

    public void setMin(double min) {
        getStateHelper().put(PropertyKeys.min, min);
    }

    public double getMax() {
        return (Double) getStateHelper().eval(PropertyKeys.max, 0.0);
    }

    public void setMax(double max) {
        getStateHelper().put(PropertyKeys.max, max);
    }

    public String getOrientation() {
        return (String) getStateHelper().eval(PropertyKeys.orientation, "horizontal");
    }

    public void setOrientation(String orientation) {
        getStateHelper().put(PropertyKeys.orientation, orientation);
    }

    public String getLabelPosition() {
        return (String) getStateHelper().eval(PropertyKeys.labelPosition, "end");
    }

    public void setLabelPosition(String labelPosition) {
        getStateHelper().put(PropertyKeys.labelPosition, labelPosition);
    }

    public String getLabelOrientation() {
        return (String) getStateHelper().eval(PropertyKeys.labelOrientation, "horizontal");
    }

    public void setLabelOrientation(String labelOrientation) {
        getStateHelper().put(PropertyKeys.labelOrientation, labelOrientation);
    }

    public boolean isShowLabels() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showLabels, Boolean.TRUE);
    }

    public void setShowLabels(boolean showLabels) {
        getStateHelper().put(PropertyKeys.showLabels, showLabels);
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

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }
}
