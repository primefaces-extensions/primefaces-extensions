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
package org.primefaces.extensions.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * An immutable implementation of the {@link ParameterizedType} interface.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public final class ParameterizedTypeImpl implements ParameterizedType {

    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type owner;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type owner) {
        this.rawType = rawType;
        this.actualTypeArguments = Arrays.copyOf(actualTypeArguments, actualTypeArguments.length);
        this.owner = owner;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return Arrays.copyOf(actualTypeArguments, actualTypeArguments.length);
    }

    @Override
    public Type getOwnerType() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParameterizedType)) {
            return false;
        }

        // Check that information is equivalent
        final ParameterizedType that = (ParameterizedType) o;
        if (this == that) {
            return true;
        }

        final Type thatOwner = that.getOwnerType();
        final Type thatRawType = that.getRawType();

        return (owner == null ? thatOwner == null : owner.equals(thatOwner))
                    && (rawType == null ? thatRawType == null : rawType.equals(thatRawType))
                    && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments)
                    ^ (owner == null ? 0 : owner.hashCode())
                    ^ (rawType == null ? 0 : rawType.hashCode());
    }
}
