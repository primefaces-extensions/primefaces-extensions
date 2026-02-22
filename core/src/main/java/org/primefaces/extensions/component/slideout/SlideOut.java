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
package org.primefaces.extensions.component.slideout;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.util.Constants;

/**
 * <code>SlideOut</code> component.
 *
 * @author Melloware info@melloware.com
 * @since 6.1
 */
@FacesComponent(value = SlideOut.COMPONENT_TYPE, namespace = SlideOut.COMPONENT_FAMILY)
@FacesComponentInfo(description = "SlideOut creates a sliding panel with a handle.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "slideout/slideout.css")
@ResourceDependency(library = Constants.LIBRARY, name = "slideout/slideout.js")
public class SlideOut extends SlideOutBaseImpl {

    public static final String HANDLE_CLASS = "ui-slideout-handle ui-slideouttab-handle-rounded";

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
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.open)) {
                final OpenEvent openEvent = new OpenEvent(this, behaviorEvent.getBehavior());
                openEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(openEvent);
                return;
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.close)) {
                final CloseEvent closeEvent = new CloseEvent(this, behaviorEvent.getBehavior());
                closeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(closeEvent);
                return;
            }
        }

        super.queueEvent(event);
    }

}
