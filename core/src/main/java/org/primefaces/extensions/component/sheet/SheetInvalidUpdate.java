/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.component.sheet;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class used to represent an invalid row/cell.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class SheetInvalidUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Object invalidRowKey;

    private int invalidColIndex;

    private transient SheetColumn invalidColumn;

    private transient Object invalidValue;

    private String invalidMessage;

    public SheetInvalidUpdate(Object invalidRowKey, int invalidColIndex, SheetColumn invalidColumn, Object invalidValue, String invalidMessage) {
        super();
        this.invalidRowKey = invalidRowKey;
        this.invalidColIndex = invalidColIndex;
        this.invalidColumn = invalidColumn;
        this.invalidValue = invalidValue;
        this.invalidMessage = invalidMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SheetInvalidUpdate)) {
            return false;
        }
        SheetInvalidUpdate that = (SheetInvalidUpdate) o;
        return getInvalidColIndex() == that.getInvalidColIndex() &&
                    Objects.equals(getInvalidRowKey(), that.getInvalidRowKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvalidRowKey(), getInvalidColIndex());
    }

    public Object getInvalidRowKey() {
        return invalidRowKey;
    }

    public void setInvalidRowKey(Object invalidRowKey) {
        this.invalidRowKey = invalidRowKey;
    }

    public int getInvalidColIndex() {
        return invalidColIndex;
    }

    public void setInvalidColIndex(int invalidColIndex) {
        this.invalidColIndex = invalidColIndex;
    }

    public SheetColumn getInvalidColumn() {
        return invalidColumn;
    }

    public void setInvalidColumn(SheetColumn invalidColumn) {
        this.invalidColumn = invalidColumn;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }

    public String getInvalidMessage() {
        return invalidMessage;
    }

    public void setInvalidMessage(String invalidMessage) {
        this.invalidMessage = invalidMessage;
    }

}
