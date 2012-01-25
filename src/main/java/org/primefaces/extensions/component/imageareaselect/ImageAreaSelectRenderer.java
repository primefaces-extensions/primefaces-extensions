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

package org.primefaces.extensions.component.imageareaselect;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link ImageAreaSelect} component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.1
 */
public class ImageAreaSelectRenderer extends CoreRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final ImageAreaSelect imageAreaSelect = (ImageAreaSelect) component;
		final String clientId = imageAreaSelect.getClientId(context);
		final String widgetVar = imageAreaSelect.resolveWidgetVar();
		final String target = ComponentUtils.findTarget(context, imageAreaSelect);

		writer.startElement("script", imageAreaSelect);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("type", "text/javascript", null);

		writer.write("$(function() {");

		writer.write("PrimeFacesExt.cw('ImageAreaSelect', '" + widgetVar + "', {");

		writer.write("id:'" + clientId + "'");
		writer.write(",target:'" + target + "'");

		if (imageAreaSelect.getAspectRatio() != null) {
			writer.write(",aspectRatio:'" + imageAreaSelect.getAspectRatio() + "'");
		}

		if (imageAreaSelect.isAutoHide() != null) {
			writer.write(",autoHide:" + imageAreaSelect.isAutoHide());
		}

		if (imageAreaSelect.getFadeSpeed() != null) {
			writer.write(",fadeSpeed:" + imageAreaSelect.getFadeSpeed());
		}

		if (imageAreaSelect.isHandles() != null) {
			writer.write(",handles:" + imageAreaSelect.isHandles());
		}

		if (imageAreaSelect.isHide() != null) {
			writer.write(",hide:" + imageAreaSelect.isHide());
		}

		if (imageAreaSelect.getImageHeight() != null) {
			writer.write(",imageHeight:" + imageAreaSelect.getImageHeight());
		}

		if (imageAreaSelect.getImageWidth() != null) {
			writer.write(",imageWidth:" + imageAreaSelect.getImageWidth());
		}

		if (imageAreaSelect.isMovable() != null) {
			writer.write(",movable:" + imageAreaSelect.isMovable());
		}

		if (imageAreaSelect.isPersistent() != null) {
			writer.write(",persistent:" + imageAreaSelect.isPersistent());
		}

		if (imageAreaSelect.isResizable() != null) {
			writer.write(",resizable:" + imageAreaSelect.isResizable());
		}

		if (imageAreaSelect.isShow() != null) {
			writer.write(",show:" + imageAreaSelect.isShow());
		}

		if (imageAreaSelect.getParentSelector() != null) {
			writer.write(",parent:'" + imageAreaSelect.getParentSelector() + "'");
		}

		if (imageAreaSelect.isKeyboardSupport() != null) {
			writer.write(",keyboardSupport:" + imageAreaSelect.isKeyboardSupport());
		}

		encodeClientBehaviors(context, imageAreaSelect);

		writer.write("}, true);});");
		writer.endElement("script");
	}
}
