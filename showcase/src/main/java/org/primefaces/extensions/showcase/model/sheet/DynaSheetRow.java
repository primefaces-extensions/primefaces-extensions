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
package org.primefaces.extensions.showcase.model.sheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model object to represent a row in sheet component.
 */
public class DynaSheetRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private List<DynaSheetCell> cells = new ArrayList<>();
    private boolean readOnly;

    /**
     * Gets the {@link #id}.
     *
     * @return Returns the {@link #id}.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the {@link #id}.
     *
     * @param id The {@link #id} to set.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the {@link #cells}.
     *
     * @return Returns the {@link #cells}.
     */
    public List<DynaSheetCell> getCells() {
        return cells;
    }

    /**
     * Sets the {@link #cells}.
     *
     * @param cells The {@link #cells} to set.
     */
    public void setCells(final List<DynaSheetCell> cells) {
        this.cells = cells;
    }

    /**
     * Gets the {@link #readOnly}.
     *
     * @return Returns the {@link #readOnly}.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the {@link #readOnly}.
     *
     * @param readOnly The {@link #readOnly} to set.
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

}
