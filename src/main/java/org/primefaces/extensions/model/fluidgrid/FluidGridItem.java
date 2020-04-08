/**
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
package org.primefaces.extensions.model.fluidgrid;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;
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
    private Object data;
    private String type;

    public FluidGridItem() {
        // generate key
        setKey(generateKey());
    }

    public FluidGridItem(final Object data) {
        this.data = data;
        type = DEFAULT_TYPE;

        // generate key
        setKey(generateKey());
    }

    public FluidGridItem(final Object data, final String type) {
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
    public Object getData() {
        return data;
    }

    @Override
    public void setData(final Object data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (key == null ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FluidGridItem)) {
            return false;
        }
        final FluidGridItem other = (FluidGridItem) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        }
        else if (!key.equals(other.key)) {
            return false;
        }
        return true;
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

    public String generateKey() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
