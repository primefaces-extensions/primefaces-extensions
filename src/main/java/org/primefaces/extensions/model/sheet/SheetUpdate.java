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
package org.primefaces.extensions.model.sheet;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents the data associated with the update of a single cell.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class SheetUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Object rowData;

    private final Object oldValue;

    private final Object newValue;

    private final Object rowKey;

    private final int colIndex;

    private transient String toString;

    private transient int hashCode;

    /**
     * Constructs an instance representing a single cell update.
     *
     * @param rowKey the row key of the row being updated
     * @param colIndex the column index of the column being updated
     * @param rowData the rowData associated with the row being updated
     * @param oldValue the old cell value
     * @param newValue the new cell value
     */
    public SheetUpdate(Object rowKey, int colIndex, Object rowData,
                Object oldValue, Object newValue) {
        this.rowKey = rowKey;
        this.colIndex = colIndex;
        this.rowData = rowData;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (toString == null) {
            toString = new ToStringBuilder(this).appendSuper(super.toString()).append("rowData", rowData)
                        .append("oldValue", oldValue).append("newValue", newValue).append("rowIndex", rowKey)
                        .append("colIndex", colIndex).toString();
        }
        return toString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof SheetUpdate)) {
            return false;
        }
        SheetUpdate castOther = (SheetUpdate) other;
        return new EqualsBuilder().append(rowKey, castOther.rowKey).append(colIndex, castOther.colIndex).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = new HashCodeBuilder().append(rowKey).append(colIndex).toHashCode();
        }
        return hashCode;
    }

    /**
     * The rowData value.
     *
     * @return the rowData the row data for this update
     */
    public Object getRowData() {
        return rowData;
    }

    /**
     * The oldValue value.
     *
     * @return the oldValue
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * The newValue value.
     *
     * @return the newValue
     */
    public Object getNewValue() {
        return newValue;
    }

    /**
     * The rowKey value.
     *
     * @return the rowKey
     */
    public Object getRowKey() {
        return rowKey;
    }

    /**
     * The colIndex value.
     *
     * @return the colIndex
     */
    public int getColIndex() {
        return colIndex;
    }

}
