/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.util;

import java.io.Serializable;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

@SuppressWarnings({ "deprecation", "serial" })
public class LegacyMethodBinding  extends MethodBinding implements Serializable {

    private final MethodExpression expression;

    public LegacyMethodBinding(final MethodExpression expression) {
        this.expression = expression;
    }

    @Override
	public Class<?> getType(final FacesContext context) throws MethodNotFoundException {
        try {
            return expression.getMethodInfo(context.getELContext()).getReturnType();
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e.getMessage(), e.getCause());
        } catch (ELException e) {
            throw new EvaluationException(e.getMessage(), e.getCause());
        }
    }

	@Override
	public Object invoke(final FacesContext context, final Object[] params)
		throws EvaluationException, MethodNotFoundException {

        try {
            return expression.invoke(context.getELContext(), params);
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e.getMessage(), e.getCause());
        } catch (ELException e) {
            throw new EvaluationException(e.getMessage(), e.getCause());
        }
    }

    @Override
	public String getExpressionString() {
        return expression.getExpressionString();
    }
}
