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
package org.primefaces.extensions.component.tristatemanycheckbox;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.renderkit.SelectManyRenderer;
import org.primefaces.util.HTML;

/**
 * TriStateManyCheckboxRenderer
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class TriStateManyCheckboxRenderer extends SelectManyRenderer {

        @Override
        public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {

                //only convert the values of the maps
                if (submittedValue instanceof Map) {
                        Map<String, String> mapSub = (Map<String, String>) submittedValue;
                        List<String> mapValues = new ArrayList<String>(mapSub.values());

                        //TODO:convert value


                        //TODO:merge the converted and the map.


                        return mapSub;

                } else {
                        throw new FacesException("Value of '" + component.getClientId() + "'must be a Map instance");
                }
        }

        @Override
        public void decode(FacesContext context, UIComponent component) {
                if (!shouldDecode(component)) {
                        return;
                }
                UISelectMany checkbox = (TriStateManyCheckbox) component;
                Converter converter = getConverter(context, checkbox);

                decodeBehaviors(context, checkbox);

                String submitParam = getSubmitParam(context, checkbox);
                Map<String, String[]> params = context.getExternalContext().getRequestParameterValuesMap();

                String[] valuesArray = null;
                if (params.containsKey(submitParam)) {
                        valuesArray = params.get(submitParam);
                }
                List<SelectItem> selectItems = getSelectItems(context, checkbox);
                Map<String, String> subValues = new HashMap<String, String>();
                if (valuesArray != null && valuesArray.length == selectItems.size()) {
                        int idx = -1;
                        for (SelectItem item : selectItems) {
                                idx++;
                                String itemValueAsString = getOptionAsString(context, component, converter, item.getValue());
                                subValues.put(itemValueAsString, valuesArray[idx]);
                        }
                }
                checkbox.setSubmittedValue(subValues);
        }

        @Override
        public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
                TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
                encodeMarkup(context, checkbox);
                encodeScript(context, checkbox);
        }

        protected void encodeMarkup(FacesContext context, TriStateManyCheckbox checkbox) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                String clientId = checkbox.getClientId(context);
                String style = checkbox.getStyle();
                String styleClass = checkbox.getStyleClass();
                styleClass = styleClass == null ? SelectManyCheckbox.STYLE_CLASS : SelectManyCheckbox.STYLE_CLASS + " " + styleClass;

                writer.startElement("table", checkbox);
                writer.writeAttribute("id", clientId, "id");
                writer.writeAttribute("class", styleClass, "styleClass");
                if (style != null) {
                        writer.writeAttribute("style", style, "style");
                }

                encodeSelectItems(context, checkbox);

                writer.endElement("table");
        }

        protected void encodeSelectItems(FacesContext context, TriStateManyCheckbox checkbox) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                List<SelectItem> selectItems = getSelectItems(context, checkbox);
                Converter converter = getConverter(context, checkbox);
                Map values = getValues(checkbox);
                //todo change overwrite getSubmittedValue to support Map
                Object submittedValues = getSubmittedValues(checkbox);
                String layout = checkbox.getLayout();
                boolean pageDirection = layout != null && layout.equals("pageDirection");

                int idx = -1;
                for (SelectItem selectItem : selectItems) {
                        idx++;
                        if (pageDirection) {
                                writer.startElement("tr", null);
                        }

                        encodeOption(context, checkbox, values, submittedValues, converter, selectItem, idx);

                        if (pageDirection) {
                                writer.endElement("tr");
                        }
                }
        }

        protected void encodeOption(FacesContext context, UIInput component, Map values, Object submittedValues, Converter converter, SelectItem option, int idx) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
                String itemValueAsString = getOptionAsString(context, component, converter, option.getValue());
                String name = checkbox.getClientId(context);
                String id = name + UINamingContainer.getSeparatorChar(context) + idx;
                boolean disabled = option.isDisabled() || checkbox.isDisabled();

                Map valuesArray;
                Object itemValue;
                if (submittedValues != null) {
                        //TODO: FINISH THIS ADD CAST TO COMPILE
                        valuesArray = (Map) submittedValues;
                        itemValue = itemValueAsString;
                } else {
                        valuesArray = values;
                        itemValue = option.getValue();
                }


                boolean selected = isSelected(context, component, itemValue, valuesArray, converter);

                int valueInput = getValueForInput(context, component, itemValue, valuesArray, converter);
                if (option.isNoSelectionOption() && values != null && "".equals(valueInput)) {
                        return;
                }

                writer.startElement("td", null);

                writer.startElement("div", null);
                writer.writeAttribute("class", HTML.CHECKBOX_CLASS, null);

                encodeOptionInput(context, checkbox, id, name, disabled, itemValueAsString, valueInput);
                encodeOptionOutput(context, checkbox, valueInput, disabled);

                writer.endElement("div");
                writer.endElement("td");

                writer.startElement("td", null);
                encodeOptionLabel(context, checkbox, id, option, disabled);
                writer.endElement("td");
        }

        protected void encodeOptionInput(FacesContext context, TriStateManyCheckbox checkbox, String id, String name, boolean disabled, String value, int valueInput) throws IOException {
                ResponseWriter writer = context.getResponseWriter();

                writer.startElement("div", null);
                writer.writeAttribute("class", HTML.CHECKBOX_INPUT_WRAPPER_CLASS, null);

                writer.startElement("input", null);
                writer.writeAttribute("id", id, null);
                writer.writeAttribute("name", name, null);
                writer.writeAttribute("type", "text", null);
                writer.writeAttribute("value", valueInput, null);
                writer.writeAttribute("itemValue", value, null);

                if (disabled) {
                        writer.writeAttribute("disabled", "disabled", null);
                }

                if (checkbox.getOnchange() != null) {
                        writer.writeAttribute("onchange", checkbox.getOnchange(), null);
                }

                writer.endElement("input");

                writer.endElement("div");
        }

        protected void encodeOptionOutput(FacesContext context, TriStateManyCheckbox checkbox, int valCheck, boolean disabled) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                String styleClass = HTML.CHECKBOX_BOX_CLASS;
                styleClass = (valCheck == 1 || valCheck == 2) ? styleClass + " ui-state-active" : styleClass;
                styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

                String iconClass = HTML.CHECKBOX_ICON_CLASS;

                //if stateIcon is defined use it insted of default icons.
                String stateOneIconClass = checkbox.getStateOneIcon() != null ? "ui-icon " + checkbox.getStateOneIcon() : " ";
                String stateTwoIconClass = checkbox.getStateTwoIcon() != null ? "ui-icon " + checkbox.getStateTwoIcon() : "ui-icon ui-icon-check";
                String stataThreeIconClass = checkbox.getStateThreeIcon() != null ? "ui-icon " + checkbox.getStateThreeIcon() : "ui-icon ui-icon-closethick";

                String statesIconsClasses = stateOneIconClass + ";" + stateTwoIconClass + ";" + stataThreeIconClass;

                iconClass = valCheck == 0 ? iconClass + " " + stateOneIconClass : iconClass;
                iconClass = valCheck == 1 ? iconClass + " " + stateTwoIconClass : iconClass;
                iconClass = valCheck == 2 ? iconClass + " " + stataThreeIconClass : iconClass;

                writer.startElement("div", null);
                writer.writeAttribute("class", styleClass, null);
                writer.writeAttribute("statesIcons", statesIconsClasses, null);

                writer.startElement("span", null);
                writer.writeAttribute("class", iconClass, null);
                writer.endElement("span");

                writer.endElement("div");
        }

        protected void encodeScript(FacesContext context, TriStateManyCheckbox checkbox) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                String clientId = checkbox.getClientId(context);

                startScript(writer, clientId);

                writer.write("$(function() {");
                writer.write("PrimeFacesExt.cw('TriStateManyCheckbox','" + checkbox.resolveWidgetVar() + "',{");
                writer.write("id:'" + clientId + "'");
                encodeClientBehaviors(context, checkbox);
                writer.write("});});");

                endScript(writer);
        }

        protected void encodeOptionLabel(FacesContext context, TriStateManyCheckbox checkbox, String containerClientId, SelectItem option, boolean disabled) throws IOException {
                ResponseWriter writer = context.getResponseWriter();

                writer.startElement("label", null);
                writer.writeAttribute("for", containerClientId, null);
                if (disabled) {
                        writer.writeAttribute("class", "ui-state-disabled", null);
                }

                if (option.isEscape()) {
                        writer.writeText(option.getLabel(), null);
                } else {
                        writer.write(option.getLabel());
                }
                writer.endElement("label");
        }

        @Override
        protected String getSubmitParam(FacesContext context, UISelectMany selectMany) {
                String getSubParm = selectMany.getClientId(context);
                return getSubParm;
        }

        /*
         * return the value for the triState based on the value of the
         * selectItems on the iteration
         */
        protected int getValueForInput(FacesContext context, UIInput component, Object itemValue, Map valueArray, Converter converter) {

                int retInt = 0;
                if (itemValue == null || valueArray == null) {
                        return retInt;
                }
                if (valueArray.containsKey(itemValue)) {
                        retInt = Integer.valueOf(((String) valueArray.get(itemValue)));
                        return retInt % 3;
                } else {
                        return retInt;
                }
        }

        @Override
        protected Map getValues(UIComponent component) {
                UISelectMany selectMany = (UISelectMany) component;
                Object value = selectMany.getValue();

                if (value == null) {
                        return null;
                        //it should be a Map instance for <ItemStringValue,Value>
                } else if (value instanceof Map) {
                        return ((Map) value);
                } else {
                        throw new FacesException("Value of '" + component.getClientId() + "'must be a Map instance");
                }
        }
}
