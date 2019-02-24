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
package org.primefaces.extensions.component.inputphone;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.json.JSONArray;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

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

        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPhone", inputPhone.resolveWidgetVar(), clientId);
        wb.attr("target", clientId);
        if (!inputPhone.isAllowDropdown()) {
            wb.attr("allowDropdown", inputPhone.isAllowDropdown());
        }
        if (!inputPhone.isAutoHideDialCode()) {
            wb.attr("autoHideDialCode", inputPhone.isAutoHideDialCode());
        }
        if (inputPhone.getAutoPlaceholderEnum() != InputPhone.AutoPlaceholder.polite) {
            wb.attr("autoPlaceholder", inputPhone.getAutoPlaceholder());
        }
        encodeCountries(wb, "excludeCountries", inputPhone.getExcludeCountries());
        if (!inputPhone.isFormatOnDisplay()) {
            wb.attr("formatOnDisplay", inputPhone.isFormatOnDisplay());
        }
        if (StringUtils.isNotEmpty(inputPhone.getInitialCountry())) {
            wb.attr("initialCountry", inputPhone.getInitialCountry());
        }
        if (!inputPhone.isNationalMode()) {
            wb.attr("nationalMode", inputPhone.isNationalMode());
        }
        encodeCountries(wb, "onlyCountries", inputPhone.getOnlyCountries());
        if (inputPhone.getPlaceholderNumberTypeEnum() != InputPhone.PlaceholderNumberType.mobile) {
            wb.attr("placeholderNumberType", inputPhone.getPlaceholderNumberType().toUpperCase());
        }
        encodeCountries(wb, "preferredCountries", inputPhone.getPreferredCountries());
        if (inputPhone.isSeparateDialCode()) {
            wb.attr("separateDialCode", inputPhone.isSeparateDialCode());
        }
        if (inputPhone.isUtilsScriptRequired()) {
            wb.attr("utilsScript",
                    context.getApplication()
                            .getResourceHandler()
                            .createResource("inputphone/utils.js", "primefaces-extensions")
                            .getRequestPath());
        }

        encodeClientBehaviors(context, inputPhone);

        wb.finish();
    }

    private void encodeCountries(WidgetBuilder wb, String attribute, Object value) throws IOException {
        Collection<String> countries = toCollection(value);
        if (!countries.isEmpty()) {
            wb.nativeAttr(attribute, new JSONArray(countries).toString());
        }
    }

    private Collection<String> toCollection(Object object) {
        if (String.class.isInstance(object)) {
            String string = ((String) object).replaceAll(" ", "").toLowerCase();
            return Arrays.asList(string.split(","));
        }
        return (Collection<String>) object;
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
