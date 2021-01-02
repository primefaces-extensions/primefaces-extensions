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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SheetRowColIndex)) {
            return false;
        }
        SheetRowColIndex that = (SheetRowColIndex) o;
        return Objects.equals(getRowKey(), that.getRowKey()) &&
                    Objects.equals(getColIndex(), that.getColIndex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRowKey(), getColIndex());
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
