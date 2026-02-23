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

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.component.api.AbstractParameter;

/**
 * Component for the <code>AssignableParameter</code>; applies a RemoteCommand parameter to a backing bean.
 *
 * @since 0.5
 */
@FacesComponent(value = AssignableParameterBase.COMPONENT_TYPE, namespace = AbstractParameter.COMPONENT_FAMILY)
@FacesComponentInfo(name = "assignableParam", description = "AssignableParam can be used the apply a parameter of the RemoteCommand to a backing bean.")
public class AssignableParameter extends AssignableParameterBaseImpl {

    @Override
    public ValueExpression getValueExpression(final String name) {
        if ("value".equals(name)) {
            return getAssignTo();
        }
        return super.getValueExpression(name);
    }
}
