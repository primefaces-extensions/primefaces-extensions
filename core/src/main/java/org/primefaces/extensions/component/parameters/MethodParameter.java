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
package org.primefaces.extensions.component.parameters;

import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.primefaces.extensions.component.base.AbstractParameter;
import org.primefaces.extensions.util.DummyValueExpression;

/**
 * Component class for the <code>MethodParameter</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class MethodParameter extends AbstractParameter {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MethodParameter";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        type
    }

    public MethodParameter() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return AbstractParameter.COMPONENT_FAMILY;
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type, null);
    }

    public void setType(final String type) {
        getStateHelper().put(PropertyKeys.type, type);
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
            // get type from parent component
            // MethodSignatureTagHandler stores all parameters to the parent component
            final UIComponent parent = getParent();
            final Map<String, Object> parentAttribtues = parent.getAttributes();
            final Class<?>[] parameterTypes = (Class<?>[]) parentAttribtues.get(MethodSignatureTagHandler.PARAMETERS_TYPES_ATTRIBUTE_NAME);
            final Class<?> parameterType = parameterTypes[parent.getChildren().indexOf(this)];

            return new DummyValueExpression(parameterType);
        }

        return super.getValueExpression(name);
    }
}
