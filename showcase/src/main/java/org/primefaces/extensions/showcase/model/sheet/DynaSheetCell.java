/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.model.sheet;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model object to represent a single cell in sheet component
 */
public class DynaSheetCell implements Comparable<DynaSheetCell>, Serializable {

    private static final long serialVersionUID = 1L;

    private Integer value;
    private String style;
    private Integer hourOfDay;

    public static DynaSheetCell create(final Integer hourOfDay, final Integer value) {
        final DynaSheetCell cell = new DynaSheetCell();
        cell.setHourOfDay(hourOfDay);
        cell.setValue(value);
        cell.setStyle(StringUtils.EMPTY);
        if (hourOfDay == 2) {
            cell.setStyle("cell-blue");
        }
        if (hourOfDay == 3) {
            cell.setStyle("cell-orange");
        }
        return cell;
    }

    /**
     * Gets the {@link #hourOfDay}.
     *
     * @return Returns the {@link #hourOfDay}.
     */
    public Integer getHourOfDay() {
        return hourOfDay;
    }

    /**
     * Sets the {@link #hourOfDay}.
     *
     * @param hourOfDay The {@link #hourOfDay} to set.
     */
    public void setHourOfDay(final Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    /**
     * Gets the {@link #value}.
     *
     * @return Returns the {@link #value}.
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Sets the {@link #value}.
     *
     * @param value The {@link #value} to set.
     */
    public void setValue(final Integer value) {
        this.value = value;
    }

    /**
     * Gets the {@link #style}.
     *
     * @return Returns the {@link #style}.
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the {@link #style}.
     *
     * @param style The {@link #style} to set.
     */
    public void setStyle(final String style) {
        this.style = style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final DynaSheetCell other) {
        return new CompareToBuilder().append(value, other.value).toComparison();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof DynaSheetCell)) {
            return false;
        }
        final DynaSheetCell castOther = (DynaSheetCell) other;
        return new EqualsBuilder().append(getValue(), castOther.getValue())
                    .append(getHourOfDay(), castOther.getHourOfDay()).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getValue()).append(getHourOfDay())
                    .toHashCode();
    }

}
