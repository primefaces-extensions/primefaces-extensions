/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

package org.primefaces.extensions.event;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.timepicker.TimePicker} components.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
@SuppressWarnings("serial")
public class TimeSelectEvent extends AbstractAjaxBehaviorEvent {

	private Date time;

	public TimeSelectEvent(final UIComponent component, final Behavior behavior, final Date time) {
		super(component, behavior);
		this.time = time;
	}

	public Date getTime() {
		return time;
	}
}
