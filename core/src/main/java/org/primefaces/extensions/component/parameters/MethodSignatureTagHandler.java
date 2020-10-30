/*
 * Copyright 2011-2020 PrimeFaces Extensions
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

import java.util.Arrays;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * {@link TagHandler} for the <code>MethodSignature</code> tag.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class MethodSignatureTagHandler extends TagHandler {

    public static final String PARAMETERS_TYPES_ATTRIBUTE_NAME = "METHOD_SIGNATURE_PARAMETER_TYPES";

    private final Class<?>[] parameterTypes;

    public MethodSignatureTagHandler(final TagConfig config) {
        super(config);

        final TagAttribute parametersTag = getRequiredAttribute("parameters");
        try {
            parameterTypes = parseParameterTypes(parametersTag.getValue());
        }
        catch (final ClassNotFoundException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    @Override
    public void apply(final FaceletContext ctx, final UIComponent parent) {
        // store all parameter types to parent component
        parent.getAttributes().put(PARAMETERS_TYPES_ATTRIBUTE_NAME, parameterTypes);
    }

    public Class<?>[] getParameterTypes() {
        if (parameterTypes != null) {
            return Arrays.copyOf(parameterTypes, parameterTypes.length);
        }
        return new Class<?>[] {};
    }

    private Class<?>[] parseParameterTypes(final String parameters) throws ClassNotFoundException {
        final String[] splitParameters = parameters.split(",");
        final Class<?>[] types = new Class<?>[splitParameters.length];

        for (int i = 0; i < splitParameters.length; i++) {
            types[i] = Class.forName(splitParameters[i].trim());
        }

        return types;
    }
}
