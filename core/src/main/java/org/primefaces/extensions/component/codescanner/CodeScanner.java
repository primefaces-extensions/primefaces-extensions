/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.component.codescanner;

import java.util.Collection;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.MixedClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.codescanner.Code;
import org.primefaces.extensions.model.codescanner.Code.Format;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>CodeScanner</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "codescanner/codescanner.js")
public class CodeScanner extends UIComponentBase implements Widget, ClientBehaviorHolder, MixedClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CodeScanner";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CodeScannerRenderer";

    public static final String STYLE_CLASS = "ui-codescanner ui-widget";
    public static final String EVENT_CODE_SCANNED = "codeScanned";

    private static final Collection<String> EVENT_NAMES = LangUtils.unmodifiableList(EVENT_CODE_SCANNED);

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        widgetVar,
        type,
        style,
        styleClass,
        width,
        height,
        autoStart,
        onsuccess,
        onerror,
        video,
        deviceId,
        forVal("for");

        private final String toString;
        PropertyKeys(String toString) {
            this.toString = toString;
        }
        PropertyKeys() {
            toString = null;
        }
        @Override public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    @SuppressWarnings("java:S115")
    public enum ReaderType {
        multi,
        bar,
        qr
    }
    // @formatter:on

    public CodeScanner() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getDefaultEventName() {
        return EVENT_CODE_SCANNED;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, ReaderType.multi.name());
    }

    public ReaderType getTypeEnum() {
        return ReaderType.valueOf(getType());
    }

    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public Integer getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, null);
    }

    public void setWidth(Integer width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public Integer getHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.height, null);
    }

    public void setHeight(Integer height) {
        getStateHelper().put(PropertyKeys.height, height);
    }

    public boolean isAutoStart() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoStart, true);
    }

    public void setAutoStart(boolean autoStart) {
        getStateHelper().put(PropertyKeys.autoStart, autoStart);
    }

    public String getOnsuccess() {
        return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    public void setOnsuccess(String onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, onsuccess);
    }

    public String getOnerror() {
        return (String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(String onerror) {
        getStateHelper().put(PropertyKeys.onerror, onerror);
    }

    public boolean isVideo() {
        return (Boolean) getStateHelper().eval(PropertyKeys.video, true);
    }

    public void setVideo(boolean video) {
        getStateHelper().put(PropertyKeys.video, video);
    }

    public String getDeviceId() {
        return (String) getStateHelper().eval(PropertyKeys.deviceId, null);
    }

    public void setDeviceId(String deviceId) {
        getStateHelper().put(PropertyKeys.deviceId, deviceId);
    }

    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forVal, null);
    }

    public void setFor(String aFor) {
        getStateHelper().put(PropertyKeys.forVal, aFor);
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public Collection<String> getUnobstrusiveEventNames() {
        return getEventNames();
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (EVENT_CODE_SCANNED.equals(eventName)) {
                final Code code = getCode(getClientId(context), params);
                final SelectEvent<Code> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), code);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            }
        }
    }

    protected static Code getCode(final String clientId, final Map<String, String> params) {
        return new Code(params.get(clientId + "_value"),
                    Format.values()[Integer.parseInt(params.get(clientId + "_format"))]);
    }

}
