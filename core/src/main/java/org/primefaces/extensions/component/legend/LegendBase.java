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
package org.primefaces.extensions.component.legend;

import java.util.Map;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Facet;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;

/**
 * <code>Legend</code> component base class.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
@FacesComponentBase
public abstract class LegendBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Legend";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LegendRenderer";

    public LegendBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Facet(description = "Legend title.")
    public abstract UIComponent getTitleFacet();

    @Facet(description = "Legend footer.")
    public abstract UIComponent getFooterFacet();

    @Property(description = "Legend title.")
    public abstract String getTitle();

    @Property(description = "Legend footer.")
    public abstract String getFooter();

    @Property(description = "Map of label to color for legend items.")
    public abstract Map<String, String> getValues();

    @Property(description = "Layout orientation: vertical or horizontal.", defaultValue = "vertical")
    public abstract String getLayout();
}
