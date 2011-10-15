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

package org.primefaces.extensions.converter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.extensions.component.remotecommand.RemoteCommandParameter;
import org.primefaces.extensions.util.GsonConverter;

/**
 * {@link Converter} which converts a JSON string to an object an vice-versa.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@FacesConverter(value = "org.primefaces.extensions.converter.JsonConverter")
public class JsonConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		final ValueExpression ve;

		if (component instanceof RemoteCommandParameter) {
			ve = ((RemoteCommandParameter) component).getApplyTo();
		} else {
			ve = component.getValueExpression("value");
		}

		return GsonConverter.getGson().fromJson(value, ve.getType(context.getELContext()));
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		return GsonConverter.getGson().toJson(value);
	}
}
