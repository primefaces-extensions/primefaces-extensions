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
package org.primefaces.extensions.component.metergroup;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;

/**
 * <code>MeterGroup</code> component.
 *
 * @since 16.0.0
 */
@FacesComponentBase
public abstract class MeterGroupBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MeterGroup";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MeterGroupRenderer";

    public MeterGroupBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "A list of meter items (MeterGroupItem).", required = true)
    public abstract Object getValue();

    @Property(defaultValue = "0.0", description = "Min value of the meter.")
    public abstract double getMin();

    @Property(defaultValue = "0.0", description = "Max value of the meter. If 0, defaults to 100 or sum of values.")
    public abstract double getMax();

    @Property(defaultValue = "horizontal", description = "Orientation of the meter. Valid values: horizontal, vertical.")
    public abstract String getOrientation();

    @Property(defaultValue = "end", description = "Position of the labels relative to the meters. Valid values: start, end.")
    public abstract String getLabelPosition();

    @Property(defaultValue = "horizontal", description = "Orientation of the labels. Valid values: horizontal, vertical.")
    public abstract String getLabelOrientation();

    @Property(defaultValue = "true", description = "Whether to show the labels.")
    public abstract boolean isShowLabels();

}
