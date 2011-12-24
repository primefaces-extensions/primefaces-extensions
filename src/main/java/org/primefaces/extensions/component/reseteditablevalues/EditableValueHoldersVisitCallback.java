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

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/**
 * Callback calling while a component subtree is visiting. The aim of this callback is gathering of all components implementing
 * {@link EditableValueHolder} below the subtree.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class EditableValueHoldersVisitCallback implements VisitCallback {

	private List<EditableValueHolder> editableValueHolders = new ArrayList<EditableValueHolder>();

	@Override
	public VisitResult visit(final VisitContext context, final UIComponent target) {
		if (!target.isRendered()) {
			return VisitResult.REJECT;
		}

		if (target instanceof EditableValueHolder) {
			editableValueHolders.add((EditableValueHolder) target);
		}

		return VisitResult.ACCEPT;
	}

	public List<EditableValueHolder> getEditableValueHolders() {
		return editableValueHolders;
	}
}
