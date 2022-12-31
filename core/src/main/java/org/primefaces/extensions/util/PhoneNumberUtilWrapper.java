/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;

import org.primefaces.util.LangUtils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0.1
 */
public class PhoneNumberUtilWrapper {

    private static final String MESSAGE_INVALID_VALUE_KEY = "primefaces.extensions.inputphone.INVALID";

    private PhoneNumberUtilWrapper() {
        // private constructor to prevent instantiation
    }

    public static void validate(final String number, final String country, final String validatorMessage) {
        try {
            final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            final Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                throw getInvalidValueConverterException(validatorMessage);
            }
        }
        catch (final NumberParseException e) {
            throw getInvalidValueConverterException(validatorMessage);
        }
    }

    private static ConverterException getInvalidValueConverterException(final String validatorMessage) {
        return new ConverterException(getInvalidValueFacesMessage(validatorMessage));
    }

    private static FacesMessage getInvalidValueFacesMessage(final String validatorMessage) {
        if (LangUtils.isEmpty(validatorMessage)) {
            return MessageFactory.getMessage(MESSAGE_INVALID_VALUE_KEY, FacesMessage.SEVERITY_ERROR);
        }
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessage, null);
    }

}
