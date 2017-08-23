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
package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

import org.primefaces.extensions.model.common.KeyData;

/**
 * Event wrapper.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class EventDataWrapper extends FacesEvent {

	private FacesEvent event = null;
	private KeyData data = null;

	public EventDataWrapper(final UIComponent component, final FacesEvent event, final KeyData data) {
		super(component);
		this.event = event;
		this.data = data;
	}

	public FacesEvent getFacesEvent() {
		return this.event;
	}

	public KeyData getData() {
		return data;
	}

	@Override
	public PhaseId getPhaseId() {
		return this.event.getPhaseId();
	}

	@Override
	public void setPhaseId(PhaseId phaseId) {
		this.event.setPhaseId(phaseId);
	}

	@Override
	public boolean isAppropriateListener(FacesListener listener) {
		return false;
	}

	@Override
	public void processListener(FacesListener listener) {
		throw new IllegalStateException("processListener is not supported in EventWrapper");
	}
}
