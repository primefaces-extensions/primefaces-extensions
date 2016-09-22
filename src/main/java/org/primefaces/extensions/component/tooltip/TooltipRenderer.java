/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.tooltip;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;

/**
 * Renderer for the {@link Tooltip} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
public class TooltipRenderer extends CoreRenderer {

   @Override
   public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
      final ResponseWriter writer = context.getResponseWriter();
      final Tooltip tooltip = (Tooltip) component;
      final String clientId = tooltip.getClientId(context);
      final String widgetVar = tooltip.resolveWidgetVar();
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

      startScript(writer, clientId);
      writer.write("$(function() {");

      writer.write("PrimeFaces.cw('ExtTooltip', '" + widgetVar + "',{");
      writer.write("id:'" + clientId + "'");
      writer.write(",widgetVar:'" + widgetVar + "'");
      writer.write(",global:" + global);
      writer.write(",shared:" + shared);
      writer.write(",autoShow:" + autoShow);

      if (target == null) {
         writer.write(",forTarget:null");
      } else {
         writer.write(",forTarget:'" + target + "'");
      }

      // content
      writer.write(",content: {");
      String text = null;
      if (tooltip.getChildCount() > 0) {
         final FastStringWriter fsw = new FastStringWriter();
         final ResponseWriter clonedWriter = writer.cloneWithWriter(fsw);
         context.setResponseWriter(clonedWriter);
         renderChildren(context, tooltip);
         context.setResponseWriter(writer);
         text = fsw.toString();
      } else {
         final String valueToRender = ComponentUtils.getValueToRender(context, tooltip);
         if (valueToRender != null) {
            text = valueToRender;
         }
      }

      final boolean hasText = !global && StringUtils.isNotBlank(text);
      if (hasText) {
         writer.write("text: \"" + escapeText(text) + "\"");
      }

      if (StringUtils.isNotBlank(header)) {
         String headerValue = "";
         if (hasText) {
            headerValue = ",";
         }
         headerValue = headerValue + "title: \"" + escapeText(header) + "\"";
         writer.write(headerValue);
      }
      writer.write("}");

      // style (if no class is set it will default to ThemeRoller widget=true)
      final boolean isStyled = StringUtils.isNotBlank(styleClass);
      writer.write(",style: {");
      writer.write("widget:" + !isStyled);
      if (isStyled) {
         writer.write(",classes:'" + styleClass + "'");
      }
      writer.write("}");

      // events
      if (mouseTracking) {
         writer.write(",hide:{fixed:true}");
      } else if (shared && !global) {
         writer.write(",show:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('"
                  + target + "')" + ",delay:"
                  + tooltip.getShowDelay() + ",effect:function(){$(this)." + tooltip.getShowEffect() + "("
                  + tooltip.getShowEffectLength() + ");}}");
         writer.write(",hide:{target:PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector('"
                  + target + "')" + ",delay:"
                  + tooltip.getHideDelay() + ",fixed:" + tooltip.isFixed() + ",effect:function(){$(this)."
                  + tooltip.getHideEffect() + "(" + tooltip.getHideEffectLength() + ");}}");
      } else if (autoShow) {
         writer.write(",show:{when:false,ready:true}");
         writer.write(",hide:false");
      } else {
         writer.write(",show:{event:'" + tooltip.getShowEvent() + "',delay:" + tooltip.getShowDelay()
                  + ",effect:function(){$(this)." + tooltip.getShowEffect() + "(" + tooltip.getShowEffectLength()
                  + ");}}");
         writer.write(",hide:{event:'" + tooltip.getHideEvent() + "',delay:" + tooltip.getHideDelay() + ",fixed:"
                  + tooltip.isFixed() + ",effect:function(){$(this)." + tooltip.getHideEffect() + "("
                  + tooltip.getHideEffectLength() + ");}}");
      }

      // position
      writer.write(",position: {");
      writer.write("at:'" + tooltip.getAtPosition() + "'");
      writer.write(",my:'" + tooltip.getMyPosition() + "'");
      writer.write(",adjust:{x:" + tooltip.getAdjustX() + ",y:" + tooltip.getAdjustY() + "}");
      writer.write(",viewport:$(window)");
      if (mouseTracking) {
         writer.write(",target:'mouse'");
      } else if (shared && !global) {
         writer.write(",target:'event'");
         writer.write(",effect:false");
      }

      writer.write("}},true);});");
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
