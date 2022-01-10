/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.clipboard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ClipboardErrorEvent;
import org.primefaces.extensions.event.ClipboardSuccessEvent;
import org.primefaces.util.Constants;

/**
 * <code>Clipboard</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "clipboard/clipboard.js")
public class Clipboard extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Clipboard";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ClipboardRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(ClipboardSuccessEvent.NAME, ClipboardErrorEvent.NAME));

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        // @formatter:off
        widgetVar,
        action,
        trigger,
        target,
        text,
        onsuccess,
        onerror
        // @formatter:on
    }

    /**
     * Default constructor
     */
    public Clipboard() {
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
        return ClipboardSuccessEvent.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }
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
            final String action = params.get(clientId + "_action");
            final String trigger = params.get(clientId + "_trigger");

            if (ClipboardSuccessEvent.NAME.equals(eventName)) {
                final String text = params.get(clientId + "_text");
                final ClipboardSuccessEvent successEvent = new ClipboardSuccessEvent(this, behaviorEvent.getBehavior(),
                            action, text, trigger);
                successEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(successEvent);

                return;
            }
            else if (ClipboardErrorEvent.NAME.equals(eventName)) {
                final ClipboardErrorEvent errorEvent = new ClipboardErrorEvent(this, behaviorEvent.getBehavior(), action,
                            trigger);
                errorEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(errorEvent);

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

    public String getAction() {
        return (String) getStateHelper().eval(PropertyKeys.action, "copy");
    }

    public void setAction(final String _action) {
        getStateHelper().put(PropertyKeys.action, _action);
    }

    public String getTrigger() {
        return (String) getStateHelper().eval(PropertyKeys.trigger, null);
    }

    public void setTrigger(final String _trigger) {
        getStateHelper().put(PropertyKeys.trigger, _trigger);
    }

    public String getTarget() {
        return (String) getStateHelper().eval(PropertyKeys.target, null);
    }

    public void setTarget(final String _target) {
        getStateHelper().put(PropertyKeys.target, _target);
    }

    public String getText() {
        return (String) getStateHelper().eval(PropertyKeys.text, "PrimeFaces Rocks!");
    }

    public void setText(final String _text) {
        getStateHelper().put(PropertyKeys.text, _text);
    }

    public String getOnsuccess() {
        return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    public void setOnsuccess(final String _onSuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, _onSuccess);
    }

    public String getOnerror() {
        return (String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    public void setOnerror(final String _onError) {
        getStateHelper().put(PropertyKeys.onerror, _onError);
    }
}
