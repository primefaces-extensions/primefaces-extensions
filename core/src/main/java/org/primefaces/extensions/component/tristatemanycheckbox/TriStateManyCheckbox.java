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
package org.primefaces.extensions.component.tristatemanycheckbox;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.api.Widget;
import org.primefaces.util.Constants;
import org.primefaces.util.MessageFactory;

/**
 * TriStateManyCheckbox
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
public class TriStateManyCheckbox extends HtmlSelectManyCheckbox implements Widget {

    public static final String UI_ICON = "ui-icon ";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TriStateManyCheckbox";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.";

    /**
     * PropertyKeys
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        //@formatter:off CHECKSTYLE:OFF
        widgetVar,
        layout,
        stateOneIcon,
        stateTwoIcon,
        stateThreeIcon,
        stateOneTitle,
        stateTwoTitle,
        stateThreeTitle
        //@formatter:on CHECKSTYLE:ON
    }

    public TriStateManyCheckbox() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.widgetVar, widgetVar);
    }

    @Override
    public String getLayout() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.layout, null);
    }

    @Override
    public void setLayout(final String layout) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.layout, layout);
    }

    public String getStateOneIcon() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateOneIcon, null);
    }

    public void setStateOneIcon(final String stateOneIcon) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateOneIcon, stateOneIcon);
    }

    public String getStateTwoIcon() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateTwoIcon, null);
    }

    public void setStateTwoIcon(final String stateTwoIcon) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateTwoIcon, stateTwoIcon);
    }

    public String getStateThreeIcon() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateThreeIcon, null);
    }

    public void setStateThreeIcon(final String stateThreeIcon) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateThreeIcon, stateThreeIcon);
    }

    public String getStateOneTitle() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateOneTitle, Constants.EMPTY_STRING);
    }

    public void setStateOneTitle(final String stateOneTitle) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateOneTitle, stateOneTitle);
    }

    public String getStateTwoTitle() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateTwoTitle, Constants.EMPTY_STRING);
    }

    public void setStateTwoTitle(final String stateTwoTitle) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateTwoTitle, stateTwoTitle);
    }

    public String getStateThreeTitle() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateThreeTitle, Constants.EMPTY_STRING);
    }

    public void setStateThreeTitle(final String stateThreeTitle) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateThreeTitle, stateThreeTitle);
    }

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
            final FacesMessage message = MessageFactory.getFacesMessage(UISelectMany.INVALID_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, params);
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
    }
}
