/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
