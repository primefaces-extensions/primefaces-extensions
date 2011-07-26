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
 */
package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesListener;

import org.primefaces.extensions.component.imagerotateandresize.ImageRotateAndResize;

/**
 * Event which is triggered by the {@link ImageRotateAndResize} component.
 *
 * @author Thomas Andraschko
 * @since 0.1
 */
@SuppressWarnings("serial")
public class RotateEvent extends AjaxBehaviorEvent {

	private int degree;

	public RotateEvent(final UIComponent component,
			final Behavior behavior,
			final int degree) {
		super(component, behavior);
		this.degree = degree;
	}

	@Override
	public boolean isAppropriateListener(final FacesListener listener) {
		return false;
	}

	@Override
	public void processListener(final FacesListener listener) {
		throw new UnsupportedOperationException();
	}

	public int getDegree() {
		return degree;
	}
}
