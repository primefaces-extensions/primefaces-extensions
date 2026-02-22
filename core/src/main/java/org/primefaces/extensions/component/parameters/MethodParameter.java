/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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

import jakarta.el.ValueExpression;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;

import org.primefaces.extensions.component.api.AbstractParameter;
import org.primefaces.extensions.util.DummyValueExpression;

/**
 * Component for the <code>MethodParameter</code>; receives a RemoteCommand parameter in the actionListener or action method.
 *
 * @since 0.5
 */
@FacesComponent(value = MethodParameterBase.COMPONENT_TYPE, namespace = AbstractParameter.COMPONENT_FAMILY)
public class MethodParameter extends MethodParameterBaseImpl {

    @Override
    public ValueExpression getValueExpression(final String name) {
        if ("value".equals(name)) {
            final UIComponent parent = getParent();
            final java.util.Map<String, Object> parentAttributes = parent.getAttributes();
            final Class<?>[] parameterTypes = (Class<?>[]) parentAttributes.get(MethodSignatureTagHandler.PARAMETERS_TYPES_ATTRIBUTE_NAME);
            final Class<?> parameterType = parameterTypes[parent.getChildren().indexOf(this)];
            return new DummyValueExpression(parameterType);
        }
        return super.getValueExpression(name);
    }
}
