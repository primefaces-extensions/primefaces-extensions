/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.component.keyfilter.KeyFilter;
import org.primefaces.renderkit.CoreRenderer;

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
		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("div", switchComponent);
		writer.writeAttribute("id", switchComponent.getClientId(context), null);

		DefaultCase defaultCase = null;
		boolean caseMatched = false;

		for (UIComponent child : switchComponent.getChildren()) {
			if (child instanceof Case) {
				final Case caseComponent = (Case) child;

				if ((caseComponent.getValue() == null && switchComponent.getValue() != null)
						|| (switchComponent.getValue() == null && caseComponent.getValue() != null)) {
					continue;
				}

				if (caseComponent.getValue() == null && switchComponent.getValue() == null) {
					renderChildren(context, caseComponent);
					caseMatched = true;
					break;

				} else if (caseComponent.getValue().equals(switchComponent.getValue())) {
					renderChildren(context, caseComponent);
					caseMatched = true;
					break;
				}
			} else if (child instanceof DefaultCase) {
				defaultCase = (DefaultCase) child;
			} else {
				throw new FacesException("The switch component accepts only case and defaultCase as children.");
			}	
		}

		if (!caseMatched && defaultCase != null) {
			renderChildren(context, defaultCase);
		}

		writer.endElement("div");
	}
}
