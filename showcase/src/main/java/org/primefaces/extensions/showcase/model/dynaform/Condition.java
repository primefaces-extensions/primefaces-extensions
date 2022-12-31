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
package org.primefaces.extensions.showcase.model.dynaform;

import java.io.Serializable;

/**
 * Condition
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class Condition implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tableColumn;
    private int inputOffset;
    private String valueOperator;
    private String inputValue;
    private int index; // internal index in the list of all conditions

    public Condition() {
    }

    public Condition(String tableColumn, int inputOffset, String valueOperator, String inputValue, int index) {
        this.tableColumn = tableColumn;
        this.inputOffset = inputOffset;
        this.valueOperator = valueOperator;
        this.inputValue = inputValue;
        this.index = index;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public int getInputOffset() {
        return inputOffset;
    }

    public void setInputOffset(int inputOffset) {
        this.inputOffset = inputOffset;
    }

    public String getValueOperator() {
        return valueOperator;
    }

    public void setValueOperator(String valueOperator) {
        this.valueOperator = valueOperator;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Condition condition = (Condition) o;

        if (index != condition.index) {
            return false;
        }
        if (inputOffset != condition.inputOffset) {
            return false;
        }
        if (inputValue != null ? !inputValue.equals(condition.inputValue) : condition.inputValue != null) {
            return false;
        }
        if (tableColumn != null ? !tableColumn.equals(condition.tableColumn) : condition.tableColumn != null) {
            return false;
        }
        if (valueOperator != null ? !valueOperator.equals(condition.valueOperator) : condition.valueOperator != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = tableColumn != null ? tableColumn.hashCode() : 0;
        result = 31 * result + inputOffset;
        result = 31 * result + (valueOperator != null ? valueOperator.hashCode() : 0);
        result = 31 * result + (inputValue != null ? inputValue.hashCode() : 0);
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return "Condition {"
                    + "tableColumn='" + tableColumn + '\''
                    + ", inputOffset=" + inputOffset
                    + ", valueOperator='" + valueOperator + '\''
                    + ", inputValue='" + inputValue + '\''
                    + '}';
    }
}
