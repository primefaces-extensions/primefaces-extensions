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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Spreadsheet component model for storing a row/column index.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class SheetRowColIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String rowKey;
    private final Integer colIndex;

    /**
     * Constructs an instance of RowColIndex for the row and column specified.
     *
     * @param rowKey the row represented by this index
     * @param col the column represented by this index
     */
    public SheetRowColIndex(final String rowKey, final Integer col) {
        this.rowKey = rowKey;
        colIndex = col;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof SheetRowColIndex)) {
            return false;
        }
        final SheetRowColIndex castOther = (SheetRowColIndex) other;
        return new EqualsBuilder().append(rowKey, castOther.rowKey).append(colIndex, castOther.colIndex).isEquals();
    }

    @Override
    public String toString() {
        return "RowColIndex [rowKey=" + rowKey + ", colIndex=" + colIndex + "]";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rowKey).append(colIndex).toHashCode();
    }

    /**
     * The rowIndex value.
     *
     * @return the rowIndex
     */
    public String getRowKey() {
        return rowKey;
    }

    /**
     * The colIndex value.
     *
     * @return the colIndex
     */
    public Integer getColIndex() {
        return colIndex;
    }

}
