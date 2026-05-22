/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.KanbanAddEvent;
import org.primefaces.extensions.event.KanbanBoardDragEvent;
import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.event.KanbanItemClickEvent;
import org.primefaces.extensions.util.Constants;

/**
 * <code>Kanban</code> component.
 *
 * @author jxmai
 * @since 16.0.0
 */
@FacesComponent(value = Kanban.COMPONENT_TYPE, namespace = Kanban.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Kanban board component for visualizing workflow and managing tasks with drag-and-drop functionality.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "kanban/kanban.css")
@ResourceDependency(library = Constants.LIBRARY, name = "kanban/kanban.js")
public class Kanban extends KanbanBaseImpl {

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final String clientId = getClientId(context);

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.drop)) {
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

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.itemAdd)) {
                final String columnId = params.get(clientId + "_columnId");
                final KanbanAddEvent addEvent = new KanbanAddEvent(this,
                            behaviorEvent.getBehavior(), columnId);
                addEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(addEvent);
                return;
            }

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.itemClick)) {
                final String itemId = params.get(clientId + "_itemId");
                final String columnId = params.get(clientId + "_columnId");
                final KanbanItemClickEvent clickEvent = new KanbanItemClickEvent(this,
                            behaviorEvent.getBehavior(), itemId, columnId);
                clickEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(clickEvent);
                return;
            }

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.dragBoard)) {
                final String boardId = params.get(clientId + "_boardId");
                final KanbanBoardDragEvent dragBoardEvent = new KanbanBoardDragEvent(this,
                            behaviorEvent.getBehavior(), boardId);
                dragBoardEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(dragBoardEvent);
                return;
            }

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.dragendBoard)) {
                final String boardId = params.get(clientId + "_boardId");
                final String positionStr = params.get(clientId + "_newPosition");
                final int newPosition = parsePosition(positionStr);
                final KanbanBoardDragEvent dragendBoardEvent = new KanbanBoardDragEvent(this,
                            behaviorEvent.getBehavior(), boardId, newPosition);
                dragendBoardEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(dragendBoardEvent);
                return;
            }
        }
        super.queueEvent(event);
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
