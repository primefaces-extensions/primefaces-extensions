/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
    private final List<DynaFormRow> regularRows = new ArrayList<DynaFormRow>();
    private List<DynaFormRow> extendedRows = null;
    private final List<DynaFormLabel> labels = new ArrayList<DynaFormLabel>();
    private final List<DynaFormControl> controls = new ArrayList<DynaFormControl>();

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
            extendedRows = new ArrayList<DynaFormRow>();
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
        final List<DynaFormControl> controlsToBeRemoved = new ArrayList<DynaFormControl>();
        final List<DynaFormLabel> labelsToBeRemoved = new ArrayList<DynaFormLabel>();
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
