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

package org.primefaces.extensions.component.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;

import org.primefaces.extensions.event.EventDataWrapper;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.util.SavedEditableValueState;

/**
 * Abstract base class for all components with dynamic behavior like UIData.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public abstract class AbstractDynamicData extends UIComponentBase implements NamingContainer, UniqueIdVendor {

	protected static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	protected KeyData data;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		saved,
		lastId,
		var,
		value;

		private String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public String getVar() {
		return (String) getStateHelper().eval(PropertyKeys.var, null);
	}

	public void setVar(String var) {
		setAttribute(PropertyKeys.var, var);
	}

	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value, null);
	}

	public void setValue(Object value) {
		setAttribute(PropertyKeys.value, value);
	}

	public void setAttribute(PropertyKeys property, Object value) {
		getStateHelper().put(property, value);

		@SuppressWarnings("unchecked")
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

	/**
	 * Finds instance of {@link org.primefaces.extensions.model.common.KeyData} by corresponding key.
	 *
	 * @param  key unique key
	 * @return KeyData found data
	 */
	protected abstract KeyData findData(String key);

	/**
	 * Exposes variables for each iteration.
	 */
	protected abstract void exposeVars();

	/**
	 * Processes children components during processDecodes(), processValidators(), processUpdates().
	 *
	 * @param context faces context {@link FacesContext}
	 * @param phaseId current JSF phase id
	 */
	protected abstract void processChildren(FacesContext context, PhaseId phaseId);

	/**
	 * Visits children components during visitTree().
	 *
	 * @param  context  visit context {@link VisitContext}
	 * @param  callback visit callback {@link VisitCallback}
	 * @return boolean true - indicates that the children's visit is complete (e.g. all components that need to be visited have
	 *         been visited), false - otherwise.
	 */
	protected abstract boolean visitChildren(VisitContext context, VisitCallback callback);

	/**
	 * Searches a child component with the given clientId during invokeOnComponent() and invokes the callback on it if found.
	 *
	 * @param  context  faces context {@link FacesContext}
	 * @param  clientId client Id
	 * @param  callback {@link ContextCallback}
	 * @return boolean true - child component was found, else - otherwise
	 */
	protected abstract boolean invokeOnChildren(FacesContext context, String clientId, ContextCallback callback);

	public void setData(String key) {
		saveDescendantState();

		this.data = findData(key);

		exposeVars();
		restoreDescendantState();
	}

	public void setData(KeyData data) {
		saveDescendantState();

		this.data = data;

		exposeVars();
		restoreDescendantState();
	}

	public void resetData() {
		saveDescendantState();

		this.data = null;

		exposeVars();
		restoreDescendantState();
	}

	public KeyData getData() {
		return data;
	}

	@Override
	public String getContainerClientId(FacesContext context) {
		String clientId = super.getContainerClientId(context);
		KeyData data = getData();
		String key = (data != null ? data.getKey() : null);

		if (key == null) {
			return clientId;
		} else {
			StringBuilder builder = new StringBuilder();

			return builder.append(clientId).append(UINamingContainer.getSeparatorChar(context)).append(key).toString();
		}
	}

	@Override
	public void processDecodes(FacesContext context) {
		if (!isRendered()) {
			return;
		}

		pushComponentToEL(context, this);

		@SuppressWarnings("unchecked")
		Map<String, SavedEditableValueState> saved =
		    (Map<String, SavedEditableValueState>) getStateHelper().get(PropertyKeys.saved);

		FacesMessage.Severity sev = context.getMaximumSeverity();
		boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));

		if (saved == null || !hasErrors) {
			getStateHelper().remove(PropertyKeys.saved);
		}

		processFacets(context, PhaseId.APPLY_REQUEST_VALUES, this);
		processChildren(context, PhaseId.APPLY_REQUEST_VALUES);

		try {
			decode(context);
		} catch (RuntimeException e) {
			context.renderResponse();
			throw e;
		} finally {
			popComponentFromEL(context);
		}
	}

	@Override
	public void processValidators(FacesContext context) {
		if (!isRendered()) {
			return;
		}

		pushComponentToEL(context, this);

		Application app = context.getApplication();
		app.publishEvent(context, PreValidateEvent.class, this);

		processFacets(context, PhaseId.PROCESS_VALIDATIONS, this);
		processChildren(context, PhaseId.PROCESS_VALIDATIONS);

		app.publishEvent(context, PostValidateEvent.class, this);
		popComponentFromEL(context);
	}

	@Override
	public void processUpdates(FacesContext context) {
		if (!isRendered()) {
			return;
		}

		pushComponentToEL(context, this);
		processFacets(context, PhaseId.UPDATE_MODEL_VALUES, this);
		processChildren(context, PhaseId.UPDATE_MODEL_VALUES);
		popComponentFromEL(context);
	}

	@Override
	public void queueEvent(FacesEvent event) {
		super.queueEvent(new EventDataWrapper(this, event, getData()));
	}

	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		if (!(event instanceof EventDataWrapper)) {
			super.broadcast(event);

			return;
		}

		FacesContext context = FacesContext.getCurrentInstance();
		KeyData oldData = getData();
		EventDataWrapper eventDataWrapper = (EventDataWrapper) event;
		FacesEvent originalEvent = eventDataWrapper.getFacesEvent();
		UIComponent originalSource = (UIComponent) originalEvent.getSource();
		setData(eventDataWrapper.getData());

		UIComponent compositeParent = null;
		try {
			if (!UIComponent.isCompositeComponent(originalSource)) {
				compositeParent = getCompositeComponentParent(originalSource);
			}

			if (compositeParent != null) {
				compositeParent.pushComponentToEL(context, null);
			}

			originalSource.pushComponentToEL(context, null);
			originalSource.broadcast(originalEvent);
		} finally {
			originalSource.popComponentFromEL(context);
			if (compositeParent != null) {
				compositeParent.popComponentFromEL(context);
			}
		}

		setData(oldData);
	}

	@Override
	public boolean visitTree(VisitContext context, VisitCallback callback) {
		if (!isVisitable(context)) {
			return false;
		}

		final FacesContext fc = context.getFacesContext();
		KeyData oldData = getData();
		resetData();

		pushComponentToEL(fc, null);

		try {
			VisitResult result = context.invokeVisitCallback(this, callback);

			if (result == VisitResult.COMPLETE) {
				return true;
			}

			if (result == VisitResult.ACCEPT && !context.getSubtreeIdsToVisit(this).isEmpty()) {
				if (getFacetCount() > 0) {
					for (UIComponent facet : getFacets().values()) {
						if (facet.visitTree(context, callback)) {
							return true;
						}
					}
				}

				if (visitChildren(context, callback)) {
					return true;
				}
			}
		} finally {
			popComponentFromEL(fc);
			setData(oldData);
		}

		return false;
	}

	@Override
	public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) {
		KeyData oldData = getData();
		resetData();

		try {
			if (clientId.equals(super.getClientId(context))) {
				this.pushComponentToEL(context, getCompositeComponentParent(this));
				callback.invokeContextCallback(context, this);

				return true;
			}

			if (getFacetCount() > 0) {
				for (UIComponent c : getFacets().values()) {
					if (clientId.equals(c.getClientId(context))) {
						callback.invokeContextCallback(context, c);

						return true;
					}
				}
			}

			return invokeOnChildren(context, clientId, callback);
		} catch (FacesException fe) {
			throw fe;
		} catch (Exception e) {
			throw new FacesException(e);
		} finally {
			popComponentFromEL(context);
			setData(oldData);
		}
	}

	protected void processFacets(FacesContext context, PhaseId phaseId, UIComponent component) {
		resetData();

		if (component.getFacetCount() > 0) {
			for (UIComponent facet : component.getFacets().values()) {
				if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
					facet.processDecodes(context);
				} else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
					facet.processValidators(context);
				} else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
					facet.processUpdates(context);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}
	}

	public String createUniqueId(FacesContext context, String seed) {
		Integer i = (Integer) getStateHelper().get(PropertyKeys.lastId);
		int lastId = ((i != null) ? i : 0);
		getStateHelper().put(PropertyKeys.lastId, ++lastId);

		return UIViewRoot.UNIQUE_ID_PREFIX + (seed == null ? lastId : seed);
	}

	protected void saveDescendantState() {
		for (UIComponent child : getChildren()) {
			saveDescendantState(FacesContext.getCurrentInstance(), child);
		}
	}

	protected void saveDescendantState(FacesContext context, UIComponent component) {
		// force id reset
		component.setId(component.getId());

		@SuppressWarnings("unchecked")
		Map<String, SavedEditableValueState> saved =
		    (Map<String, SavedEditableValueState>) getStateHelper().get(PropertyKeys.saved);

		if (component instanceof EditableValueHolder) {
			EditableValueHolder input = (EditableValueHolder) component;
			SavedEditableValueState state = null;
			String clientId = component.getClientId(context);

			if (saved == null) {
				state = new SavedEditableValueState();
				getStateHelper().put(PropertyKeys.saved, clientId, state);
			}

			if (state == null) {
				state = saved.get(clientId);

				if (state == null) {
					state = new SavedEditableValueState();
					getStateHelper().put(PropertyKeys.saved, clientId, state);
				}
			}

			state.setValue(input.getLocalValue());
			state.setValid(input.isValid());
			state.setSubmittedValue(input.getSubmittedValue());
			state.setLocalValueSet(input.isLocalValueSet());
		}

		for (UIComponent child : component.getChildren()) {
			saveDescendantState(context, child);
		}

		if (component.getFacetCount() > 0) {
			for (UIComponent facet : component.getFacets().values()) {
				saveDescendantState(context, facet);
			}
		}
	}

	protected void restoreDescendantState() {
		for (UIComponent child : getChildren()) {
			restoreDescendantState(FacesContext.getCurrentInstance(), child);
		}
	}

	protected void restoreDescendantState(FacesContext context, UIComponent component) {
		// force id reset
		component.setId(component.getId());

		@SuppressWarnings("unchecked")
		Map<String, SavedEditableValueState> saved =
		    (Map<String, SavedEditableValueState>) getStateHelper().get(PropertyKeys.saved);

		if (saved == null) {
			return;
		}

		if (component instanceof EditableValueHolder) {
			EditableValueHolder input = (EditableValueHolder) component;
			String clientId = component.getClientId(context);

			SavedEditableValueState state = saved.get(clientId);
			if (state == null) {
				state = new SavedEditableValueState();
			}

			input.setValue(state.getValue());
			input.setValid(state.isValid());
			input.setSubmittedValue(state.getSubmittedValue());
			input.setLocalValueSet(state.isLocalValueSet());
		}

		for (UIComponent child : component.getChildren()) {
			restoreDescendantState(context, child);
		}

		if (component.getFacetCount() > 0) {
			for (UIComponent facet : component.getFacets().values()) {
				restoreDescendantState(context, facet);
			}
		}
	}
}
