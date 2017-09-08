/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.util;

import java.util.Locale;

import javax.faces.context.FacesContext;

import org.primefaces.util.ComponentUtils;

/**
 * Locale utils for this project.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @version 0.2
 * @since 0.2
 */
public class LocaleUtils {

    /**
     * Prevent instantiation.
     */
    private LocaleUtils() {
        // prevent instantiation
    }

    /**
     * Gets a {@link Locale} instance by the value of the component attribute "locale" which can be String or {@link Locale} or null. It can be used in any web
     * projects as a helper method.
     *
     * @param locale given locale
     * @return resolved Locale
     */
    public static Locale resolveLocale(Object locale) {
        if (locale instanceof String) {
            locale = ComponentUtils.toLocale((String) locale);
        }

        if (locale == null) {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }

        return (Locale) locale;
    }

}
