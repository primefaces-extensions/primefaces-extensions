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
package org.primefaces.extensions.component.fab;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import org.primefaces.component.api.Widget;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.util.ComponentUtils;

/**
 * <code>FloatingActionButton</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "fab/fab.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "fab/fab.js"),
})
public class FloatingActionButton extends AbstractMenu implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FloatingActionButton";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FloatingActionButtonRenderer";

    public static final String STYLE_CLASS = "ui-fab ui-widget";

    // @formatter:off
    public enum PropertyKeys {
        model,
        tabindex,
        icon,
        iconActive,
        widgetVar,
        style,
        styleClass
    }
    // @formatter:on

    public FloatingActionButton() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public org.primefaces.model.menu.MenuModel getModel() {
        return (org.primefaces.model.menu.MenuModel) getStateHelper().eval(PropertyKeys.model, null);
    }

    public void setModel(org.primefaces.model.menu.MenuModel model) {
        getStateHelper().put(PropertyKeys.model, model);
    }

    @Override
    public String getTabindex() {
        return (String) getStateHelper().eval(PropertyKeys.tabindex, "0");
    }

    @Override
    public void setTabindex(String tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, tabindex);
    }

    public String getIcon() {
        return (String) getStateHelper().eval(PropertyKeys.icon, "pi pi-plus");
    }

    public void setIcon(String icon) {
        getStateHelper().put(PropertyKeys.icon, icon);
    }

    public String getIconActive() {
        return (String) getStateHelper().eval(PropertyKeys.iconActive, null);
    }

    public void setIconActive(String iconActive) {
        getStateHelper().put(PropertyKeys.iconActive, iconActive);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
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

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

}
