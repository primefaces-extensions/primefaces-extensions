/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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
package org.primefaces.extensions.component.inputpin;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;
import javax.faces.convert.NumberConverter;

import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.extensions.component.inputphone.InputPhone;
import org.primefaces.extensions.util.Constants;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.extensions.util.MessageFactory;
import org.primefaces.util.LangUtils;

/**
 * <code>InputPin</code> component.
 *
 * @since 14.0.0
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "inputpin/inputpin.css")
@ResourceDependency(library = Constants.LIBRARY, name = "inputpin/inputpin.js")
public class InputPin extends AbstractPrimeHtmlInputText implements Widget, InputHolder, RTLAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputPin";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputPinRenderer";

    public static final String STYLE_CLASS = "ui-inputpin ui-widget";
    public static final String RTL_STYLE_CLASS = "ui-inputpin-rtl";
    public static final String CELL_STYLE_CLASS = "ui-inputpin-cell " + InputText.STYLE_CLASS;
    public static final String INPUT_SUFFIX = "_input";
    public static final String HIDDEN_SUFFIX = "_hidden";

    // disabled, readonly, style, styleClass, size, placeholder handled by component renderer
    public static final List<String> INPUT_PIN_ATTRS_WITHOUT_EVENTS = LangUtils.unmodifiableList(
                "accesskey",
                "alt",
                "autocomplete",
                "dir",
                "lang",
                "inputmode",
                "tabindex",
                "title");

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        placeholder,
        autocomplete,
        type,
        numeric,
        inputStyle,
        inputStyleClass,
        separator,
        ariaLabel
    }
    // @formatter:on

    public InputPin() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getInputClientId() {
        return getClientId() + INPUT_SUFFIX + 1;
    }

    @Override
    public String getValidatableInputClientId() {
        return getClientId() + HIDDEN_SUFFIX;
    }

    @Override
    public String getLabelledBy() {
        return (String) getStateHelper().get("labelledby");
    }

    @Override
    public void setLabelledBy(final String labelledBy) {
        getStateHelper().put("labelledby", labelledBy);
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(InputPhone.PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(final String placeholder) {
        getStateHelper().put(InputPhone.PropertyKeys.placeholder, placeholder);
    }

    @Override
    public String getAutocomplete() {
        return (String) getStateHelper().eval(PropertyKeys.autocomplete, "off");
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, "text");
    }

    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public boolean isNumeric() {
        return (Boolean) getStateHelper().eval(PropertyKeys.numeric, false);
    }

    public void setNumeric(final boolean numeric) {
        getStateHelper().put(PropertyKeys.numeric, numeric);
    }

    public String getInputStyle() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyle, null);
    }

    public void setInputStyle(final String inputStyle) {
        getStateHelper().put(PropertyKeys.inputStyle, inputStyle);
    }

    public String getInputStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyleClass, null);
    }

    public void setInputStyleClass(final String inputStyleClass) {
        getStateHelper().put(PropertyKeys.inputStyleClass, inputStyleClass);
    }

    public String getSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.separator, null);
    }

    public void setSeparator(final String separator) {
        getStateHelper().put(PropertyKeys.separator, separator);
    }

    public String getAriaLabel() {
        return (String) getStateHelper().eval(PropertyKeys.ariaLabel, null);
    }

    public void setAriaLabel(final String ariaLabel) {
        getStateHelper().put(PropertyKeys.ariaLabel, ariaLabel);
    }

    @Override
    protected void validateValue(FacesContext context, Object newValue) {
        super.validateValue(context, newValue);
        if (!isValid()) {
            return;
        }
        String submittedValue = (String) getSubmittedValue();
        if (LangUtils.isEmpty(submittedValue)) {
            return;
        }
        if (isNumeric()) {
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
                    String exampleValue = "9".repeat(getSize());
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
