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

@FacesRenderer(componentFamily = Letteravatar.COMPONENT_FAMILY, rendererType = Letteravatar.DEFAULT_RENDERER)
public class LetteravatarRenderer extends Renderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        final Letteravatar letteravatar = (Letteravatar) component;

        encodeMarkup(context, letteravatar);
    }

    public void encodeMarkup(FacesContext context, Letteravatar letteravatar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        String clientId = letteravatar.getClientId(context);
        String style = letteravatar.getStyle();
        String styleClass = letteravatar.getStyleClass();

        writer.startElement("img", letteravatar);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        final Integer width = letteravatar.getWidth();
        final Integer height = letteravatar.getHeight();
        final String avatar = letteravatar.getValue();
        final Boolean rounded = letteravatar.isRounded();

        writer.writeAttribute("width", width, null);
        writer.writeAttribute("height", height, null);
        writer.writeAttribute("avatar", avatar, null);
        if (rounded) {
            writer.writeAttribute("style", "border-radius: 50%;", null);
        }

        writer.endElement("img");
    }

}
