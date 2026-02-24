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
package org.primefaces.extensions.component.imagezoom;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link ImageZoom} component.
 *
 * @author Melloware
 * @since 11.0.3
 */
@FacesRenderer(rendererType = ImageZoom.DEFAULT_RENDERER, componentFamily = ImageZoom.COMPONENT_FAMILY)
public class ImageZoomRenderer extends CoreRenderer<ImageZoom> {

    @Override
    public void encodeEnd(final FacesContext context, final ImageZoom component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ImageZoom", component);
        wb.attr("target", SearchExpressionUtils.resolveClientIdsForClientSide(context, component, component.getFor()))
                    .attr("margin", component.getMargin(), 0)
                    .attr("scrollOffset", component.getScrollOffset(), 40)
                    .attr("background", component.getBackground(), "#fff")
                    .attr("container", component.getContainer(), "#fff")
                    .attr("template", component.getTemplate(), "#fff");
        wb.finish();
    }
}
