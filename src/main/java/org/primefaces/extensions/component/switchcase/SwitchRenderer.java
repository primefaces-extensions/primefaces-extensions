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
 * $Id: $
 */

package org.primefaces.extensions.component.switchcase;

import org.primefaces.renderkit.CoreRenderer;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Renderer for the {@link Switch} component.
 *
 * @author Michael Gmeiner / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 */
public class SwitchRenderer extends CoreRenderer {
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		final Switch switchComponent = (Switch) component;

		DefaultCase caseToRender = null;
		DefaultCase defaultCase = null;

		for (UIComponent child : switchComponent.getChildren()) {

			child.setRendered(false);

			if (child instanceof Case) {
				final Case caseComponent = (Case) child;

				if ((caseComponent.getValue() == null && switchComponent.getValue() != null)
						|| (switchComponent.getValue() == null && caseComponent.getValue() != null)) {
					continue;
				}

				if ((caseComponent.getValue() == null && switchComponent.getValue() == null)
						|| caseComponent.getValue().equals(switchComponent.getValue())) {

					caseToRender = caseComponent;
					if (caseToRender != null || defaultCase != null) {
						break;
					}
				}

			} else if (child instanceof DefaultCase) {
				defaultCase = (DefaultCase) child;
			} else {
				throw new FacesException("Switch only accepts case or defaultCase as children.");
			}	
		}

		if (caseToRender == null) {
			caseToRender = defaultCase;
		}
		
		if (caseToRender != null) {
			final ResponseWriter writer = context.getResponseWriter();
			writer.startElement("div", switchComponent);
			writer.writeAttribute("id", switchComponent.getClientId(context), null);
			
			if (caseToRender.getStyle() != null) {
				writer.writeAttribute("style", caseToRender.getStyle(), null);
			}
			
			if (caseToRender.getStyleClass() != null) {
				writer.writeAttribute("class", caseToRender.getStyleClass(), null);
			}
			
			caseToRender.setRendered(true);
			renderChildren(context, caseToRender);
			
			writer.endElement("div");
		}
	}

    @Override
	public boolean getRendersChildren() {
		return true;
	}

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
    	//Do Nothing
    }
}
