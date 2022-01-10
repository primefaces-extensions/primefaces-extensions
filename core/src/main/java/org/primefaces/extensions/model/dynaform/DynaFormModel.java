/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model class for <code>DynaForm</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormModel implements Serializable {

    private static final long serialVersionUID = 20120514L;

    private final String uuid;
    private final List<DynaFormRow> regularRows = new ArrayList<>();
    private List<DynaFormRow> extendedRows = null;
    private final List<DynaFormLabel> labels = new ArrayList<>();
    private final List<DynaFormControl> controls = new ArrayList<>();

    public DynaFormModel() {
        uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public List<DynaFormRow> getRegularRows() {
        return regularRows;
    }

    public List<DynaFormRow> getExtendedRows() {
        return extendedRows;
    }

    public List<DynaFormControl> getControls() {
        return controls;
    }

    public List<DynaFormLabel> getLabels() {
        return labels;
    }

    /**
     * Creates a new regular row.
     *
     * @return {@link DynaFormRow}
     */
    public DynaFormRow createRegularRow() {
        final DynaFormRow dynaFormRow = new DynaFormRow(regularRows.size() + 1, false, this);
        regularRows.add(dynaFormRow);

        return dynaFormRow;
    }

    /**
     * Creates a new extended row.
     *
     * @return {@link DynaFormRow}
     */
    public DynaFormRow createExtendedRow() {
        if (extendedRows == null) {
            extendedRows = new ArrayList<>();
        }

        final DynaFormRow dynaFormRow = new DynaFormRow(extendedRows.size() + 1, true, this);
        extendedRows.add(dynaFormRow);

        return dynaFormRow;
    }

    /**
     * Removes the passed regular row.
     *
     * @param rowToBeRemoved {@link DynaFormRow} to be removed
     */
    public void removeRegularRow(final DynaFormRow rowToBeRemoved) {
        final int idx = rowToBeRemoved != null ? regularRows.indexOf(rowToBeRemoved) : -1;
        if (idx >= 0) {
            removeRow(regularRows, rowToBeRemoved, idx);
        }
    }

    /**
     * Removes the regular row by its index (position in the list).
     *
     * @param idx index of the row to be removed
     */
    public void removeRegularRow(final int idx) {
        DynaFormRow rowToBeRemoved = null;
        if (0 <= idx && idx < regularRows.size()) {
            rowToBeRemoved = regularRows.get(idx);
        }

        if (rowToBeRemoved != null) {
            removeRow(regularRows, rowToBeRemoved, idx);
        }
    }

    /**
     * Removes the passed extended row.
     *
     * @param rowToBeRemoved {@link DynaFormRow} to be removed
     */
    public void removeExtendedRow(final DynaFormRow rowToBeRemoved) {
        final int idx = rowToBeRemoved != null ? extendedRows.indexOf(rowToBeRemoved) : -1;
        if (idx >= 0) {
            removeRow(extendedRows, rowToBeRemoved, idx);
        }
    }

    /**
     * Removes the extended row by its index (position in the list).
     *
     * @param idx index of the row to be removed
     */
    public void removeExtendedRow(final int idx) {
        DynaFormRow rowToBeRemoved = null;
        if (0 <= idx && idx < extendedRows.size()) {
            rowToBeRemoved = extendedRows.get(idx);
        }

        if (rowToBeRemoved != null) {
            removeRow(extendedRows, rowToBeRemoved, idx);
        }
    }

    private void removeRow(final List<DynaFormRow> rows, final DynaFormRow rowToBeRemoved, final int idx) {
        final List<DynaFormControl> controlsToBeRemoved = new ArrayList<>();
        final List<DynaFormLabel> labelsToBeRemoved = new ArrayList<>();
        for (final AbstractDynaFormElement element : rowToBeRemoved.getElements()) {
            if (element instanceof DynaFormControl) {
                controlsToBeRemoved.add((DynaFormControl) element);
            }
            else if (element instanceof DynaFormLabel) {
                labelsToBeRemoved.add((DynaFormLabel) element);
            }
        }

        controls.removeAll(controlsToBeRemoved);
        labels.removeAll(labelsToBeRemoved);
        for (final DynaFormLabel label : labels) {
            if (label.getForControl() != null && controlsToBeRemoved.contains(label.getForControl())) {
                // control was removed ==> label should not reference this control anymore
                label.setForControl(null);
            }
        }

        rows.remove(rowToBeRemoved);

        // re-index rows, so that the new row's IDs will be generated correct
        int row = idx;
        final List<DynaFormRow> rowsToBeAdjusted = rows.subList(idx, rows.size());
        for (final DynaFormRow dynaFormRow : rowsToBeAdjusted) {
            ++row;
            dynaFormRow.setRow(row);
            for (final AbstractDynaFormElement element : dynaFormRow.getElements()) {
                element.setRow(row);
                if (element instanceof DynaFormControl) {
                    final DynaFormControl control = (DynaFormControl) element;
                    final int delta = rowToBeRemoved.getElements().size();
                    control.setPosition(control.getPosition() - delta);
                    control.generateKey();
                }
            }
        }
    }

    public boolean isExistExtendedGrid() {
        return getExtendedRows() != null && !getExtendedRows().isEmpty();
    }
}
