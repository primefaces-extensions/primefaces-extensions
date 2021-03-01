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
package org.primefaces.extensions.component.counter;

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

/**
 * <code>Counter</code> component.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
public abstract class CounterBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CounterRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        //@formatter:off
        widgetVar,
        style,
        styleClass,
        start,
        end,
        decimals,
        duration,
        useGrouping,
        useEasing,
        smartEasingThreshold,
        smartEasingAmount,
        locale,
        separator,
        decimal,
        prefix,
        suffix,
        autoStart,
        visible,
        onstart,
        onend
        //@formatter:off
    }

    public CounterBase() {
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

    public Double getStart() {
        return (Double) getStateHelper().eval(PropertyKeys.start, 0.0);
    }

    public void setStart(final Double start) {
        getStateHelper().put(PropertyKeys.start, start);
    }

    public Double getEnd() {
        return (Double) getStateHelper().eval(PropertyKeys.end, null);
    }

    public void setEnd(final Double end) {
        getStateHelper().put(PropertyKeys.end, end);
    }

    public Integer getDecimals() {
        return (Integer) getStateHelper().eval(PropertyKeys.decimals, 0);
    }

    public void setDecimals(final Integer decimals) {
        getStateHelper().put(PropertyKeys.decimals, decimals);
    }

    public Integer getDuration() {
        return (Integer) getStateHelper().eval(PropertyKeys.duration, 2);
    }

    public void setDuration(final Integer duration) {
        getStateHelper().put(PropertyKeys.duration, duration);
    }

    public Boolean isUseGrouping() {
        return (Boolean) getStateHelper().eval(PropertyKeys.useGrouping, true);
    }

    public void setUseGrouping(final Boolean useGrouping) {
        getStateHelper().put(PropertyKeys.useGrouping, useGrouping);
    }

    public Boolean isUseEasing() {
        return (Boolean) getStateHelper().eval(PropertyKeys.useEasing, true);
    }

    public void setUseEasing(final Boolean useEasing) {
        getStateHelper().put(PropertyKeys.useEasing, useEasing);
    }

    public Integer getSmartEasingThreshold() {
        return (Integer) getStateHelper().eval(PropertyKeys.smartEasingThreshold, 999);
    }

    public void setSmartEasingThreshold(final Integer smartEasingThreshold) {
        getStateHelper().put(PropertyKeys.smartEasingThreshold, smartEasingThreshold);
    }

    public Integer getSmartEasingAmount() {
        return (Integer) getStateHelper().eval(PropertyKeys.smartEasingAmount, 333);
    }

    public void setSmartEasingAmount(final Integer smartEasingAmount) {
        getStateHelper().put(PropertyKeys.smartEasingAmount, smartEasingAmount);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public String getSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.separator, null);
    }

    public void setSeparator(final String separator) {
        getStateHelper().put(PropertyKeys.separator, separator);
    }

    public String getDecimal() {
        return (String) getStateHelper().eval(PropertyKeys.decimal, null);
    }

    public void setDecimal(final String decimal) {
        getStateHelper().put(PropertyKeys.decimal, decimal);
    }

    public String getPrefix() {
        return (String) getStateHelper().eval(PropertyKeys.prefix, "");
    }

    public void setPrefix(final String prefix) {
        getStateHelper().put(PropertyKeys.prefix, prefix);
    }

    public String getSuffix() {
        return (String) getStateHelper().eval(PropertyKeys.suffix, "");
    }

    public void setSuffix(final String suffix) {
        getStateHelper().put(PropertyKeys.suffix, suffix);
    }

    public Boolean isAutoStart() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoStart, true);
    }

    public void setAutoStart(final Boolean autoStart) {
        getStateHelper().put(PropertyKeys.autoStart, autoStart);
    }

    public boolean isVisible() {
        return (Boolean) getStateHelper().eval(PropertyKeys.visible, true);
    }

    public void setVisible(final boolean visible) {
        getStateHelper().put(PropertyKeys.visible, visible);
    }

    public String getOnend() {
        return (String) getStateHelper().eval(PropertyKeys.onend, null);
    }

    public void setOnend(final String onend) {
        getStateHelper().put(PropertyKeys.onend, onend);
    }

    public String getOnstart() {
        return (String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public void setOnstart(final String onstart) {
        getStateHelper().put(PropertyKeys.onstart, onstart);
    }

}
