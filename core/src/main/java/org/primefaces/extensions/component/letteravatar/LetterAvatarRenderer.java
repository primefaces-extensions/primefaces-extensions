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
package org.primefaces.extensions.component.letteravatar;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.LangUtils;

/**
 * Renderer for the {@link LetterAvatar} component.
 *
 * @author https://github.com/aripddev
 * @since 7.0
 */
public class LetterAvatarRenderer extends CoreRenderer {

    private static final Pattern VALUE_PATTTERN = Pattern.compile("\\b[a-zA-Z]");

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final LetterAvatar letterAvatar = (LetterAvatar) component;

        encodeMarkup(context, letterAvatar);
    }

    public void encodeMarkup(final FacesContext context, final LetterAvatar letterAvatar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = letterAvatar.getClientId(context);
        final String value = letterAvatar.getValue();

        final Matcher m = VALUE_PATTTERN.matcher(value);
        final StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }
        String initials = sb.toString();
        initials = initials.length() == 1 ? initials : initials.charAt(0) + initials.substring(initials.length() - 1);

        final boolean rounded = letterAvatar.isRounded();

        String color = letterAvatar.getColor();
        if (LangUtils.isValueBlank(color)) {
            color = "#fff"; // keep it for mix-blend-mode
        }

        String backgroundColor = letterAvatar.getBackgroundColor();
        if (LangUtils.isValueBlank(backgroundColor)) {
            backgroundColor = "hsl(" + hue(value) + ", 100%, 50%)";
        }

        final String size = letterAvatar.getSize();
        String style = styleContainer(size, color, backgroundColor);
        if (!LangUtils.isValueBlank(letterAvatar.getStyle())) {
            style += letterAvatar.getStyle();
        }

        final String styleClass = getStyleClassBuilder(context)
                    .add(LetterAvatar.COMPONENT_CLASS)
                    .add(letterAvatar.getStyleClass())
                    .add(rounded, LetterAvatar.COMPONENT_CLASS_ROUNDED)
                    .build();

        writer.startElement("span", letterAvatar);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("title", value, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        writer.writeAttribute(Attrs.STYLE, style, Attrs.STYLE);

        renderChildren(context, letterAvatar);

        writer.startElement("span", letterAvatar);
        writer.writeAttribute(Attrs.CLASS, "ui-letteravatar-initials", null);
        writer.writeAttribute(Attrs.STYLE, styleInitials(size), null);
        writer.write(initials);
        writer.endElement("span");

        writer.endElement("span");
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) {
        // NOOP
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public static int hue(final String str) {
        return Math.abs((str.hashCode() % 40) * 9);
    }

    protected String styleContainer(final String size, final String color, final String backgroundColor) {
        return "color:" + color
                    + ";background-color:" + backgroundColor
                    + ";height:" + size
                    + ";width:" + size
                    + ";";
    }

    protected String styleInitials(final String size) {
        return "font-size:calc(" + size + "/2)"
                    + ";line-height:" + size
                    + ";";
    }
}
