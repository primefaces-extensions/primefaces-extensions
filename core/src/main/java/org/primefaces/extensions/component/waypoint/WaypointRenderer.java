/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.waypoint;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Waypoint} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6
 */
public class WaypointRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final Waypoint waypoint = (Waypoint) component;
        encodeScript(fc, waypoint);
    }

    private void encodeScript(final FacesContext fc, final Waypoint waypoint) throws IOException {
        final String context = SearchExpressionFacade.resolveClientIds(fc, waypoint, waypoint.getForContext());
        final String target = SearchExpressionFacade.resolveClientIds(fc, waypoint, waypoint.getFor(),
                    SearchExpressionUtils.SET_PARENT_FALLBACK);

        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtWaypoint", waypoint);
        wb.attr("target", target);
        wb.attr("continuous", waypoint.isContinuous());
        wb.attr("enabled", waypoint.isEnabled());
        wb.attr("horizontal", waypoint.isHorizontal());
        wb.attr("triggerOnce", waypoint.isTriggerOnce());

        if (context != null) {
            wb.attr("context", context);
        }

        if (waypoint.getOffset() != null) {
            wb.nativeAttr("offset", waypoint.getOffset());
        }

        encodeClientBehaviors(fc, waypoint);

        wb.finish();
    }
}
