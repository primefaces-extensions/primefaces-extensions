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
package org.primefaces.extensions.component.calculator;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ButtonEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;

/**
 * <code>Calculator</code> component base class.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "open", event = OpenEvent.class, description = "Fires when the calculator panel is opened.", defaultEvent = true),
            @FacesBehaviorEvent(name = "close", event = CloseEvent.class, description = "Fires when the calculator panel is closed."),
            @FacesBehaviorEvent(name = "button", event = ButtonEvent.class, description = "Fires when a calculator button is pressed.")
})
public abstract class CalculatorBase extends UIComponentBase implements Widget, RTLAware, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Calculator";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CalculatorRenderer";

    public CalculatorBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Search expression for the target input component to attach the calculator to.")
    public abstract String getFor();

    @Property(description = "When to show the calculator: 'focus', 'button', or 'both'.", defaultValue = "focus")
    public abstract String getShowOn();

    @Property(description = "Layout of the calculator: 'standard' or 'horizontal'.", defaultValue = "standard")
    public abstract String getLayout();

    @Property(description = "Locale for number formatting. Can be a string or java.util.Locale instance.")
    public abstract Object getLocale();

    @Property(description = "Decimal precision (number of decimal places).", defaultValue = "10")
    public abstract Integer getPrecision();

    @Property(description = "Client-side callback when the calculator panel is opened.")
    public abstract String getOnopen();

    @Property(description = "Client-side callback when the calculator panel is closed.")
    public abstract String getOnclose();

    @Property(description = "Client-side callback when a calculator button is activated.")
    public abstract String getOnbutton();
}
