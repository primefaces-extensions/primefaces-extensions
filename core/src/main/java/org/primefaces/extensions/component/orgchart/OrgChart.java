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
package org.primefaces.extensions.component.orgchart;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.OrgChartClickEvent;
import org.primefaces.extensions.event.OrgChartDropEvent;
import org.primefaces.extensions.util.Constants;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by Melloware
 * @version $Revision$
 * @since 6.3
 */
@FacesComponent(value = OrgChart.COMPONENT_TYPE, namespace = OrgChart.COMPONENT_FAMILY)
@FacesComponentInfo(name = "orgchart", description = "OrgChart is a simple and direct organization chart component.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "orgchart/orgchart.js")
@ResourceDependency(library = Constants.LIBRARY, name = "orgchart/orgchart.css")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.css")
public class OrgChart extends OrgChartBaseImpl {

    public static final String STYLE_CLASS = "ui-orgchart ";

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isAjaxRequestSource(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            FacesContext context = event.getFacesContext();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String clientId = getClientId(context);
            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.click)) {
                final String id = params.get(clientId + "_nodeId");
                final String hierarchyStr = params.get(clientId + "_hierarchy");
                final OrgChartClickEvent orgChartClickEvent = new OrgChartClickEvent(this,
                            behaviorEvent.getBehavior(), id, hierarchyStr);
                orgChartClickEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(orgChartClickEvent);
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.drop)) {
                final String hierarchyStr = params.get(clientId + "_hierarchy");
                final String draggedNodeId = params.get(clientId + "_draggedNodeId");
                final String droppedZoneId = params.get(clientId + "_droppedZoneId");
                final OrgChartDropEvent orgChartDropEvent = new OrgChartDropEvent(this,
                            behaviorEvent.getBehavior(), hierarchyStr, draggedNodeId, droppedZoneId);
                orgChartDropEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(orgChartDropEvent);
            }
            else {
                super.queueEvent(event);
            }
        }
        else {
            super.queueEvent(event);
        }
    }
}
