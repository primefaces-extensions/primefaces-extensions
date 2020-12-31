/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
