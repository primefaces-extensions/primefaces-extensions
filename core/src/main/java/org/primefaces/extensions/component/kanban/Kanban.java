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
package org.primefaces.extensions.component.kanban;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.util.Constants;

/**
 * <code>Kanban</code> component.
 *
 * @author @jxmai / last modified by Melloware
 * @version $Revision$
 * @since 16.0.0
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "kanban/kanban.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "kanban/kanban.js")
public class Kanban extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String STYLE_CLASS = "ui-kanban ";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Kanban";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.kanban.KanbanRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(KanbanDragEvent.NAME));

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        widgetVar, //
        value, //
        style, //
        styleClass, //
        draggable, //
        addItemButton, //
        extender
    }

    public Kanban() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return KanbanDragEvent.NAME;
    }

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final String clientId = getClientId(fc);

            if (KanbanDragEvent.NAME.equals(eventName)) {
                final String itemId = params.get(clientId + "_itemId");
                final String sourceColumnId = params.get(clientId + "_sourceColumnId");
                final String targetColumnId = params.get(clientId + "_targetColumnId");
                final String positionStr = params.get(clientId + "_newPosition");
                final int newPosition = positionStr != null ? Integer.parseInt(positionStr) : 0;

                final KanbanDragEvent kanbanDragEvent = new KanbanDragEvent(this,
                            behaviorEvent.getBehavior(), itemId, sourceColumnId, targetColumnId, newPosition);
                kanbanDragEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(kanbanDragEvent);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    private boolean isSelfRequest(final FacesContext context) {
        return getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(final Object value) {
        getStateHelper().put(PropertyKeys.value, value);
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

    public Boolean getDraggable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggable, true);
    }

    public void setDraggable(final Boolean draggable) {
        getStateHelper().put(PropertyKeys.draggable, draggable);
    }

    public Boolean getAddItemButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.addItemButton, false);
    }

    public void setAddItemButton(final Boolean addItemButton) {
        getStateHelper().put(PropertyKeys.addItemButton, addItemButton);
    }

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(final String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
    }
}
