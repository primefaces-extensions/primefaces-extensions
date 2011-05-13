/*
 * Copyright 2011 Thomas Andraschko.
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
package org.primefaces.extensions.component.imagerotateandresize;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;

public class ImageRotateAndResizeRenderer extends CoreRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		super.decodeBehaviors(context, component);
	}

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ImageRotateAndResize imageRotateAndResize = (ImageRotateAndResize) component;
        String target = findTarget(context, imageRotateAndResize).getClientId(context);
        String clientId = imageRotateAndResize.getClientId(context);
        String widgetVar = imageRotateAndResize.resolveWidgetVar();

        writer.startElement("script", imageRotateAndResize);
        writer.writeAttribute("type", "text/javascript", null);

        writer.write("$(function() {");

        writer.write(widgetVar + " = new PrimeFaces.Extensions.widget.ImageRotateAndResize('" + clientId + "', {");
        writer.write("target:'" + target + "'");

        encodeClientBehaviors(context, imageRotateAndResize);
        
        writer.write("});});");
        writer.endElement("script");
    }

    protected UIComponent findTarget(FacesContext facesContext, ImageRotateAndResize imageRotate) {
        String _for = imageRotate.getFor();

        if(_for != null) {
            UIComponent component = imageRotate.findComponent(_for);
            if (component == null) {
                throw new FacesException("Cannot find component \"" + _for + "\" in view.");
            } else {
                return component;
            }
        } else {
            throw new FacesException("\"for\" attribute for ImageRotateAndResize can not be null or empty");
        }
    }
}
