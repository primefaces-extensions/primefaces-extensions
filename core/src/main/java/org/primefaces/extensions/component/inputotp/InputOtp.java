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

import java.util.List;
import java.util.Objects;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.NumberConverter;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.extensions.util.Constants;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.extensions.util.MessageFactory;
import org.primefaces.util.LangUtils;

/**
 * <code>InputOtp</code> component.
 *
 * @since 14.0.0
 */
@FacesComponent(value = InputOtp.COMPONENT_TYPE, namespace = InputOtp.COMPONENT_FAMILY)
@FacesComponentInfo(description = "InputOtp is a one-time password / verification code input with separate cells.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "inputotp/inputotp.css")
@ResourceDependency(library = Constants.LIBRARY, name = "inputotp/inputotp.js")
public class InputOtp extends InputOtpBaseImpl {

    public static final String STYLE_CLASS = "ui-inputotp ui-widget";
    public static final String RTL_STYLE_CLASS = "ui-inputotp-rtl";
    public static final String CELL_STYLE_CLASS = "ui-inputotp-input " + InputText.STYLE_CLASS;
    public static final String SEPARATOR_STYLE_CLASS = "ui-inputotp-separator";
    public static final String INPUT_SUFFIX = "_input";
    public static final String HIDDEN_SUFFIX = "_hidden";

    public static final List<String> INPUT_OTP_ATTRIBUTES_WITHOUT_EVENTS = List.of(
                "accesskey",
                "alt",
                "autocomplete",
                "dir",
                "lang",
                "inputmode",
                "tabindex",
                "title");

    @Override
    public String getInputClientId() {
        return getClientId() + INPUT_SUFFIX + 1;
    }

    @Override
    public String getValidatableInputClientId() {
        return getClientId() + HIDDEN_SUFFIX;
    }

    @Override
    protected void validateValue(FacesContext context, Object newValue) {
        super.validateValue(context, newValue);
        if (!isValid()) {
            return;
        }
        String submittedValue = Objects.toString(getSubmittedValue(), org.primefaces.util.Constants.EMPTY_STRING);
        if (LangUtils.isEmpty(submittedValue)) {
            return;
        }

        if (isValid() && isRequired() && submittedValue.length() != getLength()) {
            String requiredMessageStr = getRequiredMessage();
            FacesMessage message;
            if (null != requiredMessageStr) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            requiredMessageStr,
                            requiredMessageStr);
            }
            else {
                message = MessageFactory.getMessage(REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR,
                            MessageFactory.getLabel(context, this));
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
        }

        if (isValid() && isIntegerOnly()) {
            boolean isDigit = ExtLangUtils.isDigitsOnly(submittedValue);
            if (!isDigit) {
                setValid(false);
                String validatorMessage = getValidatorMessage();
                FacesMessage message;
                if (validatorMessage != null) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                validatorMessage,
                                validatorMessage);
                }
                else {
                    String exampleValue = "9".repeat(getLength());
                    message = MessageFactory.getMessage(NumberConverter.NUMBER_ID,
                                FacesMessage.SEVERITY_ERROR,
                                getSubmittedValue(),
                                exampleValue,
                                MessageFactory.getLabel(context, this));
                }
                context.addMessage(getClientId(context), message);
            }
        }
    }
}
