/*
 * Copyright 2011-2016 PrimeFaces Extensions
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
package org.primefaces.extensions.component.qrcode;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link QRCode} component.
 *
 * @author Mauricio Fenoglio / last modified by Melloware
 * @since 1.2.0
 */
public class QRCodeRenderer extends CoreRenderer {

   @Override
   public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
      final QRCode qrCode = (QRCode) component;
      encodeMarkup(context, qrCode);
      encodeScript(context, qrCode);
   }

   protected void encodeScript(final FacesContext context, final QRCode qrCode) throws IOException {
      final WidgetBuilder wb = getWidgetBuilder(context);
      wb.initWithDomReady("ExtQRCode", qrCode.resolveWidgetVar(), qrCode.getClientId());
      wb.attr("render", qrCode.getRenderMethod())
               .attr("mode", qrCode.getRenderMode())
               .attr("minVersion", qrCode.getMinVersion())
               .attr("maxVersion", qrCode.getMaxVersion())
               .attr("left", qrCode.getLeftOffset())
               .attr("top", qrCode.getTopOffset())
               .attr("size", qrCode.getSize())
               .attr("fill", qrCode.getFillColor())
               .attr("ecLevel", qrCode.getEcLevel())
               .attr("background", qrCode.getBackground())
               .attr("text", qrCode.getText())
               .attr("radius", qrCode.getRadius())
               .attr("quiet", qrCode.getQuiet())
               .attr("mSize", qrCode.getMSize())
               .attr("mPosX", qrCode.getMPosX())
               .attr("mPosY", qrCode.getMPosY())
               .attr("label", qrCode.getLabel())
               .attr("fontname", qrCode.getFontName())
               .attr("fontcolor", qrCode.getFontColor());

      wb.finish();
   }

   private void encodeMarkup(final FacesContext context, final QRCode qrCode) throws IOException {
      final ResponseWriter writer = context.getResponseWriter();
      final String clientId = qrCode.getClientId(context);
      writer.startElement("span", null);
      writer.writeAttribute("id", clientId, null);
      writer.endElement("span");
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
