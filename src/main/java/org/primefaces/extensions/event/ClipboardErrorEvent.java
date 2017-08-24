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
import javax.faces.component.behavior.Behavior;

import org.primefaces.event.AbstractAjaxBehaviorEvent;

/**
 * Event which is triggered by the {@link org.primefaces.extensions.component.clipboard.Clipboard} component when the copy/cut action has failed.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@SuppressWarnings("serial")
public class ClipboardErrorEvent extends AbstractAjaxBehaviorEvent {

    public static final String NAME = "error";

    /**
     * action: return the action we aim at the target element. This will return either copy or cut
     */
    private final String action;

    /**
     * trigger: returns the element which triggered the copy or the cut action.
     */
    private final String trigger;

    public ClipboardErrorEvent(final UIComponent component, final Behavior behavior, final String action,
                final String trigger) {
        super(component, behavior);
        this.action = action;
        this.trigger = trigger;
    }

    /**
     * Gets the {@link #action}.
     *
     * @return Returns the {@link #action}.
     */
    public final String getAction() {
        return action;
    }

    /**
     * Gets the {@link #trigger}.
     *
     * @return Returns the {@link #trigger}.
     */
    public final String getTrigger() {
        return trigger;
    }

}
