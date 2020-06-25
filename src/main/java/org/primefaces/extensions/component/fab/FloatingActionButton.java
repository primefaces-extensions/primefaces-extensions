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
package org.primefaces.extensions.component.fab;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * <code>FloatingActionButton</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "fab/fab.css")
@ResourceDependency(library = "primefaces-extensions", name = "fab/fab.js")
public class FloatingActionButton extends AbstractMenu implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FloatingActionButton";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FloatingActionButtonRenderer";

    public static final String STYLE_CLASS = "ui-fab ui-widget";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(OpenEvent.NAME, CloseEvent.NAME));

    // @formatter:off
    public enum PropertyKeys {
        model,
        tabindex,
        icon,
        iconActive,
        keepOpen,
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

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return OpenEvent.NAME;
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (ComponentUtils.isRequestSource(this, context)) {
            decode(context);
        }
        else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!ComponentUtils.isRequestSource(this, context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!ComponentUtils.isRequestSource(this, context)) {
            super.processUpdates(context);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (OpenEvent.NAME.equals(eventName)) {
                final OpenEvent openEvent = new OpenEvent(this, behaviorEvent.getBehavior());
                openEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(openEvent);

                return;
            }
            else if (CloseEvent.NAME.equals(eventName)) {
                final CloseEvent closeEvent = new CloseEvent(this, behaviorEvent.getBehavior());
                closeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(closeEvent);

                return;
            }
        }

        super.queueEvent(event);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    public void setModel(final org.primefaces.model.menu.MenuModel model) {
        getStateHelper().put(PropertyKeys.model, model);
    }

    @Override
    public String getTabindex() {
        return (String) getStateHelper().eval(PropertyKeys.tabindex, "0");
    }

    @Override
    public void setTabindex(final String tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, tabindex);
    }

    public String getIcon() {
        return (String) getStateHelper().eval(PropertyKeys.icon, "pi pi-plus");
    }

    public void setIcon(final String icon) {
        getStateHelper().put(PropertyKeys.icon, icon);
    }

    public String getIconActive() {
        return (String) getStateHelper().eval(PropertyKeys.iconActive, null);
    }

    public void setIconActive(final String iconActive) {
        getStateHelper().put(PropertyKeys.iconActive, iconActive);
    }

    public boolean isKeepOpen() {
        return (Boolean) getStateHelper().eval(PropertyKeys.keepOpen, false);
    }

    public void setKeepOpen(final boolean keepOpen) {
        getStateHelper().put(PropertyKeys.keepOpen, keepOpen);
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

}
