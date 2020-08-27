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
 */

package org.primefaces.extensions.showcase.controller.dynaform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.dynaform.DynaForm;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.extensions.showcase.model.dynaform.Condition;
import org.primefaces.extensions.util.visitcallback.ExecutableVisitCallback;
import org.primefaces.extensions.util.visitcallback.VisitTaskExecutor;

/**
 * ClearDynaFormController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ClearDynaFormController implements Serializable {

	private static final long serialVersionUID = 20130504L;

	public static final Set<VisitHint> VISIT_HINTS = EnumSet.of(VisitHint.SKIP_UNRENDERED);

	private DynaFormModel model;
	private List<Condition> conditions;

	@PostConstruct
	protected void initialize() {
		model = new DynaFormModel();
		conditions = new ArrayList<Condition>();

		// 1. condition and row
		Condition condition = new Condition("model", 2, "eq", "mercedes", 0);
		conditions.add(condition);

		DynaFormRow row = model.createRegularRow();
		row.addControl(condition, "column");
		row.addControl(condition, "offset");
		row.addControl(condition, "operator");
		row.addControl(condition, "value");
		row.addControl(condition, "clear");

		// 2. condition and row
		condition = new Condition("manufacturer", 1, "not", "chrysler group", 1);
		conditions.add(condition);

		row = model.createRegularRow();
		row.addControl(condition, "column");
		row.addControl(condition, "offset");
		row.addControl(condition, "operator");
		row.addControl(condition, "value");
		row.addControl(condition, "clear");

		// 3. condition and row
		condition = new Condition("year", 0, "lt", "2010", 2);
		conditions.add(condition);

		row = model.createRegularRow();
		row.addControl(condition, "column");
		row.addControl(condition, "offset");
		row.addControl(condition, "operator");
		row.addControl(condition, "value");
		row.addControl(condition, "clear");
	}

	public void clearInputs(final int index) {
		final FacesContext fc = FacesContext.getCurrentInstance();
		final DynaForm dynaForm = (DynaForm) fc.getViewRoot().findComponent(":mainForm:dynaForm");

		// Ids of components to be visited
		final String[] ids = new String[] { "tableColumn", "inputValue", "inputOffset", "valueOperator" };

		final VisitTaskExecutor visitTaskExecutor = new ClearInputsExecutor(fc.getELContext(), ids, index);

		// clear inputs in the visit callback
		final ExecutableVisitCallback visitCallback = new ExecutableVisitCallback(visitTaskExecutor);
		dynaForm.visitTree(VisitContext.createVisitContext(fc, null, VISIT_HINTS), visitCallback);
	}

	public void removeCondition(final Condition condition) {
		model.removeRegularRow(condition.getIndex());
		conditions.remove(condition);

		// re-index conditions
		int idx = 0;
		for (final Condition cond : conditions) {
			cond.setIndex(idx);
			idx++;
		}
	}

	public String getConditions() {
		final StringBuilder sb = new StringBuilder();
		for (final Condition condition : conditions) {
			sb.append(condition.toString());
			sb.append("<br/>");
		}

		return sb.toString();
	}

	public DynaFormModel getModel() {
		return model;
	}
}
