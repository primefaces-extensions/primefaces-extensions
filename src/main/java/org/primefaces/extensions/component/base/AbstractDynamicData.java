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
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;

import org.primefaces.extensions.event.EventDataWrapper;
import org.primefaces.extensions.model.common.IdentificableData;
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

	protected IdentificableData data;

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

	public String getVar() {
		return (String) getStateHelper().eval(PropertyKeys.var, null);
	}

	public void setVar(final String var) {
		setAttribute(PropertyKeys.var, var);
	}

	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value, null);
	}

	public void setValue(final Object value) {
		setAttribute(PropertyKeys.value, value);
	}

	public void setAttribute(final PropertyKeys property, final Object value) {
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

	protected abstract IdentificableData findData(final String key);

	protected abstract void processChildren(final FacesContext context, final PhaseId phaseId);

	public void setData(final String key) {
		saveDescendantState();

		this.data = findData(key);

		exposeVarData();
		restoreDescendantState();
	}

	public void setData(final IdentificableData data) {
		saveDescendantState();

		this.data = data;

		exposeVarData();
		restoreDescendantState();
	}

	public void resetData() {
		saveDescendantState();

		this.data = null;

		exposeVarData();
		restoreDescendantState();
	}

	public IdentificableData getData() {
		return data;
	}

	@Override
	public String getContainerClientId(final FacesContext context) {
		String clientId = super.getContainerClientId(context);
		IdentificableData data = getData();
		String key = (data != null ? data.getKey() : null);

		if (key == null) {
			return clientId;
		} else {
			StringBuilder builder = new StringBuilder();

			return builder.append(clientId).append(UINamingContainer.getSeparatorChar(context)).append(key).toString();
		}
	}

	@Override
	public void processDecodes(final FacesContext context) {
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
	public void processValidators(final FacesContext context) {
		if (!isRendered()) {
			return;
		}

		pushComponentToEL(context, this);

		Application app = context.getApplication();
		app.publishEvent(context, PreValidateEvent.class, this);
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
		processChildren(context, PhaseId.UPDATE_MODEL_VALUES);
		popComponentFromEL(context);
	}

	@Override
	public void queueEvent(final FacesEvent event) {
		super.queueEvent(new EventDataWrapper(this, event, getData()));
	}

	@Override
	public void broadcast(final FacesEvent event) throws AbortProcessingException {
		if (!(event instanceof EventDataWrapper)) {
			super.broadcast(event);

			return;
		}

		EventDataWrapper eventDataWrapper = (EventDataWrapper) event;
		FacesEvent originalEvent = eventDataWrapper.getFacesEvent();
		UIComponent originalSource = (UIComponent) originalEvent.getSource();
		setData(eventDataWrapper.getData());

		originalSource.broadcast(originalEvent);
	}

	public String createUniqueId(final FacesContext context, final String seed) {
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

	protected void saveDescendantState(final FacesContext context, final UIComponent component) {
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

	protected void restoreDescendantState(final FacesContext context, final UIComponent component) {
		// force id reset
		component.setId(component.getId());

		@SuppressWarnings("unchecked")
		Map<String, SavedEditableValueState> saved =
		    (Map<String, SavedEditableValueState>) getStateHelper().get(PropertyKeys.saved);

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

	protected void processFacets(final FacesContext context, final PhaseId phaseId, final UIComponent component) {
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

	protected void exposeVarData() {
		if (getData() == null) {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(getVar());
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(getVar(), getData().getData());
		}
	}
}
