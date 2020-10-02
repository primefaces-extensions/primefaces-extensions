/*
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
import org.primefaces.component.inputtext.InputText;
import org.primefaces.extensions.config.PrimeExtensionsEnvironment;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.Constants;
import org.primefaces.extensions.util.PhoneNumberUtilWrapper;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.util.ComponentUtils;
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
    private static final String HIDDEN_ID = "_iso2";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final InputPhone inputPhone = (InputPhone) component;

        if (!shouldDecode(inputPhone)) {
            return;
        }

        decodeBehaviors(context, inputPhone);

        final String inputId = inputPhone.getClientId(context) + Constants.SEP_INPUT;
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
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue) {
        final String value = (String) submittedValue;
        if (LangUtils.isValueBlank(value)) {
            return null;
        }

        final InputPhone inputPhone = (InputPhone) component;
        final Converter<?> converter = inputPhone.getConverter();

        if (converter != null) {
            return converter.getAsObject(context, inputPhone, value);
        }

        String country = context.getExternalContext().getRequestParameterMap().get(inputPhone.getClientId() + HIDDEN_ID);
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

    protected void encodeMarkup(final FacesContext context, final InputPhone inputPhone, final String valueToRender) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputPhone.getClientId(context);

        String styleClass = inputPhone.getStyleClass();
        styleClass = styleClass == null ? InputPhone.STYLE_CLASS : InputPhone.STYLE_CLASS + Constants.SPACE + styleClass;

        writer.startElement(Constants.ELEM_SPAN, inputPhone);
        writer.writeAttribute(Constants.ATTR_ID, clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, Constants.ATTR_STYLE_CLASS);

        if (inputPhone.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, inputPhone.getStyle(), Attrs.STYLE);
        }

        encodeInput(context, inputPhone, clientId, valueToRender);
        encodeHiddenInput(context, inputPhone, clientId);

        writer.endElement(Constants.ELEM_SPAN);
    }

    protected void encodeInput(final FacesContext context, final InputPhone inputPhone, final String clientId, final String valueToRender)
                throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + Constants.SEP_INPUT;
        final String inputStyle = inputPhone.getInputStyle();

        writer.startElement(Constants.ELEM_INPUT, null);
        writer.writeAttribute(Constants.ATTR_ID, inputId, null);
        writer.writeAttribute(Constants.ATTR_NAME, inputId, null);
        writer.writeAttribute(Constants.ATTR_TYPE, inputPhone.getType(), null);
        writer.writeAttribute(Constants.ATTR_VALUE, valueToRender, null);
        writer.writeAttribute(Attrs.CLASS, createStyleClass(inputPhone), Constants.ATTR_STYLE_CLASS);

        if (!isValueBlank(inputStyle)) {
            writer.writeAttribute(Attrs.STYLE, inputStyle, null);
        }

        renderAccessibilityAttributes(context, inputPhone);
        renderPassThruAttributes(context, inputPhone, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, inputPhone, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, inputPhone);

        writer.endElement(Constants.ELEM_INPUT);
    }

    protected void encodeHiddenInput(final FacesContext context, final InputPhone inputPhone, final String clientId)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement(Constants.ELEM_INPUT, null);
        writer.writeAttribute(Constants.ATTR_TYPE, Constants.TYPE_HIDDEN, null);
        writer.writeAttribute(Constants.ATTR_ID, clientId + HIDDEN_ID, null);
        writer.writeAttribute(Constants.ATTR_NAME, clientId + HIDDEN_ID, null);
        writer.writeAttribute(Constants.ATTR_VALUE, inputPhone.getInitialCountry(), null);
        writer.endElement(Constants.ELEM_INPUT);
    }

    protected void encodeScript(final FacesContext context, final InputPhone inputPhone) throws IOException {
        final String clientId = inputPhone.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtInputPhone", inputPhone.resolveWidgetVar(), clientId);
        wb.attr("allowDropdown", inputPhone.isAllowDropdown(), false);
        wb.attr("autoHideDialCode", inputPhone.isAutoHideDialCode(), false);
        wb.attr("autoPlaceholder", inputPhone.getAutoPlaceholder(), InputPhone.AutoPlaceholder.polite.name());
        encodeCountries(wb, "excludeCountries", inputPhone.getExcludeCountries());
        wb.attr("formatOnDisplay", inputPhone.isFormatOnDisplay(), false);
        if (!LangUtils.isValueBlank(inputPhone.getInitialCountry())) {
            wb.attr("initialCountry", inputPhone.getInitialCountry());
        }
        if (InputPhone.COUNTRY_AUTO.equals(inputPhone.getInitialCountry())) {
            if (inputPhone.getGeoIpLookup() == null) {
                throw new FacesException("InputPhone geoIpLookup property is required when initialCountry is 'auto'.");
            }
            wb.nativeAttr("geoIpLookup", inputPhone.getGeoIpLookup());
        }
        wb.attr("nationalMode", inputPhone.isNationalMode(), false);
        encodeCountries(wb, "onlyCountries", inputPhone.getOnlyCountries());
        if (inputPhone.getPlaceholderNumberTypeEnum() != InputPhone.PlaceholderNumberType.mobile) {
            wb.attr("placeholderNumberType", inputPhone.getPlaceholderNumberType().toUpperCase());
        }
        encodeCountries(wb, "preferredCountries", inputPhone.getPreferredCountries());
        wb.attr("separateDialCode", inputPhone.isSeparateDialCode(), true);
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

    protected String createStyleClass(final InputPhone inputText) {
        String defaultClass = InputText.STYLE_CLASS;
        defaultClass = inputText.isValid() ? defaultClass : defaultClass + " ui-state-error";
        defaultClass = !inputText.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";

        String styleClass = inputText.getInputStyleClass();
        styleClass = styleClass == null ? defaultClass : defaultClass + Constants.SPACE + styleClass;

        return styleClass;
    }

    private void encodeCountries(final WidgetBuilder wb, final String attribute, final Object value) throws IOException {
        final Collection<String> countries = toCollection(value);
        if (!countries.isEmpty()) {
            wb.nativeAttr(attribute, new JSONArray(countries).toString());
        }
    }

    private Collection<String> toCollection(final Object object) {
        if (object instanceof String) {
            final String string = ((String) object).replace(' ', ',').toLowerCase();
            return Arrays.asList(string.split(","));
        }
        else {
            return (Collection<String>) object;
        }
    }

}
