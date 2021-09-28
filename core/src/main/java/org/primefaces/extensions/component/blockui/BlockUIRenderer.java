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
package org.primefaces.extensions.component.blockui;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link BlockUI} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @version $Revision$
 * @since 0.2
 */
public class BlockUIRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        encodeMarkup(fc, component);
        encodeScript(fc, component);
    }

    protected void encodeMarkup(final FacesContext fc, final UIComponent component) throws IOException {
        final BlockUI blockUI = (BlockUI) component;
        if (blockUI.getContent() == null && blockUI.getChildCount() > 0) {
            final ResponseWriter writer = fc.getResponseWriter();
            writer.startElement("div", null);
            writer.writeAttribute("id", blockUI.getClientId(fc) + "_content", null);
            writer.writeAttribute(Attrs.STYLE, "display: none;", null);
            renderChildren(fc, component);
            writer.endElement("div");
        }
    }

    protected void encodeScript(final FacesContext fc, final UIComponent component) throws IOException {
        final BlockUI blockUI = (BlockUI) component;
        final String clientId = blockUI.getClientId(fc);

        // get source
        String source = blockUI.getSource();
        if (source == null) {
            source = blockUI.getParent().getClientId(fc);
        }
        else {
            source = SearchExpressionFacade.resolveClientIds(fc, blockUI, source);
        }

        if (source == null) {
            throw new FacesException("Cannot find source for blockUI component '" + clientId + "'.");
        }

        // get target
        String target = blockUI.getTarget();
        if (target != null) {
            target = SearchExpressionFacade.resolveClientIds(fc, blockUI, target);
        }

        // get content
        String jqContent = null;
        boolean isContentExtern = false;
        if (blockUI.getContent() != null) {
            final UIComponent contentComponent = blockUI.findComponent(blockUI.getContent());
            if (contentComponent == null) {
                throw new FacesException("Cannot find content for blockUI component '" + clientId + "'.");
            }

            jqContent = "#" + contentComponent.getClientId(fc);
            isContentExtern = true;
        }
        else if (blockUI.getChildCount() > 0) {
            jqContent = "#" + clientId + "_content";
        }

        // get reg. expression
        final String eventRegEx;
        final String events = blockUI.getEvent();

        if (LangUtils.isBlank(events)) {
            // no events means all events of the given source are accepted
            eventRegEx = "/" + Constants.RequestParams.PARTIAL_SOURCE_PARAM + "=" + source + "(.)*$/";
        }
        else {
            eventRegEx = getEventRegEx(events);
        }

        // generate script
        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtBlockUI", blockUI);
        wb.attr("source", source);
        wb.attr("target", target, null);
        wb.attr("autoShow", blockUI.isAutoShow());
        wb.attr("focusInput", blockUI.isFocusInput());
        wb.attr("showOverlay", blockUI.isShowOverlay());
        wb.attr("centerX", blockUI.isCenterX());
        wb.attr("centerY", blockUI.isCenterY());
        wb.attr("fadeIn", blockUI.getFadeIn());
        wb.attr("fadeOut", blockUI.getFadeOut());

        wb.nativeAttr("css", blockUI.getCss());
        wb.nativeAttr("overlayCSS", blockUI.getCssOverlay());

        wb.attr("timeout", blockUI.getTimeout(), 0);

        wb.selectorAttr("content", jqContent);

        wb.attr("contentExtern", isContentExtern);
        wb.attr("namingContSep", Character.toString(UINamingContainer.getSeparatorChar(fc)));
        wb.nativeAttr("regEx", eventRegEx);

        wb.finish();
    }

    protected String getEventRegEx(final String events) {
        final String[] arrEvents = events.split("[\\s,]+");
        final StringBuilder sb = new StringBuilder("/");

        for (int i = 0; i < arrEvents.length; i++) {
            sb.append(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            sb.append("=");
            sb.append(arrEvents[i]);

            if (i + 1 < arrEvents.length) {
                sb.append("|");
            }
        }

        sb.append("/");
        return sb.toString();
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
