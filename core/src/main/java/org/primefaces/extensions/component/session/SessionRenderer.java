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
package org.primefaces.extensions.component.session;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.servlet.http.HttpSession;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Session} component.
 *
 * @author Frank Cornelis
 * @since 12.0.4
 */
@FacesRenderer(componentFamily = Session.COMPONENT_FAMILY, rendererType = SessionRenderer.RENDERER_TYPE)
public class SessionRenderer extends CoreRenderer {

    public static final String RENDERER_TYPE = "org.primefaces.extensions.component.SessionRenderer";

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        Session sessionComponent = (Session) component;
        Integer reactionPeriod = sessionComponent.getReactionPeriod();
        if (null == reactionPeriod) {
            reactionPeriod = 60;
        }

        WidgetBuilder wb = getWidgetBuilder(facesContext);
        wb.init("Session", sessionComponent);
        wb.attr("reactionPeriod", reactionPeriod);
        wb.attr("multiWindowSupport", sessionComponent.isMultiWindowSupport());
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession httpSession = (HttpSession) externalContext.getSession(false);
        if (httpSession != null) {
            int maxInactiveInterval = httpSession.getMaxInactiveInterval();
            if (maxInactiveInterval > 0) {
                wb.attr("max_inactive_interval", maxInactiveInterval);
            }
        }

        if (sessionComponent.getOnexpire() != null) {
            wb.callback("onexpire", "function(e)", sessionComponent.getOnexpire());
        }
        if (sessionComponent.getOnexpired() != null) {
            wb.callback("onexpired", "function(e)", sessionComponent.getOnexpired());
        }

        wb.finish();
    }
}