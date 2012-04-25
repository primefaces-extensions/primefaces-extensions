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
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.primefaces.extensions.event.EventWrapper;
import org.primefaces.extensions.model.common.DataWrapper;
import org.primefaces.extensions.util.SavedEditableValueState;

/**
 * Abstract base class for all components with dynamic behavior like UIData.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public abstract class AbstractDynamicData extends UIComponentBase implements NamingContainer {

	protected static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	protected String key;

	protected DataWrapper data;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		saved,
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

	protected abstract DataWrapper findData(final String key);

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		saveDescendantState();

		this.key = key;
		this.data = (key != null ? findData(key) : null);

		if (this.key == null || this.data == null) {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(getVar());
		} else {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(getVar(), this.data.getData());
		}

		restoreDescendantState();
	}

	public DataWrapper getData() {
		return data;
	}

	@Override
	public String getContainerClientId(FacesContext context) {
		String clientId = super.getContainerClientId(context);
		String key = getKey();

		if (key == null) {
			return clientId;
		} else {
			StringBuilder builder = new StringBuilder();

			return builder.append(clientId).append(UINamingContainer.getSeparatorChar(context)).append(key).toString();
		}
	}

	@Override
	public void queueEvent(FacesEvent event) {
		super.queueEvent(new EventWrapper(this, event, getKey()));
	}

	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		if (!(event instanceof EventWrapper)) {
			super.broadcast(event);

			return;
		}

		EventWrapper eventWrapper = (EventWrapper) event;
		FacesEvent originalEvent = eventWrapper.getFacesEvent();
		UIComponent originalSource = (UIComponent) originalEvent.getSource();
		setKey(eventWrapper.getKey());

		originalSource.broadcast(originalEvent);
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
}
