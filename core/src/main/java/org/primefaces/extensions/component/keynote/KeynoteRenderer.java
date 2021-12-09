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
package org.primefaces.extensions.component.keynote;

import java.io.IOException;
import java.util.Collection;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.model.keynote.KeynoteItem;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.WidgetBuilder;

public class KeynoteRenderer extends CoreRenderer {

    public static final String CONTAINER_CLASS = "ui-keynote reveal";
    public static final String SLIDES_CLASS = "slides";
    public static final String ITEM_CLASS = "ui-keynote-item";
    public static final String SPEAKER_NOTE_CLASS = "notes";

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        final Keynote keynote = (Keynote) component;

        // encodeCSS(context, "primefaces-extensions", "keynote/theme/" + keynote.getTheme() + ".css"); // TODO will be removed after completion of theming
        // encodeCSS(context, "primefaces", "primeicons/primeicons.css"); // TODO will be removed after completion of theming
        if (!"none".equals(keynote.getTheme())) {
            encodeCSS(context, keynote.getLibrary(), keynote.getTheme());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Keynote keynote = (Keynote) component;

        encodeMarkup(context, keynote);
        encodeScript(context, keynote);
    }

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Keynote keynote) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = keynote.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(CONTAINER_CLASS)
                    .add(keynote.getStyleClass())
                    .build();

        writer.startElement("div", keynote);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (keynote.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, keynote.getStyle(), Attrs.STYLE);
        }

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, SLIDES_CLASS, "class");

        if (keynote.getVar() != null) {
            // dynamic items
            final Object value = keynote.getValue();
            if (value != null) {
                if (!(value instanceof Collection)) {
                    throw new FacesException("Value in Keynote must be of type Collection / List");
                }

                for (final UIComponent kid : keynote.getChildren()) {
                    if (kid.isRendered() && !(kid instanceof UIKeynoteItem)) {
                        // first render children like stamped elements, etc.
                        renderChild(context, kid);
                    }
                }

                final Collection<KeynoteItem> col = (Collection<KeynoteItem>) value;
                for (final KeynoteItem keynoteItem : col) {
                    // find ui item by type
                    final UIKeynoteItem uiItem = keynote.getItem(keynoteItem.getType());

                    if (uiItem.isRendered()) {
                        // set data in request scope
                        keynote.setData(keynoteItem);

                        // render item
                        renderItem(context, writer, keynote, uiItem);
                    }
                }
            }
        }
        else {
            // static items
            for (final UIComponent kid : keynote.getChildren()) {
                if (kid.isRendered()) {
                    if (kid instanceof UIKeynoteItem) {
                        // render item
                        renderItem(context, writer, keynote, (UIKeynoteItem) kid);
                    }
                    else {
                        // render a child like stamped element, etc.
                        renderChild(context, kid);
                    }
                }
            }
        }

        writer.endElement("div");

        writer.endElement("div");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final Keynote keynote) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.init("ExtKeynote", keynote)
                    .attr("width", keynote.getWidth(), 960)
                    .attr("height", keynote.getHeight(), 700)
                    .attr("margin", keynote.getMargin(), 0.04)
                    .attr("minScale", keynote.getMinScale(), 0.2)
                    .attr("maxScale", keynote.getMaxScale(), 2.0)
                    .attr("autoSlide", keynote.getAutoSlide(), 0)
                    .attr("center", keynote.isCenter(), true)
                    .attr("controls", keynote.isControls(), true)
                    .attr("disableLayout", keynote.isDisableLayout(), false)
                    .attr("embedded", keynote.isEmbedded(), false)
                    .attr("loop", keynote.isLoop(), false)
                    .attr("navigationMode", keynote.getNavigationMode(), "default")
                    .attr("progress", keynote.isProgress(), true)
                    .attr("showNotes", keynote.isShowNotes(), false)
                    .attr("slideNumber", keynote.getSlideNumber(), "false")
                    .attr("touch", keynote.isTouch(), true)
                    .attr("transition", keynote.getTransition(), "slide")
                    .attr("transitionSpeed", keynote.getTransitionSpeed(), "default")
                    .attr("backgroundTransition", keynote.getBackgroundTransition(), "fade")
                    .attr("theme", keynote.getTheme());

        encodeClientBehaviors(context, keynote);

        wb.finish();
    }

    private void encodeCSS(FacesContext context, String library, String theme) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        ExternalContext externalContext = context.getExternalContext();

        Resource cssResource = context.getApplication().getResourceHandler().createResource(theme + ".css", library);
        if (cssResource == null) {
            throw new FacesException("Error loading CSS, cannot find \"" + theme + "\" resource of \"" + library + "\" library");
        }
        else {
            writer.startElement("link", null);
            writer.writeAttribute("type", "text/css", null);
            writer.writeAttribute("rel", "stylesheet", null);
            writer.writeAttribute("href", externalContext.encodeResourceURL(cssResource.getRequestPath()), null);
            writer.endElement("link");
        }
    }

    protected void renderItem(final FacesContext context, final ResponseWriter writer, final Keynote keynote,
                final UIKeynoteItem uiItem)
                throws IOException {

        writer.startElement("section", null);

        if (uiItem.isMarkdown()) {
            writer.writeAttribute("data-markdown", "", null);
            writer.writeAttribute("data-separator", uiItem.getSeparator(), null);
            writer.writeAttribute("data-separator-vertical", uiItem.getSeparatorVertical(), null);
        }

        if (uiItem.getBackgroundColor() != null) {
            writer.writeAttribute("data-background-color", uiItem.getBackgroundColor(), null);
        }
        if (uiItem.getBackgroundImage() != null) {
            writer.writeAttribute("data-background-image", uiItem.getBackgroundImage(), null);
        }
        if (uiItem.getBackgroundSize() != null) {
            writer.writeAttribute("data-background-size", uiItem.getBackgroundSize(), null);
        }
        if (uiItem.getBackgroundPosition() != null) {
            writer.writeAttribute("data-background-position", uiItem.getBackgroundPosition(), null);
        }
        if (uiItem.getBackgroundRepeat() != null) {
            writer.writeAttribute("data-background-repeat", uiItem.getBackgroundRepeat(), null);
        }
        if (uiItem.getBackgroundOpacity() != null) {
            writer.writeAttribute("data-background-opacity", uiItem.getBackgroundOpacity(), null);
        }
        if (uiItem.getBackgroundVideo() != null) {
            writer.writeAttribute("data-background-video", uiItem.getBackgroundVideo(), null);
        }
        if (uiItem.isBackgroundVideoLoop()) {
            writer.writeAttribute("data-background-video-loop", "", null);
        }
        if (uiItem.isBackgroundVideoMuted()) {
            writer.writeAttribute("data-background-video-muted", "", null);
        }

        if (uiItem.getStyleClass() != null) {
            writer.writeAttribute(Attrs.CLASS, ITEM_CLASS + " " + uiItem.getStyleClass(), null);
        }
        else {
            writer.writeAttribute(Attrs.CLASS, ITEM_CLASS, null);
        }

        if (uiItem.isMarkdown()) {
            writer.startElement("textarea", null);
            writer.writeAttribute("data-template", "", null);
        }

        // encode content of pe:keynoteItem
        uiItem.encodeAll(context);

        if (uiItem.isMarkdown()) {
            writer.endElement("textarea");
        }

        if (keynote.isShowNotes()) {
            writer.startElement("aside", null);
            writer.writeAttribute(Attrs.CLASS, SPEAKER_NOTE_CLASS, null);
            String note = uiItem.getNote() == null ? Constants.EMPTY_STRING : uiItem.getNote();
            writer.writeText(note, null);
            writer.endElement("aside");
        }

        writer.endElement("section");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
