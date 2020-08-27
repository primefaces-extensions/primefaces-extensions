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
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.rowKey);
        hash = 29 * hash + Objects.hashCode(this.colIndex);
        return hash;
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
        final SheetRowColIndex other = (SheetRowColIndex) obj;
        if (!Objects.equals(this.rowKey, other.rowKey)) {
            return false;
        }
        if (!Objects.equals(this.colIndex, other.colIndex)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SheetRowColIndex{" + "rowKey=" + rowKey + ", colIndex=" + colIndex + '}';
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
