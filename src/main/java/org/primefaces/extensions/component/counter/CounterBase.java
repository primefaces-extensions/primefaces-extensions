/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.counter;

import javax.faces.component.UIComponentBase;
import org.primefaces.component.api.Widget;

public abstract class CounterBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CounterRenderer";

    protected enum PropertyKeys {

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
        separator,
        decimal,
        prefix,
        suffix,
        autoStart,
        visible,
        oncountercomplete;

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
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

    public String getSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.separator, ",");
    }

    public void setSeparator(final String separator) {
        getStateHelper().put(PropertyKeys.separator, separator);
    }

    public String getDecimal() {
        return (String) getStateHelper().eval(PropertyKeys.decimal, ".");
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

    public Boolean isVisible() {
        return (Boolean) getStateHelper().eval(PropertyKeys.visible, true);
    }

    public void setVisible(final Boolean visible) {
        getStateHelper().put(PropertyKeys.visible, visible);
    }

    public String getOncountercomplete() {
        return (String) getStateHelper().eval(PropertyKeys.oncountercomplete, null);
    }

    public void setOncountercomplete(final String oncountercomplete) {
        getStateHelper().put(PropertyKeys.oncountercomplete, oncountercomplete);
    }

}
