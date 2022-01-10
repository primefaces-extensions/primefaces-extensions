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
package org.primefaces.extensions.showcase.controller.dynaform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.extensions.showcase.model.dynaform.FormField;

/**
 * ContainerDynaFormController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ContainerDynaFormController implements Serializable {

    private static final long serialVersionUID = 20130426L;

    private DynaFormModel model;
    private List<SelectItem> list;
    private FormField selectedField;
    private String selectedItem;
    private String containerClientId; // container clientId of the currently handled input field

    @PostConstruct
    protected void initialize() {
        list = new ArrayList<SelectItem>();
        list.add(new SelectItem("Item 1", "Item 1"));
        list.add(new SelectItem("Item 2", "Item 2"));
        list.add(new SelectItem("Item 3", "Item 3"));
        list.add(new SelectItem("Item 4", "Item 4"));

        model = new DynaFormModel();

        // add rows, labels and editable controls
        // set relationship between label and editable controls to support outputLabel
        // with "for" attribute

        // 1. row
        DynaFormRow row = model.createRegularRow();

        final DynaFormLabel label11 = row.addLabel("Input 1");
        final DynaFormControl control12 = row.addControl(new FormField("Some text"));
        label11.setForControl(control12);

        final DynaFormLabel label13 = row.addLabel("Input 2");
        final DynaFormControl control14 = row.addControl(new FormField("Some text"));
        label13.setForControl(control14);

        // 2. row
        row = model.createRegularRow();

        final DynaFormLabel label21 = row.addLabel("Input 3");
        final DynaFormControl control22 = row.addControl(new FormField("Some text"));
        label21.setForControl(control22);

        final DynaFormLabel label23 = row.addLabel("Input 4");
        final DynaFormControl control24 = row.addControl(new FormField("Some text"));
        label23.setForControl(control24);
    }

    public void updateSelection() {
        final FacesContext fc = FacesContext.getCurrentInstance();

        // update selected item value for the currently handled field
        selectedField.setValue(selectedItem);

        // update the corresponding input field in UI
        PrimeFaces.current().ajax().update(containerClientId + UINamingContainer.getSeparatorChar(fc) + "txt");
    }

    public List<String> getValues() {
        if (model == null) {
            return null;
        }

        final List<String> values = new ArrayList<String>();
        for (final DynaFormLabel dynaFormLabel : model.getLabels()) {
            values.add(
                        dynaFormLabel.getValue() + ": " + ((FormField) dynaFormLabel.getForControl().getData()).getValue());
        }

        return values;
    }

    public DynaFormModel getModel() {
        return model;
    }

    public List<SelectItem> getList() {
        return list;
    }

    public FormField getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(final FormField selectedField) {
        this.selectedField = selectedField;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(final String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getContainerClientId() {
        return containerClientId;
    }

    public void setContainerClientId(final String containerClientId) {
        this.containerClientId = containerClientId;
    }
}
