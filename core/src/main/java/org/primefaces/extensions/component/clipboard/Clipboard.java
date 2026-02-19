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
package org.primefaces.extensions.component.clipboard;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.ClipboardErrorEvent;
import org.primefaces.extensions.event.ClipboardSuccessEvent;

/**
 * <code>Clipboard</code> component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@FacesComponent(value = Clipboard.COMPONENT_TYPE, namespace = Clipboard.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Clipboard provides copy/cut to clipboard functionality for text or elements.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "clipboard/clipboard.js")
public class Clipboard extends ClipboardBaseImpl {

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isAjaxRequestSource(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void processValidators(final FacesContext fc) {
        if (!isAjaxRequestSource(fc)) {
            super.processValidators(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isAjaxRequestSource(fc)) {
            super.processUpdates(fc);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext fc = event.getFacesContext();
            final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            final String clientId = getClientId(fc);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final String action = params.get(clientId + "_action");
            final String trigger = params.get(clientId + "_trigger");

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.success)) {
                final String text = params.get(clientId + "_text");
                final ClipboardSuccessEvent successEvent = new ClipboardSuccessEvent(this, behaviorEvent.getBehavior(),
                            action, text, trigger);
                successEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(successEvent);
                return;
            }
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.error)) {
                final ClipboardErrorEvent errorEvent = new ClipboardErrorEvent(this, behaviorEvent.getBehavior(),
                            action, trigger);
                errorEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(errorEvent);
                return;
            }
        }
        super.queueEvent(event);
    }
}
