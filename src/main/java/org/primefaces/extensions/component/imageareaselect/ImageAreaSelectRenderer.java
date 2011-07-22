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
 */
package org.primefaces.extensions.component.imageareaselect;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;

public class ImageAreaSelectRenderer extends CoreRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		decodeBehaviors(context, component);
	}

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ImageAreaSelect imageAreaSelect = (ImageAreaSelect) component;
        String target = findTarget(context, imageAreaSelect).getClientId(context);
        String clientId = imageAreaSelect.getClientId(context);
        String widgetVar = imageAreaSelect.resolveWidgetVar();

        writer.startElement("script", imageAreaSelect);
        writer.writeAttribute("type", "text/javascript", null);

        writer.write("$(function() {");

        writer.write(widgetVar + " = new PrimeFaces.Extensions.widget.ImageAreaSelect('" + clientId + "', {");
        writer.write("target:'" + target + "'");

        if (imageAreaSelect.getAspectRatio() != null)
        	writer.write(",aspectRatio:'" + imageAreaSelect.getAspectRatio() + "'");
        if (imageAreaSelect.isAutoHide() != null)
        	writer.write(",autoHide:" + imageAreaSelect.isAutoHide());
        if (imageAreaSelect.getFadeSpeed() != null)
        	writer.write(",fadeSpeed:" + imageAreaSelect.getFadeSpeed());
        if (imageAreaSelect.isHandles() != null)
        	writer.write(",handles:" + imageAreaSelect.isHandles());
        if (imageAreaSelect.isHide() != null)
        	writer.write(",hide:" + imageAreaSelect.isHide());
        if (imageAreaSelect.getImageHeight() != null)
        	writer.write(",imageHeight:" + imageAreaSelect.getImageHeight());
        if (imageAreaSelect.getImageWidth() != null)
        	writer.write(",imageWidth:" + imageAreaSelect.getImageWidth());
        if (imageAreaSelect.isMovable() != null)
        	writer.write(",movable:" + imageAreaSelect.isMovable());
        if (imageAreaSelect.isPersistent() != null)
        	writer.write(",persistent:" + imageAreaSelect.isPersistent());
        if (imageAreaSelect.isResizable() != null)
        	writer.write(",resizable:" + imageAreaSelect.isResizable());
        if (imageAreaSelect.isShow() != null)
        	writer.write(",show:" + imageAreaSelect.isShow());
        if (imageAreaSelect.getImageAreaSelectParent() != null)
        	writer.write(",parent:'" + findParent(context, imageAreaSelect).getClientId(context) + "'");
        if (imageAreaSelect.isKeyboardSupport() != null)
        	writer.write(",keyboardSupport:" + imageAreaSelect.isKeyboardSupport());

        encodeClientBehaviors(context, imageAreaSelect);

        writer.write("});});");
        writer.endElement("script");
    }

    protected UIComponent findTarget(FacesContext facesContext, ImageAreaSelect imageAreaSelect) {
        String _for = imageAreaSelect.getFor();

        if (_for != null) {
            UIComponent component = imageAreaSelect.findComponent(_for);
            if (component == null) {
                throw new FacesException("Cannot find component \"" + _for + "\" in view.");
            } else {
                return component;
            }
        } else {
            throw new FacesException("\"for\" attribute for ImageAreaSelect can not be null or empty");
        }
    }

    protected UIComponent findParent(FacesContext facesContext, ImageAreaSelect imageAreaSelect) {
        String parentId = imageAreaSelect.getImageAreaSelectParent();
        UIComponent component = imageAreaSelect.findComponent(parentId);
        if (component == null) {
            throw new FacesException("Cannot find component \"" + parentId + "\" in view.");
        } else {
            return component;
        }
    }
}
