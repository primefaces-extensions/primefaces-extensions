/*
 * Copyright 2011 PrimeFaces Extensions.
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

import org.primefaces.extensions.component.tooltip.*;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.extensions.renderkit.widget.WidgetRenderer;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Tooltip} component.
 *
 * @author  Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since   1.2.0
 */
public class QRCodeRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		QRCode qrCode = (QRCode) component;
                encodeMarkup(context, qrCode);
                encodeScript(context,qrCode);		
	}
        
        protected void encodeScript(final FacesContext context, final QRCode qrCode) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = qrCode.getClientId(context);                
		startScript(writer, clientId);
		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('QRCode', '" + qrCode.resolveWidgetVar() + "',{"); 
                WidgetRenderer.renderOptions(clientId, writer, qrCode);
		writer.write("});});");
		endScript(writer);
	}
        
        private void encodeMarkup(FacesContext context, QRCode qrCode) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
		String clientId = qrCode.getClientId(context);                               
		writer.startElement("span", null);
                writer.writeAttribute("id", clientId, null);                                
		writer.endElement("span");
        }

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		//do nothing
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
        
}
