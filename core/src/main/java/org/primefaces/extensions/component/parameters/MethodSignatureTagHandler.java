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
