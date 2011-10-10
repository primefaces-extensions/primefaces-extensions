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
package org.primefaces.extensions.component.savestate;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

/**
 * Component class for the <code>SaveState</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class SaveState extends UIParameter {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SaveState";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public Object saveState(final FacesContext context) {
		final Object[] values = new Object[3];

		values[0] = super.saveState(context);

		final Object objectToSave = getValue();
		if (objectToSave instanceof StateHolder) {
			values[1] = Boolean.TRUE;
			values[2] = saveAttachedState(context, objectToSave);
		} else {
			values[1] = Boolean.FALSE;
			values[2] = objectToSave;
		}
		return values;
	}

	@Override
	public void restoreState(final FacesContext context, final Object state) {
		final Object[] values = (Object[]) state;
		final Object savedObject;

		super.restoreState(context, values[0]);

		final Boolean isStateHolder = (Boolean) values[1];
		if (Boolean.TRUE.equals(isStateHolder)) {
			savedObject = restoreAttachedState(context, values[2]);
		} else {
			savedObject = values[2];
		}

		final ValueExpression ve = getValueExpression("value");
		final ELContext elContext = context.getELContext();
		if (ve != null) {
			ve.setValue(elContext, savedObject);
		}
	}
}
