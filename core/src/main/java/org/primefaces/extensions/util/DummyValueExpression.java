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
        /* NOOP */
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
