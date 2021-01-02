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
package org.primefaces.extensions.showcase.model.dynaform;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InventoryProperty
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class InventoryProperty implements Serializable {

    private static final long serialVersionUID = 20120521L;

    private String position;
    private Object value;
    private boolean required;

    public InventoryProperty(String position, boolean required) {
        this.position = position;
        this.required = required;
    }

    public InventoryProperty(String position, Object value, boolean required) {
        this.position = position;
        this.value = value;
        this.required = required;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Object getValue() {
        return value;
    }

    public Object getFormattedValue() {
        if (value instanceof Date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

            return simpleDateFormat.format(value);
        }

        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
