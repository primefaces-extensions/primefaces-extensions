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
package org.primefaces.extensions.component.tooltip;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.*;

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

        final ResponseWriter writer = context.getResponseWriter();
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

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtTooltip", tooltip);
        wb.attr("global", global);
        wb.attr("shared", shared);
        wb.attr("autoShow", autoShow);
        if (target == null) {
            wb.nativeAttr("forTarget", null);
        }
        else {
            wb.attr("forTarget", target);
        }

        // content
        wb.append(",content: {");

        final boolean hasText = !global && !LangUtils.isValueBlank(text);
        if (hasText) {
            wb.append("text: \"" + EscapeUtils.forJavaScript(text) + "\"");
        }

        if (!LangUtils.isValueBlank(header)) {
            String headerValue = Constants.EMPTY_STRING;
            if (hasText) {
                headerValue = ",";
            }
            headerValue = headerValue + "title: \"" + EscapeUtils.forJavaScript(header) + "\"";
            wb.append(headerValue);
        }
        wb.append("}");

        // style (if no class is set it will default to ThemeRoller widget=true)
        final boolean isStyled = !LangUtils.isValueBlank(styleClass);
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
        wb.append("}");

        wb.finish();
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) {
        // do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
