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

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.Widget;

/**
 * Utilities for rendering or working with {@link Widget}s.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class WidgetUtils {

	private static final Map<String, String[]> PROPERTY_KEYS_CACHE = new HashMap<String, String[]>();

	public static void renderWidgetScript(final FacesContext context, final String clientId, final ResponseWriter writer,
			final Widget widget, final boolean useDoubleQuotesForStrings, final boolean hasStyleSheet) throws IOException {

		final Class<?> widgetClass = widget.getClass();
		final String widgetName = widgetClass.getSimpleName();
		final String widgetVar = widget.resolveWidgetVar();

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('" + widgetName + "', '" + widgetVar + "', {");

		renderAllOptions(clientId, writer, widget, useDoubleQuotesForStrings);

		writer.write("}, " + hasStyleSheet + ");});");
    }

	public static final void renderAllOptions(final String clientId, final ResponseWriter writer, final Widget widget,
			final boolean useDoubleQuotesForStrings) throws IOException {

		renderAllOptions(clientId, writer, widget, useDoubleQuotesForStrings, null);
	}

	public static final void renderAllOptions(final String clientId, final ResponseWriter writer, final Widget widget,
			final boolean useDoubleQuotesForStrings, final Map<String, Object> additionalOptions) throws IOException {

		final Class<?> widgetClass = widget.getClass();
		final String widgetName = widgetClass.getSimpleName();

		writer.write("id:'" + clientId + "'");

		final String[] propertyKeys = getPropertyKeys(widgetClass, widgetName);
		for (int i = 0; i < propertyKeys.length; i++) {
			final Object propertyValue = getPropertValue(propertyKeys[i], widget, widgetName);

			renderOption(writer, propertyKeys[i], propertyValue, useDoubleQuotesForStrings);
		}

		if (additionalOptions != null) {
			for (String option : additionalOptions.keySet()) {
				renderOption(writer, option, additionalOptions.get(option), useDoubleQuotesForStrings);
			}
		}
	}

	private static void renderOption(final ResponseWriter writer, final String option, final Object value,
			final boolean useDoubleQuotesForStrings) throws IOException {

		if (value != null) {
			if (String.class.isAssignableFrom(value.getClass()) ||
				Character.class.isAssignableFrom(value.getClass())) {

				//don't add quotes for objects or arrays
				final String stringValue = ((String) value).trim();
				if (stringValue.startsWith("{") || stringValue.startsWith("[")) {
					writer.write("," + option + ":" + stringValue);
				} else {
					if (useDoubleQuotesForStrings) {
						writer.write("," + option + ":\"" + stringValue + "\"");
					} else {
						writer.write("," + option + ":'" + stringValue + "'");
					}
				}
			} else {
				writer.write("," + option+ ":" + value);
			}
		}
	}

    private static Object getPropertValue(final String property, final Widget widget, final String widgetName) {
    	final Class<?> widgetClass = widget.getClass();

    	try {
    		final Method getter = new PropertyDescriptor(property, widgetClass).getReadMethod();
    		return getter.invoke(widget);
    	} catch (Exception e) {
    		throw new FacesException("Can not get value for property '" + property + "' and widget '" + widgetName + "'", e);
    	}
    }

    private static String[] getPropertyKeys(final Class<?> widgetClass, final String widgetName) {
    	String[] propertyKeys;

    	//try from cache first
    	if (PROPERTY_KEYS_CACHE.containsKey(widgetName)) {
    		propertyKeys = PROPERTY_KEYS_CACHE.get(widgetName);
    	} else {
    		synchronized (PROPERTY_KEYS_CACHE) {
    	    	if (PROPERTY_KEYS_CACHE.containsKey(widgetName)) {
    	    		propertyKeys = PROPERTY_KEYS_CACHE.get(widgetName);
    	    	} else {
	    	    	//try to find PropertyKeys subclass
	    	    	Class<?> propertyKeysClass = null;

	    	    	//find child class named "PropertyKeys"
	    			final Class<?>[] childClasses = widgetClass.getDeclaredClasses();
	    			for (int i = 0; i < childClasses.length; i++) {
	    				final Class<?> childClass = childClasses[i];
	    				if (childClass.getSimpleName().equals("PropertyKeys")) {
	    					propertyKeysClass = childClass;
	    				}
	    			}

	    			if (propertyKeysClass == null) {
	    				throw new FacesException("Widget '" + widgetName + "' does not has PropertyKeys class");
	    			}

	    			//call toString() -> This is required for "for"
	    			final Object[] propertyKeysObjects = propertyKeysClass.getEnumConstants();
	    			propertyKeys = new String[propertyKeysObjects.length];

	    			for (int i = 0; i < propertyKeysObjects.length; i++) {
	    				propertyKeys[i] = propertyKeysObjects[i].toString();
	    			}

	    			//add to cache
	    	    	PROPERTY_KEYS_CACHE.put(widgetName, propertyKeys);
				}
    		}
    	}

    	return propertyKeys;
    }
}
