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
package org.primefaces.extensions.component.keynote;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;

public class KeynoteItemRenderer extends CoreRenderer {

    public static final String ITEM_CLASS = "ui-keynote-item";
    public static final String SPEAKER_NOTE_CLASS = "notes";

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIKeynoteItem uiKeynoteItem = (UIKeynoteItem) component;

        writer.startElement("section", null);

        if (uiKeynoteItem.isMarkdown()) {
            writer.writeAttribute("data-markdown", "", null);
            writer.writeAttribute("data-separator", uiKeynoteItem.getSeparator(), null);
            writer.writeAttribute("data-separator-vertical", uiKeynoteItem.getSeparatorVertical(), null);
        }

        if (uiKeynoteItem.getBackgroundColor() != null) {
            writer.writeAttribute("data-background-color", uiKeynoteItem.getBackgroundColor(), null);
        }
        if (uiKeynoteItem.getBackgroundImage() != null) {
            writer.writeAttribute("data-background-image", uiKeynoteItem.getBackgroundImage(), null);
        }
        if (uiKeynoteItem.getBackgroundSize() != null) {
            writer.writeAttribute("data-background-size", uiKeynoteItem.getBackgroundSize(), null);
        }
        if (uiKeynoteItem.getBackgroundPosition() != null) {
            writer.writeAttribute("data-background-position", uiKeynoteItem.getBackgroundPosition(), null);
        }
        if (uiKeynoteItem.getBackgroundRepeat() != null) {
            writer.writeAttribute("data-background-repeat", uiKeynoteItem.getBackgroundRepeat(), null);
        }
        if (uiKeynoteItem.getBackgroundOpacity() != null) {
            writer.writeAttribute("data-background-opacity", uiKeynoteItem.getBackgroundOpacity(), null);
        }
        if (uiKeynoteItem.getBackgroundVideo() != null) {
            writer.writeAttribute("data-background-video", uiKeynoteItem.getBackgroundVideo(), null);
        }
        if (uiKeynoteItem.isBackgroundVideoLoop()) {
            writer.writeAttribute("data-background-video-loop", "", null);
        }
        if (uiKeynoteItem.isBackgroundVideoMuted()) {
            writer.writeAttribute("data-background-video-muted", "", null);
        }
        if (uiKeynoteItem.getVisibility() != null) {
            writer.writeAttribute("data-visibility", uiKeynoteItem.getVisibility(), null);
        }

        if (uiKeynoteItem.getStyleClass() != null) {
            writer.writeAttribute(Attrs.CLASS, ITEM_CLASS + " " + uiKeynoteItem.getStyleClass(), null);
        }
        else {
            writer.writeAttribute(Attrs.CLASS, ITEM_CLASS, null);
        }

        if (uiKeynoteItem.isMarkdown()) {
            writer.startElement("textarea", null);
            writer.writeAttribute("data-template", "", null);
        }

        // encode content
        renderChildren(context, uiKeynoteItem);

        if (uiKeynoteItem.isMarkdown()) {
            writer.endElement("textarea");
        }

        if (uiKeynoteItem.getNote() != null) {
            writer.startElement("aside", null);
            writer.writeAttribute(Attrs.CLASS, SPEAKER_NOTE_CLASS, null);
            writer.writeText(uiKeynoteItem.getNote(), null);
            writer.endElement("aside");
        }

        writer.endElement("section");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(final FacesContext fc, final UIComponent component) {
        // nothing to do
    }
}
