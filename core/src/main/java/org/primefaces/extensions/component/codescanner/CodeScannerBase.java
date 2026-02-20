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
package org.primefaces.extensions.component.codescanner;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;

/**
 * <code>CodeScanner</code> component base class.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 10.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "codeScanned", event = SelectEvent.class,
                        description = "Fires when a code is successfully scanned.", defaultEvent = true)
})
public abstract class CodeScannerBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CodeScanner";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CodeScannerRenderer";

    public CodeScannerBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Reader type: 'multi', 'bar', or 'qr'.", defaultValue = "multi")
    public abstract String getType();

    @Property(description = "Width of the video element in pixels.", required = true)
    public abstract Integer getWidth();

    @Property(description = "Height of the video element in pixels.", required = true)
    public abstract Integer getHeight();

    @Property(description = "Whether to start scanning automatically.", defaultValue = "true")
    public abstract boolean isAutoStart();

    @Property(description = "Client-side callback when a code is successfully scanned.")
    public abstract String getOnsuccess();

    @Property(description = "Client-side callback when scanning fails.")
    public abstract String getOnerror();

    @Property(description = "Whether to show the video stream.", defaultValue = "true")
    public abstract boolean isVideo();

    @Property(description = "Camera device ID to use when multiple cameras are available.")
    public abstract String getDeviceId();

    @Property(description = "Search expression for the input component to write the scanned value to.", required = true)
    public abstract String getFor();

    @SuppressWarnings("java:S115")
    public enum ReaderType {
        multi, bar, qr
    }
}
