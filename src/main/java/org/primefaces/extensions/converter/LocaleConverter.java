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

package org.primefaces.extensions.converter;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link Converter} which converts a string to a {@link java.util.Locale} an vice-versa.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@FacesConverter(value = "org.primefaces.extensions.converter.LocaleConverter")
public class LocaleConverter implements Converter, Serializable {

	private static final long serialVersionUID = 20121214L;

	private char separator = '_';

	public Object getAsObject(final FacesContext fc, final UIComponent component, final String value) {
		if (StringUtils.isBlank(value)) {
			return fc.getApplication().getDefaultLocale();
		}

		return getLocaleObject(value, separator);
	}

	public String getAsString(final FacesContext fc, final UIComponent component, final Object value) {
		if (value == null) {
			final Locale defaultLocale = fc.getApplication().getDefaultLocale();
			if (defaultLocale == null) {
				return null;
			}

			return getLocaleString(defaultLocale, separator);
		}

		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Locale) {
			return getLocaleString((Locale) value, separator);
		} else {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
			                                              "Wrong type: '" + value.getClass().getSimpleName()
			                                              + "' is not 'Locale'.", StringUtils.EMPTY));
		}
	}

	public static Locale getLocaleObject(final String locale, final char seperator) {
		String replacedLocale = locale;
		if (seperator != '-' && seperator != '_') {
			replacedLocale = replacedLocale.replace(seperator, '_');
		}

		replacedLocale = replacedLocale.replace('-', '_');

		final String[] parts = replacedLocale.split("_");
		if (parts.length == 0
		    || !parts[0].matches("[a-zA-Z]{2,2}")
		    || (parts.length > 1 && parts[1].length() != 0 && !parts[1].matches("[a-zA-Z]{2,2}"))) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
			                                              "'" + locale + "' does not represent a valid locale",
			                                              StringUtils.EMPTY));
		}

		switch (parts.length) {
		case 3:
			return new Locale(parts[0], parts[1], parts[2]);

		case 2:
			return new Locale(parts[0], parts[1]);

		case 1:
			return new Locale(parts[0]);

		default:
			return new Locale(parts[0], parts[1], StringUtils.join(ArrayUtils.subarray(parts, 2, parts.length), '_'));
		}
	}

	public static String getLocaleString(final Locale locale, final char seperator) {
		if (StringUtils.isBlank(locale.getCountry())) {
			return locale.getLanguage();
		}

		return locale.getLanguage() + seperator + locale.getCountry();
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}
}
