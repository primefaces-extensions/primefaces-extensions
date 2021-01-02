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
package org.primefaces.extensions.util;

import org.primefaces.util.EscapeUtils;

/**
 * Builds a JavaScript var object or array string. A simple way to generalized a lot of code used in renderers.
 *
 * @author Mark Lassiter / Melloware
 * @since 6.2
 */
public class JavascriptVarBuilder {

    private final StringBuilder sb = new StringBuilder();

    private final boolean isObject;

    private boolean firstValue = true;

    private final boolean isVar;

    /**
     * Constructs an instance of the builder.
     *
     * @param varName the variable name
     * @param isObject true if build an Object, false if an array.
     */
    public JavascriptVarBuilder(final String varName, final boolean isObject) {
        this.isObject = isObject;
        isVar = varName != null;
        if (isVar) {
            sb.append("var ");
            sb.append(varName);
            sb.append("=");
        }
        if (isObject) {
            sb.append("{");
        }
        else {
            sb.append("[");
        }
    }

    /**
     * Called internally to prepare for next value
     */
    private void next() {
        if (firstValue) {
            firstValue = false;
        }
        else {
            sb.append(",");
        }
    }

    /**
     * Appends an Object name/value pair to the object.
     *
     * @param propertyName the property name
     * @param propertyValue the property value
     * @param quoted if true, the value is quoted and escaped.
     * @return this builder
     */
    public JavascriptVarBuilder appendProperty(final String propertyName, final String propertyValue, final boolean quoted) {
        next();
        sb.append(propertyName);
        sb.append(":");
        appendText(propertyValue, quoted);
        return this;
    }

    /**
     * appends a property with the name "rYY_cXX" where YY is the row and XX is he column.
     *
     * @param row
     * @param col
     * @param propertyValue
     * @param quoted
     * @return
     */
    public JavascriptVarBuilder appendRowColProperty(final int row, final int col, final String propertyValue, final boolean quoted) {
        return appendProperty("r" + row + "_c" + col, propertyValue, quoted);
    }

    /**
     * Appends text to the var string
     *
     * @param value the value to append
     * @param quoted if true, the value is quoted and escaped.
     * @return this builder
     */
    public JavascriptVarBuilder appendText(final String value, final boolean quoted) {
        if (quoted) {
            sb.append("\"");
            if (value != null) {
                sb.append(EscapeUtils.forJavaScript(value));
            }
            sb.append("\"");
        }
        else if (value != null) {
            sb.append(value);
        }
        return this;
    }

    /**
     * Appends an array value.
     *
     * @param value
     * @param quoted
     * @return
     */
    public JavascriptVarBuilder appendArrayValue(final String value, final boolean quoted) {
        next();
        return appendText(value, quoted);
    }

    /**
     * Closes the array or object.
     *
     * @return
     */
    public JavascriptVarBuilder closeVar() {
        if (isObject) {
            sb.append("}");
        }
        else {
            sb.append("]");
        }

        if (isVar) {
            sb.append(";");
        }
        return this;
    }

    /**
     * Returns the string for the var.
     */
    @Override
    public String toString() {
        return sb.toString();
    }
}
