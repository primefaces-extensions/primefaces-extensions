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
package org.primefaces.extensions.component.timer;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.AjaxSource;
import org.primefaces.component.api.Widget;
import org.primefaces.util.Constants;

/**
 * Timer component
 *
 * @author f.strazzullo
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "moment/moment.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "timer/timer.css")
@ResourceDependency(library = "primefaces-extensions", name = "timer/timer.js")
public class Timer extends UIComponentBase implements Widget, AjaxSource {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Timer";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String STYLE_CLASS = "ui-timer ui-widget ui-widget-header ui-corner-all";
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_INTERVAL_MS = 1000;

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off
        widgetVar,
        singleRun,
        timeout,
        interval,
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
        form,
        title,
        ignoreComponentNotFound
        //@formatter:on

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

    public void setSingleRun(final boolean singleRun) {
        getStateHelper().put(PropertyKeys.singleRun, singleRun);
    }

    @Override
    public int getTimeout() {
        return (Integer) getStateHelper().eval(PropertyKeys.timeout, DEFAULT_TIMEOUT);
    }

    public void setTimeout(final int timeout) {
        getStateHelper().put(PropertyKeys.timeout, timeout);
    }

    public int getInterval() {
        return (Integer) getStateHelper().eval(PropertyKeys.interval, DEFAULT_INTERVAL_MS);
    }

    public void setInterval(final int interval) {
        getStateHelper().put(PropertyKeys.interval, interval);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final java.lang.String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    @Override
    public java.lang.String getUpdate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.update, null);
    }

    public void setUpdate(final java.lang.String update) {
        getStateHelper().put(PropertyKeys.update, update);
    }

    public MethodExpression getListener() {
        return (MethodExpression) getStateHelper().eval(PropertyKeys.listener, null);
    }

    public void setListener(final MethodExpression listener) {
        getStateHelper().put(PropertyKeys.listener, listener);
    }

    public boolean isImmediate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(final boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    @Override
    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public void setOnstart(final java.lang.String onstart) {
        getStateHelper().put(PropertyKeys.onstart, onstart);
    }

    @Override
    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    public void setOncomplete(final java.lang.String oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, oncomplete);
    }

    public java.lang.String getOntimercomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.ontimercomplete, null);
    }

    public void setOntimercomplete(final java.lang.String oncomplete) {
        getStateHelper().put(PropertyKeys.ontimercomplete, oncomplete);
    }

    public java.lang.String getOntimerstep() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.ontimerstep, null);
    }

    public void setOntimerstep(final java.lang.String onstep) {
        getStateHelper().put(PropertyKeys.ontimerstep, onstep);
    }

    @Override
    public java.lang.String getProcess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
    }

    public void setProcess(final java.lang.String process) {
        getStateHelper().put(PropertyKeys.process, process);
    }

    @Override
    public java.lang.String getOnerror() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(final java.lang.String onerror) {
        getStateHelper().put(PropertyKeys.onerror, onerror);
    }

    @Override
    public java.lang.String getOnsuccess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    public void setOnsuccess(final java.lang.String onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, onsuccess);
    }

    @Override
    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public void setGlobal(final boolean global) {
        getStateHelper().put(PropertyKeys.global, global);
    }

    @Override
    public java.lang.String getDelay() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.delay, null);
    }

    public void setDelay(final java.lang.String delay) {
        getStateHelper().put(PropertyKeys.delay, delay);
    }

    @Override
    public boolean isAsync() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public void setAsync(final boolean async) {
        getStateHelper().put(PropertyKeys.async, async);
    }

    public boolean isAutoStart() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoStart, true);
    }

    public void setAutoStart(final boolean autoStart) {
        getStateHelper().put(PropertyKeys.autoStart, autoStart);
    }

    @Override
    public boolean isPartialSubmit() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
    }

    public void setPartialSubmit(final boolean partialSubmit) {
        getStateHelper().put(PropertyKeys.partialSubmit, partialSubmit);
    }

    @Override
    public boolean isResetValues() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resetValues, false);
    }

    public void setResetValues(final boolean resetValues) {
        getStateHelper().put(PropertyKeys.resetValues, resetValues);
    }

    @Override
    public boolean isIgnoreAutoUpdate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ignoreAutoUpdate, false);
    }

    public void setIgnoreAutoUpdate(final boolean ignoreAutoUpdate) {
        getStateHelper().put(PropertyKeys.ignoreAutoUpdate, ignoreAutoUpdate);
    }

    @Override
    public boolean isPartialSubmitSet() {
        return getStateHelper().get(PropertyKeys.partialSubmit) != null || getValueExpression("partialSubmit") != null;
    }

    @Override
    public boolean isResetValuesSet() {
        return getStateHelper().get(PropertyKeys.resetValues) != null || getValueExpression("resetValues") != null;
    }

    @Override
    public boolean isAjaxified() {
        return true;
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, Constants.EMPTY_STRING);
    }

    public void setFormat(final String format) {
        getStateHelper().put(PropertyKeys.format, format);
    }

    public String getFormat() {
        return (String) getStateHelper().eval(PropertyKeys.format, Constants.EMPTY_STRING);
    }

    public void setFormatFunction(final String format) {
        getStateHelper().put(PropertyKeys.formatFunction, format);
    }

    public String getFormatFunction() {
        return (String) getStateHelper().eval(PropertyKeys.formatFunction, Constants.EMPTY_STRING);
    }

    public void setStyle(final java.lang.String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, Constants.EMPTY_STRING);
    }

    public void setStyleClass(final java.lang.String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public java.lang.String getTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.title, Constants.EMPTY_STRING);
    }

    public void setTitle(final java.lang.String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    public boolean isVisible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.visible, true);
    }

    public void setVisible(final boolean visible) {
        getStateHelper().put(PropertyKeys.visible, visible);
    }

    public boolean isForward() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.forward, false);
    }

    public void setForward(final boolean forward) {
        getStateHelper().put(PropertyKeys.forward, forward);
    }

    @Override
    public java.lang.String getPartialSubmitFilter() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.partialSubmitFilter, null);
    }

    public void setPartialSubmitFilter(final java.lang.String partialSubmitFilter) {
        getStateHelper().put(PropertyKeys.partialSubmitFilter, partialSubmitFilter);
    }

    @Override
    public java.lang.String getForm() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.form, null);
    }

    public void setForm(final java.lang.String form) {
        getStateHelper().put(PropertyKeys.form, form);
    }

    @Override
    public void broadcast(final javax.faces.event.FacesEvent event) {
        super.broadcast(event); // backward compatibility

        final FacesContext facesContext = getFacesContext();
        final MethodExpression me = getListener();

        if (me != null) {
            me.invoke(facesContext.getELContext(), new Object[] {});
        }
    }

    @Override
    public boolean isIgnoreComponentNotFound() {
        return (Boolean) getStateHelper().eval(PropertyKeys.ignoreComponentNotFound, false);
    }

    public void setIgnoreComponentNotFound(final boolean ignoreComponentNotFound) {
        getStateHelper().put(PropertyKeys.ignoreComponentNotFound, ignoreComponentNotFound);
    }
}
