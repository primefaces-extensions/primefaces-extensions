/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
import java.util.List;

import jakarta.faces.FacesException;
import jakarta.faces.application.Resource;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.model.keynote.KeynoteItem;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(rendererType = Keynote.DEFAULT_RENDERER, componentFamily = Keynote.COMPONENT_FAMILY)
public class KeynoteRenderer extends CoreRenderer<Keynote> {

    public static final String CONTAINER_CLASS = "ui-keynote reveal";
    public static final String SLIDES_CLASS = "slides";

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(final FacesContext context, final Keynote component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeBegin(final FacesContext context, final Keynote component) throws IOException {
        if (!"none".equals(component.getTheme())) {
            encodeCSS(context, component.getLibrary(), component.getTheme());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(final FacesContext context, final Keynote component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Keynote component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(CONTAINER_CLASS)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, SLIDES_CLASS, "class");

        if (component.getVar() != null) {
            // dynamic items
            final Object value = component.getValue();
            if (value != null) {
                if (!(value instanceof Collection)) {
                    throw new FacesException("Value in Keynote must be of type Collection / List");
                }

                final List<UIComponent> children = component.getChildren();
                for (int i = 0; i < children.size(); i++) {
                    final UIComponent kid = children.get(i);
                    if (kid.isRendered() && !(kid instanceof UIKeynoteItem)) {
                        // first render children like stamped elements, etc.
                        renderChild(context, kid);
                    }
                }

                final Collection<KeynoteItem> col = (Collection) value;
                for (int i = 0; i < col.size(); i++) {
                    final KeynoteItem keynoteItem = (KeynoteItem) col.toArray()[i];
                    // find ui item by type
                    final UIKeynoteItem uiItem = component.getItem(keynoteItem.getType());

                    if (uiItem.isRendered()) {
                        // set data in request scope
                        component.setData(keynoteItem);

                        // render item
                        renderChild(context, uiItem);
                    }
                }
            }
        }
        else {
            // static items
            final List<UIComponent> children = component.getChildren();
            for (int i = 0; i < children.size(); i++) {
                final UIComponent kid = children.get(i);
                if (kid.isRendered()) {
                    renderChild(context, kid);
                }
            }
        }

        writer.endElement("div");

        writer.endElement("div");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final Keynote component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.init("ExtKeynote", component)
                    .attr("width", component.getWidth(), 960)
                    .attr("height", component.getHeight(), 700)
                    .attr("margin", component.getMargin(), 0.04)
                    .attr("minScale", component.getMinScale(), 0.2)
                    .attr("maxScale", component.getMaxScale(), 2.0)
                    .attr("autoSlide", component.getAutoSlide(), 0)
                    .attr("center", component.isCenter(), true)
                    .attr("controls", component.isControls(), true)
                    .attr("disableLayout", component.isDisableLayout(), false)
                    .attr("embedded", component.isEmbedded(), false)
                    .attr("loop", component.isLoop(), false)
                    .attr("navigationMode", component.getNavigationMode(), "default")
                    .attr("progress", component.isProgress(), true)
                    .attr("showNotes", component.isShowNotes(), false)
                    .attr("slideNumber", component.getSlideNumber(), "false")
                    .attr("touch", component.isTouch(), true)
                    .attr("transition", component.getTransition(), "slide")
                    .attr("transitionSpeed", component.getTransitionSpeed(), "default")
                    .attr("backgroundTransition", component.getBackgroundTransition(), "fade")
                    .attr("theme", component.getTheme(), "none");

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    private void encodeCSS(final FacesContext context, final String library, final String theme) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final ExternalContext externalContext = context.getExternalContext();

        final Resource cssResource = context.getApplication()
                    .getResourceHandler()
                    .createResource("keynote/theme/" + theme + ".css", library);
        if (cssResource == null) {
            throw new FacesException(
                        "Error loading CSS, cannot find \"" + theme + "\" resource of \"" + library + "\" library");
        }
        else {
            writer.startElement("link", null);
            writer.writeAttribute("type", "text/css", null);
            writer.writeAttribute("rel", "stylesheet", null);
            writer.writeAttribute("href", externalContext.encodeResourceURL(cssResource.getRequestPath()), null);
            writer.endElement("link");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeChildren(final FacesContext context, final Keynote component) {
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
