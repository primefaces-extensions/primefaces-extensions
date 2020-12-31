/*
 * Copyright 2011-2021 PrimeFaces Extensions
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

import static org.primefaces.util.MessageFactory.getFormattedText;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.util.LocaleUtils;

/**
 * PrimeFaces Extensions message factory. Delegates as much as possible to {@link org.primefaces.util.MessageFactory}.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0
 */
public class MessageFactory {

    private static final String PF_EXTENSIONS_BUNDLE_BASENAME = "org.primefaces.extensions.Messages";
    private static final String DEFAULT_DETAIL_SUFFIX = "_detail";

    private MessageFactory() {
    }

    public static FacesMessage getMessage(final String messageId,
                final FacesMessage.Severity severity,
                final Object... params) {
        final FacesMessage message = getMessage(LocaleUtils.getCurrentLocale(), messageId, params);
        message.setSeverity(severity);
        return message;
    }

    public static FacesMessage getMessage(final Locale locale,
                final String messageId,
                final Object... params) {
        final FacesMessage message = org.primefaces.util.MessageFactory.getFacesMessage(locale, messageId, params);
        if (message.getSummary() == null) {
            populateMessageFromExtensions(message, locale, messageId, params);
        }
        return message;
    }

    public static Object getLabel(final FacesContext facesContext, final UIComponent component) {
        return org.primefaces.util.MessageFactory.getLabel(facesContext, component);
    }

    private static void populateMessageFromExtensions(final FacesMessage message,
                final Locale locale,
                final String messageId,
                final Object[] params) {
        final ResourceBundle extensionsBundle = getExtensionsBundle(locale);
        message.setSummary(getFormattedText(locale, extensionsBundle.getString(messageId), params));
        try {
            message.setDetail(getFormattedText(locale, extensionsBundle.getString(messageId + DEFAULT_DETAIL_SUFFIX), params));
        }
        catch (final MissingResourceException e) {
            // NoOp
        }
    }

    private static ResourceBundle getExtensionsBundle(final Locale locale) {
        return ResourceBundle.getBundle(PF_EXTENSIONS_BUNDLE_BASENAME,
                    locale,
                    Thread.currentThread().getContextClassLoader());

    }

}
