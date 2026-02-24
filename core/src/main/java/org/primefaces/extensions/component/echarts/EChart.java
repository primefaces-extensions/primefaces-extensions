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
package org.primefaces.extensions.component.echarts;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.BehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.EChartEvent;

@FacesComponent(value = EChart.COMPONENT_TYPE, namespace = EChart.COMPONENT_FAMILY)
@FacesComponentInfo(name = "echart", description = "Apache ECharts component using raw JSON model.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "echarts/echarts.js")
public class EChart extends EChartBaseImpl {

    @Override
    public void queueEvent(FacesEvent event) {
        if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.itemSelect)) {
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
