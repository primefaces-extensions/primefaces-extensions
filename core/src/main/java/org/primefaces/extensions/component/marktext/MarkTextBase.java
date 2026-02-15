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
package org.primefaces.extensions.component.marktext;

import jakarta.el.MethodExpression;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

/**
 * <code>MarkText</code> component.
 *
 * @author jxmai
 * @since 16.0.0
 */
public abstract class MarkTextBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MarkTextRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        //@formatter:off
        widgetVar,
        style,
        styleClass,
        value,
        forValue,
        caseSensitive,
        separateWordSearch,
        accuracy,
        actionListener,
        synonyms
        //@formatter:on
    }

    public MarkTextBase() {
        super.setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getValue() {
        return (String) getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(final String value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(final String forValue) {
        getStateHelper().put(PropertyKeys.forValue, forValue);
    }

    public Boolean getCaseSensitive() {
        return (Boolean) getStateHelper().eval(PropertyKeys.caseSensitive, false);
    }

    public void setCaseSensitive(final Boolean caseSensitive) {
        getStateHelper().put(PropertyKeys.caseSensitive, caseSensitive);
    }

    public Boolean getSeparateWordSearch() {
        return (Boolean) getStateHelper().eval(PropertyKeys.separateWordSearch, true);
    }

    public void setSeparateWordSearch(final Boolean separateWordSearch) {
        getStateHelper().put(PropertyKeys.separateWordSearch, separateWordSearch);
    }

    public String getAccuracy() {
        return (String) getStateHelper().eval(PropertyKeys.accuracy, "partially");
    }

    public void setAccuracy(final String accuracy) {
        getStateHelper().put(PropertyKeys.accuracy, accuracy);
    }

    public MethodExpression getActionListener() {
        return (MethodExpression) getStateHelper().get(PropertyKeys.actionListener);
    }

    public void setActionListener(final MethodExpression actionListener) {
        getStateHelper().put(PropertyKeys.actionListener, actionListener);
    }

    public Object getSynonyms() {
        return getStateHelper().eval(PropertyKeys.synonyms, null);
    }

    public void setSynonyms(final Object synonyms) {
        getStateHelper().put(PropertyKeys.synonyms, synonyms);
    }
}