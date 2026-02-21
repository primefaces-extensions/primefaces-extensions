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
package org.primefaces.extensions.component.tooltip;

import java.io.IOException;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.EscapeUtils;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Tooltip} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesRenderer(rendererType = Tooltip.DEFAULT_RENDERER, componentFamily = Tooltip.COMPONENT_FAMILY)
public class TooltipRenderer extends CoreRenderer<Tooltip> {

    @Override
    public void encodeEnd(final FacesContext context, final Tooltip component) throws IOException {
        final String header = component.getHeader();
        final String styleClass = component.getStyleClass();
        final boolean global = component.isGlobal();
        final boolean shared = component.isShared();
        final boolean autoShow = component.isAutoShow();
        final boolean mouseTracking = component.isMouseTracking();
        String target = null;

        if (!global || component.getFor() != null) {
            target = SearchExpressionUtils.resolveClientIdsForClientSide(context, component, component.getFor());
        }

        final ResponseWriter writer = context.getResponseWriter();
        String text = null;
        if (component.getChildCount() > 0) {
            final FastStringWriter fsw = new FastStringWriter();
            final ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
            context.setResponseWriter(clonedWriter);
            renderChildren(context, component);
            context.setResponseWriter(writer);
            text = fsw.toString();
        }
        else {
            final String valueToRender = ComponentUtils.getValueToRender(context, component);
            if (valueToRender != null) {
                text = valueToRender;
            }
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtTooltip", component);
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

        final boolean hasText = !global && LangUtils.isNotBlank(text);
        if (hasText) {
            wb.append("text: \"" + EscapeUtils.forJavaScript(text) + "\"");
        }

        if (LangUtils.isNotBlank(header)) {
            String headerValue = Constants.EMPTY_STRING;
            if (hasText) {
                headerValue = ",";
            }
            headerValue = headerValue + "title: \"" + EscapeUtils.forJavaScript(header) + "\"";
            wb.append(headerValue);
        }
        wb.append("}");

        // style (if no class is set it will default to ThemeRoller widget=true)
        final boolean isStyled = LangUtils.isNotBlank(styleClass);
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
                        + component.getShowDelay() + ",effect:function(){$(this)." + component.getShowEffect() + "("
                        + component.getShowEffectLength() + ");}}");
            wb.append(",hide:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('"
                        + target + "')" + ",delay:"
                        + component.getHideDelay() + ",fixed:" + component.isFixed() + ",effect:function(){$(this)."
                        + component.getHideEffect() + "(" + component.getHideEffectLength() + ");}}");
        }
        else if (autoShow) {
            wb.append(",show:{when:false,ready:true}");
            wb.append(",hide:false");
        }
        else {
            wb.append(",show:{event:'" + component.getShowEvent() + "',delay:" + component.getShowDelay()
                        + ",effect:function(){$(this)." + component.getShowEffect() + "(" + component.getShowEffectLength()
                        + ");}}");
            wb.append(",hide:{event:'" + component.getHideEvent() + "',delay:" + component.getHideDelay() + ",fixed:"
                        + component.isFixed() + ",effect:function(){$(this)." + component.getHideEffect() + "("
                        + component.getHideEffectLength() + ");}}");
        }

        // position
        wb.append(",position: {");
        wb.append("at:'" + component.getAtPosition() + "'");
        wb.append(",my:'" + component.getMyPosition() + "'");
        wb.append(",adjust:{x:" + component.getAdjustX() + ",y:" + component.getAdjustY() + "}");
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
    public void encodeChildren(final FacesContext context, final Tooltip component) {
        // do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}