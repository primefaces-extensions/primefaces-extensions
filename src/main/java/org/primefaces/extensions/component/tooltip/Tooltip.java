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

package org.primefaces.extensions.component.tooltip;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * <code>Tooltip</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@ResourceDependencies({
         @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
         @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
         @ResourceDependency(library = "primefaces", name = "core.js"),
         @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
         @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.css"),
         @ResourceDependency(library = "primefaces-extensions", name = "tooltip/tooltip.js")
})
public class Tooltip extends UIOutput implements Widget {

   public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Tooltip";
   public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
   private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TooltipRenderer";

   /**
    * Properties that are tracked by state saving.
    *
    * @author Oleg Varaksin / last modified by $Author$
    * @version $Revision$
    */
   protected enum PropertyKeys {

      widgetVar, 
      global, 
      shared, 
      autoShow, 
      mouseTracking, 
      fixed, 
      header, 
      adjustX, 
      adjustY, 
      atPosition, 
      myPosition, 
      showEvent, 
      showDelay, 
      showEffect, 
      showEffectLength, 
      styleClass, 
      hideEvent, 
      hideDelay, 
      hideEffect, 
      hideEffectLength, 
      forValue("for");

      private String toString;

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

   public Tooltip() {
      setRendererType(DEFAULT_RENDERER);
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

   public boolean isGlobal() {
      return (Boolean) getStateHelper().eval(PropertyKeys.global, false);
   }

   public void setGlobal(final boolean global) {
      getStateHelper().put(PropertyKeys.global, global);
   }

   public boolean isShared() {
      return (Boolean) getStateHelper().eval(PropertyKeys.shared, false);
   }

   public void setShared(final boolean shared) {
      getStateHelper().put(PropertyKeys.shared, shared);
   }

   public boolean isAutoShow() {
      return (Boolean) getStateHelper().eval(PropertyKeys.autoShow, false);
   }

   public void setAutoShow(final boolean autoShow) {
      getStateHelper().put(PropertyKeys.autoShow, autoShow);
   }

   public boolean isMouseTracking() {
      return (Boolean) getStateHelper().eval(PropertyKeys.mouseTracking, false);
   }

   public void setMouseTracking(final boolean mouseTracking) {
      getStateHelper().put(PropertyKeys.mouseTracking, mouseTracking);
   }

   public boolean isFixed() {
      return (Boolean) getStateHelper().eval(PropertyKeys.fixed, false);
   }

   public void setFixed(final boolean fixed) {
      getStateHelper().put(PropertyKeys.fixed, fixed);
   }

   public int getAdjustX() {
      return (Integer) getStateHelper().eval(PropertyKeys.adjustX, 0);
   }

   public void setAdjustX(final int adjustX) {
      getStateHelper().put(PropertyKeys.adjustX, adjustX);
   }

   public int getAdjustY() {
      return (Integer) getStateHelper().eval(PropertyKeys.adjustY, 0);
   }

   public void setAdjustY(final int adjustY) {
      getStateHelper().put(PropertyKeys.adjustY, adjustY);
   }

   public String getAtPosition() {
      return (String) getStateHelper().eval(PropertyKeys.atPosition, "bottom right");
   }

   public void setAtPosition(final String atPosition) {
      getStateHelper().put(PropertyKeys.atPosition, atPosition);
   }

   public String getMyPosition() {
      return (String) getStateHelper().eval(PropertyKeys.myPosition, "top left");
   }

   public void setMyPosition(final String myPosition) {
      getStateHelper().put(PropertyKeys.myPosition, myPosition);
   }

   public String getShowEvent() {
      return (String) getStateHelper().eval(PropertyKeys.showEvent, "mouseenter");
   }

   public void setShowEvent(final String showEvent) {
      getStateHelper().put(PropertyKeys.showEvent, showEvent);
   }

   public int getShowDelay() {
      return (Integer) getStateHelper().eval(PropertyKeys.showDelay, 0);
   }

   public void setShowDelay(final int showDelay) {
      getStateHelper().put(PropertyKeys.showDelay, showDelay);
   }

   public String getShowEffect() {
      return (String) getStateHelper().eval(PropertyKeys.showEffect, "fadeIn");
   }

   public void setShowEffect(final String showEffect) {
      getStateHelper().put(PropertyKeys.showEffect, showEffect);
   }

   public int getShowEffectLength() {
      return (Integer) getStateHelper().eval(PropertyKeys.showEffectLength, 500);
   }

   public void setShowEffectLength(final int showEffectLength) {
      getStateHelper().put(PropertyKeys.showEffectLength, showEffectLength);
   }

   public String getHideEvent() {
      return (String) getStateHelper().eval(PropertyKeys.hideEvent, "mouseleave");
   }

   public void setHideEvent(final String hideEvent) {
      getStateHelper().put(PropertyKeys.hideEvent, hideEvent);
   }

   public int getHideDelay() {
      return (Integer) getStateHelper().eval(PropertyKeys.hideDelay, 0);
   }

   public void setHideDelay(final int hideDelay) {
      getStateHelper().put(PropertyKeys.hideDelay, hideDelay);
   }

   public String getHideEffect() {
      return (String) getStateHelper().eval(PropertyKeys.hideEffect, "fadeOut");
   }

   public void setHideEffect(final String hideEffect) {
      getStateHelper().put(PropertyKeys.hideEffect, hideEffect);
   }

   public int getHideEffectLength() {
      return (Integer) getStateHelper().eval(PropertyKeys.hideEffectLength, 500);
   }

   public void setHideEffectLength(final int hideEffectLength) {
      getStateHelper().put(PropertyKeys.hideEffectLength, hideEffectLength);
   }

   public String getFor() {
      return (String) getStateHelper().eval(PropertyKeys.forValue, null);
   }

   public void setFor(final String forValue) {
      getStateHelper().put(PropertyKeys.forValue, forValue);
   }

   @Override
   public String resolveWidgetVar() {
      return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
   }

   public String getStyleClass() {
      return (String) getStateHelper().eval(PropertyKeys.styleClass);
   }

   public void setStyleClass(final String styleClass) {
      getStateHelper().put(PropertyKeys.styleClass, styleClass);
   }

   public String getHeader() {
      return (String) getStateHelper().eval(PropertyKeys.header);
   }

   public void setHeader(final String header) {
      getStateHelper().put(PropertyKeys.header, header);
   }

}
