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
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.primefaces.component.inputmask.InputMask;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * InputNumberRenderer
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class InputNumberRenderer extends InputRenderer {

        @Override
        public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
                throws ConverterException {
                
                InputNumber inputNumber = (InputNumber) component;
                Converter converter = inputNumber.getConverter();
                String submittedValueString = (String) submittedValue;
                Double doubleSubmited = Double.valueOf(submittedValueString);
                if (converter != null) {
                                //todo convert
                                return doubleSubmited;
                } else {
                                return doubleSubmited;
                }
        }

        @Override
        public void decode(FacesContext context, UIComponent component) {
                InputNumber inputNumber = (InputNumber) component;

                if (inputNumber.isDisabled() || inputNumber.isReadonly()) {
                        return;
                }

                decodeBehaviors(context, inputNumber);

                String inputId = inputNumber.getClientId(context) + "_input";
                String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

                if (submittedValue != null) {
                        inputNumber.setSubmittedValue(submittedValue);
                }
               
        }

        @Override
        public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
                InputNumber inputNumber = (InputNumber) component;
                encodeMarkup(context, inputNumber);
                encodeScript(context, inputNumber);
        }

        protected void encodeMarkup(FacesContext context, InputNumber inputNumber) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                String clientId = inputNumber.getClientId(context);
                boolean disabled = inputNumber.isDisabled();

                String style = inputNumber.getStyle();
                String styleClass = inputNumber.getStyleClass();
                //todo ver meter aca clases para esto
                styleClass = styleClass == null ? HTML.CHECKBOX_CLASS : HTML.CHECKBOX_CLASS + " " + styleClass;

                writer.startElement("div", inputNumber);
                //writer.writeAttribute("id", clientId, "id");
                writer.writeAttribute("class", styleClass, "styleClass");
                if (style != null) {
                        writer.writeAttribute("style", style, "style");
                }

                encodeInput(context, inputNumber, clientId, disabled);
                encodeOutput(context, inputNumber, clientId, disabled);
                
                writer.endElement("div");
        }

        protected void encodeInput(final FacesContext context, final InputNumber inputNumber, final String clientId, final boolean disabled) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                String inputId = clientId + "_input";

                writer.startElement("div", inputNumber);
                //todo ver esto de meter otra clase.
                writer.writeAttribute("class", HTML.CHECKBOX_INPUT_WRAPPER_CLASS, null);

                writer.startElement("input", null);
                writer.writeAttribute("id", inputId, "id");
                writer.writeAttribute("name", inputId, null);

                if (disabled) {
                        writer.writeAttribute("disabled", "disabled", null);
                }

                if (inputNumber.getOnchange() != null) {
                        writer.writeAttribute("onchange", inputNumber.getOnchange(), null);
                }

                writer.endElement("input");
                writer.endElement("div");
        }

        protected void encodeOutput(final FacesContext context, final InputNumber inputNumber, final String clientId, final boolean disabled) throws IOException {

                ResponseWriter writer = context.getResponseWriter();
                String styleClass = inputNumber.getStyleClass();
                String defaultClass = InputMask.STYLE_CLASS;
                defaultClass = !inputNumber.isValid() ? defaultClass + " ui-state-error" : defaultClass;
                defaultClass = inputNumber.isDisabled() ? defaultClass + " ui-state-disabled" : defaultClass;
                styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

                writer.startElement("input", null);
                writer.writeAttribute("id", clientId, null);
                writer.writeAttribute("name", clientId, null);
                writer.writeAttribute("type", "text", null);


                renderPassThruAttributes(context, inputNumber, HTML.INPUT_TEXT_ATTRS);

                if (inputNumber.isDisabled()) {
                        writer.writeAttribute("disabled", "disabled", "disabled");
                }
                if (inputNumber.isReadonly()) {
                        writer.writeAttribute("readonly", "readonly", "readonly");
                }
                if (inputNumber.getStyle() != null) {
                        writer.writeAttribute("style", inputNumber.getStyle(), "style");
                }

                writer.writeAttribute("class", styleClass, "");

                writer.endElement("input");
        }

        protected void encodeScript(final FacesContext context, final InputNumber inputNumber) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                String clientId = inputNumber.getClientId(context);
                startScript(writer, clientId);
                String valueToRender = ComponentUtils.getValueToRender(context, inputNumber);
                if (valueToRender == null) {
                        valueToRender = "";
                }

                writer.write("$(function() {");
                writer.write("PrimeFacesExt.cw('InputNumber','" + inputNumber.resolveWidgetVar() + "',{");
                writer.write("id:'" + clientId + "'");
                writer.write(",valueToRender:'" + valueToRender + "'");

                String metaOptions = getOptions(inputNumber);
                if (!metaOptions.isEmpty()) {
                        writer.write(",pluginOptions:" + metaOptions);
                }
                encodeClientBehaviors(context, inputNumber);
                writer.write("});});");

                endScript(writer);
        }

        private String getOptions(InputNumber inputNumber) {

                String decimalSeparator = inputNumber.getDecimalSeparator();
                String thousandSeparator = inputNumber.getThousandSeparator();
                String symbol = inputNumber.getSymbol();
                String symbolPosition = inputNumber.getSymbolPosition();
                String minValue = inputNumber.getMinValue();
                String maxValue = inputNumber.getMaxValue();
                String roundMethod = inputNumber.getRoundMethod();
                String decimalPlaces = inputNumber.getDecimalPlaces();

                String options = "";
                options += decimalSeparator.isEmpty() ? "" : "aDec: '" + decimalSeparator + "',";
                options += thousandSeparator.isEmpty() ? "" : "aSep: '" + thousandSeparator + "',";
                options += symbol.isEmpty() ? "" : "aSign: '" + symbol + "',";
                options += symbolPosition.isEmpty() ? "" : "pSign: '" + symbolPosition + "',";
                options += minValue.isEmpty() ? "" : "vMin: '" + minValue + "',";
                options += maxValue.isEmpty() ? "" : "vMax: '" + maxValue + "',";
                options += roundMethod.isEmpty() ? "" : "mRound: '" + roundMethod + "',";
                options += decimalPlaces.isEmpty() ? "" : "mDec: '" + decimalPlaces + "',";

                
                //if all options are empty return empty
                if (options.isEmpty()){
                        return "";
                }
                
                //delete the last comma
                int lastInd = options.length() - 1;
                if (options.charAt(lastInd) == ',') {
                        options = options.substring(0, lastInd);
                }
                return "{" + options + "}";
                
        }
}
