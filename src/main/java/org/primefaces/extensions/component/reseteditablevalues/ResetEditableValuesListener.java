/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.reseteditablevalues;

import java.util.List;

import javax.faces.FacesWrapper;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.SystemEventListener;

import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * Class for all added at runtime "reset" listeners.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class ResetEditableValuesListener extends AjaxBehaviorListenerImpl implements ActionListener, StateHolder {

	private String components;

	/**
	 * This constructor is required for serialization. Please don't remove.
	 */
	public ResetEditableValuesListener() {
	}

	public ResetEditableValuesListener(final String components) {
		this.components = components;
	}

	public void processAction(final ActionEvent event) throws AbortProcessingException {
		processReset(event.getComponent());
	}

	@Override
	public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
		processReset(event.getComponent());
	}

	protected void processReset(final UIComponent source) {
		final FacesContext fc = FacesContext.getCurrentInstance();
		final String clientId = source.getClientId(fc);

		if (!clientId.equals(fc.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM))) {
			return;
		}

		final List<UIComponent> foundComponents = ComponentUtils.findComponents(fc, source, components);
		for (UIComponent foundComponent : foundComponents) {
			PreRenderEditableValuesListener ppevListener = getPreRenderEditableValuesListener(foundComponent);
			if (ppevListener == null) {
				ppevListener = new PreRenderEditableValuesListener();
				foundComponent.subscribeToEvent(PreRenderComponentEvent.class, ppevListener);
			}

			ppevListener.setReset(true);
		}
	}

	protected PreRenderEditableValuesListener getPreRenderEditableValuesListener(final UIComponent component) {
		List<SystemEventListener> systemEventListeners = component.getListenersForEventClass(PreRenderComponentEvent.class);
		if (systemEventListeners != null && !systemEventListeners.isEmpty()) {
			for (SystemEventListener systemEventListener : systemEventListeners) {
				if (systemEventListener instanceof PreRenderEditableValuesListener) {
					return (PreRenderEditableValuesListener) systemEventListener;
				}

				FacesListener wrapped = null;
				if (systemEventListener instanceof FacesWrapper<?>) {
					wrapped = (FacesListener) ((FacesWrapper<?>) systemEventListener).getWrapped();
				}

				while (wrapped != null) {
					if (wrapped instanceof PreRenderEditableValuesListener) {
						return (PreRenderEditableValuesListener) wrapped;
					}

					if (wrapped instanceof FacesWrapper<?>) {
						wrapped = (FacesListener) ((FacesWrapper<?>) wrapped).getWrapped();
					} else {
						wrapped = null;
					}
				}
			}
		}

		return null;
	}

	public boolean isTransient() {
		return false;
	}

	public void restoreState(final FacesContext facesContext, final Object state) {
		Object[] values = (Object[]) state;
		components = (String) values[0];
	}

	public Object saveState(final FacesContext facesContext) {
		Object[] values = new Object[1];
		values[0] = components;

		return values;
	}

	public void setTransient(final boolean value) {
	}
}
