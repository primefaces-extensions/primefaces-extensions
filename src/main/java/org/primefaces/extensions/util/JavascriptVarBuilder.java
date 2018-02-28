/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
package org.primefaces.extensions.util;

import org.primefaces.util.ComponentUtils;

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
    public JavascriptVarBuilder(String varName, boolean isObject) {
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
    public JavascriptVarBuilder appendProperty(String propertyName, String propertyValue, boolean quoted) {
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
    public JavascriptVarBuilder appendRowColProperty(int row, int col, String propertyValue, boolean quoted) {
        return appendProperty("r" + row + "_c" + col, propertyValue, quoted);
    }

    /**
     * Appends text to the var string
     *
     * @param value the value to append
     * @param quoted if true, the value is quoted and escaped.
     * @return this builder
     */
    public JavascriptVarBuilder appendText(String value, boolean quoted) {
        if (quoted) {
            sb.append("\"");
            if (value != null) {
                sb.append(ComponentUtils.escapeEcmaScriptText(value));
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
    public JavascriptVarBuilder appendArrayValue(String value, boolean quoted) {
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
