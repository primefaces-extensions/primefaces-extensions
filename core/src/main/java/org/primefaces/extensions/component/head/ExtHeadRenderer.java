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
package org.primefaces.extensions.component.head;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;

/**
 * Renderer for the {@link ExtHead} component which extends PF {@link org.primefaces.renderkit.HeadRenderer}.
 * <p>
 * Ordering of rendered resources:
 * </p>
 *
 * <pre>
 * - first facet if defined
 * - Theme CSS
 * - JSF, PF, PF Extensions CSS resources
 * - middle facet if defined
 * - JSF, PF, PF Extensions JS resources
 * - title
 * - shortcut icon
 * - h:head content (encoded by super class at encodeChildren)
 * - last facet if defined
 * </pre>
 *
 * @author Thomas Andraschko
 * @since 0.2
 */
public class ExtHeadRenderer extends org.primefaces.renderkit.HeadRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final ExtHead extHead = (ExtHead) component;

        // encode title and shortcut icon
        encodeTitle(extHead, writer);
        encodeShortcutIcon(context, extHead, writer);

        super.encodeEnd(context, component);
    }

    private void encodeTitle(final ExtHead extHead, final ResponseWriter writer) throws IOException {
        if (extHead.getTitle() != null) {
            writer.startElement(Attrs.TITLE, null);
            writer.writeText(extHead.getTitle(), Attrs.TITLE);
            writer.endElement(Attrs.TITLE);
        }
    }

    private void encodeShortcutIcon(final FacesContext context, final ExtHead extHead, final ResponseWriter writer) throws IOException {
        if (extHead.getShortcutIcon() != null) {
            ExternalContext externalContext = context.getExternalContext();
            writer.startElement("link", null);
            writer.writeAttribute("rel", "shortcut icon", null);
            writer.writeAttribute("href", externalContext.encodeResourceURL(extHead.getShortcutIcon()), null);
            writer.endElement("link");
        }
    }
}
