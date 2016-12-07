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
package org.primefaces.extensions.event;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

/**
 * Event which is triggered by the
 * {@link org.primefaces.extensions.component.calculator.Calculator} component
 * when a button is pressed.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@SuppressWarnings("serial")
public class ButtonEvent extends AbstractAjaxBehaviorEvent {

   public static final String NAME = "button";

   private final String name;
   private final BigDecimal value;

   public ButtonEvent(final UIComponent component, final Behavior behavior, final String name, final BigDecimal value) {
      super(component, behavior);
      this.name = name;
      this.value = value;
   }

   public final String getName() {
      return name;
   }

   public final BigDecimal getValue() {
      return value;
   }

}
