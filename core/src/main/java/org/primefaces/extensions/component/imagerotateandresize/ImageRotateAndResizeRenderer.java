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

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link ImageRotateAndResize} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@FacesRenderer(rendererType = ImageRotateAndResize.DEFAULT_RENDERER, componentFamily = ImageRotateAndResize.COMPONENT_FAMILY)
public class ImageRotateAndResizeRenderer extends CoreRenderer<ImageRotateAndResize> {

    @Override
    public void decode(final FacesContext context, final ImageRotateAndResize component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final ImageRotateAndResize component) throws IOException {

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtImageRotateAndResize", component);
        wb.attr("target", SearchExpressionUtils.resolveClientIdsForClientSide(context, component, component.getFor()));

        encodeClientBehaviors(context, component);
        wb.finish();
    }
}