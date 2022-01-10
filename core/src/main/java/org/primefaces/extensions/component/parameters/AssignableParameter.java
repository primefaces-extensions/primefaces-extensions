/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.parameters;

import javax.el.ValueExpression;

import org.primefaces.extensions.component.base.AbstractParameter;

/**
 * Component class for the <code>AssignableParameter</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class AssignableParameter extends AbstractParameter {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.AssignableParameter";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        assignTo
    }

    public AssignableParameter() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return AbstractParameter.COMPONENT_FAMILY;
    }

    public ValueExpression getAssignTo() {
        ValueExpression expression = (ValueExpression) getStateHelper().eval(PropertyKeys.assignTo, null);
        if (expression == null) {
            expression = getValueExpression(PropertyKeys.assignTo.toString());
        }

        return expression;
    }

    public void setAssignTo(final ValueExpression assignTo) {
        getStateHelper().put(PropertyKeys.assignTo, assignTo);
    }

    /**
     * Enables converters to get the value type from the "value" expression.
     *
     * @param name DOCUMENT_ME
     * @return DOCUMENT_ME
     */
    @Override
    public ValueExpression getValueExpression(final String name) {
        if ("value".equals(name)) {
            return getAssignTo();
        }

        return super.getValueExpression(name);
    }
}
