/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.event;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.calculator.Calculator} component when a button is pressed.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
public class ButtonEvent extends AbstractAjaxBehaviorEvent {

    public static final String NAME = "button";
    private static final long serialVersionUID = 1L;

    private final String buttonName;
    private final BigDecimal value;

    public ButtonEvent(final UIComponent component, final Behavior behavior, final String buttonName, final BigDecimal value) {
        super(component, behavior);
        this.buttonName = buttonName;
        this.value = value;
    }

    public final String getName() {
        return buttonName;
    }

    public final BigDecimal getValue() {
        return value;
    }

}
