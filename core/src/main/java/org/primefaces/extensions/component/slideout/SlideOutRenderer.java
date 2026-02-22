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
package org.primefaces.extensions.component.slideout;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link SlideOut} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@FacesRenderer(rendererType = SlideOut.DEFAULT_RENDERER, componentFamily = SlideOut.COMPONENT_FAMILY)
public class SlideOutRenderer extends CoreRenderer<SlideOut> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(final FacesContext context, final SlideOut component) {
        decodeBehaviors(context, component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeEnd(final FacesContext context, final SlideOut component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeChildren(final FacesContext context, final SlideOut component) {
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
    private void encodeMarkup(final FacesContext context, final SlideOut component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String widgetVar = component.resolveWidgetVar();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);

        if (component.getPanelStyleClass() != null) {
            writer.writeAttribute(Attrs.CLASS, component.getPanelStyleClass(), "styleClass");
        }
        if (component.getPanelStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getPanelStyle(), Attrs.STYLE);
        }

        // handle
        encodeHandle(context, component);

        // content of the panel
        renderChildren(context, component);

        writer.endElement("div");
    }

    /**
     * HTML markup for the tab handle.
     */
    private void encodeHandle(final FacesContext context, final SlideOut component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String styleClass = getStyleClassBuilder(context)
                    .add(SlideOut.HANDLE_CLASS)
                    .add(component.getHandleStyleClass())
                    .build();

        writer.startElement("a", null);
        writer.writeAttribute("id", getHandleId(context, component), null);
        if (component.getHandleStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getHandleStyle(), Attrs.STYLE);
        }
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        // icon
        encodeIcon(context, component);

        // handle text
        if (component.getTitle() != null) {
            writer.writeText(component.getTitle(), "title");
        }

        writer.endElement("a");
    }

    /**
     * HTML markup for the tab handle icon if defined.
     */
    private void encodeIcon(final FacesContext context, final SlideOut component) throws IOException {
        if (component.getIcon() == null) {
            return;
        }

        // fontawesome icons are OK but themeroller we need to add styles
        String icon = component.getIcon().trim();
        if (icon.startsWith("ui")) {
            icon = "ui-icon " + icon + " ui-slideouttab-icon";
        }

        // <i class="ui-icon fa fa-television"></i>
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", null);
        writer.writeAttribute(Attrs.CLASS, icon, null);
        writer.endElement("span");
        writer.write(" ");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final SlideOut component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        final String handleId = getHandleId(context, component);
        wb.init("ExtSlideOut", component);
        wb.attr("tabLocation", component.getLocation());
        wb.attr("tabHandle", handleId);
        wb.attr("speed", component.getAnimateSpeed());
        wb.attr("action", ExtLangUtils.lowerCase(component.getShowOn()));
        wb.attr("clickScreenToClose", component.isClickScreenToClose());
        wb.attr("onLoadSlideOut", component.isAutoOpen());
        wb.attr("positioning", component.isSticky() ? "absolute" : "fixed");
        wb.attr("offset", component.getOffset());
        wb.attr("offsetReverse", component.isOffsetReverse());
        wb.attr("handleOffsetReverse", component.isHandleOffsetReverse());
        wb.attr("bounceTimes", component.getBounceTimes());
        wb.attr("bounceDistance", component.getBounceDistance());
        wb.nativeAttr("clickScreenToCloseFilters", "['.ui-slideouttab-panel', 'button', 'a']");

        if (component.getHandleOffset() != null) {
            wb.attr("handleOffset", component.getHandleOffset());
        }

        if (component.getOnopen() != null) {
            wb.callback("onOpen", "function()", component.getOnopen());
        }
        if (component.getOnclose() != null) {
            wb.callback("onClose", "function()", component.getOnclose());
        }
        if (component.getOnslide() != null) {
            wb.callback("onSlide", "function()", component.getOnslide());
        }
        if (component.getOnbeforeopen() != null) {
            wb.callback("onBeforeOpen", "function()", component.getOnbeforeopen());
        }
        if (component.getOnbeforeclose() != null) {
            wb.callback("onBeforeClose", "function()", component.getOnbeforeclose());
        }
        if (component.getOnbeforeslide() != null) {
            wb.callback("onBeforeSlide", "function()", component.getOnbeforeslide());
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    private String getHandleId(final FacesContext context, final SlideOut component) {
        final String clientId = component.getClientId(context);
        return clientId + "_handle";
    }

}
