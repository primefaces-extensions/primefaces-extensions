/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.util;

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
 * Dummy {@link ValueExpression} which just provides the type.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DummyValueExpression extends ValueExpression {

    private static final long serialVersionUID = 1L;

    private final Class<?> type;

    public DummyValueExpression(final Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getExpectedType() {
        return null;
    }

    @Override
    public Class<?> getType(final ELContext context) {
        return type;
    }

    @Override
    public Object getValue(final ELContext context) {
        return null;
    }

    @Override
    public boolean isReadOnly(final ELContext context) {
        return false;
    }

    @Override
    public void setValue(final ELContext context, final Object value) {
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj;
    }

    @Override
    public String getExpressionString() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isLiteralText() {
        return false;
    }
}
