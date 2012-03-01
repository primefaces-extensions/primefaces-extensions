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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

/**
 * Message utils.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
public class MessageUtils {

	public static final String FACES_MESSAGES = "javax.faces.Messages";

	/**
	 * Returns the <code>label</code> property from the specified component.
	 *
	 * @param  context   - the <code>FacesContext</code> for the current request
	 * @param  component - the component of interest
	 * @return the label, if any, of the component
	 */
	public static Object getLabel(final FacesContext context, final UIComponent component) {
		Object label = component.getAttributes().get("label");
		if (label == null || (label instanceof String && StringUtils.isEmpty((String) label))) {
			label = component.getValueExpression("label");
		}

		// use the "clientId" if there was no label specified.
		if (label == null) {
			label = component.getClientId(context);
		}

		return label;
	}

	/**
	 * Creates and returns a FacesMessage for the specified Locale, message key and parameter(s).
	 *
	 * @param  locale - the target <code>Locale</code>
	 * @param  key    messageId - the key of the message in the resource bundle
	 * @param  params - substittion parameters
	 * @return a localized <code>FacesMessage</code> with the severity of FacesMessage.SEVERITY_ERROR
	 * @throws NullPointerException DOCUMENT_ME
	 */
	public static FacesMessage getMessage(final Locale locale, final String key, final Object... params) {
		String summary = null;
		String detail = null;
		ResourceBundle bundle;
		String bundleName;

		final Application application;
		final FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			application = context.getApplication();
		} else {
			ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
			application = aFactory.getApplication();
		}

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = application.getClass().getClassLoader();
		}

		// try to find message in user provided bundle
		bundleName = application.getMessageBundle();
		if (bundleName != null) {
			bundle = ResourceBundle.getBundle(bundleName, locale, loader);
			if (bundle != null) {
				try {
					summary = bundle.getString(key);
					detail = bundle.getString(key + "_detail");
				} catch (MissingResourceException e) {
					// ignore
				}
			}
		}

		if (summary == null) {
			// try to find message in JSF standard provided bundle
			bundle = ResourceBundle.getBundle(FACES_MESSAGES, locale, loader);
			if (bundle == null) {
				throw new NullPointerException();
			}

			try {
				summary = bundle.getString(key);
				detail = bundle.getString(key + "_detail");
			} catch (MissingResourceException e) {
				// ignore
			}
		}

		if (summary != null && params != null) {
			summary = MessageFormat.format(summary, params);
		}

		if (detail != null && params != null) {
			detail = MessageFormat.format(detail, params);
		}

		if (summary != null) {
			// create and return a message
			return new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, (detail != null ? detail : StringUtils.EMPTY));
		}

		return null;
	}
}
