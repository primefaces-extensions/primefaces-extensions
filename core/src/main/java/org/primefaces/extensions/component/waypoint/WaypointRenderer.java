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
package org.primefaces.extensions.component.waypoint;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Waypoint} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6
 */
@FacesRenderer(rendererType = Waypoint.DEFAULT_RENDERER, componentFamily = Waypoint.COMPONENT_FAMILY)
public class WaypointRenderer extends CoreRenderer<Waypoint> {

    @Override
    public void decode(final FacesContext context, final Waypoint component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final Waypoint component) throws IOException {
        encodeScript(fc, component);
    }

    private void encodeScript(final FacesContext fc, final Waypoint component) throws IOException {
        final String context = SearchExpressionUtils.resolveClientIdsForClientSide(fc, component, component.getForContext());
        final String target = SearchExpressionUtils.resolveClientIdsForClientSide(fc, component, component.getFor());

        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtWaypoint", component);
        wb.attr("target", target);
        wb.attr("continuous", component.isContinuous());
        wb.attr("enabled", component.isEnabled());
        wb.attr("horizontal", component.isHorizontal());
        wb.attr("triggerOnce", component.isTriggerOnce());

        if (context != null) {
            wb.attr("context", context);
        }

        if (component.getOffset() != null) {
            wb.nativeAttr("offset", component.getOffset());
        }

        encodeClientBehaviors(fc, component);

        wb.finish();
    }
}
