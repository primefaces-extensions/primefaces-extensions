/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
