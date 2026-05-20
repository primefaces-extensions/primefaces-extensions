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

import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.extensions.event.KanbanAddEvent;
import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.event.KanbanItemClickEvent;
import org.primefaces.util.Constants;

/**
 * <code>Kanban</code> component.
 *
 * @author jxmai
 * @since 15.0.16
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "kanban/kanban.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "kanban/kanban.js")
public class Kanban extends KanbanBase implements ClientBehaviorHolder {

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(KanbanDragEvent.NAME, KanbanAddEvent.NAME, KanbanItemClickEvent.NAME));

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
                final int newPosition = parsePosition(positionStr);

                final KanbanDragEvent kanbanDragEvent = new KanbanDragEvent(this,
                            behaviorEvent.getBehavior(), itemId, sourceColumnId, targetColumnId, newPosition);
                kanbanDragEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(kanbanDragEvent);
                return;
            }

            if (KanbanAddEvent.NAME.equals(eventName)) {
                final String columnId = params.get(clientId + "_columnId");
                final KanbanAddEvent addEvent = new KanbanAddEvent(this,
                            behaviorEvent.getBehavior(), columnId);
                addEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(addEvent);
                return;
            }

            if (KanbanItemClickEvent.NAME.equals(eventName)) {
                final String itemId = params.get(clientId + "_itemId");
                final String columnId = params.get(clientId + "_columnId");
                final KanbanItemClickEvent clickEvent = new KanbanItemClickEvent(this,
                            behaviorEvent.getBehavior(), itemId, columnId);
                clickEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(clickEvent);
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

    private int parsePosition(final String positionStr) {
        if (positionStr != null) {
            try {
                return Integer.parseInt(positionStr);
            }
            catch (final NumberFormatException e) {
                // fallback to default position 0 on malformed input
            }
        }
        return 0;
    }
}
