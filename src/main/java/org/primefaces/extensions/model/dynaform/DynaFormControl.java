/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.model.dynaform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.primefaces.extensions.model.common.KeyData;

/**
 * Class representing a control inside of <code>DynaForm</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormControl extends AbstractDynaFormElement implements KeyData {

    public static final String DEFAULT_TYPE = "default";
    private static final long serialVersionUID = 1L;
    private static final String KEY_PREFIX_ROW = "r";
    private static final String KEY_PREFIX_COLUMN = "c";
    private static final String KEY_SUFFIX_REGULAR = "reg";
    private static final String KEY_SUFFIX_EXTENDED = "ext";
    private static final String KEY_SUFFIX_POSITION = "p";

    private String key;
    private Object data;
    private String type;
    private int position;

    public DynaFormControl(Object data, String type, int colspan, int rowspan, int row, int column, int position,
                boolean extended) {
        super(colspan, rowspan, row, column, extended);
        this.position = position;

        this.data = data;
        if (type != null) {
            this.type = type;
        }
        else {
            this.type = DEFAULT_TYPE;
        }

        generateKey();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + position;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        final DynaFormControl that = (DynaFormControl) o;
        if (position != that.position) {
            return false;
        }
        return true;
    }

    void generateKey() {
        final StringBuilder sb = new StringBuilder();
        sb.append(KEY_PREFIX_ROW).append(getRow()).append(KEY_PREFIX_COLUMN).append(getColumn()).append(KEY_SUFFIX_POSITION).append(getPosition());
        if (isExtended()) {
            sb.append(KEY_SUFFIX_EXTENDED);
        }
        else {
            sb.append(KEY_SUFFIX_REGULAR);
        }
        setKey(sb.toString());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("key", key).append("data", data)
                    .append("type", type).append("colspan", getColspan())
                    .append("rowspan", getRowspan()).append("row", getRow())
                    .append("column", getColumn())
                    .append("extended", isExtended())
                    .append("position", getPosition()).toString();
    }
}
