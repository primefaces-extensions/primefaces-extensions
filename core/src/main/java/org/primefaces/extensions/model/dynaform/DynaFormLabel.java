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
package org.primefaces.extensions.model.dynaform;

import java.util.Objects;

/**
 * Class representing a label inside of <code>DynaForm</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormLabel extends AbstractDynaFormElement {

    private static final long serialVersionUID = 1L;

    private final String value;
    private final boolean escape;
    private DynaFormControl forControl;
    private String targetClientId;
    private boolean targetRequired = false;
    private boolean targetValid = true;
    private String styleClass;

    public DynaFormLabel(String value, boolean escape, int colspan, int rowspan, int row, int column, boolean extended) {
        super(colspan, rowspan, row, column, extended);

        this.value = value;
        this.escape = escape;
    }

    public String getValue() {
        return value;
    }

    public boolean isEscape() {
        return escape;
    }

    public DynaFormControl getForControl() {
        return forControl;
    }

    public void setForControl(DynaFormControl forControl) {
        this.forControl = forControl;
    }

    public String getTargetClientId() {
        return targetClientId;
    }

    public void setTargetClientId(String targetClientId) {
        this.targetClientId = targetClientId;
    }

    public boolean isTargetRequired() {
        return targetRequired;
    }

    public void setTargetRequired(boolean targetRequired) {
        this.targetRequired = targetRequired;
    }

    public boolean isTargetValid() {
        return targetValid;
    }

    public void setTargetValid(boolean targetValid) {
        this.targetValid = targetValid;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(final String styleClass) {
        this.styleClass = styleClass;
    }

    @Override
    public String toString() {
        return "DynaFormLabel{" + "value=" + value + ", escape=" + escape + ", forControl=" + forControl
                    + ", targetClientId=" + targetClientId + ", targetRequired=" + targetRequired
                    + ", targetValid=" + targetValid + ", styleClass=" + styleClass + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DynaFormLabel)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DynaFormLabel that = (DynaFormLabel) o;
        return isEscape() == that.isEscape() &&
                    isTargetRequired() == that.isTargetRequired() &&
                    isTargetValid() == that.isTargetValid() &&
                    Objects.equals(getValue(), that.getValue()) &&
                    Objects.equals(getTargetClientId(), that.getTargetClientId()) &&
                    Objects.equals(getStyleClass(), that.getStyleClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue(), isEscape(), getTargetClientId(), isTargetRequired(), isTargetValid(), getStyleClass());
    }
}
