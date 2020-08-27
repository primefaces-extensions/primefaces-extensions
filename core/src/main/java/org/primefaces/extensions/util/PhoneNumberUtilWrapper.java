/*
 * Copyright 2011-2020 PrimeFaces Extensions
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

import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;

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

    public static void validate(final String number, final String country) {
        try {
            final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            final Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                throw getInvalidValueConverterException();
            }
        }
        catch (final NumberParseException e) {
            throw getInvalidValueConverterException();
        }
    }

    private static ConverterException getInvalidValueConverterException() {
        return new ConverterException(MessageFactory.getMessage(MESSAGE_INVALID_VALUE_KEY, FacesMessage.SEVERITY_ERROR));
    }

}
