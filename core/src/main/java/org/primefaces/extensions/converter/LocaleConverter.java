/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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

import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * {@link Converter} which converts a string to a {@link java.util.Locale} an vice-versa.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@FacesConverter(value = "org.primefaces.extensions.converter.LocaleConverter")
public class LocaleConverter implements Converter<Object>, Serializable {

    private static final long serialVersionUID = 20121214L;

    private char separator = '_';

    @Override
    public Object getAsObject(final FacesContext fc, final UIComponent component, final String value) {
        if (LangUtils.isValueBlank(value)) {
            return fc.getApplication().getDefaultLocale();
        }

        return getLocaleObject(value, separator);
    }

    @Override
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
        }
        else if (value instanceof Locale) {
            return getLocaleString((Locale) value, separator);
        }
        else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Wrong type: '" + value.getClass().getSimpleName()
                                    + "' is not 'Locale'.",
                        Constants.EMPTY_STRING));
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
                    || parts.length > 1 && parts[1].length() != 0 && !parts[1].matches("[a-zA-Z]{2,2}")) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "'" + locale + "' does not represent a valid locale",
                        Constants.EMPTY_STRING));
        }

        switch (parts.length) {
            case 3:
                return new Locale(parts[0], parts[1], parts[2]);

            case 2:
                return new Locale(parts[0], parts[1]);

            case 1:
                return new Locale(parts[0]);

            default:
                String[] arr = ExtLangUtils.subarray(parts, 2, parts.length);
                return new Locale(parts[0], parts[1], String.join("_", arr));
        }
    }

    public static String getLocaleString(final Locale locale, final char seperator) {
        if (LangUtils.isValueBlank(locale.getCountry())) {
            return locale.getLanguage();
        }

        return locale.getLanguage() + seperator + locale.getCountry();
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(final char separator) {
        this.separator = separator;
    }
}
