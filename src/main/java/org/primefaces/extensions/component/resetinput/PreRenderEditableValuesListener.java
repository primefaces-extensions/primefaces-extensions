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

package org.primefaces.extensions.component.resetinput;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;

/**
 * {@link ComponentSystemEventListener} for an <code>EditableValueHolder</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class PreRenderEditableValuesListener implements ComponentSystemEventListener, Serializable {

	private static final long serialVersionUID = 20111225L;

	private static final Set<VisitHint> VISIT_HINTS = EnumSet.of(VisitHint.SKIP_UNRENDERED);

	private boolean reset = false;

	/**
	 * This constructor is required for serialization. Please do not remove.
	 */
	public PreRenderEditableValuesListener() {
	}

	public void processEvent(final ComponentSystemEvent event) {
		if (!reset) {
			return;
		}

		final UIComponent source = event.getComponent();
		if (!source.isRendered()) {
			return;
		}

		if (source instanceof EditableValueHolder) {
			((EditableValueHolder) source).resetValue();
		} else {
			EditableValueHoldersVisitCallback visitCallback = new EditableValueHoldersVisitCallback();
			source.visitTree(VisitContext.createVisitContext(FacesContext.getCurrentInstance(), null, VISIT_HINTS),
			                 visitCallback);

			final List<EditableValueHolder> editableValueHolders = visitCallback.getEditableValueHolders();
			for (EditableValueHolder editableValueHolder : editableValueHolders) {
				editableValueHolder.resetValue();
			}
		}

		reset = false;
	}

	public void setReset(final boolean reset) {
		this.reset = reset;
	}
}
