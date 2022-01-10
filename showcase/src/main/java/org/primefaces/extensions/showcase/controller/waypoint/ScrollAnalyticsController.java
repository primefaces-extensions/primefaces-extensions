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
package org.primefaces.extensions.showcase.controller.waypoint;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.WaypointEvent;

/**
 * WaypointController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ScrollAnalyticsController implements Serializable {

    private static final long serialVersionUID = 20120816L;

    public void adInView(final WaypointEvent e) {
        final UIComponent container = e.getComponent().findComponent("container");

        container.invokeOnComponent(FacesContext.getCurrentInstance(), e.getWaypointId(), new ContextCallback() {

            @Override
            public void invokeContextCallback(final FacesContext fc, final UIComponent component) {
                final HtmlPanelGroup panelGroup = (HtmlPanelGroup) component;
                final String analyticsId = panelGroup != null ? (String) panelGroup.getAttributes().get("analyticsid")
                            : "";

                final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Ad with ID: " + analyticsId + " was read", null);
                fc.addMessage(null, msg);
            }
        });
    }
}
