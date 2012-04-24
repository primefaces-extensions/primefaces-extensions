/*
 * Copyright 2011 PrimeFaces Extensions.
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
 *
 * $Id$
 */
package org.primefaces.extensions.component.ajaxerrorhandler;

import org.primefaces.component.api.Widget;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import java.util.ArrayList;
import java.util.List;

/**
 * AjaxErrorHandler
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
@ResourceDependencies({
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
        @ResourceDependency(library = "primefaces", name = "primefaces.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "ajaxerrorhandler/ajaxerrorhandler.js")
})
public class AjaxErrorHandler extends UIComponentBase implements Widget {
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.AjaxErrorHandler";
    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Pavol Slany / last modified by $Author$
     * @version $Revision$
     */
    static public enum PropertyKeys {
        id,
        type,
        title,
        body,
        button,
        buttonOnclick,
        onerror,
        mode,
        widgetVar;

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
    }

    public AjaxErrorHandler() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        List<String> setAttributes =
                (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }

        if (setAttributes != null && value == null) {
            final String attributeName = property.toString();
            final ValueExpression ve = getValueExpression(attributeName);
            if (ve == null) {
                setAttributes.remove(attributeName);
            } else if (!setAttributes.contains(attributeName)) {
                setAttributes.add(attributeName);
            }
        }
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    public String resolveWidgetVar() {
        final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        //        final FacesContext context = FacesContext.getCurrentInstance();
        //        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
        return null;
    }


    public String getId() {
        return (String) getStateHelper().eval(PropertyKeys.id, null);
    }

    public void setId(String id) {
        setAttribute(PropertyKeys.id, id);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, null);
    }

    public void setType(String type) {
        setAttribute(PropertyKeys.type, type);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(String title) {
        setAttribute(PropertyKeys.title, title);
    }

    public String getBody() {
        return (String) getStateHelper().eval(PropertyKeys.body, null);
    }

    public void setBody(String body) {
        setAttribute(PropertyKeys.body, body);
    }

    public String getButton() {
        return (String) getStateHelper().eval(PropertyKeys.button, null);
    }

    public void setButton(String button) {
        setAttribute(PropertyKeys.button, button);
    }

    public String getButtonOnclick() {
        return (String) getStateHelper().eval(PropertyKeys.buttonOnclick, null);
    }

    public void setButtonOnclick(String buttonOnclick) {
        setAttribute(PropertyKeys.buttonOnclick, buttonOnclick);
    }

    public String getOnerror() {
        return (String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(String onerror) {
        setAttribute(PropertyKeys.onerror, onerror);
    }

    public String getMode() {
        return (String) getStateHelper().eval(PropertyKeys.mode, null);
    }

    public void setMode(String mode) {
        setAttribute(PropertyKeys.mode, mode);
    }
}
