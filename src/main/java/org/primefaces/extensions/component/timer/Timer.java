/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.timer;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.api.AjaxSource;
import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * Timer component
 * 
 * @author f.strazzullo
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "timer/timer.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "timer/timer.js")
})
public class Timer extends UIComponentBase implements Widget, AjaxSource {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Timer";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String STYLE_CLASS = "ui-timer ui-widget ui-widget-header ui-corner-all";
    private static final int DEFAULT_TIMEOUT = 10;

    protected enum PropertyKeys {

        //@formatter:off
        widgetVar, 
        singleRun, 
        timeout, 
        update, 
        listener,
        immediate, 
        ontimercomplete, 
        ontimerstep, 
        onstart, 
        oncomplete, 
        process, 
        onerror, 
        onsuccess, 
        global, 
        delay, 
        async, 
        autoStart, 
        partialSubmit, 
        resetValues, 
        format, 
        style, 
        styleClass, 
        ignoreAutoUpdate, 
        visible, 
        forward, 
        formatFunction, 
        partialSubmitFilter, 
        form;
        //@formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        public String getToString() {
            return toString;
        }

        public void setToString(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    public Timer() {
        setRendererType(TimerRenderer.RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isSingleRun() {
        return Boolean.TRUE.equals(getStateHelper().eval(PropertyKeys.singleRun, true));
    }

    public void setSingleRun(boolean singleRun) {
        getStateHelper().put(PropertyKeys.singleRun, singleRun);
    }

    public int getTimeout() {
        return (Integer) getStateHelper().eval(PropertyKeys.timeout, DEFAULT_TIMEOUT);
    }

    public void setTimeout(int timeout) {
        getStateHelper().put(PropertyKeys.timeout, timeout);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public java.lang.String getUpdate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.update, null);
    }

    public void setUpdate(java.lang.String _update) {
        getStateHelper().put(PropertyKeys.update, _update);
    }

    public MethodExpression getListener() {
        return (MethodExpression) getStateHelper().eval(PropertyKeys.listener, null);
    }

    public void setListener(MethodExpression _listener) {
        getStateHelper().put(PropertyKeys.listener, _listener);
    }

    public boolean isImmediate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean _immediate) {
        getStateHelper().put(PropertyKeys.immediate, _immediate);
    }

    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public void setOnstart(java.lang.String _onstart) {
        getStateHelper().put(PropertyKeys.onstart, _onstart);
    }

    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    public void setOncomplete(java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
    }

    public java.lang.String getOntimercomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.ontimercomplete, null);
    }

    public void setOntimercomplete(java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.ontimercomplete, _oncomplete);
    }

    public java.lang.String getOntimerstep() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.ontimerstep, null);
    }

    public void setOntimerstep(java.lang.String onstep) {
        getStateHelper().put(PropertyKeys.ontimerstep, onstep);
    }

    public java.lang.String getProcess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
    }

    public void setProcess(java.lang.String _process) {
        getStateHelper().put(PropertyKeys.process, _process);
    }

    public java.lang.String getOnerror() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(java.lang.String _onerror) {
        getStateHelper().put(PropertyKeys.onerror, _onerror);
    }

    public java.lang.String getOnsuccess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    public void setOnsuccess(java.lang.String _onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, _onsuccess);
    }

    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public void setGlobal(boolean _global) {
        getStateHelper().put(PropertyKeys.global, _global);
    }

    public java.lang.String getDelay() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.delay, null);
    }

    public void setDelay(java.lang.String _delay) {
        getStateHelper().put(PropertyKeys.delay, _delay);
    }

    public boolean isAsync() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public void setAsync(boolean _async) {
        getStateHelper().put(PropertyKeys.async, _async);
    }

    public boolean isAutoStart() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoStart, true);
    }

    public void setAutoStart(boolean _autoStart) {
        getStateHelper().put(PropertyKeys.autoStart, _autoStart);
    }

    public boolean isPartialSubmit() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
    }

    public void setPartialSubmit(boolean _partialSubmit) {
        getStateHelper().put(PropertyKeys.partialSubmit, _partialSubmit);
    }

    public boolean isResetValues() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resetValues, false);
    }

    public void setResetValues(boolean _resetValues) {
        getStateHelper().put(PropertyKeys.resetValues, _resetValues);
    }

    public boolean isIgnoreAutoUpdate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ignoreAutoUpdate, false);
    }

    public void setIgnoreAutoUpdate(boolean _ignoreAutoUpdate) {
        getStateHelper().put(PropertyKeys.ignoreAutoUpdate, _ignoreAutoUpdate);
    }

    public boolean isPartialSubmitSet() {
        return getStateHelper().get(PropertyKeys.partialSubmit) != null || this.getValueExpression("partialSubmit") != null;
    }

    public boolean isResetValuesSet() {
        return getStateHelper().get(PropertyKeys.resetValues) != null || this.getValueExpression("resetValues") != null;
    }

    public boolean isAjaxified() {
        return true;
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, StringUtils.EMPTY);
    }

    public void setFormat(String format) {
        getStateHelper().put(PropertyKeys.format, format);
    }

    public String getFormat() {
        return (String) getStateHelper().eval(PropertyKeys.format, StringUtils.EMPTY);
    }

    public void setFormatFunction(String format) {
        getStateHelper().put(PropertyKeys.formatFunction, format);
    }

    public String getFormatFunction() {
        return (String) getStateHelper().eval(PropertyKeys.formatFunction, StringUtils.EMPTY);
    }

    public void setStyle(java.lang.String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, StringUtils.EMPTY);
    }

    public void setStyleClass(java.lang.String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public boolean isVisible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.visible, true);
    }

    public void setVisible(boolean _visible) {
        getStateHelper().put(PropertyKeys.visible, _visible);
    }

    public boolean isForward() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.forward, false);
    }

    public void setForward(boolean _forward) {
        getStateHelper().put(PropertyKeys.forward, _forward);
    }

    public java.lang.String getPartialSubmitFilter() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.partialSubmitFilter, null);
    }

    public void setPartialSubmitFilter(java.lang.String _partialSubmitFilter) {
        getStateHelper().put(PropertyKeys.partialSubmitFilter, _partialSubmitFilter);
    }

    public java.lang.String getForm() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.form, null);
    }

    public void setForm(java.lang.String _form) {
        getStateHelper().put(PropertyKeys.form, _form);
    }

    public void broadcast(javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
        super.broadcast(event); // backward compatibility

        FacesContext facesContext = getFacesContext();
        MethodExpression me = getListener();

        if (me != null) {
            me.invoke(facesContext.getELContext(), new Object[] {});
        }
    }

    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

}
