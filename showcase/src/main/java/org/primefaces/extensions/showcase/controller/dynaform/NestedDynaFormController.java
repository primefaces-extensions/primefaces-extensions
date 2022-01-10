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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.extensions.showcase.model.dynaform.BookProperty;

/**
 * AnotherDynaFormController
 *
 * @author Sébastien Lepage / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class NestedDynaFormController implements Serializable {

    private static final long serialVersionUID = 20120423L;

    private DynaFormModel model;

    @PostConstruct
    protected void initialize() {
        model = new DynaFormModel();

        // add rows, labels and editable controls
        // set relationship between label and editable controls to support outputLabel
        // with "for" attribute

        // 1. regular row
        DynaFormRow row = model.createRegularRow();

        // 1.1 Nested model
        DynaFormModel nestedModel = new DynaFormModel();
        DynaFormRow nestedRow1 = nestedModel.createRegularRow();

        final DynaFormLabel label11 = nestedRow1.addLabel("Firstname");
        final DynaFormLabel label12 = nestedRow1.addLabel("Lastname");

        nestedRow1 = nestedModel.createRegularRow();
        final DynaFormControl control21 = nestedRow1.addControl(new BookProperty("firstname", true), "input");
        final DynaFormControl control22 = nestedRow1.addControl(new BookProperty("lastname", true), "input");

        label11.setForControl(control21);
        label12.setForControl(control22);
        row.addModel(nestedModel, 2, 1);

        // 2. regular row
        row = model.createRegularRow();
        final DynaFormLabel label31 = row.addLabel("Date from");

        // 2.1 Nested model
        nestedModel = new DynaFormModel();
        final DynaFormRow nestedRow2 = nestedModel.createRegularRow();

        final DynaFormControl control32 = nestedRow2.addControl(new BookProperty("beginDate", false), "date");
        label31.setForControl(control32);

        final DynaFormLabel label33 = nestedRow2.addLabel("till");
        final DynaFormControl control34 = nestedRow2.addControl(new BookProperty("endDate", false), "date");
        label33.setForControl(control34);
        row.addModel(nestedModel);

        // 2.2 Nested model
        nestedModel = new DynaFormModel();
        final DynaFormRow nestedRow3 = nestedModel.createRegularRow();

        final DynaFormLabel label36 = nestedRow3.addLabel("Located from");
        final DynaFormControl control37 = nestedRow3.addControl(new BookProperty("locationStart", "Lane A", false),
                    "input");
        label36.setForControl(control37);

        final DynaFormLabel label38 = nestedRow3.addLabel("To");
        final DynaFormControl control39 = nestedRow3.addControl(new BookProperty("locationEnd", "Lane B", false),
                    "input");
        label38.setForControl(control39);

        row.addModel(nestedModel);
    }

    public DynaFormModel getModel() {
        return model;
    }

    public String submitForm() {
        final FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
        final boolean hasErrors = sev != null && FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0;

        PrimeFaces.current().ajax().addCallbackParam("isValid", !hasErrors);

        return null;
    }

    public List<BookProperty> getBookProperties() {
        if (model == null) {
            return null;
        }

        final List<BookProperty> bookProperties = new ArrayList<BookProperty>();
        for (final DynaFormControl dynaFormControl : model.getControls()) {
            bookProperties.add((BookProperty) dynaFormControl.getData());
        }

        return bookProperties;
    }
}
