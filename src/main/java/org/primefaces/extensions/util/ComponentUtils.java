/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.util;

import org.primefaces.component.api.AjaxSource;

import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Component utils for this project.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class ComponentUtils extends org.primefaces.util.ComponentUtils {

    /**
     * Prevent instantiation.
     */
    private ComponentUtils() {
        // prevent instantiation
    }

    public static Object getConvertedSubmittedValue(final FacesContext fc, final EditableValueHolder evh) {
        Object submittedValue = evh.getSubmittedValue();
        if (submittedValue == null) {
            return null;
        }

        try {
            UIComponent component = (UIComponent) evh;
            Renderer renderer = getRenderer(fc, component);
            if (renderer != null) {
                // convert submitted value by renderer
                return renderer.getConvertedValue(fc, component, submittedValue);
            } else if (submittedValue instanceof String) {
                // convert submitted value by registred (implicit or explicit) converter
                Converter converter = getConverter(fc, component);
                if (converter != null) {
                    return converter.getAsObject(fc, component, (String) submittedValue);
                }
            }
        } catch (Exception e) {
            // an conversion error occured
        }

        return submittedValue;
    }

    public static Renderer getRenderer(final FacesContext fc, final UIComponent component) {
        String rendererType = component.getRendererType();
        if (rendererType != null) {
            return fc.getRenderKit().getRenderer(component.getFamily(), rendererType);
        }

        return null;
    }

    public static boolean isAjaxifiedComponent(final UIComponent component) {
        // check for ajax source
        if (component instanceof AjaxSource && ((AjaxSource) component).isAjaxified()) {
            return true;
        }

        if (component instanceof ClientBehaviorHolder) {
            // check for attached f:ajax / p:ajax
            Collection<List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors().values();
            if (!behaviors.isEmpty()) {
                for (List<ClientBehavior> listBehaviors : behaviors) {
                    for (ClientBehavior clientBehavior : listBehaviors) {
                        if (clientBehavior instanceof javax.faces.component.behavior.AjaxBehavior
                                || clientBehavior instanceof org.primefaces.behavior.ajax.AjaxBehavior) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * NOTE: COPIED FROM TOBAGO.
     * <p/>
     * <p>Puts two backslashes before #;&,.+*~':"!^$[]()=>|/ to escape them. Two are needed, because of JavaScript string
     * literals. Puts three backslashes before a \ itself, to escape it.</p>
     * It is used as EL function.
     */
    public static String escapeSelector(final String value) {
        final StringBuilder builder = new StringBuilder();

        for (char c : value.toCharArray()) {
            switch (c) {
                case '\\':
                    builder.append("\\\\\\\\");
                    break;

                case '#':
                case ';':
                case '&':
                case ',':
                case '.':
                case '+':
                case '*':
                case '~':
                case '\'':
                case ':':
                case '"':
                case '!':
                case '^':
                case '$':
                case '[':
                case ']':
                case '(':
                case ')':
                case '=':
                case '>':
                case '|':
                case '/':
                    builder.append("\\\\");

                default:
                    builder.append(c);
                    break;
            }
        }

        return builder.toString();
    }

    /**
     * Gets a {@link Locale} instance by the value of the component attribute "locale" which can be String or {@link Locale} or
     * null. It can be used in any web projects as a helper method.
     *
     * @param locale given locale
     * @return resolved Locale
     */
    public static Locale resolveLocale(Object locale) {
        if (locale instanceof String) {
            locale = org.primefaces.util.ComponentUtils.toLocale((String) locale);
        }

        if (locale == null) {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }

        return (Locale) locale;
    }

    /**
     * Gets a {@link TimeZone} instance by the parameter "timeZone" which can be String or {@link TimeZone} or null.
     *
     * @param timeZone given time zone
     * @return resolved TimeZone
     */
    public static TimeZone resolveTimeZone(Object timeZone) {
        if (timeZone instanceof String) {
            return TimeZone.getTimeZone((String) timeZone);
        } else if (timeZone instanceof TimeZone) {
            return (TimeZone) timeZone;
        } else {
            return TimeZone.getDefault();
        }
    }
}
