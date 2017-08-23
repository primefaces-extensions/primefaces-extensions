/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.analogclock;

import java.util.Date;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.api.Widget;
import org.primefaces.context.RequestContext;
import org.primefaces.util.ComponentUtils;

/**
 * <code>AnalogClock</code> component
 * 
 * @author f.strazzullo
 * @since 3.0.0
 */
@ResourceDependencies({
         @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
         @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
         @ResourceDependency(library = "primefaces", name = "core.js"),
         @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
         @ResourceDependency(library = "primefaces-extensions", name = "analogclock/analogclock.js")
})
public class AnalogClock extends UIComponentBase implements Widget {

   public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.AnalogClock";
   public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
   private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.AnalogClockRenderer";

   private static final String DEFAULT_THEME = "omega";

   protected static enum PropertyKeys {
      colorTheme, 
      width, 
      widgetVar, 
      startTime, 
      mode;
   }

   public AnalogClock() {
      setRendererType(DEFAULT_RENDERER);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   public void setStartTime(Date _pattern) {
      getStateHelper().put(PropertyKeys.startTime, _pattern);
   }

   public Date getStartTime() {
      return (Date) getStateHelper().eval(PropertyKeys.startTime, new Date());
   }

   public String getMode() {
      return (java.lang.String) getStateHelper().eval(PropertyKeys.mode, "client");
   }

   public void setMode(String _mode) {
      getStateHelper().put(PropertyKeys.mode, _mode);
   }

   public Object getWidth() {
      return this.getStateHelper().eval(PropertyKeys.width, "auto");
   }

   public void setWidth(Object width) {
      this.getStateHelper().put(PropertyKeys.width, width);
   }

   public String getWidgetVar() {
      return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
   }

   public void setWidgetVar(String _widgetVar) {
      getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
   }

   public Object getColorTheme() {
      return getStateHelper().eval(PropertyKeys.colorTheme, getDefaultColorTheme());
   }

   private String getDefaultColorTheme() {
      String defaultTheme = DEFAULT_THEME;
      if (StringUtils.isNotEmpty(RequestContext.getCurrentInstance().getApplicationContext().getConfig().getTheme())) {
         ELContext elContext = getFacesContext().getELContext();
         ValueExpression defaultThemeVE = getFacesContext().getApplication().getExpressionFactory()
                  .createValueExpression(elContext,
                           RequestContext.getCurrentInstance().getApplicationContext().getConfig().getTheme(),
                           String.class);
         defaultTheme = (String) defaultThemeVE.getValue(elContext);
      }
      return defaultTheme;
   }

   public void setColorTheme(Object colorScheme) {
      getStateHelper().put(PropertyKeys.colorTheme, colorScheme);
   }

   public String resolveWidgetVar() {
      return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
   }

}
