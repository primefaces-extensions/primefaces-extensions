/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

    private final transient Object rowData;

    private final transient Object oldValue;

    private final transient Object newValue;

    private final transient Object rowKey;

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
            hashCode = 73 * hashCode + Objects.hashCode(rowKey);
            hashCode = 73 * hashCode + colIndex;
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
        if (colIndex != other.colIndex) {
            return false;
        }
        if (!Objects.equals(rowKey, other.rowKey)) {
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
