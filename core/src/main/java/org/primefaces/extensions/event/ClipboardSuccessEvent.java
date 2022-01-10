/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
