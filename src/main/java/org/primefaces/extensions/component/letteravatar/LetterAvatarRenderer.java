/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link LetterAvatar} component.
 *
 * @author https://github.com/aripddev
 * @since 7.0
 */
@FacesRenderer(componentFamily = LetterAvatar.COMPONENT_FAMILY, rendererType = LetterAvatar.DEFAULT_RENDERER)
public class LetterAvatarRenderer extends CoreRenderer {

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

        final String size = letterAvatar.getSize();
        final String color = letterAvatar.getColor();
        final String value = letterAvatar.getValue();

        Pattern p = Pattern.compile("\\b[a-zA-Z]");
        Matcher m = p.matcher(value);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }
        String initials = sb.toString();
        // TODO In Java 9, use Matcher#results() to get a Stream<MatchResult>
//        String initials = Pattern.compile("\\b[a-zA-Z]")
//                .matcher(value)
//                .results()
//                .map(MatchResult::group)
//                .collect(Collectors.joining(""));
//              or .toArray(String[]::new);
        initials = initials.length() == 1 ? initials : initials.charAt(0) + initials.substring(initials.length() - 1);

        final Boolean rounded = letterAvatar.isRounded();

        final String clientId = letterAvatar.getClientId(context);

        String backgroundColor = "hsl(" + hue(value) + ", 100%, 50%)";

        String style = letterAvatar.getStyle();
        style = style == null ? styleDiv(size, color, backgroundColor, rounded) : styleDiv(size, color, backgroundColor, rounded) + " " + style;
        String styleClass = letterAvatar.getStyleClass();
        styleClass = styleClass == null ? LetterAvatar.COMPONENT_CLASS : LetterAvatar.COMPONENT_CLASS + " " + styleClass;

        writer.startElement("div", letterAvatar);
        writer.writeAttribute("id", clientId, null);

        if (rounded) {
            styleClass = styleClass + " " + LetterAvatar.COMPONENT_CLASS_ROUNDED;
        }
        writer.writeAttribute("class", styleClass, "styleClass");

        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        writer.writeAttribute("title", value, null);

        writer.startElement("span", letterAvatar);
        writer.writeAttribute("class", "ui-letteravatar-initials", null);
        writer.writeAttribute("style", styleSpan(size), null);
        writer.write(initials);
        writer.endElement("span");

        writer.endElement("div");
    }

    private int hash(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = ((result << 5) - result) + str.charAt(i);
            result |= 0;
        }
        return result;
    }

    private int hue(String str) {
        return Math.abs(hash(str) % 360);
    }

    private String styleDiv(String size, String color, String backgroundColor, boolean rounded) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("color", color);
        map.put("background-color", backgroundColor);
        if (rounded) {
            map.put("border-radius", "50%");
        }
        map.put("height", size);
        map.put("text-align", "center");
        map.put("width", size);
        return map.entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(";"));
    }

    private String styleSpan(String size) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("font-size", "calc(" + size + " / 2)"); // 50% of parent
        map.put("line-height", "1");
        map.put("position", "relative");
        map.put("top", "calc(" + size + " / 4)"); // 25% of parent
        return map.entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(";"));
    }

}
