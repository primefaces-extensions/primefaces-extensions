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

package org.primefaces.extensions.component.remotecommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import org.primefaces.component.api.AjaxSource;
import org.primefaces.extensions.component.base.AbstractParameter;
import org.primefaces.extensions.component.parameters.AssignableParameter;
import org.primefaces.extensions.component.parameters.MethodParameter;

/**
 * Component class for the <code>RemoteCommand</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class RemoteCommand extends UICommand implements AjaxSource {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.RemoteCommand";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.RemoteCommandRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		name,
		update,
		process,
		onstart,
		oncomplete,
		onerror,
		onsuccess,
		global,
		async,
		partialSubmit,
		action,
		autoRun,
		actionListener;

		private String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public RemoteCommand() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getName() {
		return (String) getStateHelper().eval(PropertyKeys.name, null);
	}

	public void setName(final String name) {
		setAttribute(PropertyKeys.name, name);
	}

	public String getUpdate() {
		return (String) getStateHelper().eval(PropertyKeys.update, null);
	}

	public void setUpdate(final String update) {
		setAttribute(PropertyKeys.update, update);
	}

	public String getProcess() {
		return (String) getStateHelper().eval(PropertyKeys.process, null);
	}

	public void setProcess(final String process) {
		setAttribute(PropertyKeys.process, process);
	}

	public String getOnstart() {
		return (String) getStateHelper().eval(PropertyKeys.onstart, null);
	}

	public void setOnstart(final String onstart) {
		setAttribute(PropertyKeys.onstart, onstart);
	}

	public String getOncomplete() {
		return (String) getStateHelper().eval(PropertyKeys.oncomplete, null);
	}

	public void setOncomplete(final String oncomplete) {
		setAttribute(PropertyKeys.oncomplete, oncomplete);
	}

	public String getOnerror() {
		return (String) getStateHelper().eval(PropertyKeys.onerror, null);
	}

	public void setOnerror(final String onerror) {
		setAttribute(PropertyKeys.onerror, onerror);
	}

	public String getOnsuccess() {
		return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
	}

	public void setOnsuccess(final String onsuccess) {
		setAttribute(PropertyKeys.onsuccess, onsuccess);
	}

	public boolean isGlobal() {
		return (Boolean) getStateHelper().eval(PropertyKeys.global, true);
	}

	public void setGlobal(final boolean global) {
		setAttribute(PropertyKeys.global, global);
	}

	public boolean isAsync() {
		return (Boolean) getStateHelper().eval(PropertyKeys.async, false);
	}

	public void setAsync(final boolean async) {
		setAttribute(PropertyKeys.async, async);
	}

	public boolean isPartialSubmit() {
		return (Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
	}

	public void setPartialSubmit(final boolean partialSubmit) {
		setAttribute(PropertyKeys.partialSubmit, partialSubmit);
	}

	public boolean isAutoRun() {
		return (Boolean) getStateHelper().eval(PropertyKeys.autoRun, false);
	}

	public void setAutoRun(final boolean autoRun) {
		setAttribute(PropertyKeys.autoRun, autoRun);
	}

	public MethodExpression getActionListenerMethodExpression() {
		return (MethodExpression) getStateHelper().get(PropertyKeys.actionListener);
	}

	public void setActionListenerMethodExpression(final MethodExpression actionListener) {
		getStateHelper().put(PropertyKeys.actionListener, actionListener);
	}

	public boolean isPartialSubmitSet() {
		return (getStateHelper().get(PropertyKeys.partialSubmit) != null) || (this.getValueExpression("partialSubmit") != null);
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes =
				(List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}

	@Override
	public void broadcast(final FacesEvent event) throws AbortProcessingException {
		for (FacesListener listener : getFacesListeners(FacesListener.class)) {
			if (event.isAppropriateListener(listener)) {
				event.processListener(listener);
			}
		}

		if (event instanceof ActionEvent) {
			final FacesContext context = getFacesContext();

			//invoke actionListener
			final MethodExpression listener = getActionListenerMethodExpression();
			if (listener != null) {
				listener.invoke(context.getELContext(), getConvertedMethodParameters(context));
			}

			//invoke action
			final ActionListener actionListener = context.getApplication().getActionListener();
			if (actionListener != null) {
				actionListener.processAction((ActionEvent) event);
			}
		}
	}

	private transient List<AbstractParameter> allParameters = null;
	private transient List<AssignableParameter> assignableParameters = null;
	private transient List<MethodParameter> methodParameters = null;
	private transient Object[] convertedMethodParams = null;

	protected void findChildParameters() {
		if (allParameters == null || assignableParameters == null || methodParameters == null) {
			allParameters = new ArrayList<AbstractParameter>();
			assignableParameters = new ArrayList<AssignableParameter>();
			methodParameters = new ArrayList<MethodParameter>();

			for (final UIComponent child : super.getChildren()) {
				if (child instanceof AbstractParameter) {
					allParameters.add((AbstractParameter) child);

					if (child instanceof AssignableParameter) {
						assignableParameters.add((AssignableParameter) child);
					} else {
						if (child instanceof MethodParameter) {
							methodParameters.add((MethodParameter) child);
						}
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
				} else {
					final Object convertedValue = converter.getAsObject(context, methodParameter, parameterValue);
					convertedMethodParams[i] = convertedValue;
				}
			}
		}

		return convertedMethodParams;
	}

	public String getParameterValue(final FacesContext context, final String name) {
		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String clientId = getClientId(context);

		return params.get(clientId + "_" + name);
	}
}
