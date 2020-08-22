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
package org.primefaces.extensions.component.codescanner;

import java.util.Collection;
import java.util.Map;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.api.MixedClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.codescanner.CodeScanned;
import org.primefaces.extensions.model.codescanner.CodeScanned.Format;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>CodeScanner</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0.5
 */
public class CodeScanner extends UIComponentBase implements Widget, MixedClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CodeScanner";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CodeScannerRenderer";

    public static final String STYLE_CLASS = "ui-codescanner ui-widget";
    public static final String EVENT_CODE_SCANNED = "codeScanned";

    private static final Collection<String> EVENT_NAMES = LangUtils.unmodifiableList(EVENT_CODE_SCANNED);

    // @formatter:off
    public enum PropertyKeys {
        widgetVar,
        type,
        style,
        styleClass
    }
    // @formatter:on

    public CodeScanner() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, "TODO");
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

    @Override
    public Collection<String> getUnobstrusiveEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (EVENT_CODE_SCANNED.equals(eventName)) {
                final CodeScanned codeScanned = getCodeScanned(getClientId(context), params);
                final SelectEvent<CodeScanned> selectEvent = new SelectEvent<>(this, ajaxBehaviorEvent.getBehavior(), codeScanned);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            }
            else {
                // e.g. blur, focus, change
                super.queueEvent(event);
            }
        }
        else {
            // e.g. valueChange
            super.queueEvent(event);
        }
    }

    protected CodeScanned getCodeScanned(final String clientId, final Map<String, String> params) {
        return new CodeScanned(params.get(clientId + "_value"),
                    Format.values()[Integer.valueOf(params.get(clientId + "_format"))]);
    }

}
