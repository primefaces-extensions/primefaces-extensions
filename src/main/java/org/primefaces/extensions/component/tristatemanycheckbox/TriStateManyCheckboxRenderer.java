/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.renderkit.SelectManyRenderer;
import org.primefaces.util.HTML;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.primefaces.context.RequestContext;
import org.primefaces.util.WidgetBuilder;

/**
 * TriStateManyCheckboxRenderer
 *
 * @author  Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
public class TriStateManyCheckboxRenderer extends SelectManyRenderer {

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
	    throws ConverterException {
		//only convert the values of the maps
		if (submittedValue instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapSub = (Map<String, Object>) submittedValue;
			List<String> keyValues = new ArrayList<String>(mapSub.keySet());
			Map<String, Object> mapSubConv = new LinkedHashMap<String, Object>();
			TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
			Converter converter = checkbox.getConverter();
			if (converter != null) {
				for (String keyVal : keyValues) {
					Object mapVal = converter.getAsObject(context, checkbox, (String) mapSub.get(keyVal));
					mapSubConv.put(keyVal, mapVal);
				}

				return mapSubConv;
			} else {
				return mapSub;
			}
		} else {
			throw new FacesException("Value of '" + component.getClientId() + "'must be a Map instance");
		}
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		if (!shouldDecode(component)) {
			return;
		}

		TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
		decodeBehaviors(context, checkbox);

		String submitParam = getSubmitParam(context, checkbox);
		Map<String, String[]> params = context.getExternalContext().getRequestParameterValuesMap();

		String[] valuesArray = null;
		if (params.containsKey(submitParam)) {
			valuesArray = params.get(submitParam);
		}

		checkbox.setSubmittedValue(getSubmitedMap(context, checkbox, valuesArray));
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
		Converter converter = checkbox.getConverter();
		Map<String, Object> values = getValues(checkbox);
		Map<String, Object> submittedMap = getSubmittedFromComp(checkbox);
		String layout = checkbox.getLayout();
		boolean pageDirection = layout != null && layout.equals("pageDirection");

		if (submittedMap != null) {
			values = submittedMap;
		}

		if (converter != null && submittedMap == null) {
			for (String keyMapO : values.keySet()) {
				String keyValue = converter.getAsString(context, checkbox, values.get(keyMapO));
				values.put(keyMapO, keyValue);
			}
		}

		int idx = -1;
		for (SelectItem selectItem : selectItems) {
			idx++;
			if (pageDirection) {
				writer.startElement("tr", null);
			}

			encodeOption(context, checkbox, values, converter, selectItem, idx);
			if (pageDirection) {
				writer.endElement("tr");
			}
		}
	}

	protected void encodeOption(FacesContext context, UIInput component, Map<String, Object> values, Converter converter,
	                            SelectItem option, int idx) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
		String itemValueAsString = String.valueOf(option.getValue());
		String name = checkbox.getClientId(context);
		String id = name + UINamingContainer.getSeparatorChar(context) + idx;
		boolean disabled = option.isDisabled() || checkbox.isDisabled();

		String itemValue = (String) option.getValue();

		int valueInput = getValueForInput(context, component, itemValue, values, converter);
		if (option.isNoSelectionOption() && values != null && "".equals(itemValue)) {
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

	protected void encodeOptionInput(FacesContext context, TriStateManyCheckbox checkbox, String id, String name,
	                                 boolean disabled, String value, int valueInput) throws IOException {
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

	protected void encodeOptionOutput(final FacesContext context, final TriStateManyCheckbox checkbox, final int valCheck,
	                                  final boolean disabled) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String styleClass = HTML.CHECKBOX_BOX_CLASS;
		styleClass = (valCheck == 1 || valCheck == 2) ? styleClass + " ui-state-active" : styleClass;
		styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

		//if stateIcon is defined use it insted of default icons.
		String stateOneIconClass =
		    checkbox.getStateOneIcon() != null ? TriStateManyCheckbox.UI_ICON + checkbox.getStateOneIcon() : "";
		String stateTwoIconClass =
		    checkbox.getStateTwoIcon() != null ? TriStateManyCheckbox.UI_ICON + checkbox.getStateTwoIcon()
		                                       : TriStateManyCheckbox.UI_ICON + "ui-icon-check";
		String stataThreeIconClass =
		    checkbox.getStateThreeIcon() != null ? TriStateManyCheckbox.UI_ICON + checkbox.getStateThreeIcon()
		                                         : TriStateManyCheckbox.UI_ICON + "ui-icon-closethick";

		String statesIconsClasses =
		    "[\"" + stateOneIconClass + "\",\"" + stateTwoIconClass + "\",\"" + stataThreeIconClass + "\"]";
                
                String statesTitles =
		    "[\"" + checkbox.getStateOneTitle() + "\",\"" 
                       + checkbox.getStateTwoTitle() + "\",\"" + checkbox.getStateThreeTitle() + "\"]";
                
                

		String iconClass = "ui-chkbox-icon ui-c"; //HTML.CHECKBOX_ICON_CLASS;
                String activeTitle = "";
		if (valCheck == 0) {
			iconClass = iconClass + " " + stateOneIconClass;
                        activeTitle = checkbox.getStateOneTitle();
		} else if (valCheck == 1) {
			iconClass = iconClass + " " + stateTwoIconClass;
                        activeTitle = checkbox.getStateTwoTitle();
		} else if (valCheck == 2) {
			iconClass = iconClass + " " + stataThreeIconClass;
                        activeTitle = checkbox.getStateThreeTitle();
		}
                
                String dataTitles = "";
                String titleAtt = "";
                
                if(!checkbox.getStateOneTitle().isEmpty() 
                    || !checkbox.getStateTwoTitle().isEmpty() || !checkbox.getStateThreeTitle().isEmpty()){
                        // preparation with singe quotes for .data('titlestates')
                        dataTitles = "data-titlestates='" + statesTitles + "' ";
                        // active title Att 
                        titleAtt = " title=\"" + activeTitle + "\" ";
                }                

                String tabIndexTag = " tabIndex=0 ";                
                if(checkbox.getTabindex()!=null){
                    tabIndexTag = "tabIndex=" + checkbox.getTabindex()+ " ";
                }      
                
                // preparation with singe quotes for .data('iconstates') 
		writer.write("<div " + tabIndexTag + titleAtt + "class=\"" + styleClass + "\" data-iconstates='" + statesIconsClasses + "' "
                                + dataTitles + ">"
                                + "<span class=\"" + iconClass + "\"></span></div>");                
		
	}

	protected void encodeScript(FacesContext context, TriStateManyCheckbox checkbox) throws IOException {
        WidgetBuilder wb = RequestContext.getCurrentInstance().getWidgetBuilder();
        wb.initWithDomReady("ExtTriStateManyCheckbox", checkbox.resolveWidgetVar(), checkbox.getClientId());
        encodeClientBehaviors(context, checkbox);
        wb.finish();
	}

	protected void encodeOptionLabel(FacesContext context, TriStateManyCheckbox checkbox, String containerClientId,
	                                 SelectItem option, boolean disabled) throws IOException {
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
		return selectMany.getClientId(context);
	}

	/*
	 * return the value for the triState based on the value of the
	 * selectItems on the iteration
	 */
	protected int getValueForInput(FacesContext context, UIInput component, String itemValue, Map<String, Object> valueArray,
	                               Converter converter) {
		try {
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
		} catch (NumberFormatException ex) {
			throw new FacesException("State of '" + component.getClientId() + "' must be an integer representation");
		}
	}

	@Override
    @SuppressWarnings("unchecked")
	protected Map<String, Object> getValues(UIComponent component) {
		UISelectMany selectMany = (UISelectMany) component;
		Object value = selectMany.getValue();

		if (value == null) {
			return null;
		} else if (value instanceof Map) {
			//it should be a Map instance for <ItemStringValue,Value>
			return ((Map) value);
		} else {
			throw new FacesException("Value of '" + component.getClientId() + "'must be a Map instance");
		}
	}

	protected Map<String, Object> getSubmitedMap(FacesContext context, TriStateManyCheckbox checkbox, String[] valuesArray) {
		List<SelectItem> selectItems = getSelectItems(context, checkbox);
		Map<String, Object> subValues = new LinkedHashMap<String, Object>();
		if (valuesArray != null && valuesArray.length == selectItems.size()) {
			int idx = -1;
			for (SelectItem item : selectItems) {
				idx++;

				String keyMap = (String) item.getValue();
				Object valueMap = valuesArray[idx];
				subValues.put(keyMap, valueMap);
			}

			return subValues;
		} else {
			return null;
		}
	}

	protected Map<String, Object> getSubmittedFromComp(UIComponent component) {
		TriStateManyCheckbox checkbox = (TriStateManyCheckbox) component;
		@SuppressWarnings("unchecked")
		Map<String, Object> ret = (Map<String, Object>) checkbox.getSubmittedValue();
		if (ret != null) {
			Map<String, Object> subValues = new LinkedHashMap<String, Object>();

			//need to reverse the order of element on the map to take the value as on decode.
			Set<String> keys = ret.keySet();
			String[] tempArray = (String[]) keys.toArray();

			int length = tempArray.length;
			for (int i = length - 1; i >= 0; i--) {
				String key = tempArray[i];
				Object val = ret.get(key);
				subValues.put(key, val);
			}

			return subValues;
		} else {
			return null;
		}
	}
}
