/*
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
package org.primefaces.extensions.model.sheet;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = 7;
            hashCode = 73 * hashCode + Objects.hashCode(this.rowKey);
            hashCode = 73 * hashCode + this.colIndex;
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SheetUpdate other = (SheetUpdate) obj;
        if (this.colIndex != other.colIndex) {
            return false;
        }
        if (!Objects.equals(this.rowKey, other.rowKey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = "SheetUpdate{" + "rowData=" + rowData + ", oldValue=" + oldValue
                        + ", newValue=" + newValue + ", rowKey=" + rowKey + ", colIndex=" + colIndex + '}';
        }
        return toString;
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
