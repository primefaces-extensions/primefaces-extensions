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
package org.primefaces.extensions.component.clipboard;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ClipboardErrorEvent;
import org.primefaces.extensions.event.ClipboardSuccessEvent;

/**
 * <code>Clipboard</code> component base class.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "success", event = ClipboardSuccessEvent.class,
                        description = "Fires when the clipboard copy/cut action succeeds.", defaultEvent = true),
            @FacesBehaviorEvent(name = "error", event = ClipboardErrorEvent.class,
                        description = "Fires when the clipboard copy/cut action fails.")
})
public abstract class ClipboardBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Clipboard";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ClipboardRenderer";

    public ClipboardBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Clipboard action: 'copy' or 'cut'.", defaultValue = "copy")
    public abstract String getAction();

    @Property(description = "Search expression for the element that triggers the clipboard action.", required = true)
    public abstract String getTrigger();

    @Property(description = "Search expression for the element whose content is copied/cut.")
    public abstract String getTarget();

    @Property(description = "Text to copy when no target is specified.", defaultValue = "PrimeFaces Rocks!")
    public abstract String getText();

    @Property(description = "Client-side callback when the copy/cut action succeeds.")
    public abstract String getOnsuccess();

    @Property(description = "Client-side callback when the copy/cut action fails.")
    public abstract String getOnerror();
}
