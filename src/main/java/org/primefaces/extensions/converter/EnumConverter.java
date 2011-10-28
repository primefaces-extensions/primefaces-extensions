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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * {@link Converter} which converts a {@link String} to an {@link Enum} an vice-versa.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@FacesConverter(value = "org.primefaces.extensions.converter.EnumConverter")
public class EnumConverter implements Converter {

	private static final String ATTRIBUTE_ENUM_TYPE =
		"org.primefaces.extensions.converter.EnumConverter.ENUM_TYPE";

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof Enum<?>) {
			component.getAttributes().put(ATTRIBUTE_ENUM_TYPE, value.getClass());
			return ((Enum<?>) value).name();
		}

		throw new ConverterException(new FacesMessage("Value is not an enum: " + value.getClass()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		final Class<Enum> enumType = (Class<Enum>) component.getAttributes().get(ATTRIBUTE_ENUM_TYPE);
		try {
			return Enum.valueOf(enumType, value);
		} catch (IllegalArgumentException e) {
			throw new ConverterException(new FacesMessage("Value is not an enum of type: " + enumType));
		}
	}
}
