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

public abstract class CommandPaletteItemBase extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CommandPaletteItem";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        group, value, label, groupTitle, itemTitle
    }

    public CommandPaletteItemBase() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getGroup() {
        return (String) getStateHelper().eval(PropertyKeys.group, null);
    }

    public void setGroup(String group) {
        getStateHelper().put(PropertyKeys.group, group);
    }

    public String getValue() {
        return (String) getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(String value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getLabel() {
        return (String) getStateHelper().eval(PropertyKeys.label, null);
    }

    public void setLabel(String label) {
        getStateHelper().put(PropertyKeys.label, label);
    }

    public String getGroupTitle() {
        return (String) getStateHelper().eval(PropertyKeys.groupTitle, null);
    }

    public void setGroupTitle(String groupTitle) {
        getStateHelper().put(PropertyKeys.groupTitle, groupTitle);
    }

    public String getItemTitle() {
        return (String) getStateHelper().eval(PropertyKeys.itemTitle, null);
    }

    public void setItemTitle(String itemTitle) {
        getStateHelper().put(PropertyKeys.itemTitle, itemTitle);
    }
}
