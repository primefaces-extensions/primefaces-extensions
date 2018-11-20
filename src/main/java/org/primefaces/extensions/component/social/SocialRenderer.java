/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.social;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.EscapeUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Social} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.2
 */
public class SocialRenderer extends CoreRenderer {

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
        final Social social = (Social) component;
        encodeMarkup(context, social);
        encodeScript(context, social);
    }

    /**
     * Create the HTML markup for the DOM.
     */
    private void encodeMarkup(final FacesContext context, final Social social) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = social.getClientId(context);
        final String widgetVar = social.resolveWidgetVar();
        final String styleClass = social.getTheme() + " " + StringUtils.defaultString(social.getStyleClass());

        writer.startElement("div", social);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (social.getStyle() != null) {
            writer.writeAttribute("style", social.getStyle(), "style");
        }
        writer.endElement("div");
    }

    /**
     * Create the Javascript.
     */
    private void encodeScript(final FacesContext context, final Social social) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        final String clientId = social.getClientId(context);
        wb.init("ExtSocial", social.resolveWidgetVar(), clientId);
        wb.attr("showLabel", social.isShowLabel());
        wb.attr("shareIn", social.getShareIn());
        if (!LangUtils.isValueBlank(social.getUrl())) {
            wb.attr("url", social.getUrl());
        }
        if (!LangUtils.isValueBlank(social.getText())) {
            wb.attr("text", social.getText());
        }

        final boolean showCount = BooleanUtils.toBoolean(social.getShowCount());
        if (showCount) {
            wb.attr("showCount", showCount);
        }
        else {
            if (social.getShowCount().equalsIgnoreCase("inside")) {
                wb.attr("showCount", social.getShowCount());
            }
            else {
                wb.attr("showCount", showCount);
            }
        }

        // shares array
        wb.append(",shares: [");
        final String[] shares = StringUtils.split(social.getShares(), ',');
        for (int i = 0; i < shares.length; i++) {
            // { share: "pinterest", media: "http://mysite.com" },
            final String share = StringUtils.lowerCase(shares[i]);
            if (LangUtils.isValueBlank(share)) {
                continue;
            }
            if (i != 0) {
                wb.append(",");
            }
            wb.append("{");
            addShareProperty(wb, "share", share);
            if (share.equalsIgnoreCase("twitter")) {
                wb.attr("via", social.getTwitterUsername());
                wb.attr("hashtags", social.getTwitterHashtags());
            }
            if (share.equalsIgnoreCase("email")) {
                wb.attr("to", social.getEmailTo());
            }
            if (share.equalsIgnoreCase("pinterest")) {
                wb.attr("media", social.getPinterestMedia());
            }
            wb.append("}");
        }
        wb.append("]");

        // javascript
        wb.append(",on: {");
        if (social.getOnclick() != null) {
            addCallback(wb, "click", "function(e)", social.getOnclick());
        }
        if (social.getOnmouseenter() != null) {
            addCallback(wb, "mouseenter", "function(e)", social.getOnmouseenter());
        }
        if (social.getOnmouseleave() != null) {
            addCallback(wb, "mouseleave", "function(e)", social.getOnmouseleave());
        }
        wb.append("}");

        encodeClientBehaviors(context, social);

        wb.finish();
    }

    private void addShareProperty(final WidgetBuilder wb, final String property, final String value) throws IOException {
        if (value != null) {
            wb.append(property);
            wb.append(":\"");
            wb.append(EscapeUtils.forJavaScriptAttribute(value));
            wb.append("\"");
        }
    }

    public void addCallback(final WidgetBuilder wb, final String name, final String signature, final String callback) throws IOException {
        if (callback != null) {
            wb.append(name);
            wb.append(":");
            wb.append(signature);
            wb.append("{");
            wb.append(callback);
            wb.append("},");
        }
    }

}
