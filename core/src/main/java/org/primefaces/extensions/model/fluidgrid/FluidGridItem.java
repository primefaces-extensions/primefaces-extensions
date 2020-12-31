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
