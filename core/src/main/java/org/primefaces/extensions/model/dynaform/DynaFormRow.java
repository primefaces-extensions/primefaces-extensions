/*
 * Copyright 2011-2021 PrimeFaces Extensions
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

/**
 * Class representing a row inside of <code>DynaForm</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormRow implements Serializable {

    private static final long serialVersionUID = 20120514L;

    private int row;
    private boolean extended;
    private int totalColspan = 0;
    private DynaFormModel dynaFormModel;

    // all elements (label and controls) in this row
    private final List<AbstractDynaFormElement> elements = new ArrayList<>();

    /**
     * This constructor is required for serialization. Please do not remove.
     */
    public DynaFormRow() {
    }

    public DynaFormRow(final int row, final boolean extended, final DynaFormModel dynaFormModel) {
        this.row = row;
        this.extended = extended;
        this.dynaFormModel = dynaFormModel;
    }

    /**
     * Adds control with data, default type, colspan = 1 and rowspan = 1.
     *
     * @param data data object
     * @return DynaFormControl added control
     */
    public DynaFormControl addControl(final Serializable data) {
        return addControl(data, DynaFormControl.DEFAULT_TYPE, 1, 1);
    }

    /**
     * Adds control with given data, type, colspan = 1 and rowspan = 1.
     *
     * @param data data object
     * @param type type to match the type attribute in pe:dynaFormControl
     * @return DynaFormControl added control
     */
    public DynaFormControl addControl(final Serializable data, final String type) {
        return addControl(data, type, 1, 1);
    }

    /**
     * Adds control with given data, default type, colspan and rowspan.
     *
     * @param data data object
     * @param colspan colspan
     * @param rowspan rowspan
     * @return DynaFormControl added control
     */
    public DynaFormControl addControl(final Serializable data, final int colspan, final int rowspan) {
        return addControl(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan);
    }

    /**
     * Adds control with given data, type, colspan and rowspan.
     *
     * @param data data object
     * @param type type to match the type attribute in pe:dynaFormControl
     * @param colspan colspan
     * @param rowspan rowspan
     * @return DynaFormControl added control
     */
    public DynaFormControl addControl(final Serializable data, final String type, final int colspan, final int rowspan) {
        final DynaFormControl dynaFormControl = new DynaFormControl(data,
                    type,
                    colspan,
                    rowspan,
                    row,
                    elements.size() + 1,
                    dynaFormModel.getControls().size() + 1,
                    extended);

        elements.add(dynaFormControl);
        dynaFormModel.getControls().add(dynaFormControl);
        totalColspan = totalColspan + colspan;

        return dynaFormControl;
    }

    /***
     * Adds nested model with colspan = 1 and rowspan = 1.
     *
     * @param model the DynaFormModel
     * @return DynaFormModelElement added model
     */
    public DynaFormModelElement addModel(final DynaFormModel model) {
        return addModel(model, 1, 1);
    }

    /***
     * Adds nested model with given colspan and rowspan.
     *
     * @param model
     * @param colspan
     * @param rowspan
     * @return DynaFormModelElement added model
     */
    public DynaFormModelElement addModel(final DynaFormModel model, final int colspan, final int rowspan) {
        final DynaFormModelElement nestedModel = new DynaFormModelElement(model,
                    colspan,
                    rowspan,
                    row,
                    elements.size() + 1,
                    dynaFormModel.getControls().size() + 1,
                    extended);

        elements.add(nestedModel);
        dynaFormModel.getControls().addAll(model.getControls());
        totalColspan = totalColspan + colspan;

        return nestedModel;
    }

    /**
     * Adds a label with given text, colspan = 1 and rowspan = 1.
     *
     * @param value label text
     * @return DynaFormLabel added label
     */
    public DynaFormLabel addLabel(final String value) {
        return addLabel(value, true, 1, 1);
    }

    /**
     * Adds a label with given text, colspan and rowspan.
     *
     * @param value label text
     * @param colspan colspan
     * @param rowspan rowspan
     * @return DynaFormLabel added label
     */
    public DynaFormLabel addLabel(final String value, final int colspan, final int rowspan) {
        return addLabel(value, true, colspan, rowspan);
    }

    /**
     * Adds a label with given text, escape flag, colspan and rowspan.
     *
     * @param value label text
     * @param escape boolean flag if the label text should escaped or not
     * @param colspan colspan
     * @param rowspan rowspan
     * @return DynaFormLabel added label
     */
    public DynaFormLabel addLabel(final String value, final boolean escape, final int colspan, final int rowspan) {
        final DynaFormLabel dynaFormLabel = new DynaFormLabel(value, escape, colspan, rowspan, row, elements.size() + 1, extended);

        elements.add(dynaFormLabel);
        dynaFormModel.getLabels().add(dynaFormLabel);
        totalColspan = totalColspan + colspan;

        return dynaFormLabel;
    }

    public List<AbstractDynaFormElement> getElements() {
        return elements;
    }

    public int getTotalColspan() {
        return totalColspan;
    }

    void setRow(final int row) {
        this.row = row;
    }
}
