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
package org.primefaces.extensions.component.imagerotateandresize;

import java.util.Map;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.extensions.util.Constants;

/**
 * Component class for the <code>ImageRotateAndResize</code> component.
 *
 * @author Thomas Andraschko
 * @since 0.1
 */
@FacesComponent(value = ImageRotateAndResize.COMPONENT_TYPE, namespace = ImageRotateAndResize.COMPONENT_FAMILY)
@FacesComponentInfo(description = "ImageRotateAndResize allows rotating and resizing an image.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "imagerotateandresize/imagerotateandresize.js")
public class ImageRotateAndResize extends ImageRotateAndResizeBaseImpl {

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String clientId = getClientId(context);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.resize)) {
                final double width = Double.parseDouble(params.get(clientId + "_width"));
                final double height = Double.parseDouble(params.get(clientId + "_height"));
                super.queueEvent(new ResizeEvent(this, behaviorEvent.getBehavior(), width, height));
                return;
            }
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.rotate)) {
                final int degree = Integer.parseInt(params.get(clientId + "_degree"));
                super.queueEvent(new RotateEvent(this, behaviorEvent.getBehavior(), degree));
                return;
            }
        }
        super.queueEvent(event);
    }
}
