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
package org.primefaces.extensions.component.speedtest;

import java.util.Map;

import jakarta.faces.FacesException;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.SpeedTestEvent;

/**
 * <code>Speedtest</code> component.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.2
 */
@FacesComponent(value = Speedtest.COMPONENT_TYPE, namespace = Speedtest.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Speedtest allows to measure the network speed.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "echarts/echarts.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "speedtest/speedtest.js")
public class Speedtest extends SpeedtestBaseImpl {

    private static Double convertParam(final String clientId, final String paramName,
                final Map<String, String> params) {
        double res;
        try {
            res = Double.parseDouble(params.get(clientId + paramName));
        }
        catch (final Exception e) {
            throw new FacesException("Speedtest: can not convert result value for '" + paramName + "'");
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String clientId = getClientId(context);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.speedtest)) {
                // Get parameters:
                final Double pingTimeMS = convertParam(clientId, "_PingTimeMS", params);
                final Double jitterTimeMS = convertParam(clientId, "_JitterTimeMS", params);
                final Double speedMbpsDownload = convertParam(clientId, "_SpeedMbpsDownload", params);
                final Double speedMbpsUpload = convertParam(clientId, "_SpeedMbpsUpload", params);
                final SpeedTestEvent speedtestEvent = new SpeedTestEvent(this, behaviorEvent.getBehavior(),
                            pingTimeMS, jitterTimeMS, speedMbpsDownload, speedMbpsUpload);
                speedtestEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(speedtestEvent);
                return;
            }
            else {
                super.queueEvent(event);
                return;
            }
        }
        super.queueEvent(event);
    }
}
