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

package org.primefaces.extensions.showcase.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.extensions.showcase.model.State;

/**
 * TriStateManyCheckboxConverter converter class.
 *
 * @author  Mauricio Fenoglio / last modified by $Author:$
 * @version $Revision:$
 */
@FacesConverter("triStateManyCheckboxConverter")
public class TriStateManyCheckboxConverter implements Converter {

	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		State res;
		if (value.equals("0")) {
			res = new State("One");
		} else if (value.equals("1")) {
			res = new State("Two");
		} else {
			res = new State("Three");
		}

		return res;
	}

	public String getAsString(final FacesContext context, final UIComponent component, final Object valueO) {
		State value = (State) valueO;
		String res;
		if (value.getState().equals("One")) {
			res = "0";
		} else if (value.getState().equals("Two")) {
			res = "1";
		} else {
			res = "2";
		}

		return res;
	}
}
