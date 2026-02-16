/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.component.tristatemanycheckbox;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UISelectMany;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.util.Constants;
import org.primefaces.extensions.util.MessageFactory;

/**
 * TriStateManyCheckbox
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@FacesComponent(value = TriStateManyCheckbox.COMPONENT_TYPE, namespace = TriStateManyCheckbox.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Tri-State multiple checkbox component.")
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "tristatemanycheckbox/tristatemanycheckbox.js")
public class TriStateManyCheckbox extends TriStateManyCheckboxBaseImpl {

    public static final String UI_ICON = "ui-icon ";

    @Override
    protected void validateValue(final FacesContext context, final Object value) {
        final Map<Object, Object> mapValues = (Map) value;

        // call all validators
        final Validator[] validators = getValidators();
        if (getValidators() != null) {
            for (final Validator validator : validators) {
                final Iterator<Object> it = mapValues.values().iterator();
                while (it.hasNext()) {
                    final Object newValue = it.next();
                    try {
                        validator.validate(context, this, newValue);
                    }
                    catch (final ValidatorException ve) {
                        // If the validator throws an exception, we're
                        // invalid, and we need to add a message
                        setValid(false);

                        FacesMessage message;
                        final String validatorMessageString = getValidatorMessage();

                        if (null != validatorMessageString) {
                            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessageString, validatorMessageString);
                            message.setSeverity(FacesMessage.SEVERITY_ERROR);
                        }
                        else {
                            final Collection<FacesMessage> messages = ve.getFacesMessages();
                            if (null != messages) {
                                message = null;

                                final String cid = getClientId(context);
                                for (final FacesMessage m : messages) {
                                    context.addMessage(cid, m);
                                }
                            }
                            else {
                                message = ve.getFacesMessage();
                            }
                        }

                        if (message != null) {
                            context.addMessage(getClientId(context), message);
                        }
                    }
                }
            }
        }

        boolean doAddMessage = false;

        // Ensure that if the state are all 0 and a
        // value is required, a message is queued
        if (isRequired()
                    && isValid()) {
            final Iterator<Object> it = mapValues.values().iterator();
            boolean cCheck = true;
            while (it.hasNext() && cCheck) {
                final Object val = it.next();
                if (!"0".equals(getConverter().getAsString(context, this, val))) {
                    cCheck = false;
                }
            }

            if (cCheck) {
                doAddMessage = true;
            }
        }

        if (doAddMessage) {
            final Object[] params = new Object[2];
            params[0] = MessageFactory.getLabel(context, this);

            // Enqueue an error message if an invalid value was specified
            final FacesMessage message = org.primefaces.util.MessageFactory.getFacesMessage(context, UISelectMany.INVALID_MESSAGE_ID,
                        FacesMessage.SEVERITY_ERROR, params);
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
    }
}
