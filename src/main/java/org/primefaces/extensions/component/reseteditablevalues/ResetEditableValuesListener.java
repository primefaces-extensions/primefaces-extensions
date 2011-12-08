/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.reseteditablevalues;

import java.util.List;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * {@link ActionListener} for the <code>ResetEditableValues</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class ResetEditableValuesListener implements ActionListener, StateHolder {

	private String components;

	/**
	 * This constructor is required for serialization. Please do not remove.
	 */
	public ResetEditableValuesListener() {
	}

	public ResetEditableValuesListener(final String components) {
		this.components = components;
	}

	@Override
	public void processAction(final ActionEvent actionEvent) {
		final FacesContext context = FacesContext.getCurrentInstance();
		UIComponent source = actionEvent.getComponent();
		final String clientId = source.getClientId(context);

		if (!clientId.equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM))) {
			return;
		}

		final List<UIComponent> foundComponents = ComponentUtils.findComponents(context, source, components);

		for (UIComponent foundComponent : foundComponents) {
			if (foundComponent instanceof EditableValueHolder) {
				((EditableValueHolder) foundComponent).resetValue();
			} else {
				EditableValueHoldersVisitCallback visitCallback = new EditableValueHoldersVisitCallback();
				foundComponent.visitTree(VisitContext.createVisitContext(context), visitCallback);

				final List<EditableValueHolder> editableValueHolders = visitCallback.getEditableValueHolders();
				for (EditableValueHolder editableValueHolder : editableValueHolders) {
					editableValueHolder.resetValue();
				}
			}
		}
	}

	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public void restoreState(final FacesContext facesContext, final Object state) {
		Object[] values = (Object[]) state;
		components = (String) values[0];
	}

	@Override
	public Object saveState(final FacesContext facesContext) {
		Object[] values = new Object[1];
		values[0] = components;

		return values;
	}

	@Override
	public void setTransient(final boolean value) {
	}
}
