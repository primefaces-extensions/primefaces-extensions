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
package org.primefaces.extensions.component.echarts;

import java.util.Collection;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.extensions.event.EChartEvent;
import org.primefaces.util.Constants;
import org.primefaces.util.MapBuilder;

@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = Constants.LIBRARY, name = "echarts/echarts.js")
public class EChart extends EChartBase {
    private static final String DEFAULT_EVENT = "itemSelect";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>> builder()
                .put("itemSelect", ItemSelectEvent.class)
                .build();

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
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
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }
    }

    private boolean isSelfRequest(FacesContext facesContext) {
        String clientId = getClientId(facesContext);
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
        String partialSourceParam = requestParameterMap.get(Constants.RequestParams.PARTIAL_SOURCE_PARAM);
        return clientId.equals(partialSourceParam);
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if (isSelfRequest(event.getFacesContext()) && event instanceof AjaxBehaviorEvent) {
            BehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            Map<String, String> map = getFacesContext().getExternalContext().getRequestParameterMap();
            String name = map.get("name");
            String componentType = map.get("componentType");
            String componentSubType = map.get("componentSubType");
            String seriesType = map.get("seriesType");
            String seriesName = map.get("seriesName");
            String data = map.get("value");
            int componentIndex = Integer.parseInt(map.get("componentIndex"));
            int dataIndex = Integer.parseInt(map.get("dataIndex"));
            int seriesIndex = Integer.parseInt(map.get("seriesIndex"));

            EChartEvent eChartEvent = new EChartEvent(this, behaviorEvent.getBehavior(), name, dataIndex, seriesIndex, componentType, componentSubType,
                        seriesType, seriesName, data, componentIndex);

            super.queueEvent(eChartEvent);
        }
        else {
            super.queueEvent(event);
        }
    }
}