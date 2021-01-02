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
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * FormField
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class FormField implements Serializable {

    private static final long serialVersionUID = 20120521L;

    private Object value;
    private boolean required;
    private List<SelectItem> selectItems;

    public FormField(Object value) {
        this.value = value;
    }

    public FormField(boolean required) {
        this.required = required;
    }

    public FormField(Object value, boolean required) {
        this.value = value;
        this.required = required;
    }

    public FormField(Object value, boolean required, List<SelectItem> selectItems) {
        this.value = value;
        this.required = required;
        this.selectItems = selectItems;
    }

    public Object getValue() {
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

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }
}
