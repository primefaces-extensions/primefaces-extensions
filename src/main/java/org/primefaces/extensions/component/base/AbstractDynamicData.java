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
package org.primefaces.extensions.component.base;

import org.primefaces.component.api.UITabPanel;
import org.primefaces.extensions.event.EventDataWrapper;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.util.SavedEditableValueState;

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
import javax.faces.render.Renderer;
import java.util.Map;
import org.primefaces.util.ComponentTraversalUtils;

/**
 * Abstract base class for all components with dynamic behavior like UIData.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public abstract class AbstractDynamicData extends UIComponentBase implements NamingContainer, UniqueIdVendor {

	protected KeyData data;
	private String clientId = null;
	private final StringBuilder idBuilder = new StringBuilder();
	private Boolean isNested = null;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		// @formatter:off
      saved,
      lastId,
      var,
      varContainerId,
      value;
      //@formatter:on

		private final String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
			toString = null;
		}

		@Override
		public String toString() {
			return toString != null ? toString : super.toString();
		}
	}

	public String getVar() {
		return (String) getStateHelper().get(PropertyKeys.var);
	}

	public void setVar(final String var) {
		getStateHelper().put(PropertyKeys.var, var);
	}

	public String getVarContainerId() {
		return (String) getStateHelper().get(PropertyKeys.varContainerId);
	}

	public void setVarContainerId(final String varContainerId) {
		getStateHelper().put(PropertyKeys.varContainerId, varContainerId);
	}

	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value, null);
	}

	public void setValue(final Object value) {
		getStateHelper().put(PropertyKeys.value, value);
	}

	/**
	 * Finds instance of {@link org.primefaces.extensions.model.common.KeyData}
	 * by corresponding key.
	 *
    * @param key unique key
    * @return KeyData found data
    */
   protected abstract KeyData findData(String key);

   /**
    * Processes children components during processDecodes(),
    * processValidators(), processUpdates().
    *
    * @param context faces context {@link FacesContext}
    * @param phaseId current JSF phase id
    */
   protected abstract void processChildren(FacesContext context, PhaseId phaseId);

   /**
    * Visits children components during visitTree().
    *
    * @param context visit context {@link VisitContext}
    * @param callback visit callback {@link VisitCallback}
    * @return boolean true - indicates that the children's visit is complete
    *         (e.g. all components that need to be visited have been visited),
    *         false - otherwise.
    */
   protected abstract boolean visitChildren(VisitContext context, VisitCallback callback);

   /**
    * Searches a child component with the given clientId during
    * invokeOnComponent() and invokes the callback on it if found.
    *
    * @param context faces context {@link FacesContext}
    * @param clientId client Id
    * @param callback {@link ContextCallback}
	 * @return boolean true - child component was found, else - otherwise
	 */
	protected abstract boolean invokeOnChildren(FacesContext context, String clientId, ContextCallback callback);

	public void setData(final String key) {
		saveDescendantState();

		data = findData(key);
		exposeVar();

		restoreDescendantState();
	}

	public void setData(final KeyData keyData) {
		saveDescendantState();

		data = keyData;
		exposeVar();

		restoreDescendantState();
	}

	public void resetData() {
		saveDescendantState();

		data = null;
		exposeVar();
	}

	public KeyData getData() {
		return data;
	}

	@Override
	public String getClientId(final FacesContext context) {
		if (clientId != null) {
			return clientId;
		}

		String id = getId();
		if (id == null) {
			final UniqueIdVendor parentUniqueIdVendor = ComponentTraversalUtils.closestUniqueIdVendor(this);

			if (parentUniqueIdVendor == null) {
				final UIViewRoot viewRoot = context.getViewRoot();

				if (viewRoot != null) {
					id = viewRoot.createUniqueId(context, null);
				} else {
					throw new FacesException("Cannot create clientId for " + this.getClass().getCanonicalName());
				}
			} else {
				id = parentUniqueIdVendor.createUniqueId(context, null);
			}

			setId(id);
		}

		final UIComponent namingContainer = ComponentTraversalUtils.closestNamingContainer(this);
		if (namingContainer != null) {
			final String containerClientId = namingContainer.getContainerClientId(context);

			if (containerClientId != null) {
            clientId = idBuilder.append(containerClientId).append(UINamingContainer.getSeparatorChar(context))
                     .append(id)
                     .toString();
            idBuilder.setLength(0);
         } else {
				clientId = id;
			}
		} else {
			clientId = id;
		}

		final Renderer renderer = getRenderer(context);
		if (renderer != null) {
			clientId = renderer.convertClientId(context, clientId);
		}

		return clientId;
	}

	@Override
	public void setId(final String id) {
		super.setId(id);

		clientId = null;
	}

	@Override
	public String getContainerClientId(final FacesContext context) {
		final String clientId = this.getClientId(context);

		final KeyData data = getData();
		final String key = data != null ? data.getKey() : null;

		if (key == null) {
			return clientId;
      } else {
         final String containerClientId = idBuilder.append(clientId).append(UINamingContainer.getSeparatorChar(context))
                  .append(key).toString();
         idBuilder.setLength(0);

         return containerClientId;
      }
   }

	@Override
	public void processDecodes(final FacesContext context) {
		if (!isRendered()) {
			return;
		}

		pushComponentToEL(context, this);
		preDecode(context);
		processFacets(context, PhaseId.APPLY_REQUEST_VALUES, this);
		processChildren(context, PhaseId.APPLY_REQUEST_VALUES);

		try {
			decode(context);
		} catch (final RuntimeException e) {
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

		final Application app = context.getApplication();
		app.publishEvent(context, PreValidateEvent.class, this);

		processFacets(context, PhaseId.PROCESS_VALIDATIONS, this);
		processChildren(context, PhaseId.PROCESS_VALIDATIONS);

		app.publishEvent(context, PostValidateEvent.class, this);
		popComponentFromEL(context);
	}

	@Override
	public void processUpdates(final FacesContext context) {
		if (!isRendered()) {
			return;
		}

		pushComponentToEL(context, this);
		processFacets(context, PhaseId.UPDATE_MODEL_VALUES, this);
		processChildren(context, PhaseId.UPDATE_MODEL_VALUES);
		popComponentFromEL(context);
	}

	protected void preDecode(final FacesContext context) {
		final Map<String, SavedEditableValueState> saved = (Map<String, SavedEditableValueState>) getStateHelper()
				.get(PropertyKeys.saved);
		if (null == saved) {
			getStateHelper().remove(PropertyKeys.saved);
		} else if (!keepSaved(context)) {
			for (final SavedEditableValueState saveState : saved.values()) {
				saveState.reset();
			}
		}
	}

	private boolean keepSaved(final FacesContext context) {
		return contextHasErrorMessages(context) || isNestedWithinIterator();
	}

	private boolean contextHasErrorMessages(final FacesContext context) {
		final FacesMessage.Severity sev = context.getMaximumSeverity();
		return sev != null && FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0;
	}

	protected Boolean isNestedWithinIterator() {
		if (isNested == null) {
			UIComponent parent = this;
         while (null != (parent = parent.getParent())) {
            if (parent instanceof javax.faces.component.UIData
                     || parent.getClass().getName().endsWith("UIRepeat")
                     || parent instanceof UITabPanel && ((UITabPanel) parent).isRepeating()) { 
               isNested = Boolean.TRUE;
					break;
				}
			}
			if (isNested == null) {
				isNested = Boolean.FALSE;
			}
		}
		return isNested;
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

		final FacesContext context = FacesContext.getCurrentInstance();
		final KeyData oldData = getData();
		final EventDataWrapper eventDataWrapper = (EventDataWrapper) event;
		final FacesEvent originalEvent = eventDataWrapper.getFacesEvent();
		final UIComponent originalSource = (UIComponent) originalEvent.getSource();
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
	public boolean visitTree(final VisitContext context, final VisitCallback callback) {
		if (!isVisitable(context)) {
			return false;
		}

		final FacesContext fc = context.getFacesContext();
		final KeyData oldData = getData();
		resetData();

		pushComponentToEL(fc, null);

		try {
			final VisitResult result = context.invokeVisitCallback(this, callback);

			if (result == VisitResult.COMPLETE) {
				return true;
			}

			if (result == VisitResult.ACCEPT && !context.getSubtreeIdsToVisit(this).isEmpty()) {
				if (getFacetCount() > 0) {
					for (final UIComponent facet : getFacets().values()) {
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
   public boolean invokeOnComponent(final FacesContext context, final String clientId, final ContextCallback callback) {
      final KeyData oldData = getData();
      resetData();

		try {
			if (clientId.equals(super.getClientId(context))) {
				pushComponentToEL(context, getCompositeComponentParent(this));
				callback.invokeContextCallback(context, this);

				return true;
			}

			if (getFacetCount() > 0) {
				for (final UIComponent c : getFacets().values()) {
					if (clientId.equals(c.getClientId(context))) {
						callback.invokeContextCallback(context, c);

						return true;
					}
				}
			}

                        // skip if the component is not a children
                        if (!clientId.startsWith(this.getClientId(context))) {
                            return false;
                        }
                        
			return invokeOnChildren(context, clientId, callback);
		} catch (final FacesException fe) {
			throw fe;
		} catch (final Exception e) {
			throw new FacesException(e);
		} finally {
			popComponentFromEL(context);
			setData(oldData);
		}
	}

	protected void processFacets(final FacesContext context, final PhaseId phaseId, final UIComponent component) {
		resetData();

		if (component.getFacetCount() > 0) {
			for (final UIComponent facet : component.getFacets().values()) {
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

	@Override
	public String createUniqueId(final FacesContext context, final String seed) {
		final Integer i = (Integer) getStateHelper().get(PropertyKeys.lastId);
		int lastId = i != null ? i : 0;
		getStateHelper().put(PropertyKeys.lastId, ++lastId);

		return UIViewRoot.UNIQUE_ID_PREFIX + (seed == null ? lastId : seed);
	}

	protected void exposeVar() {
		final FacesContext fc = FacesContext.getCurrentInstance();
		final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();

		final String var = getVar();
		if (var != null) {
			final KeyData keyData = getData();
			if (keyData == null) {
				requestMap.remove(var);
			} else {
				requestMap.put(var, keyData.getData());
			}
		}

		final String varContainerId = getVarContainerId();
		if (varContainerId != null) {
			final String containerClientId = getContainerClientId(fc);
			if (containerClientId == null) {
				requestMap.remove(varContainerId);
			} else {
				requestMap.put(varContainerId, containerClientId);
			}
		}
	}

	protected void saveDescendantState() {
		for (final UIComponent child : getChildren()) {
			saveDescendantState(FacesContext.getCurrentInstance(), child);
		}
	}

	protected void saveDescendantState(final FacesContext context, final UIComponent component) {
		// force id reset
		component.setId(component.getId());

		@SuppressWarnings("unchecked")
		final Map<String, SavedEditableValueState> saved = (Map<String, SavedEditableValueState>) getStateHelper()
				.get(PropertyKeys.saved);

		if (component instanceof EditableValueHolder) {
			final EditableValueHolder input = (EditableValueHolder) component;
			SavedEditableValueState state = null;
			final String clientId = component.getClientId(context);

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
			state.setLabelValue(((UIComponent) input).getAttributes().get("label"));
		}

		for (final UIComponent child : component.getChildren()) {
			saveDescendantState(context, child);
		}

		if (component.getFacetCount() > 0) {
			for (final UIComponent facet : component.getFacets().values()) {
				saveDescendantState(context, facet);
			}
		}
	}

	protected void restoreDescendantState() {
		for (final UIComponent child : getChildren()) {
			restoreDescendantState(FacesContext.getCurrentInstance(), child);
		}
	}

	protected void restoreDescendantState(final FacesContext context, final UIComponent component) {
		// force id reset
		component.setId(component.getId());

		@SuppressWarnings("unchecked")
		final Map<String, SavedEditableValueState> saved = (Map<String, SavedEditableValueState>) getStateHelper()
				.get(PropertyKeys.saved);

		if (component instanceof EditableValueHolder) {
			final EditableValueHolder input = (EditableValueHolder) component;
			final String clientId = component.getClientId(context);

			SavedEditableValueState state = saved.get(clientId);
			if (state == null) {
				state = new SavedEditableValueState();
			}

			input.setValue(state.getValue());
			input.setValid(state.isValid());
			input.setSubmittedValue(state.getSubmittedValue());
			input.setLocalValueSet(state.isLocalValueSet());
			if (state.getLabelValue() != null) {
				((UIComponent) input).getAttributes().put("label", state.getLabelValue());
			}
		}

		for (final UIComponent child : component.getChildren()) {
			restoreDescendantState(context, child);
		}

		if (component.getFacetCount() > 0) {
			for (final UIComponent facet : component.getFacets().values()) {
				restoreDescendantState(context, facet);
			}
		}
	}
}
