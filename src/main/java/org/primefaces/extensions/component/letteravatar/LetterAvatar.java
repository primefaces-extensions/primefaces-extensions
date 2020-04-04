/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.letteravatar;

import javax.faces.application.ResourceDependencies;
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
@ResourceDependencies({
    @ResourceDependency(library = "primefaces", name = "components.css"),
    @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
    @ResourceDependency(library = "primefaces", name = "core.js")
})
public class LetterAvatar extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LetterAvatar";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LetterAvatarRenderer";

    public final static String COMPONENT_CLASS = "ui-letteravatar";
    public final static String COMPONENT_CLASS_ROUNDED = "ui-letteravatar-rounded";

    protected enum PropertyKeys {
        // @formatter:off
        style,
        styleClass,
        value,
        rounded,
        size,
        color;
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

    public Boolean isRounded() {
        return (Boolean) getStateHelper().eval(PropertyKeys.rounded, false);
    }

    public void setRounded(Boolean rounded) {
        getStateHelper().put(PropertyKeys.rounded, rounded);
    }

    public String getSize() {
        return (String) getStateHelper().eval(PropertyKeys.size, "3rem");
    }

    public void setSize(String size) {
        getStateHelper().put(PropertyKeys.size, size);
    }

    public String getColor() {
        return (String) getStateHelper().eval(PropertyKeys.color, "#fff");
    }

    public void setColor(String color) {
        getStateHelper().put(PropertyKeys.color, color);
    }

}
