/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.speedtest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.SpeedTestEvent;
import org.primefaces.util.Constants;

/**
 * <code>Speedtest</code> component.
 *
 * @author ssibitz ssibitz@me.com
 * @since 6.2
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces", name = "raphael/raphael.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "speedtest/speedtest.js")
})
public class Speedtest extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String COMPONENT_TYPE = COMPONENT_FAMILY + ".Speedtest";
    private static final String DEFAULT_RENDERER = COMPONENT_FAMILY + ".SpeedtestRenderer";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(SpeedTestEvent.NAME));

    protected enum PropertyKeys {
        // @formatter:off
        widgetVar,
        captionPing,
        captionJitter,
        captionDownload,
        captionUpload,
        colorPing,
        colorJitter,
        colorDownload,
        colorUpload,
        file,
        style,
        styleClass;
        // @formatter:on
        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    /**
     * Default constructor
     */
    public Speedtest() {
        setRendererType(DEFAULT_RENDERER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultEventName() {
        return SpeedTestEvent.NAME;
    }

    private Double convertParam(final String clientId, final String paramName, final Map<String, String> params) {
        Double res = 0.0;
        try {
            res = Double.valueOf(params.get(clientId + paramName));
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
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(fc);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            if (SpeedTestEvent.NAME.equals(eventName)) {
                // Get parameters:
                final Double pingTimeMS = convertParam(clientId, "_PingTimeMS", params);
                final Double jitterTimeMS = convertParam(clientId, "_JitterTimeMS", params);
                final Double speedMbpsDownload = convertParam(clientId, "_SpeedMbpsDownload", params);
                final Double speedMbpsUpload = convertParam(clientId, "_SpeedMbpsUpload", params);
                final SpeedTestEvent speedtestEvent = new SpeedTestEvent(this, behaviorEvent.getBehavior(),
                            pingTimeMS, jitterTimeMS, speedMbpsDownload, speedMbpsUpload);
                speedtestEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(speedtestEvent);
                return;
            }
        }
        super.queueEvent(event);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public String getCaptionPing() {
        return (String) getStateHelper().eval(PropertyKeys.captionPing, "Ping");
    }

    public void setCaptionPing(final String _captionPing) {
        getStateHelper().put(PropertyKeys.captionPing, _captionPing);
    }

    public String getCaptionJitter() {
        return (String) getStateHelper().eval(PropertyKeys.captionJitter, "Jitter");
    }

    public void setCaptionJitter(final String _captionJitter) {
        getStateHelper().put(PropertyKeys.captionJitter, _captionJitter);
    }

    public String getCaptionDownload() {
        return (String) getStateHelper().eval(PropertyKeys.captionDownload, "Download");
    }

    public void setCaptionDownload(final String _captionDownload) {
        getStateHelper().put(PropertyKeys.captionDownload, _captionDownload);
    }

    public String getCaptionUpload() {
        return (String) getStateHelper().eval(PropertyKeys.captionUpload, "Upload");
    }

    public void setCaptionUpload(final String _captionUpload) {
        getStateHelper().put(PropertyKeys.captionUpload, _captionUpload);
    }

    public String getColorPing() {
        return (String) getStateHelper().eval(PropertyKeys.colorPing, "#993333");
    }

    public void setColorPing(final String _colorPing) {
        getStateHelper().put(PropertyKeys.colorPing, _colorPing);
    }

    public String getColorJitter() {
        return (String) getStateHelper().eval(PropertyKeys.colorJitter, "#d2900a");
    }

    public void setColorJitter(final String _colorJitter) {
        getStateHelper().put(PropertyKeys.colorJitter, _colorJitter);
    }

    public String getColorDownload() {
        return (String) getStateHelper().eval(PropertyKeys.colorDownload, "#339933");
    }

    public void setColorDownload(final String _colorDownload) {
        getStateHelper().put(PropertyKeys.colorDownload, _colorDownload);
    }

    public String getColorUpload() {
        return (String) getStateHelper().eval(PropertyKeys.colorUpload, "#333399");
    }

    public void setColorUpload(final String _colorUpload) {
        getStateHelper().put(PropertyKeys.colorUpload, _colorUpload);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
    }

    public String getFile() {
        return (String) getStateHelper().eval(PropertyKeys.file, null);
    }

    public void setFile(final String _File) {
        getStateHelper().put(PropertyKeys.file, _File);
    }
}
