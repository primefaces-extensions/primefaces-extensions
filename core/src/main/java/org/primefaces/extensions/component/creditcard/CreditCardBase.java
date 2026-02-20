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
package org.primefaces.extensions.component.creditcard;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;

/**
 * <code>CreditCard</code> component base class.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 8.0.1
 */
@FacesComponentBase
public abstract class CreditCardBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CreditCard";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CreditCardRenderer";

    public CreditCardBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Width of the credit card widget in pixels.", defaultValue = "350")
    public abstract Integer getWidth();

    @Property(description = "Whether to format the card number with spaces.", defaultValue = "true")
    public abstract boolean isFormatting();

    @Property(description = "Label for the valid date field.", defaultValue = "valid\\nthru")
    public abstract String getLabelValidDate();

    @Property(description = "Label for the month/year field.", defaultValue = "month/year")
    public abstract String getLabelMonthYear();

    @Property(description = "Placeholder for the card number input.")
    public abstract String getPlaceholderNumber();

    @Property(description = "Placeholder for the cardholder name input.", defaultValue = "Full Name")
    public abstract String getPlaceholderName();

    @Property(description = "Placeholder for the expiry date input.")
    public abstract String getPlaceholderExpiry();

    @Property(description = "Placeholder for the CVC input.")
    public abstract String getPlaceholderCvc();
}
