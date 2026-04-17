/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.model.metergroup;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model class for the MeterGroup item.
 *
 * @since 16.0.0
 */
public class MeterGroupItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label;
    private double value;
    private String color;
    private String icon;

    public MeterGroupItem() {
        // default constructor
    }

    public MeterGroupItem(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public MeterGroupItem(String label, double value, String color) {
        this(label, value);
        this.color = color;
    }

    public MeterGroupItem(String label, double value, String color, String icon) {
        this(label, value, color);
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeterGroupItem that = (MeterGroupItem) o;
        return Double.compare(that.value, value) == 0 &&
                    Objects.equals(label, that.label) &&
                    Objects.equals(color, that.color) &&
                    Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, value, color, icon);
    }
}
