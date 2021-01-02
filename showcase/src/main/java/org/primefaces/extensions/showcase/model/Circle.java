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
package org.primefaces.extensions.showcase.model;

import java.io.Serializable;

/**
 * Circle
 *
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class Circle implements Serializable {

    private static final long serialVersionUID = 20111020L;

    private int radius;
    private String backgroundColor;
    private String borderColor;
    private double scaleFactor;

    public final int getRadius() {
        return radius;
    }

    public final void setRadius(int radius) {
        this.radius = radius;
    }

    public final String getBackgroundColor() {
        return backgroundColor;
    }

    public final void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public final String getBorderColor() {
        return borderColor;
    }

    public final void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public final double getScaleFactor() {
        return scaleFactor;
    }

    public final void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
