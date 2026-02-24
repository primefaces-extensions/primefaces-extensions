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
package org.primefaces.extensions.component.imageareaselect;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link ImageAreaSelect} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@FacesRenderer(rendererType = ImageAreaSelect.DEFAULT_RENDERER, componentFamily = ImageAreaSelect.COMPONENT_FAMILY)
public class ImageAreaSelectRenderer extends CoreRenderer<ImageAreaSelect> {

    @Override
    public void decode(final FacesContext context, final ImageAreaSelect component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final ImageAreaSelect component) throws IOException {

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtImageAreaSelect", component);
        wb.attr("target", SearchExpressionUtils.resolveClientIdsForClientSide(context, component, component.getFor()))
                    .attr("aspectRatio", component.getAspectRatio())
                    .attr("autoHide", component.getAutoHide())
                    .attr("fadeSpeed", component.getFadeSpeed())
                    .attr("handles", component.getHandles())
                    .attr("hide", component.getHide())
                    .attr("imageHeight", component.getImageHeight())
                    .attr("imageWidth", component.getImageWidth())
                    .attr("movable", component.getMovable())
                    .attr("persistent", component.getPersistent())
                    .attr("resizable", component.getResizable())
                    .attr("show", component.getShow())
                    .attr("zIndex", component.getZIndex())
                    .attr("maxHeight", component.getMaxHeight())
                    .attr("maxWidth", component.getMaxWidth())
                    .attr("minHeight", component.getMinHeight())
                    .attr("minWidth", component.getMinWidth())
                    .attr("keyboardSupport", component.getKeyboardSupport())
                    .attr("parentSelector", component.getParentSelector());

        encodeClientBehaviors(context, component);
        wb.finish();
    }
}
