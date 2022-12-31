/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.component.fuzzysearch;

import javax.faces.component.UISelectOne;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

/**
 * <code>FuzzySearch</code> component.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
public abstract class FuzzySearchBase extends UISelectOne implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FuzzySearchRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        // @formatter:off
        widgetVar,
        disabled,
        label,
        onchange,
        style,
        styleClass,
        tabindex,
        unselectable,
        resultStyle,
        resultStyleClass,
        placeholder,
        highlight,
        listItemsAtTheBeginning
        // @formatter:on
    }

    public FuzzySearchBase() {
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

    public boolean isDisabled() {
        return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public void setDisabled(final boolean disabled) {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }

    public String getLabel() {
        return (String) getStateHelper().eval(PropertyKeys.label, null);
    }

    public void setLabel(final String label) {
        getStateHelper().put(PropertyKeys.label, label);
    }

    public String getOnchange() {
        return (String) getStateHelper().eval(PropertyKeys.onchange, null);
    }

    public void setOnchange(final String onchange) {
        getStateHelper().put(PropertyKeys.onchange, onchange);
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

    public String getTabindex() {
        return (String) getStateHelper().eval(PropertyKeys.tabindex, null);
    }

    public void setTabindex(final String tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, tabindex);
    }

    public boolean isUnselectable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.unselectable, true);
    }

    public void setUnselectable(final boolean unselectable) {
        getStateHelper().put(PropertyKeys.unselectable, unselectable);
    }

    public boolean isHighlight() {
        return (Boolean) getStateHelper().eval(PropertyKeys.highlight, true);
    }

    public void setHighlight(final boolean highlight) {
        getStateHelper().put(PropertyKeys.highlight, highlight);
    }

    public String getResultStyle() {
        return (String) getStateHelper().eval(PropertyKeys.resultStyle, null);
    }

    public void setResultStyle(final String resultStyle) {
        getStateHelper().put(PropertyKeys.resultStyle, resultStyle);
    }

    public String getResultStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.resultStyleClass, null);
    }

    public void setResultStyleClass(final String resultStyleClass) {
        getStateHelper().put(PropertyKeys.resultStyleClass, resultStyleClass);
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(final String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public boolean isListItemsAtTheBeginning() {
        return (Boolean) getStateHelper().eval(PropertyKeys.listItemsAtTheBeginning, false);
    }

    public void setListItemsAtTheBeginning(final boolean listItemsAtTheBeginning) {
        getStateHelper().put(PropertyKeys.listItemsAtTheBeginning, listItemsAtTheBeginning);
    }

}
