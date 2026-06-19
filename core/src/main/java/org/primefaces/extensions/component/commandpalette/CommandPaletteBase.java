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
package org.primefaces.extensions.component.commandpalette;

import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

public abstract class CommandPaletteBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CommandPalette";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CommandPaletteRenderer";

    public static final String STYLE_CLASS = "ui-commandpalette";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        forValue("for"), label, filterPlaceholder, showHeader, showFilter, onSelect, width, height, triggerEvent, style, styleClass;

        private final String toString;

        PropertyKeys(String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
            toString = null;
        }

        @Override
        public String toString() {
            return toString != null ? toString : name();
        }
    }

    public CommandPaletteBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public void setFor(String forValue) {
        getStateHelper().put(PropertyKeys.forValue, forValue);
    }

    public String getLabel() {
        return (String) getStateHelper().eval(PropertyKeys.label, null);
    }

    public void setLabel(String label) {
        getStateHelper().put(PropertyKeys.label, label);
    }

    public String getFilterPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.filterPlaceholder, "Search...");
    }

    public void setFilterPlaceholder(String filterPlaceholder) {
        getStateHelper().put(PropertyKeys.filterPlaceholder, filterPlaceholder);
    }

    public boolean isShowHeader() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showHeader, true);
    }

    public void setShowHeader(boolean showHeader) {
        getStateHelper().put(PropertyKeys.showHeader, showHeader);
    }

    public boolean isShowFilter() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showFilter, true);
    }

    public void setShowFilter(boolean showFilter) {
        getStateHelper().put(PropertyKeys.showFilter, showFilter);
    }

    public String getOnSelect() {
        return (String) getStateHelper().eval(PropertyKeys.onSelect, null);
    }

    public void setOnSelect(String onSelect) {
        getStateHelper().put(PropertyKeys.onSelect, onSelect);
    }

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width, "280");
    }

    public void setWidth(String width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public String getHeight() {
        return (String) getStateHelper().eval(PropertyKeys.height, "400");
    }

    public void setHeight(String height) {
        getStateHelper().put(PropertyKeys.height, height);
    }

    public String getTriggerEvent() {
        return (String) getStateHelper().eval(PropertyKeys.triggerEvent, "contextmenu");
    }

    public void setTriggerEvent(String triggerEvent) {
        getStateHelper().put(PropertyKeys.triggerEvent, triggerEvent);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }
}
