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
package org.primefaces.extensions.component.orgchart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIData;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.OrgChartClickEvent;
import org.primefaces.extensions.event.OrgChartDropEvent;
import org.primefaces.util.Constants;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by Melloware
 * @version $Revision$
 * @since 6.3
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "orgchart/orgchart.js")
@ResourceDependency(library = "primefaces-extensions", name = "orgchart/orgchart.css")
public class OrgChart extends UIData implements Widget, ClientBehaviorHolder {

    public static final String STYLE_CLASS = "ui-orgchart ";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.OrgChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.orgchart.OrgChartRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(OrgChartClickEvent.NAME, OrgChartDropEvent.NAME));

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        nodeId, //
        widgetVar, //
        nodeContent, //
        direction, //
        pan, //
        toggleSiblingsResp, //
        depth, //
        exportButton, //
        exportFilename, //
        exportFileextension, //
        parentNodeSymbol, //
        draggable, //
        chartClass, //
        zoom, //
        zoominLimit, //
        zoomoutLimit, //
        verticalDepth, //
        nodeTitle, //
        style, //
        styleClass, //
        extender
    }

    public OrgChart() {
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
        return OrgChartClickEvent.NAME;
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

            if (OrgChartClickEvent.NAME.equals(eventName)) {

                final String id = params.get(clientId + "_nodeId");

                final String hierarchyStr = params.get(clientId + "_hierarchy");

                final OrgChartClickEvent orgChartClickEvent = new OrgChartClickEvent(this,
                            behaviorEvent.getBehavior(), id, hierarchyStr);
                orgChartClickEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(orgChartClickEvent);
            }
            else if (OrgChartDropEvent.NAME.equals(eventName)) {
                final String hierarchyStr = params.get(clientId + "_hierarchy");

                final String draggedNodeId = params.get(clientId + "_draggedNodeId");

                final String droppedZoneId = params.get(clientId + "_droppedZoneId");

                final OrgChartDropEvent orgChartDropEvent = new OrgChartDropEvent(this,
                            behaviorEvent.getBehavior(), hierarchyStr, draggedNodeId, droppedZoneId);
                orgChartDropEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(orgChartDropEvent);
            }
        }
    }

    private boolean isSelfRequest(final FacesContext context) {
        return getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    public String getNodeId() {
        return (String) getStateHelper().eval(PropertyKeys.nodeId, "id");
    }

    public void setNodeId(final String nodeId) {
        getStateHelper().put(PropertyKeys.nodeId, nodeId);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getNodeContent() {
        return (String) getStateHelper().eval(PropertyKeys.nodeContent, "title");
    }

    public void setNodeContent(final String nodeContent) {
        getStateHelper().put(PropertyKeys.nodeContent, nodeContent);
    }

    public String getDirection() {
        return (String) getStateHelper().eval(PropertyKeys.direction, "t2b");
    }

    public void setDirection(final String direction) {
        getStateHelper().put(PropertyKeys.direction, direction);
    }

    public Boolean getPan() {
        return (Boolean) getStateHelper().eval(PropertyKeys.pan, false);
    }

    public void setPan(final Boolean pan) {
        getStateHelper().put(PropertyKeys.pan, pan);
    }

    public Boolean getToggleSiblingsResp() {
        return (Boolean) getStateHelper().eval(PropertyKeys.toggleSiblingsResp, false);
    }

    public void setToggleSiblingsResp(final Boolean toggleSiblingsResp) {
        getStateHelper().put(PropertyKeys.toggleSiblingsResp, toggleSiblingsResp);
    }

    public Integer getDepth() {
        return (Integer) getStateHelper().eval(PropertyKeys.depth, 999);
    }

    public void setDepth(final Integer depth) {
        getStateHelper().put(PropertyKeys.depth, depth);
    }

    public Boolean getExportButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.exportButton, false);
    }

    public void setExportButton(final Boolean exportButton) {
        getStateHelper().put(PropertyKeys.exportButton, exportButton);
    }

    public String getExportFilename() {
        return (String) getStateHelper().eval(PropertyKeys.exportFilename, "OrgChart");
    }

    public void setExportFilename(final String exportFilename) {
        getStateHelper().put(PropertyKeys.exportFilename, exportFilename);
    }

    public String getExportFileextension() {
        return (String) getStateHelper().eval(PropertyKeys.exportFileextension, "png");
    }

    public void setExportFileextension(final String exportFileextension) {
        getStateHelper().put(PropertyKeys.exportFileextension, exportFileextension);
    }

    public String getParentNodeSymbol() {
        return (String) getStateHelper().eval(PropertyKeys.parentNodeSymbol, "fa-users");
    }

    public void setParentNodeSymbol(final String parentNodeSymbol) {
        getStateHelper().put(PropertyKeys.parentNodeSymbol, parentNodeSymbol);
    }

    public Boolean getDraggable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggable, false);
    }

    public void setDraggable(final Boolean draggable) {
        getStateHelper().put(PropertyKeys.draggable, draggable);
    }

    public String getChartClass() {
        return (String) getStateHelper().eval(PropertyKeys.chartClass, "");
    }

    public void setChartClass(final String chartClass) {
        getStateHelper().put(PropertyKeys.chartClass, chartClass);
    }

    public Boolean getZoom() {
        return (Boolean) getStateHelper().eval(PropertyKeys.zoom, false);
    }

    public void setZoom(final Boolean zoom) {
        getStateHelper().put(PropertyKeys.zoom, zoom);
    }

    public Number getZoominLimit() {
        return (Number) getStateHelper().eval(PropertyKeys.zoominLimit, 7);
    }

    public void setZoominLimit(final Number zoominLimit) {
        getStateHelper().put(PropertyKeys.zoominLimit, zoominLimit);
    }

    public Number getZoomoutLimit() {
        return (Number) getStateHelper().eval(PropertyKeys.zoomoutLimit, 0.5);
    }

    public void setZoomoutLimit(final Number zoomoutLimit) {
        getStateHelper().put(PropertyKeys.zoomoutLimit, zoomoutLimit);
    }

    public Integer getVerticalDepth() {
        return (Integer) getStateHelper().eval(PropertyKeys.verticalDepth, null);
    }

    public void setVerticalDepth(final Integer verticalDepth) {
        getStateHelper().put(PropertyKeys.verticalDepth, verticalDepth);
    }

    public String getNodeTitle() {
        return (String) getStateHelper().eval(PropertyKeys.nodeTitle, "name");
    }

    public void setNodeTitle(final String nodeTitle) {
        getStateHelper().put(PropertyKeys.nodeTitle, nodeTitle);
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

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(final String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
    }

}
