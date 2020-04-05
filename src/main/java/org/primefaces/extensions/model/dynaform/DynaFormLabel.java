/**
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
package org.primefaces.extensions.model.dynaform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private boolean escape = true;
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("value", value)
                    .append("escape", escape)
                    .append("forControl",
                                forControl != null ? forControl.getKey() : "null")
                    .append("colspan", getColspan())
                    .append("rowspan", getRowspan()).append("row", getRow())
                    .append("column", getColumn())
                    .append("extended", isExtended()).toString();
    }
}
