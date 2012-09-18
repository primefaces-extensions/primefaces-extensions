package org.primefaces.extensions.component.switchcase;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;

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
