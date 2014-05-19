/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;

/**
 * InputNumber
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "primefaces.css"),
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "inputnumber/inputnumber.js")
})
public class InputNumber extends HtmlInputText implements Widget, InputHolder {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputNumber";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputNumberRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";


	public final static String INPUTNUMBER_CLASS = "ui-inputNum ui-widget";
	private  String decimalSeparator;
	private  String thousandSeparator;

	/**
	 * PropertyKeys
	 *
	 * @author Mauricio Fenoglio / last modified by $Author:
	 * fenoloco@gmail.com $
	 * @version $Revision$
	 * @since 0.3
	 */
	protected enum PropertyKeys {

		widgetVar,
		decimalSeparator,
		thousandSeparator,
		symbol,
		symbolPosition,
		minValue,
		maxValue,
		roundMethod,
		decimalPlaces,
		emptyValue;
		String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public InputNumber() {
		setRendererType(DEFAULT_RENDERER);     
		decimalSeparator = null;
		thousandSeparator = null;
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(final String widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public String getDecimalSeparator() {
		return (String) getStateHelper().eval(PropertyKeys.decimalSeparator, getCalculatedDecimalSepartor());
	}

	public void setDecimalSeparator(final String decimalSeparator) {
		getStateHelper().put(PropertyKeys.decimalSeparator, decimalSeparator);
	}

	public String getThousandSeparator() {
		return (String) getStateHelper().eval(PropertyKeys.thousandSeparator, getCalculatedThousandSeparator());
	}

	public void setThousandSeparator(final String thousandSeparator) {
		getStateHelper().put(PropertyKeys.thousandSeparator, thousandSeparator);
	}

	public String getSymbol() {
		return (String) getStateHelper().eval(PropertyKeys.symbol, "");
	}

	public void setSymbol(final String symbol) {
		getStateHelper().put(PropertyKeys.symbol, symbol);
	}

	public String getSymbolPosition() {
		return (String) getStateHelper().eval(PropertyKeys.symbolPosition, "");
	}

	public void setSymbolPosition(final String symbolPosition) {
		getStateHelper().put(PropertyKeys.symbolPosition, symbolPosition);
	}

	public String getMinValue() {
		return (String) getStateHelper().eval(PropertyKeys.minValue, "");
	}

	public void setMinValue(final String minValue) {
		getStateHelper().put(PropertyKeys.minValue, minValue);
	}

	public String getMaxValue() {
		return (String) getStateHelper().eval(PropertyKeys.maxValue, "");
	}

	public void setMaxValue(final String maxValue) {
		getStateHelper().put(PropertyKeys.maxValue, maxValue);
	}

	public String getRoundMethod() {
		return (String) getStateHelper().eval(PropertyKeys.roundMethod, "");
	}

	public void setRoundMethod(final String roundMethod) {
		getStateHelper().put(PropertyKeys.roundMethod, roundMethod);
	}

	public String getDecimalPlaces() {
		return (String) getStateHelper().eval(PropertyKeys.decimalPlaces, "");
	}

	public void setDecimalPlaces(final String decimalPlaces) {
		getStateHelper().put(PropertyKeys.decimalPlaces, decimalPlaces);
	}

	public String getEmptyValue() {
		return (String) getStateHelper().eval(PropertyKeys.emptyValue, "empty");
	}

	public void setEmptyValue(final String emptyValue) {
		getStateHelper().put(PropertyKeys.emptyValue, emptyValue);
	}

	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(org.primefaces.extensions.component.inputnumber.InputNumber.PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

	private String getCalculatedDecimalSepartor(){
		if(decimalSeparator==null){
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);               
			decimalSeparator = Character.toString(decimalFormatSymbols.getDecimalSeparator());
		}
		return decimalSeparator;
	}

	private String getCalculatedThousandSeparator(){
		if(thousandSeparator==null){               
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();                
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
			thousandSeparator =  Character.toString(decimalFormatSymbols.getGroupingSeparator());              
		}
		return thousandSeparator;           
	}

	public String getInputClientId() {
		return getClientId() + "_input";
	}
}
