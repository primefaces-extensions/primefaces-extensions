/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.slideout;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link SlideOut} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
public class SlideOutRenderer extends CoreRenderer {

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
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SlideOut slideOut = (SlideOut) component;
        encodeMarkup(context, slideOut);
        encodeScript(context, slideOut);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
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
    private void encodeMarkup(final FacesContext context, final SlideOut slideOut) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = slideOut.getClientId(context);
        final String widgetVar = slideOut.resolveWidgetVar();

        writer.startElement("div", slideOut);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);

        if (slideOut.getPanelStyleClass() != null) {
            writer.writeAttribute("class", slideOut.getPanelStyleClass(), "styleClass");
        }
        if (slideOut.getPanelStyle() != null) {
            writer.writeAttribute("style", slideOut.getPanelStyle(), "style");
        }

        // handle
        encodeHandle(context, slideOut);

        // content of the panel
        renderChildren(context, slideOut);

        writer.endElement("div");
    }

    /**
     * HTML markup for the tab handle.
     */
    private void encodeHandle(final FacesContext context, final SlideOut slideOut) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        String styleClass = SlideOut.HANDLE_CLASS;
        if (slideOut.getHandleStyleClass() != null) {
            styleClass = styleClass + " " + slideOut.getHandleStyleClass();
        }

        writer.startElement("a", null);
        writer.writeAttribute("id", getHandleId(context, slideOut), null);
        if (slideOut.getHandleStyle() != null) {
            writer.writeAttribute("style", slideOut.getHandleStyle(), "style");
        }
        writer.writeAttribute("class", styleClass, "styleClass");

        // icon
        encodeIcon(context, slideOut);

        // handle text
        if (slideOut.getTitle() != null) {
            writer.write(escapeText(slideOut.getTitle()));
        }

        writer.endElement("a");
    }

    /**
     * HTML markup for the tab handle icon if defined.
     */
    private void encodeIcon(final FacesContext context, final SlideOut slideOut) throws IOException {
        if (slideOut.getIcon() == null) {
            return;
        }

        // fontawesome icons are OK but themeroller we need to add styles
        String icon = slideOut.getIcon().trim();
        if (icon.startsWith("ui")) {
            icon = "ui-icon " + icon + " ui-slideouttab-icon";
        }

        // <i class="ui-icon fa fa-television"></i>
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", null);
        writer.writeAttribute("class", icon, null);
        writer.endElement("span");
        writer.write(" ");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final SlideOut slideOut) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        final String clientId = slideOut.getClientId(context);
        final String handleId = getHandleId(context, slideOut);
        wb.initWithDomReady("ExtSlideOut", slideOut.resolveWidgetVar(), clientId);
        wb.attr("tabLocation", slideOut.getLocation());
        wb.attr("tabHandle", "#" + handleId);
        wb.attr("speed", slideOut.getAnimateSpeed());
        wb.attr("action", StringUtils.lowerCase(slideOut.getShowOn()));
        wb.attr("clickScreenToClose", slideOut.isClickScreenToClose());
        wb.attr("onLoadSlideOut", slideOut.isAutoOpen());
        wb.attr("positioning", slideOut.isSticky() ? "absolute" : "fixed");
        wb.attr("offset", slideOut.getOffset());
        wb.attr("offsetReverse", slideOut.isOffsetReverse());
        wb.attr("handleOffsetReverse", slideOut.isHandleOffsetReverse());
        wb.attr("bounceTimes", slideOut.getBounceTimes());
        wb.attr("bounceDistance", slideOut.getBounceDistance());

        if (slideOut.getHandleOffset() != null) {
            wb.attr("handleOffset", slideOut.getHandleOffset());
        }

        if (slideOut.getOnopen() != null) {
            // Define a callback function before the panel is opened
            wb.callback("onOpen", "function()", slideOut.getOnopen());
        }
        if (slideOut.getOnclose() != null) {
            // Define a callback function when the panel is closed
            wb.callback("onClose", "function()", slideOut.getOnclose());
        }

        encodeClientBehaviors(context, slideOut);

        wb.finish();
    }

    private String getHandleId(final FacesContext context, final SlideOut slideOut) {
        final String clientId = slideOut.getClientId(context);
        return clientId + "_handle";
    }

}
