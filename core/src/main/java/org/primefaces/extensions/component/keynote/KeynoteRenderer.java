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

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class KeynoteRenderer extends CoreRenderer {

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

        // encodeCSS(context, "primefaces-extensions", "keynote/theme/" + keynote.getTheme() + ".css");
        // encodeCSS(context, "primefaces", "primeicons/primeicons.css");
        if (keynote.getTheme() != null) {
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

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Keynote keynote) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = keynote.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(Keynote.CONTAINER_CLASS)
                    .add(keynote.getStyleClass())
                    .build();

        writer.startElement("div", keynote);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (keynote.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, keynote.getStyle(), Attrs.STYLE);
        }

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, Keynote.SLIDES_CLASS, "class");
        renderChildren(context, keynote);
        writer.endElement("div");

        writer.endElement("div");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final Keynote keynote) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtKeynote", keynote);
        wb.attr("width", keynote.getWidth())
                    .attr("height", keynote.getHeight())
                    .attr("margin", keynote.getMargin())
                    .attr("minScale", keynote.getMinScale())
                    .attr("maxScale", keynote.getMaxScale())
                    .attr("autoSlide", keynote.getAutoSlide())
                    .attr("center", keynote.isCenter())
                    .attr("controls", keynote.isControls())
                    .attr("disableLayout", keynote.isDisableLayout())
                    .attr("embedded", keynote.isEmbedded())
                    .attr("loop", keynote.isLoop())
                    .attr("navigationMode", keynote.getNavigationMode())
                    .attr("progress", keynote.isProgress())
                    .attr("showNotes", keynote.isShowNotes())
                    .attr("touch", keynote.isTouch())
                    .attr("transition", keynote.getTransition())
                    .attr("transitionSpeed", keynote.getTransitionSpeed())
                    .attr("backgroundTransition", keynote.getBackgroundTransition())
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

}
