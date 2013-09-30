/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
 * $Id: $
 */

package org.primefaces.extensions.event.timeline;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;

/**
 * Event which is triggered when an item is dragged and dropped onto the Timeline from the outside (an external list, etc).
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   1.0.0
 */
public class TimelineDragDropEvent extends TimelineAddEvent {

	/** client ID of the dragged component */
	private String dragId;
    
    /** dragged model object if draggable item is within a data iteration component or null */
    private Object data;

	public TimelineDragDropEvent(UIComponent component, Behavior behavior, Date startDate, Date endDate, String group,
	                             String dragId, Object data) {
		super(component, behavior, startDate, endDate, group);
		this.dragId = dragId;
        this.data = data;
	}

	public String getDragId() {
		return dragId;
	}

    public Object getData() {
        return data;
    }
}
