/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
 * Event which is triggered by the {@link org.primefaces.extensions.component.clipboard.Clipboard} component when the copy/cut action has succeeded.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
public class ClipboardSuccessEvent extends AbstractAjaxBehaviorEvent {

    public static final String NAME = "success";
    private static final long serialVersionUID = 1L;

    /**
     * action: return the action we aim at the target element. This will return either copy or cut
     */
    private final String action;

    /**
     * text: only returned upon the success event. It returns the text that has been copied or cut from the target element.
     */
    private final String text;

    /**
     * trigger: returns the element which triggered the copy or the cut action.
     */
    private final String trigger;

    public ClipboardSuccessEvent(final UIComponent component, final Behavior behavior, final String action,
                final String text, final String trigger) {
        super(component, behavior);
        this.action = action;
        this.text = text;
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
     * Gets the {@link #text}.
     *
     * @return Returns the {@link #text}.
     */
    public final String getText() {
        return text;
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
