/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.letteravatar;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@FacesRenderer(componentFamily = LetterAvatar.COMPONENT_FAMILY, rendererType = LetterAvatar.DEFAULT_RENDERER)
public class LetterAvatarRenderer extends Renderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        final LetterAvatar letterAvatar = (LetterAvatar) component;

        encodeMarkup(context, letterAvatar);
    }

    public void encodeMarkup(FacesContext context, LetterAvatar letterAvatar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final Integer width = letterAvatar.getWidth();
        final Integer height = letterAvatar.getHeight();
        final String avatar = letterAvatar.getValue();
        final Boolean rounded = letterAvatar.isRounded();

        String clientId = letterAvatar.getClientId(context);
        String style = letterAvatar.getStyle();
        String styleClass = letterAvatar.getStyleClass();
        styleClass = (styleClass == null) ? LetterAvatar.COMPONENT_CLASS : LetterAvatar.COMPONENT_CLASS + " " + styleClass;

        writer.startElement("img", letterAvatar);
        writer.writeAttribute("id", clientId, null);

        if (rounded) {
            styleClass = styleClass + " " + LetterAvatar.COMPONENT_CLASS_ROUNDED;
        }
        writer.writeAttribute("class", styleClass, "styleClass");

        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        writer.writeAttribute("width", width, null);
        writer.writeAttribute("height", height, null);
        writer.writeAttribute("avatar", avatar, null);

        writer.endElement("img");
    }

}
