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
package org.primefaces.extensions.component.tooltip;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Tooltip} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
public class TooltipRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Tooltip tooltip = (Tooltip) component;
        final String clientId = tooltip.getClientId(context);
        final String header = tooltip.getHeader();
        final String styleClass = tooltip.getStyleClass();
        final boolean global = tooltip.isGlobal();
        final boolean shared = tooltip.isShared();
        final boolean autoShow = tooltip.isAutoShow();
        final boolean mouseTracking = tooltip.isMouseTracking();
        String target = null;

        if (!global || tooltip.getFor() != null) {
            target = SearchExpressionFacade.resolveClientIds(context, tooltip, tooltip.getFor());
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("ExtTooltip", tooltip.resolveWidgetVar(), clientId);
        wb.attr("global", global);
        wb.attr("shared", shared);
        wb.attr("autoShow", autoShow);
        if (target == null) {
            wb.nativeAttr("forTarget", null);
        }
        else {
            wb.attr("forTarget", target);
        }

        final ResponseWriter writer = context.getResponseWriter();

        // content
        wb.append(",content: {");
        String text = null;
        if (tooltip.getChildCount() > 0) {
            final FastStringWriter fsw = new FastStringWriter();
            final ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
            context.setResponseWriter(clonedWriter);
            renderChildren(context, tooltip);
            context.setResponseWriter(writer);
            text = fsw.toString();
        }
        else {
            final String valueToRender = ComponentUtils.getValueToRender(context, tooltip);
            if (valueToRender != null) {
                text = valueToRender;
            }
        }

        final boolean hasText = !global && StringUtils.isNotBlank(text);
        if (hasText) {
            wb.append("text: \"" + escapeText(text) + "\"");
        }

        if (StringUtils.isNotBlank(header)) {
            String headerValue = "";
            if (hasText) {
                headerValue = ",";
            }
            headerValue = headerValue + "title: \"" + escapeText(header) + "\"";
            wb.append(headerValue);
        }
        wb.append("}");

        // style (if no class is set it will default to ThemeRoller widget=true)
        final boolean isStyled = StringUtils.isNotBlank(styleClass);
        wb.append(",style: {");
        wb.append("widget:" + !isStyled);
        if (isStyled) {
            wb.append(",classes:'" + styleClass + "'");
        }
        wb.append("}");

        // events
        if (mouseTracking) {
            wb.append(",hide:{fixed:true}");
        }
        else if (shared && !global) {
            wb.append(",show:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('"
                        + target + "')" + ",delay:"
                        + tooltip.getShowDelay() + ",effect:function(){$(this)." + tooltip.getShowEffect() + "("
                        + tooltip.getShowEffectLength() + ");}}");
            wb.append(",hide:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('"
                        + target + "')" + ",delay:"
                        + tooltip.getHideDelay() + ",fixed:" + tooltip.isFixed() + ",effect:function(){$(this)."
                        + tooltip.getHideEffect() + "(" + tooltip.getHideEffectLength() + ");}}");
        }
        else if (autoShow) {
            wb.append(",show:{when:false,ready:true}");
            wb.append(",hide:false");
        }
        else {
            wb.append(",show:{event:'" + tooltip.getShowEvent() + "',delay:" + tooltip.getShowDelay()
                        + ",effect:function(){$(this)." + tooltip.getShowEffect() + "(" + tooltip.getShowEffectLength()
                        + ");}}");
            wb.append(",hide:{event:'" + tooltip.getHideEvent() + "',delay:" + tooltip.getHideDelay() + ",fixed:"
                        + tooltip.isFixed() + ",effect:function(){$(this)." + tooltip.getHideEffect() + "("
                        + tooltip.getHideEffectLength() + ");}}");
        }

        // position
        wb.append(",position: {");
        wb.append("at:'" + tooltip.getAtPosition() + "'");
        wb.append(",my:'" + tooltip.getMyPosition() + "'");
        wb.append(",adjust:{x:" + tooltip.getAdjustX() + ",y:" + tooltip.getAdjustY() + "}");
        wb.append(",viewport:$(window)");
        if (mouseTracking) {
            wb.append(",target:'mouse'");
        }
        else if (shared && !global) {
            wb.append(",target:'event'");
            wb.append(",effect:false");
        }
        wb.append("}},true);});");
        endScript(writer);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
