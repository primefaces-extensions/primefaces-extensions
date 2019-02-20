/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.component.intltelinput;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
public class InputPhoneRenderer extends InputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        InputPhone inputPhone = (InputPhone) component;

        if (!shouldDecode(inputPhone)) {
            return;
        }

        decodeBehaviors(context, inputPhone);

        String clientId = inputPhone.getClientId(context);
        String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId);

        if (submittedValue != null) {
            inputPhone.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        InputPhone inputPhone = (InputPhone) component;

        encodeMarkup(context, inputPhone);
        encodeScript(context, inputPhone);
    }

    protected void encodeScript(FacesContext context, InputPhone inputPhone) throws IOException {
        String clientId = inputPhone.getClientId(context);

        //WidgetBuilder wb = getWidgetBuilder(context);
        //wb.init("InputPhone", inputPhone.resolveWidgetVar(), clientId);
        // TODO handle tag attributes
        //wb.finish();
    }

    protected void encodeMarkup(FacesContext context, InputPhone inputPhone) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = inputPhone.getClientId(context);
        String styleClass = inputPhone.getStyleClass();
        String defaultClass = InputPhone.STYLE_CLASS;
        defaultClass = !inputPhone.isValid() ? defaultClass + " ui-state-error" : defaultClass;
        defaultClass = inputPhone.isDisabled() ? defaultClass + " ui-state-disabled" : defaultClass;
        styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

        writer.startElement("input", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", inputPhone.getType(), "text");

        String valueToRender = ComponentUtils.getValueToRender(context, inputPhone);
        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
        }

        renderAccessibilityAttributes(context, inputPhone);
        renderPassThruAttributes(context, inputPhone, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputPhone, HTML.INPUT_TEXT_EVENTS);

        if (inputPhone.getStyle() != null) {
            writer.writeAttribute("style", inputPhone.getStyle(), "style");
        }

        writer.writeAttribute("class", styleClass, "styleClass");

        renderValidationMetadata(context, inputPhone);

        writer.endElement("input");
    }

}
