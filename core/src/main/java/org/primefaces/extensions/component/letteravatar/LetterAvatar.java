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
package org.primefaces.extensions.component.letteravatar;

import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.Widget;

/**
 * <code>LetterAvatar</code> component.
 *
 * @author https://github.com/aripddev
 * @since 7.0
 */
@FacesComponent(value = LetterAvatar.COMPONENT_TYPE)
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "letteravatar/letteravatar.css")
public class LetterAvatar extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LetterAvatar";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LetterAvatarRenderer";

    public static final String COMPONENT_CLASS = "ui-letteravatar";
    public static final String COMPONENT_CLASS_ROUNDED = "ui-letteravatar-rounded";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        // @formatter:off
        style,
        styleClass,
        value,
        rounded,
        size,
        color,
        backgroundColor;
        // @formatter:on
    }

    public LetterAvatar() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
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

    public String getValue() {
        return (String) getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(String value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public boolean isRounded() {
        return (Boolean) getStateHelper().eval(PropertyKeys.rounded, false);
    }

    public void setRounded(boolean rounded) {
        getStateHelper().put(PropertyKeys.rounded, rounded);
    }

    public String getSize() {
        return (String) getStateHelper().eval(PropertyKeys.size, "3rem");
    }

    public void setSize(String size) {
        getStateHelper().put(PropertyKeys.size, size);
    }

    public String getColor() {
        return (String) getStateHelper().eval(PropertyKeys.color, null);
    }

    public void setColor(String color) {
        getStateHelper().put(PropertyKeys.color, color);
    }

    public String getBackgroundColor() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundColor, null);
    }

    public void setBackgroundColor(String backgroundColor) {
        getStateHelper().put(PropertyKeys.backgroundColor, backgroundColor);
    }

}
