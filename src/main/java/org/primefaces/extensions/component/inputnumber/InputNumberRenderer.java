/*
 * Copyright 2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.inputnumber;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.inputmask.InputMask;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * InputNumberRenderer 
 *
 * @author  Mauricio Fenoglio / last modified by $Author:$
 * @version $Revision:$
 * @since   0.3
 */
public class InputNumberRenderer extends InputRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {
	/*	InputMask inputMask = (InputMask) component;

        if(inputMask.isDisabled() || inputMask.isReadonly()) {
            return;
        }

        decodeBehaviors(context, inputMask);

		String clientId = inputMask.getClientId(context);
		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

        if(submittedValue != null) {
            inputMask.setSubmittedValue(submittedValue);
        }*/
	}
	
        
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		InputNumber inputNumber = (InputNumber) component;
		
		encodeMarkup(context, inputNumber);
		//encodeScript(context, inputMask);
	}
	
	/*protected void encodeScript(FacesContext context, InputMask inputMask) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = inputMask.getClientId(context);
        String mask = inputMask.getMask();
		
        startScript(writer, clientId);

        writer.write("PrimeFaces.cw('InputMask','" + inputMask.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        
        if(mask != null) {
            writer.write(",mask:'" + inputMask.getMask() + "'");
            
            if(inputMask.getPlaceHolder()!=null)
                writer.write(",placeholder:'" + inputMask.getPlaceHolder() + "'");
        }

        encodeClientBehaviors(context, inputMask);

		writer.write("});");
	
		endScript(writer);
	}
	*/
	protected void encodeMarkup(FacesContext context, InputNumber inputNumber) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = inputNumber.getClientId(context);
                String styleClass = inputNumber.getStyleClass();
                String defaultClass = InputMask.STYLE_CLASS;
                defaultClass = !inputNumber.isValid() ? defaultClass + " ui-state-error" : defaultClass;
                defaultClass = inputNumber.isDisabled() ? defaultClass + " ui-state-disabled" : defaultClass;
                styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

                        writer.startElement("input", null);
                        writer.writeAttribute("id", clientId, null);
                        writer.writeAttribute("name", clientId, null);
                        writer.writeAttribute("type", "text", null);

                        String valueToRender = ComponentUtils.getValueToRender(context, inputNumber);
                        if(valueToRender != null) {
                                writer.writeAttribute("value", valueToRender , null);
                        }

                        renderPassThruAttributes(context, inputNumber, HTML.INPUT_TEXT_ATTRS);

                if(inputNumber.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
                if(inputNumber.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
                if(inputNumber.getStyle() != null) writer.writeAttribute("style", inputNumber.getStyle(), "style");

                writer.writeAttribute("class", "dummyTest", "");

                writer.endElement("input");
	}
       
}
