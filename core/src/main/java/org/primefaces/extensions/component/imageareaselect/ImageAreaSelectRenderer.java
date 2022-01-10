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
package org.primefaces.extensions.component.imageareaselect;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link ImageAreaSelect} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
public class ImageAreaSelectRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ImageAreaSelect imageAreaSelect = (ImageAreaSelect) component;

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtImageAreaSelect", imageAreaSelect);
        wb.attr("target", SearchExpressionFacade.resolveClientId(context, imageAreaSelect, imageAreaSelect.getFor()))
                    .attr("aspectRatio", imageAreaSelect.getAspectRatio())
                    .attr("autoHide", imageAreaSelect.isAutoHide())
                    .attr("fadeSpeed", imageAreaSelect.getFadeSpeed())
                    .attr("handles", imageAreaSelect.isHandles())
                    .attr("hide", imageAreaSelect.isHide())
                    .attr("imageHeight", imageAreaSelect.getImageHeight())
                    .attr("imageWidth", imageAreaSelect.getImageWidth())
                    .attr("movable", imageAreaSelect.isMovable())
                    .attr("persistent", imageAreaSelect.isPersistent())
                    .attr("resizable", imageAreaSelect.isPersistent())
                    .attr("show", imageAreaSelect.isShow())
                    .attr("zIndex", imageAreaSelect.getZIndex())
                    .attr("maxHeight", imageAreaSelect.getMaxHeight())
                    .attr("maxWidth", imageAreaSelect.getMaxWidth())
                    .attr("minHeight", imageAreaSelect.getMinHeight())
                    .attr("minWidth", imageAreaSelect.getMinWidth())
                    .attr("keyboardSupport", imageAreaSelect.isKeyboardSupport())
                    .attr("parentSelector", imageAreaSelect.getParentSelector());

        encodeClientBehaviors(context, imageAreaSelect);
        wb.finish();
    }
}
