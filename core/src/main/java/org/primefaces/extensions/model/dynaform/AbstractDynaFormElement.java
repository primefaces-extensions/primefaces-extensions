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
package org.primefaces.extensions.model.dynaform;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract class representing a control (typically input element or label) inside of <code>DynaForm</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public abstract class AbstractDynaFormElement implements Serializable {

    private static final long serialVersionUID = 20120514L;

    private int colspan = 1;
    private int rowspan = 1;
    private int row;
    private int column;
    private boolean extended;

    /**
     * This constructor is required for serialization. Please do not remove.
     */
    public AbstractDynaFormElement() {
    }

    protected AbstractDynaFormElement(int colspan, int rowspan, int row, int column, boolean extended) {
        this.colspan = colspan;
        this.rowspan = rowspan;
        this.row = row;
        this.column = column;
        this.extended = extended;
    }

    public int getColspan() {
        return colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public int getRow() {
        return row;
    }

    void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isExtended() {
        return extended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDynaFormElement)) {
            return false;
        }
        AbstractDynaFormElement that = (AbstractDynaFormElement) o;
        return getRow() == that.getRow() &&
                    getColumn() == that.getColumn() &&
                    isExtended() == that.isExtended();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn(), isExtended());
    }
}
