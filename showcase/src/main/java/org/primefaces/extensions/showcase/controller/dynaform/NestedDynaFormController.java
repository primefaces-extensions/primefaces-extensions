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
