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
package org.primefaces.extensions.model.fluidgrid;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.primefaces.extensions.model.common.KeyData;

/**
 * Class representing an item inside of <code>FluidGrid</code>.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 1.1.0
 */
public class FluidGridItem implements KeyData, Serializable {

    public static final String DEFAULT_TYPE = "default";
    private static final long serialVersionUID = 1L;

    private String key;
    private Serializable data;
    private String type;

    public FluidGridItem() {
        // generate key
        setKey(generateKey());
    }

    public FluidGridItem(final Serializable data) {
        this();
        this.data = data;
        type = DEFAULT_TYPE;
    }

    public FluidGridItem(final Serializable data, final String type) {
        this.data = data;
        if (type != null) {
            this.type = type;
        }
        else {
            this.type = DEFAULT_TYPE;
        }

        // generate key
        setKey(generateKey());
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(final String key) {
        this.key = key;
    }

    @Override
    public Serializable getData() {
        return data;
    }

    @Override
    public void setData(final Serializable data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public static String generateKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FluidGridItem)) {
            return false;
        }
        FluidGridItem that = (FluidGridItem) o;
        return Objects.equals(getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("FluidGridItem [key=");
        builder.append(key);
        builder.append(", data=");
        builder.append(data);
        builder.append(", type=");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }

}
