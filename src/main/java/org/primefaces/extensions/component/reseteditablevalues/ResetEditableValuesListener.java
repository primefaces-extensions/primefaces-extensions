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

import java.io.Serializable;
import java.util.List;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;

import org.primefaces.extensions.util.ComponentUtils;

/**
 * {@link ComponentSystemEventListener} for the <code>ResetEditableValueHolders</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class ResetEditableValuesListener implements ComponentSystemEventListener, Serializable {

	private static final long serialVersionUID = 20111114L;

	private UICommand source;
	private String components;

	public ResetEditableValuesListener(final UICommand source, final String components) {
		super();
		this.source = source;
		this.components = components;
	}

	@Override
	public void processEvent(final ComponentSystemEvent event) {
		final FacesContext context = FacesContext.getCurrentInstance();
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
}
