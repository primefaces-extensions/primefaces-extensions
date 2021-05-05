/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.behavior.printer;

import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import org.primefaces.behavior.base.AbstractBehavior;
import org.primefaces.behavior.base.BehaviorAttribute;
import org.primefaces.expression.SearchExpressionFacade;

/**
 * Replace PF Printer with this version which uses Print.js library.
 * 
 * <pre>
 *     &lt;behavior&gt;
 *         &lt;behavior-id&gt;org.primefaces.component.PrinterBehavior&lt;/behavior-id&gt;
 *         &lt;behavior-class&gt;org.primefaces.extensions.behavior.printer.PrinterBehavior&lt;/behavior-class&gt;
 *     &lt;/behavior&gt;
 * </pre>
 *
 * @since 10.0.1
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "printer/printer.css")
@ResourceDependency(library = "primefaces-extensions", name = "printer/printer.js")
public class PrinterBehavior extends AbstractBehavior {

    public enum PropertyKeys implements BehaviorAttribute {
        target(String.class);

        private final Class<?> expectedType;

        PropertyKeys(Class<?> expectedType) {
            this.expectedType = expectedType;
        }

        @Override
        public Class<?> getExpectedType() {
            return expectedType;
        }
    }

    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        FacesContext context = behaviorContext.getFacesContext();

        String component = SearchExpressionFacade.resolveClientId(
                    context, behaviorContext.getComponent(), getTarget());

        return String.format("printJS('%s', 'html');return false;", component);
    }

    @Override
    protected BehaviorAttribute[] getAllAttributes() {
        return PropertyKeys.values();
    }

    public String getTarget() {
        return eval(PropertyKeys.target, null);
    }

    public void setTarget(String target) {
        put(PropertyKeys.target, target);
    }
}
