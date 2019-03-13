/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.validate;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.extensions.component.inputphone.InputPhone;

/**
 * Validator used with {@link InputPhone}.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@FacesValidator("org.primefaces.extensions.validate.PhoneValidator")
public class PhoneValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object object) throws ValidatorException {
        String country = "";
        if (InputPhone.class.isInstance(component)) {
            InputPhone inputPhone = (InputPhone) component;
            country = context.getExternalContext().getRequestParameterMap().get(inputPhone.getClientId() + "_iso2");
            if (country == null || "auto".equals(country)) {
                country = "";
            }
        }
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse((String) object, country);
            if (!phoneNumberUtil.isValidNumber(phoneNumber)) {
                throw new ValidatorException(new FacesMessage("Invalid phone number"));
            }
        }
        catch (NumberParseException e) {
            throw new ValidatorException(new FacesMessage("Invalid phone number"));
        }
    }

}
