/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
