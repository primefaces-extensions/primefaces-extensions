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
package org.primefaces.extensions.component.blockui;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
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
        BlockUI blockUI = (BlockUI) component;
        if (blockUI.getContent() == null && blockUI.getChildCount() > 0) {
            ResponseWriter writer = fc.getResponseWriter();
            writer.startElement("div", null);
            writer.writeAttribute("id", blockUI.getClientId(fc) + "_content", null);
            writer.writeAttribute("style", "display: none;", null);
            renderChildren(fc, component);
            writer.endElement("div");
        }
    }

    protected void encodeScript(final FacesContext fc, final UIComponent component) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();
        BlockUI blockUI = (BlockUI) component;
        String clientId = blockUI.getClientId(fc);

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
            UIComponent contentComponent = blockUI.findComponent(blockUI.getContent());
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
        String eventRegEx;
        String events = blockUI.getEvent();

        if (StringUtils.isBlank(events)) {
            // no events means all events of the given source are accepted
            eventRegEx = "/" + Constants.RequestParams.PARTIAL_SOURCE_PARAM + "=" + source + "(.)*$/";
        }
        else {
            String[] arrEvents = events.split("[\\s,]+");
            StringBuilder sb = new StringBuilder("/");

            for (int i = 0; i < arrEvents.length; i++) {
                sb.append(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
                sb.append("=");
                sb.append(arrEvents[i]);

                if (i + 1 < arrEvents.length) {
                    sb.append("|");
                }
            }

            sb.append("/");
            eventRegEx = sb.toString();
        }

        // generate script
        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.initWithDomReady("ExtBlockUI", blockUI.resolveWidgetVar(), clientId);
        wb.attr("source", source);
        if (target != null) {
            wb.attr("target", target);
        }

        wb.attr("autoShow", blockUI.isAutoShow());

        String css = blockUI.getCss();
        if (css != null) {
            wb.attr("css", css);
        }

        String cssOverlay = blockUI.getCssOverlay();
        if (cssOverlay != null) {
            wb.nativeAttr("overlayCSS", cssOverlay);
        }

        int timeout = blockUI.getTimeout();
        if (timeout > 0) {
            wb.attr("timeout", timeout);
        }

        wb.attr("centerX", blockUI.isCenterX());
        wb.attr("centerY", blockUI.isCenterY());

        if (jqContent != null) {
            wb.selectorAttr("content", jqContent);
        }

        wb.attr("contentExtern", isContentExtern);
        wb.attr("namingContSep", Character.toString(UINamingContainer.getSeparatorChar(fc)));
        wb.nativeAttr("regEx", eventRegEx);

        wb.append("},true);});");
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(final FacesContext fc, final UIComponent component) throws IOException {
        // nothing to do
    }
}
