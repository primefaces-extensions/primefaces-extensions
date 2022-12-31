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
