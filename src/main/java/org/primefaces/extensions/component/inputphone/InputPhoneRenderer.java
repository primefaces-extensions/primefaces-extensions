/**
 * Copyright 2011-2020 PrimeFaces Extensions
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
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

import org.primefaces.extensions.config.PrimeExtensionsEnvironment;
import org.primefaces.extensions.util.PhoneNumberUtilWrapper;

/**
 * Renderer for the {@link InputPhone} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
public class InputPhoneRenderer extends InputRenderer {

    private static final Logger LOGGER = Logger.getLogger(InputPhoneRenderer.class.getName());

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final InputPhone inputPhone = (InputPhone) component;

        if (!shouldDecode(inputPhone)) {
            return;
        }

        decodeBehaviors(context, inputPhone);

        final String inputId = inputPhone.getClientId(context) + "_input";
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

        if (submittedValue != null) {
            inputPhone.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        final InputPhone inputPhone = (InputPhone) component;

        final Object value = inputPhone.getValue();
        String valueToRender = ComponentUtils.getValueToRender(context, inputPhone, value);
        if (valueToRender == null) {
            valueToRender = Constants.EMPTY_STRING;
        }

        encodeMarkup(context, inputPhone, valueToRender);
        encodeScript(context, inputPhone);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        final String value = (String) submittedValue;
        if (LangUtils.isValueBlank(value)) {
            return null;
        }

        final InputPhone inputPhone = (InputPhone) component;
        final Converter converter = inputPhone.getConverter();

        if (converter != null) {
            return converter.getAsObject(context, inputPhone, value);
        }

        String country = context.getExternalContext().getRequestParameterMap().get(inputPhone.getClientId() + "_iso2");
        if (country == null || InputPhone.COUNTRY_AUTO.equals(country)) {
            country = Constants.EMPTY_STRING;
        }
        if (PrimeExtensionsEnvironment.getCurrentInstance(context).isLibphonenumberAvailable()) {
            PhoneNumberUtilWrapper.validate(value, country.toUpperCase());
        }
        else {
            LOGGER.warning("Libphonenumber not available, unable to validate!");
        }
        return value;
    }

    protected void encodeMarkup(FacesContext context, InputPhone inputPhone, String valueToRender) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputPhone.getClientId(context);

        String styleClass = inputPhone.getStyleClass();
        styleClass = styleClass == null ? InputPhone.STYLE_CLASS : InputPhone.STYLE_CLASS + " " + styleClass;

        writer.startElement("span", inputPhone);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");

        if (inputPhone.getStyle() != null) {
            writer.writeAttribute("style", inputPhone.getStyle(), "style");
        }

        encodeInput(context, inputPhone, clientId, valueToRender);
        encodeHiddenInput(context, inputPhone, clientId);

        writer.endElement("span");
    }

    protected void encodeInput(FacesContext context, InputPhone inputPhone, String clientId, String valueToRender)
                throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + "_input";

        final String inputStyle = inputPhone.getInputStyle();
        final String inputStyleClass = inputPhone.getInputStyleClass();

        String styleClass = InputText.STYLE_CLASS;
        styleClass = inputPhone.isValid() ? styleClass : styleClass + " ui-state-error";
        styleClass = !inputPhone.isDisabled() ? styleClass : styleClass + " ui-state-disabled";
        if (!isValueBlank(inputStyleClass)) {
            styleClass += " " + inputStyleClass;
        }

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", inputPhone.getType(), null);
        writer.writeAttribute("value", valueToRender, null);

        if (!isValueBlank(inputStyle)) {
            writer.writeAttribute("style", inputStyle, null);
        }

        writer.writeAttribute("class", styleClass, null);

        renderAccessibilityAttributes(context, inputPhone);
        renderPassThruAttributes(context, inputPhone, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputPhone, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, inputPhone);

        writer.endElement("input");
    }

    protected void encodeHiddenInput(FacesContext context, InputPhone inputPhone, String clientId)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId + "_iso2", null);
        writer.writeAttribute("name", clientId + "_iso2", null);
        writer.writeAttribute("value", inputPhone.getInitialCountry(), null);
        writer.endElement("input");
    }

    protected void encodeScript(FacesContext context, InputPhone inputPhone) throws IOException {
        final String clientId = inputPhone.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPhone", inputPhone.resolveWidgetVar(), clientId);
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
        if (InputPhone.COUNTRY_AUTO.equals(inputPhone.getInitialCountry())) {
            if (inputPhone.getGeoIpLookup() == null) {
                throw new FacesException("InputPhone geoIpLookup property is required when initialCountry is 'auto'.");
            }
            wb.nativeAttr("geoIpLookup", inputPhone.getGeoIpLookup());
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
        final Collection<String> countries = toCollection(value);
        if (countries != null && !countries.isEmpty()) {
            wb.nativeAttr(attribute, new JSONArray(countries).toString());
        }
    }

    private Collection<String> toCollection(Object object) {
        if (String.class.isInstance(object)) {
            final String string = ((String) object).replaceAll(" ", ",").toLowerCase();
            return Arrays.asList(string.split(","));
        }
        return (Collection<String>) object;
    }

}
