/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
 *
 * $Id$
 */

package org.primefaces.extensions.showcase.controller.dynaform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.extensions.showcase.model.dynaform.FormField;

/**
 * AnotherDynaFormController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class AnotherDynaFormController implements Serializable {

	private static final long serialVersionUID = 20120423L;

	private DynaFormModel modelOne;

	private DynaFormModel modelTwo;

	private boolean showModelOne = true;

	public DynaFormModel getModel() {
		return showModelOne ? getModelOne() : getModelTwo();
	}

	public DynaFormModel getModelOne() {
		if (modelOne != null) {
			return modelOne;
		}

		modelOne = new DynaFormModel();

		// add rows, labels and editable controls
		// set relationship between label and editable controls to support outputLabel
		// with "for" attribute

		// 1. regular row
		DynaFormRow row = modelOne.createRegularRow();

		final DynaFormLabel label11 = row.addLabel("Short Name", 1, 1);
		final DynaFormLabel label12 = row.addLabel("Street", 2, 1);

		// 2. regular row
		row = modelOne.createRegularRow();

		final DynaFormControl control21 = row.addControl(new FormField(true), "name", 1, 1);
		final DynaFormControl control22 = row.addControl(new FormField(false), "address", 2, 1);

		label11.setForControl(control21);
		label12.setForControl(control22);

		// 3. regular row
		row = modelOne.createRegularRow();

		final DynaFormLabel label31 = row.addLabel("Long Name", 1, 1);
		final DynaFormLabel label32 = row.addLabel("Zip code, city", 2, 1);

		// 4. regular row
		row = modelOne.createRegularRow();

		final DynaFormControl control41 = row.addControl(new FormField(true), "name", 1, 1);
		final DynaFormControl control42 = row.addControl(new FormField(false), "address", 2, 1);

		label31.setForControl(control41);
		label32.setForControl(control42);

		// 5. regular row
		row = modelOne.createRegularRow();

		row.addControl("Make choice", "separator", 3, 1);

		// 6. regular row
		row = modelOne.createRegularRow();

		row.addControl("PrimeFaces fan?", 1, 1);
		row.addControl("Married?", 1, 1);
		row.addControl("Children", 1, 1);

		// 7. regular row
		row = modelOne.createRegularRow();

		final List<SelectItem> pffan = new ArrayList<SelectItem>();
		pffan.add(new SelectItem("Sure", "Sure"));
		pffan.add(new SelectItem("Don't know", "Don't know"));
		row.addControl(new FormField("Sure", false, pffan), "radiochoice", 1, 1);

		row.addControl(new FormField(false, false), "booleanchoice", 1, 1);

		final List<SelectItem> children = new ArrayList<SelectItem>();
		children.add(new SelectItem("No", "No"));
		children.add(new SelectItem("1", "1"));
		children.add(new SelectItem("2", "2"));
		children.add(new SelectItem("More", "More"));
		row.addControl(new FormField("No", false, children), "radiochoice", 1, 1);

		// 1. extended row
		row = modelOne.createExtendedRow();

		row.addControl("Notes", 3, 1);

		// 2. extended row
		row = modelOne.createExtendedRow();

		row.addControl(new FormField("Hallo DynaForm!", false), "editor", 3, 1);

		return modelOne;
	}

	public DynaFormModel getModelTwo() {
		if (modelTwo != null) {
			return modelTwo;
		}

		modelTwo = new DynaFormModel();

		// add rows, labels and editable controls
		// set relationship between label and editable controls to support outputLabel
		// with "for" attribute

		// 1. regular row
		DynaFormRow row = modelTwo.createRegularRow();

		row.addControl("Audio Analog Output Volume", "separator", 3, 1);

		// 2. regular row
		row = modelTwo.createRegularRow();

		row.addControl("Port 1", 1, 1);
		row.addControl("Port 2", 1, 1);
		row.addControl("Port 3", 1, 1);

		// 3. regular row
		row = modelTwo.createRegularRow();

		row.addControl(new FormField(60, false), "audioslider", 1, 1);
		row.addControl(new FormField(0, false), "audioslider", 1, 1);
		row.addControl(new FormField(0, false), "audioslider", 1, 1);

		// 4. regular row
		row = modelTwo.createRegularRow();

		row.addControl("Audio Digital Output Volume", "separator", 3, 1);

		// 5. regular row
		row = modelTwo.createRegularRow();

		row.addControl("Port 4", 1, 1);
		row.addControl("Port 5", 1, 1);
		row.addControl("Port 6", 1, 1);

		// 6. regular row
		row = modelTwo.createRegularRow();

		row.addControl(new FormField(10, false), "audioslider", 1, 1);
		row.addControl(new FormField(50, false), "audioslider", 1, 1);
		row.addControl(new FormField(50, false), "audioslider", 1, 1);

		// 7. regular row
		row = modelTwo.createRegularRow();

		row.addControl("HDMI Output Volume", "separator", 3, 1);

		// 8. regular row
		row = modelTwo.createRegularRow();

		row.addControl("Port 1", 1, 1);
		row.addControl("Port 2", 1, 1);
		row.addControl("Port 3", 1, 1);

		// 9. regular row
		row = modelTwo.createRegularRow();

		row.addControl(new FormField(20, false), "audioslider", 1, 1);
		row.addControl(new FormField(80, false), "audioslider", 1, 1);
		row.addControl(new FormField(40, false), "audioslider", 1, 1);

		return modelTwo;
	}

	public String switchModel() {
		showModelOne = !showModelOne;

		// reset models (simulate new loading)
		if (showModelOne) {
			modelOne = null;
		} else {
			modelTwo = null;
		}

		return null;
	}

	public List<String> complete(final String query) {
		final List<String> results = new ArrayList<String>();

		char letter;
		for (letter = 'a'; letter <= 'm'; letter++) {
			results.add(query + letter);
		}

		return results;
	}

	public String getFormFields() {
		final DynaFormModel model = showModelOne ? getModelOne() : getModelTwo();

		if (model == null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		for (final DynaFormControl dynaFormControl : model.getControls()) {
			if (dynaFormControl.getData() instanceof FormField) {
				final FormField ff = (FormField) dynaFormControl.getData();
				if (ff.getValue() != null && StringUtils.isNotBlank(ff.getValue().toString())) {
					sb.append(ff.getValue());
					sb.append("<br/>");
				}
			}
		}

		return sb.toString();
	}

	public String submitForm() {
		final FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
		final boolean hasErrors = sev != null && FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0;

		PrimeFaces.current().ajax().addCallbackParam("isValid", !hasErrors);

		return null;
	}
}
