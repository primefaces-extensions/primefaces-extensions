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

import java.util.Objects;

import org.primefaces.component.api.SavedState;

/**
 * Keeps state of a component implementing {@link javax.faces.component.EditableValueHolder}. This class is used in
 * {@link org.primefaces.extensions.component.base.AbstractDynamicData}.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class SavedEditableValueState extends SavedState {

    private static final long serialVersionUID = 20120425L;

    private transient Object labelValue;

    private transient Object disabled;

    public void reset() {
        setSubmittedValue(null);
        setValid(true);
        setValue(null);
        setLocalValueSet(false);
    }

    public Object getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(Object labelValue) {
        this.labelValue = labelValue;
    }

    public Object getDisabled() {
        return disabled;
    }

    public void setDisabled(Object disabled) {
        this.disabled = disabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(disabled, labelValue);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof SavedEditableValueState)) {
            return false;
        }
        final SavedEditableValueState other = (SavedEditableValueState) obj;
        return Objects.equals(disabled, other.disabled) && Objects.equals(labelValue, other.labelValue);
    }
}
