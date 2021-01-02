/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>CreditCard</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 8.0.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "creditcard/creditcard.css")
@ResourceDependency(library = "primefaces-extensions", name = "creditcard/creditcard.js")
public class CreditCard extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CreditCard";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String STYLE_CLASS = "ui-credit-card ";

    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CreditCardRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        width,
        formatting,
        labelValidDate,
        labelMonthYear,
        placeholderNumber,
        placeholderName,
        placeholderExpiry,
        placeholderCvc
        // @formatter:on
    }

    /**
     * Default constructor
     */
    public CreditCard() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public int getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, 350);
    }

    public void setWidth(final int _width) {
        getStateHelper().put(PropertyKeys.width, _width);
    }

    public boolean isFormatting() {
        return (Boolean) getStateHelper().eval(PropertyKeys.formatting, true);
    }

    public void setFromatting(final boolean _formatting) {
        getStateHelper().put(PropertyKeys.formatting, _formatting);
    }

    public String getLabelValidDate() {
        return (String) getStateHelper().eval(PropertyKeys.labelValidDate, "valid\\nthru");
    }

    public void setLabelValidDate(final String _labelValidDate) {
        getStateHelper().put(PropertyKeys.labelValidDate, _labelValidDate);
    }

    public String getLabelMonthYear() {
        return (String) getStateHelper().eval(PropertyKeys.labelMonthYear, "month/year");
    }

    public void setLabelMonthYear(final String _labelMonthYear) {
        getStateHelper().put(PropertyKeys.labelMonthYear, _labelMonthYear);
    }

    public String getPlaceholderNumber() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderNumber, null);
    }

    public void setPlaceholderNumber(final String _placeholderNumber) {
        getStateHelper().put(PropertyKeys.placeholderNumber, _placeholderNumber);
    }

    public String getPlaceholderName() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderName, "Full Name");
    }

    public void setPlaceholderName(final String _placeholderName) {
        getStateHelper().put(PropertyKeys.placeholderName, _placeholderName);
    }

    public String getPlaceholderExpiry() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderExpiry, null);
    }

    public void setPlaceholderExpiry(final String _placeholderExpiry) {
        getStateHelper().put(PropertyKeys.placeholderExpiry, _placeholderExpiry);
    }

    public String getPlaceholderCvc() {
        return (String) getStateHelper().eval(PropertyKeys.placeholderCvc, null);
    }

    public void setPlaceholderCvc(final String _placeholderCvc) {
        getStateHelper().put(PropertyKeys.placeholderCvc, _placeholderCvc);
    }
}
