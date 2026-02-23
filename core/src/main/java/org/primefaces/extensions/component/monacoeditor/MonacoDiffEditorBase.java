/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.monacoeditor;

import jakarta.faces.event.AjaxBehaviorEvent;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.extensions.model.monacoeditor.DiffEditorOptions;

/**
 * CDK base for the Monaco diff editor (shared by inline and framed).
 *
 * @since 11.1.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "change", event = AjaxBehaviorEvent.class, description = "Fires when the diff content changes.", defaultEvent = true),
            @FacesBehaviorEvent(name = "initialized", event = AjaxBehaviorEvent.class, description = "Fires when the editor is initialized."),
            @FacesBehaviorEvent(name = "blur", event = AjaxBehaviorEvent.class, description = "Fires when the element loses focus."),
            @FacesBehaviorEvent(name = "focus", event = AjaxBehaviorEvent.class, description = "Fires when the element gains focus."),
            @FacesBehaviorEvent(name = "keydown", event = AjaxBehaviorEvent.class, description = "Fires when a key is pressed down."),
            @FacesBehaviorEvent(name = "keyup", event = AjaxBehaviorEvent.class, description = "Fires when a key is released."),
            @FacesBehaviorEvent(name = "mousedown", event = AjaxBehaviorEvent.class, description = "Fires when a mouse button is pressed down."),
            @FacesBehaviorEvent(name = "mousemove", event = AjaxBehaviorEvent.class, description = "Fires when the mouse is moved."),
            @FacesBehaviorEvent(name = "mouseup", event = AjaxBehaviorEvent.class, description = "Fires when a mouse button is released."),
            @FacesBehaviorEvent(name = "paste", event = AjaxBehaviorEvent.class, description = "Fires when paste is performed."),
            @FacesBehaviorEvent(name = "originalBlur", event = AjaxBehaviorEvent.class, description = "Fires when the original editor loses focus."),
            @FacesBehaviorEvent(name = "originalChange", event = AjaxBehaviorEvent.class, description = "Fires when the original content changes."),
            @FacesBehaviorEvent(name = "originalFocus", event = AjaxBehaviorEvent.class, description = "Fires when the original editor gains focus."),
            @FacesBehaviorEvent(name = "originalKeydown", event = AjaxBehaviorEvent.class,
                        description = "Fires when a key is pressed down in the original editor."),
            @FacesBehaviorEvent(name = "originalKeyup", event = AjaxBehaviorEvent.class, description = "Fires when a key is released in the original editor."),
            @FacesBehaviorEvent(name = "originalMousedown", event = AjaxBehaviorEvent.class,
                        description = "Fires when a mouse button is pressed in the original editor."),
            @FacesBehaviorEvent(name = "originalMousemove", event = AjaxBehaviorEvent.class,
                        description = "Fires when the mouse is moved in the original editor."),
            @FacesBehaviorEvent(name = "originalMouseup", event = AjaxBehaviorEvent.class,
                        description = "Fires when a mouse button is released in the original editor."),
            @FacesBehaviorEvent(name = "originalPaste", event = AjaxBehaviorEvent.class, description = "Fires when paste is performed in the original editor.")
})
public abstract class MonacoDiffEditorBase extends MonacoEditorCommon<DiffEditorOptions> {

    protected MonacoDiffEditorBase() {
        this((String) null);
    }

    protected MonacoDiffEditorBase(final String rendererType) {
        super(rendererType, DiffEditorOptions.class);
    }

    @Property(description = "Language mode for the diff editor.")
    public abstract String getLanguage();

    @Property(description = "Whether the original (left) editor is disabled.", defaultValue = "true")
    public abstract boolean isOriginalDisabled();

    @Property(description = "Language for the original editor.")
    public abstract String getOriginalLanguage();

    @Property(description = "Whether the original editor is readonly.", defaultValue = "false")
    public abstract boolean isOriginalReadonly();

    @Property(description = "Whether the original editor is required.", defaultValue = "false")
    public abstract boolean isOriginalRequired();

    @Property(description = "Tabindex for the original editor.")
    public abstract String getOriginalTabindex();

    @Property(description = "Client-side script for original blur.")
    public abstract String getOnoriginalblur();

    @Property(description = "Client-side script for original change.")
    public abstract String getOnoriginalchange();

    @Property(description = "Client-side script for original focus.")
    public abstract String getOnoriginalfocus();

    @Property(description = "Client-side script for original keydown.")
    public abstract String getOnoriginalkeydown();

    @Property(description = "Client-side script for original keyup.")
    public abstract String getOnoriginalkeyup();

    @Property(description = "Client-side script for original keypress.")
    public abstract String getOnoriginalkeypress();

    @Property(description = "Client-side script for original mouseup.")
    public abstract String getOnoriginalmouseup();

    @Property(description = "Client-side script for original mousedown.")
    public abstract String getOnoriginalmousedown();

    @Property(description = "Client-side script for original mousemove.")
    public abstract String getOnoriginalmousemove();

    @Property(description = "Client-side script for original paste.")
    public abstract String getOnoriginalpaste();

    @Property(description = "Directory for the original model.", defaultValue = "")
    public abstract String getOriginalDirectory();

    @Property(description = "Extension for the original model.", defaultValue = "")
    public abstract String getOriginalExtension();

    @Property(description = "Basename for the original model.", defaultValue = "")
    public abstract String getOriginalBasename();

    @Property(description = "Scheme for the original model.", defaultValue = "inmemory")
    public abstract String getOriginalScheme();
}
