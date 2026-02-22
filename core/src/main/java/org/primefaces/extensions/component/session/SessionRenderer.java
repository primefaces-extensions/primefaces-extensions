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
package org.primefaces.extensions.component.session;

import java.io.IOException;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;
import jakarta.servlet.http.HttpSession;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the Session component.
 *
 * @since 12.0.4
 */
@FacesRenderer(rendererType = SessionBase.DEFAULT_RENDERER, componentFamily = SessionBase.COMPONENT_FAMILY)
public class SessionRenderer extends CoreRenderer<Session> {

    @Override
    public void encodeBegin(final FacesContext facesContext, final Session component) throws IOException {
        Integer reactionPeriod = component.getReactionPeriod();
        if (reactionPeriod == null) {
            reactionPeriod = 60;
        }

        final WidgetBuilder wb = getWidgetBuilder(facesContext);
        wb.init("Session", component);
        wb.attr("reactionPeriod", reactionPeriod);
        wb.attr("multiWindowSupport", component.isMultiWindowSupport());
        final ExternalContext externalContext = facesContext.getExternalContext();
        final HttpSession httpSession = (HttpSession) externalContext.getSession(false);
        if (httpSession != null) {
            final int maxInactiveInterval = httpSession.getMaxInactiveInterval();
            if (maxInactiveInterval > 0) {
                wb.attr("max_inactive_interval", maxInactiveInterval);
            }
        }

        if (component.getOnexpire() != null) {
            wb.callback("onexpire", "function(e)", component.getOnexpire());
        }
        if (component.getOnexpired() != null) {
            wb.callback("onexpired", "function(e)", component.getOnexpired());
        }

        wb.finish();
    }
}