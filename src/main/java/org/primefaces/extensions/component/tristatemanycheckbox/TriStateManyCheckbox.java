/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
package org.primefaces.extensions.component.tristatemanycheckbox;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.MessageFactory;

/**
 * TriStateManyCheckbox
 *
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
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
    protected enum PropertyKeys {

        //@formatter:off
        widgetVar, 
        layout, 
        stateOneIcon, 
        stateTwoIcon, 
        stateThreeIcon, 
        stateOneTitle, 
        stateTwoTitle, 
        stateThreeTitle;
        //@formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        public String getToString() {
            return toString;
        }

        public void setToString(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
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
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateOneTitle, "");
    }

    public void setStateOneTitle(final String stateOneTitle) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateOneTitle, stateOneTitle);
    }

    public String getStateTwoTitle() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateTwoTitle, "");
    }

    public void setStateTwoTitle(final String stateTwoTitle) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateTwoTitle, stateTwoTitle);
    }

    public String getStateThreeTitle() {
        return (String) getStateHelper().eval(TriStateManyCheckbox.PropertyKeys.stateThreeTitle, "");
    }

    public void setStateThreeTitle(final String stateThreeTitle) {
        getStateHelper().put(TriStateManyCheckbox.PropertyKeys.stateThreeTitle, stateThreeTitle);
    }

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

    @Override
    protected void validateValue(final FacesContext context, final Object value) {
        @SuppressWarnings("unchecked")
        Map<Object, Object> mapValues = (Map) value;

        // call all validators
        Validator[] validators = this.getValidators();
        if (this.getValidators() != null) {
            for (Validator validator : validators) {
                Iterator<Object> it = mapValues.values().iterator();
                while (it.hasNext()) {
                    Object newValue = it.next();
                    try {
                        validator.validate(context, this, newValue);
                    }
                    catch (ValidatorException ve) {
                        // If the validator throws an exception, we're
                        // invalid, and we need to add a message
                        setValid(false);

                        FacesMessage message;
                        String validatorMessageString = getValidatorMessage();

                        if (null != validatorMessageString) {
                            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessageString, validatorMessageString);
                            message.setSeverity(FacesMessage.SEVERITY_ERROR);
                        }
                        else {
                            Collection<FacesMessage> messages = ve.getFacesMessages();
                            if (null != messages) {
                                message = null;

                                String cid = getClientId(context);
                                for (FacesMessage m : messages) {
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
            Iterator<Object> it = mapValues.values().iterator();
            boolean cCheck = true;
            while (it.hasNext() && cCheck) {
                final Object val = it.next();
                if (!"0".equals(this.getConverter().getAsString(context, this, val))) {
                    cCheck = false;
                }
            }

            if (cCheck) {
                doAddMessage = true;
            }
        }

        if (doAddMessage) {
            Object[] params = new Object[2];
            params[0] = MessageFactory.getLabel(context, this);

            // Enqueue an error message if an invalid value was specified
            FacesMessage message = MessageFactory.getMessage(TriStateManyCheckbox.INVALID_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, params);
            context.addMessage(getClientId(context), message);
            setValid(false);
        }
    }
}
