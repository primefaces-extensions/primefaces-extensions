/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
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
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "echarts/echarts.js")
@ResourceDependency(library = "primefaces-extensions", name = "speedtest/speedtest.js")
public class Speedtest extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String COMPONENT_TYPE = COMPONENT_FAMILY + ".Speedtest";
    private static final String DEFAULT_RENDERER = COMPONENT_FAMILY + ".SpeedtestRenderer";
    private static final Collection<String> EVENT_NAMES = List.of(SpeedTestEvent.NAME);

    @SuppressWarnings("java:S115")
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
        styleClass
        // @formatter:on
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

    private static Double convertParam(final String clientId, final String paramName, final Map<String, String> params) {
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
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = getClientId(fc);
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
        return getClientId(context)
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