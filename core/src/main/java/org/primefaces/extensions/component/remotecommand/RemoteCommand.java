/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.remotecommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.el.MethodExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.FacesListener;

import org.primefaces.extensions.component.api.AbstractParameter;
import org.primefaces.extensions.component.parameters.AssignableParameter;
import org.primefaces.extensions.component.parameters.MethodParameter;
import org.primefaces.extensions.util.Constants;

/**
 * RemoteCommand component: execute JSF backing bean methods from JavaScript.
 *
 * @since 0.2
 */
@FacesComponent(value = RemoteCommandBase.COMPONENT_TYPE, namespace = RemoteCommandBase.COMPONENT_FAMILY)
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
public class RemoteCommand extends RemoteCommandBaseImpl {

    private List<AbstractParameter> allParameters = null;
    private List<AssignableParameter> assignableParameters = null;
    private List<MethodParameter> methodParameters = null;
    private Object[] convertedMethodParams = null;

    @Override
    public boolean isPartialSubmitSet() {
        return getStateHelper().get(PropertyKeys.partialSubmit) != null || getValueExpression(PropertyKeys.partialSubmit.name()) != null;
    }

    @Override
    public boolean isResetValuesSet() {
        return getStateHelper().get(PropertyKeys.resetValues) != null || getValueExpression(PropertyKeys.resetValues.name()) != null;
    }

    @Override
    public void broadcast(final FacesEvent event) {
        for (final FacesListener listener : getFacesListeners(FacesListener.class)) {
            if (event.isAppropriateListener(listener)) {
                event.processListener(listener);
            }
        }

        if (event instanceof ActionEvent) {
            final FacesContext context = getFacesContext();
            final MethodExpression listener = getActionListenerMethodExpression();
            if (listener != null) {
                listener.invoke(context.getELContext(), getConvertedMethodParameters(context));
            }
            final ActionListener actionListener = context.getApplication().getActionListener();
            if (actionListener != null) {
                actionListener.processAction((ActionEvent) event);
            }
        }
    }

    protected void findChildParameters() {
        if (allParameters == null || assignableParameters == null || methodParameters == null) {
            allParameters = new ArrayList<>();
            assignableParameters = new ArrayList<>();
            methodParameters = new ArrayList<>();

            for (final UIComponent child : super.getChildren()) {
                if (child instanceof AbstractParameter) {
                    allParameters.add((AbstractParameter) child);
                    if (child instanceof AssignableParameter) {
                        assignableParameters.add((AssignableParameter) child);
                    }
                    else if (child instanceof MethodParameter) {
                        methodParameters.add((MethodParameter) child);
                    }
                }
            }
        }
    }

    protected List<AbstractParameter> getAllParameters() {
        findChildParameters();
        return allParameters;
    }

    protected List<AssignableParameter> getAssignableParameters() {
        findChildParameters();
        return assignableParameters;
    }

    protected List<MethodParameter> getMethodParameters() {
        findChildParameters();
        return methodParameters;
    }

    protected Object[] getConvertedMethodParameters(final FacesContext context) {
        if (convertedMethodParams == null) {
            convertedMethodParams = new Object[getMethodParameters().size()];
            for (int i = 0; i < getMethodParameters().size(); i++) {
                final MethodParameter methodParameter = getMethodParameters().get(i);
                final Converter converter = methodParameter.getConverter();
                final String parameterValue = getParameterValue(context, methodParameter.getName());
                if (converter == null) {
                    convertedMethodParams[i] = parameterValue;
                }
                else {
                    convertedMethodParams[i] = converter.getAsObject(context, methodParameter, parameterValue);
                }
            }
        }
        return convertedMethodParams;
    }

    public String getParameterValue(final FacesContext context, final String name) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        return params.get(getClientId(context) + "_" + name);
    }

    @Override
    public boolean isAjaxified() {
        return true;
    }

    @Override
    public Object saveState(final FacesContext context) {
        allParameters = null;
        assignableParameters = null;
        methodParameters = null;
        convertedMethodParams = null;
        return super.saveState(context);
    }
}
