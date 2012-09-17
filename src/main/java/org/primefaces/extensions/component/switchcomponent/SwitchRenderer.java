package org.primefaces.extensions.component.switchcomponent;

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

		DefaultCase defaultCase = null;
		boolean caseMatched = false;
	
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", switchComponent);
		writer.writeAttribute("id", switchComponent.getClientId(context), null);
		
		for (UIComponent child : switchComponent.getChildren()) {
			if (child instanceof Case) {
				final Case caseComponent = (Case) child;
				
				if (caseComponent.getValue() == null && switchComponent.getValue() == null) {
					renderChildren(context, caseComponent);
					caseMatched = true;
					break;
				} else if ((caseComponent.getValue() == null && switchComponent.getValue() != null) || (switchComponent.getValue() == null && caseComponent.getValue() != null)) {
					continue;
				} else if (caseComponent.getValue().equals(switchComponent.getValue())) {
					renderChildren(context, caseComponent);
					caseMatched = true;
					break;
				}
			} else if (child instanceof DefaultCase) {
				defaultCase = (DefaultCase) child;
			} else {
				throw new FacesException("The pe:switch component accepts only pe:case and pe:defaultCase children.");
			}	
		}
		
		if (!caseMatched && defaultCase != null) {
			renderChildren(context, defaultCase);
		}
		
		writer.endElement("div");
	}
}
