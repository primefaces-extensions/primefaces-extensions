/*
 * Copyright 2011-2016 PrimeFaces Extensions
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
package org.primefaces.extensions.component.calculator;

import java.util.Locale;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * <code>Calculator</code> component.
 *
 * @author Melloware info@melloware.com
 * @since 6.1
 */
@ResourceDependencies({
         @ResourceDependency(library = "primefaces", name = "components.css"),
         @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
         @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
         @ResourceDependency(library = "primefaces", name = "core.js"),
         @ResourceDependency(library = "primefaces-extensions", name = "calculator/calculator.css"),
         @ResourceDependency(library = "primefaces-extensions", name = "calculator/calculator.js")
})
public class Calculator extends UIComponentBase implements ClientBehaviorHolder, Widget {

   public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Calculator";
   public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
   private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CalculatorRenderer";

   private Locale appropriateLocale;

   protected enum PropertyKeys {

      // @formatter:off
      widgetVar,
      showOn,
      layout,
      locale,
      precision,
      rtl,
      styleClass,
      onopen,
      onbutton,
      onclose,
      forValue("for");
      // @formatter:on

      String toString;

      PropertyKeys(final String toString) {
         this.toString = toString;
      }

      PropertyKeys() {
      }

      @Override
      public String toString() {
         return toString != null ? toString : super.toString();
      }
   }

   /**
    * Default constructor
    */
   public Calculator() {
      setRendererType(DEFAULT_RENDERER);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String resolveWidgetVar() {
      return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public Locale calculateLocale() {
      if (appropriateLocale == null) {
         appropriateLocale = org.primefaces.extensions.util.ComponentUtils.resolveLocale(getLocale());
      }
      return appropriateLocale;
   }

   public String getWidgetVar() {
      return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
   }

   public void setWidgetVar(final String _widgetVar) {
      getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
   }

   public String getFor() {
      return (String) getStateHelper().eval(PropertyKeys.forValue, null);
   }

   public void setFor(final String _for) {
      getStateHelper().put(PropertyKeys.forValue, _for);
   }

   public String getShowOn() {
      return (String) getStateHelper().eval(PropertyKeys.showOn, "focus");
   }

   public void setShowOn(final String _showOn) {
      getStateHelper().put(PropertyKeys.showOn, _showOn);
   }

   public String getLayout() {
      return (String) getStateHelper().eval(PropertyKeys.layout, "standard");
   }

   public void setLayout(final String _layout) {
      getStateHelper().put(PropertyKeys.layout, _layout);
   }

   public Object getLocale() {
      return getStateHelper().eval(PropertyKeys.locale, null);
   }

   public void setLocale(final Object locale) {
      getStateHelper().put(PropertyKeys.locale, locale);
   }

   public int getPrecision() {
      return (Integer) getStateHelper().eval(PropertyKeys.precision, 10);
   }

   public void setPrecision(final int _precision) {
      getStateHelper().put(PropertyKeys.precision, _precision);
   }

   public void setRtl(final boolean _rtl) {
      getStateHelper().put(PropertyKeys.rtl, _rtl);
   }

   public boolean isRtl() {
      return (Boolean) getStateHelper().eval(PropertyKeys.rtl, false);
   }

   public String getStyleClass() {
      return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
   }

   public void setStyleClass(final String _styleClass) {
      getStateHelper().put(PropertyKeys.styleClass, _styleClass);
   }

   public String getOnopen() {
      return (String) getStateHelper().eval(PropertyKeys.onopen, null);
   }

   public void setOnopen(final String _onOpen) {
      getStateHelper().put(PropertyKeys.onopen, _onOpen);
   }

   public String getOnclose() {
      return (String) getStateHelper().eval(PropertyKeys.onclose, null);
   }

   public void setOnclose(final String _onClose) {
      getStateHelper().put(PropertyKeys.onclose, _onClose);
   }

   public String getOnbutton() {
      return (String) getStateHelper().eval(PropertyKeys.onbutton, null);
   }

   public void setOnbutton(final String _onButton) {
      getStateHelper().put(PropertyKeys.onbutton, _onButton);
   }

}
