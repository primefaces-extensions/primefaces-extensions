/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.component.inputphone;

import static org.primefaces.extensions.component.inputphone.InputPhone.EVENT_COUNTRY_SELECT;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.extensions.config.PrimeExtensionsEnvironment;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.PhoneNumberUtilWrapper;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link InputPhone} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
public class InputPhoneRenderer extends InputRenderer {

    private static final Logger LOGGER = Logger.getLogger(InputPhoneRenderer.class.getName());
    private static final String HIDDEN_ID = "_hidden";
    private static final String ISO2_ID = "_iso2";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final InputPhone inputPhone = (InputPhone) component;

        if (!shouldDecode(inputPhone)) {
            return;
        }

        decodeBehaviors(context, inputPhone);

        final String inputId = inputPhone.getClientId(context) + HIDDEN_ID;
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

        if (submittedValue != null) {
            inputPhone.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
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
    public Object getConvertedValue(final FacesContext context, final UIComponent component,
                final Object submittedValue) {
        final String value = (String) submittedValue;
        if (LangUtils.isBlank(value)) {
            return null;
        }

        final InputPhone inputPhone = (InputPhone) component;
        final Converter<?> converter = inputPhone.getConverter();

        if (converter != null) {
            return converter.getAsObject(context, inputPhone, value);
        }

        String country = context.getExternalContext().getRequestParameterMap()
                    .get(inputPhone.getClientId() + ISO2_ID);
        if (country == null || InputPhone.COUNTRY_AUTO.equals(country)) {
            country = Constants.EMPTY_STRING;
        }
        else {
            inputPhone.setInitialCountry(country);
        }
        if (needsValidation(context)
                    && PrimeExtensionsEnvironment.getCurrentInstance(context).isLibphonenumberAvailable()) {
            PhoneNumberUtilWrapper.validate(value, country.toUpperCase(), inputPhone.getValidatorMessage());
        }
        else {
            LOGGER.warning("Libphonenumber not available, unable to validate!");
        }
        return value;
    }

    protected boolean needsValidation(final FacesContext context) {
        final String eventName = context.getExternalContext()
                    .getRequestParameterMap()
                    .get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        return !EVENT_COUNTRY_SELECT.equals(eventName);
    }

    protected void encodeMarkup(final FacesContext context, final InputPhone inputPhone, final String valueToRender)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputPhone.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(InputPhone.STYLE_CLASS)
                    .add(inputPhone.getStyleClass())
                    .build();

        writer.startElement("span", inputPhone);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");

        if (inputPhone.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, inputPhone.getStyle(), Attrs.STYLE);
        }

        renderRTLDirection(context, inputPhone);
        encodeInput(context, inputPhone, clientId, valueToRender);
        encodeHiddenInputs(context, inputPhone, clientId, valueToRender);

        writer.endElement("span");
    }

    protected void encodeInput(final FacesContext context, final InputPhone inputPhone, final String clientId,
                final String valueToRender)
                throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + InputPhone.INPUT_SUFFIX;
        final String inputStyle = inputPhone.getInputStyle();
        final String styleClass = createStyleClass(inputPhone, "inputStyleClass", InputText.STYLE_CLASS);

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", inputPhone.getType(), null);
        writer.writeAttribute("value", valueToRender, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "inputStyleClass");

        if (!isValueBlank(inputStyle)) {
            writer.writeAttribute(Attrs.STYLE, inputStyle, null);
        }

        renderAccessibilityAttributes(context, inputPhone);
        renderPassThruAttributes(context, inputPhone, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputPhone, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, inputPhone);

        writer.endElement("input");
    }

    protected void encodeHiddenInputs(final FacesContext context, final InputPhone inputPhone, final String clientId, final String valueToRender)
                throws IOException {
        renderHiddenInput(context, clientId + ISO2_ID, inputPhone.getInitialCountry(), inputPhone.isDisabled());
        renderHiddenInput(context, clientId + HIDDEN_ID, valueToRender, inputPhone.isDisabled());
    }

    protected void encodeScript(final FacesContext context, final InputPhone inputPhone) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPhone", inputPhone);
        encodeCountries(wb, "excludeCountries", inputPhone.getExcludeCountries());
        wb.attr("allowDropdown", inputPhone.isAllowDropdown(), true);
        wb.attr("autoHideDialCode", inputPhone.isAutoHideDialCode(), true);
        wb.attr("fixDropdownWidth", inputPhone.isFixDropdownWidth(), true);
        wb.attr("formatOnDisplay", inputPhone.isFormatOnDisplay(), true);
        wb.attr("formatAsYouType", inputPhone.isFormatAsYouType(), true);
        wb.attr("nationalMode", inputPhone.isNationalMode(), true);
        wb.attr("separateDialCode", inputPhone.isSeparateDialCode(), false);
        if (inputPhone.getAutoPlaceholderEnum() != InputPhone.AutoPlaceholder.polite) {
            wb.attr("autoPlaceholder", inputPhone.getAutoPlaceholder());
        }
        if (LangUtils.isNotBlank(inputPhone.getInitialCountry())) {
            wb.attr("initialCountry", inputPhone.getInitialCountry());
        }
        if (InputPhone.COUNTRY_AUTO.equals(inputPhone.getInitialCountry())) {
            if (inputPhone.getGeoIpLookup() == null) {
                throw new FacesException("InputPhone geoIpLookup property is required when initialCountry is 'auto'.");
            }
            wb.nativeAttr("geoIpLookup", inputPhone.getGeoIpLookup());
        }
        encodeCountries(wb, "onlyCountries", inputPhone.getOnlyCountries());
        if (inputPhone.getPlaceholderNumberTypeEnum() != InputPhone.PlaceholderNumberType.mobile) {
            wb.attr("placeholderNumberType", inputPhone.getPlaceholderNumberType().toUpperCase());
        }
        encodeCountries(wb, "countryOrder", inputPhone.getPreferredCountries());

        wb.attr("utilsScript",
                    context.getApplication()
                                .getResourceHandler()
                                .createResource("inputphone/utils.js", org.primefaces.extensions.util.Constants.LIBRARY)
                                .getRequestPath());
        if (inputPhone.getLocalizedCountries() != null) {
            wb.nativeAttr("i18n", objectToJsonString(inputPhone.getLocalizedCountries()));
        }

        encodeClientBehaviors(context, inputPhone);

        wb.finish();
    }

    private void encodeCountries(final WidgetBuilder wb, final String attribute, final Object value)
                throws IOException {
        final Collection<String> countries = toCollection(value);
        if (!countries.isEmpty()) {
            wb.nativeAttr(attribute, new JSONArray(countries).toString());
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<String> toCollection(final Object object) {
        if (object instanceof String) {
            final String string = ((String) object).replace(' ', ',').toLowerCase();
            return Arrays.asList(string.split(","));
        }
        return (Collection<String>) object;
    }

    @SuppressWarnings("unchecked")
    private String objectToJsonString(final Object object) {
        if (object == null || object instanceof String) {
            return (String) object;
        }
        Map<String, String> map = (Map<String, String>) object;
        JSONObject jsonObj = new JSONObject();
        map.forEach(jsonObj::put);
        return jsonObj.toString();
    }

}