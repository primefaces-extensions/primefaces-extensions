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
package org.primefaces.extensions.component.inputotp;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;

/**
 * <code>InputOtp</code> component base class.
 *
 * @since 14.0.0
 */
@FacesComponentBase
public abstract class InputOtpBase extends AbstractPrimeHtmlInputText implements Widget, InputHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputOtp";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputOtpRenderer";

    public InputOtpBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "When true, only numeric input is accepted.", defaultValue = "false")
    public abstract boolean isIntegerOnly();

    @Property(description = "Inline style for each OTP input cell.")
    public abstract String getInputStyle();

    @Property(description = "Style class for each OTP input cell.")
    public abstract String getInputStyleClass();

    @Property(description = "Separator string or character between OTP inputs.")
    public abstract String getSeparator();

    @Property(description = "ARIA label for the OTP group.")
    public abstract String getAriaLabel();

    @Property(description = "Number of OTP input cells.", defaultValue = "4")
    public abstract int getLength();

    @Property(description = "When true, inputs are rendered as password type.", defaultValue = "false")
    public abstract boolean isMask();

}
