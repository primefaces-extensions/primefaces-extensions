/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.component.suneditor;

import jakarta.faces.event.AjaxBehaviorEvent;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.extensions.component.api.AbstractEditorInputTextArea;

@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "blur", event = AjaxBehaviorEvent.class, description = "Fires when the element loses focus."),
            @FacesBehaviorEvent(name = "change", event = AjaxBehaviorEvent.class, description = "Fires when the element's value changes.", defaultEvent = true),
            @FacesBehaviorEvent(name = "click", event = AjaxBehaviorEvent.class, description = "Fires when the element is clicked."),
            @FacesBehaviorEvent(name = "copy", event = AjaxBehaviorEvent.class, description = "Fires when the clipboard copy action is performed."),
            @FacesBehaviorEvent(name = "cut", event = AjaxBehaviorEvent.class, description = "Fires when the clipboard cut action is performed."),
            @FacesBehaviorEvent(name = "drop", event = AjaxBehaviorEvent.class, description = "Fires when a drag and drop operation is completed."),
            @FacesBehaviorEvent(name = "focus", event = AjaxBehaviorEvent.class, description = "Fires when the element gains focus."),
            @FacesBehaviorEvent(name = "initialize", event = AjaxBehaviorEvent.class, description = "Fires when the editor is initialized."),
            @FacesBehaviorEvent(name = "input", event = AjaxBehaviorEvent.class, description = "Fires when the element's value is modified."),
            @FacesBehaviorEvent(name = "keydown", event = AjaxBehaviorEvent.class, description = "Fires when a key is pressed down on the element."),
            @FacesBehaviorEvent(name = "keyup", event = AjaxBehaviorEvent.class, description = "Fires when a key is released on the element."),
            @FacesBehaviorEvent(name = "mousedown", event = AjaxBehaviorEvent.class, description = "Fires when a mouse button is pressed down on the element."),
            @FacesBehaviorEvent(name = "paste", event = AjaxBehaviorEvent.class, description = "Fires when the clipboard paste action is performed."),
            @FacesBehaviorEvent(name = "save", event = AjaxBehaviorEvent.class, description = "Fires when the editor content is saved."),
            @FacesBehaviorEvent(name = "scroll", event = AjaxBehaviorEvent.class, description = "Fires when the element is scrolled.")
})
public abstract class SunEditorBase extends AbstractEditorInputTextArea {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SunEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SunEditorRenderer";

    public SunEditorBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Width of the editor.", defaultValue = "100%")
    public abstract String getWidth();

    @Property(description = "Height of the editor.", defaultValue = "auto")
    public abstract String getHeight();

    @Property(description = "The mode to use: 'classic', 'inline', etc.", defaultValue = "classic")
    public abstract String getMode();

    @Property(description = "Locale for the editor. Can be a string or java.util.Locale instance.")
    public abstract Object getLocale();

    @Property(description = "Whether to use strict mode.", defaultValue = "true")
    public abstract boolean isStrictMode();
}
