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
package org.primefaces.extensions.component.dynaform;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.api.AbstractDynamicData;

/**
 * <code>DynaForm</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.5
 */
@FacesComponentBase
public abstract class DynaFormBase extends AbstractDynamicData implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DynaForm";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DynaFormRenderer";

    public DynaFormBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "When true, form is submitted automatically on change.", defaultValue = "false")
    public abstract boolean isAutoSubmit();

    @Property(description = "When true, extended rows are shown by default.", defaultValue = "false")
    public abstract boolean isOpenExtended();

    @Property(description = "Position of the button bar: 'top', 'bottom', or 'both'.", defaultValue = "bottom")
    public abstract String getButtonBarPosition();

    @Property(description = "Comma-separated CSS classes for label and control columns.")
    public abstract String getColumnClasses();
}
