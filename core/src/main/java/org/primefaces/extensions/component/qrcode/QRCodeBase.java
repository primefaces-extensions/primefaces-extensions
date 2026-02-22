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
package org.primefaces.extensions.component.qrcode;

import jakarta.faces.component.UIOutput;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;

/**
 * CDK base for the QRCode component.
 *
 * @since 1.2.0
 */
@FacesComponentBase
public abstract class QRCodeBase extends UIOutput implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.QRCode";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.QRCodeRenderer";

    public QRCodeBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Rendering method.")
    public abstract String getRenderMethod();

    @Property(description = "Rendering mode.", defaultValue = "0")
    public abstract Integer getRenderMode();

    @Property(description = "Minimum QR version.", defaultValue = "1")
    public abstract int getMinVersion();

    @Property(description = "Maximum QR version.", defaultValue = "40")
    public abstract int getMaxVersion();

    @Property(description = "Left offset.", defaultValue = "0")
    public abstract int getLeftOffset();

    @Property(description = "Top offset.", defaultValue = "0")
    public abstract int getTopOffset();

    @Property(description = "Size in pixels.", defaultValue = "200")
    public abstract int getSize();

    @Property(description = "Fill color.")
    public abstract String getFillColor();

    @Property(description = "Error correction level.", defaultValue = "L")
    public abstract String getEcLevel();

    @Property(description = "Background color.")
    public abstract String getBackground();

    @Property(description = "QR content text.")
    public abstract String getText();

    @Property(description = "Corner radius.", defaultValue = "0")
    public abstract Double getRadius();

    @Property(description = "Quiet zone size.", defaultValue = "0")
    public abstract Integer getQuiet();

    @Property(description = "Label font size.", defaultValue = "0.1")
    public abstract Double getLabelSize();

    @Property(description = "Label X position.", defaultValue = "0.5")
    public abstract Double getLabelPosX();

    @Property(description = "Label Y position.", defaultValue = "0.5")
    public abstract Double getLabelPosY();

    @Property(description = "Label text.")
    public abstract String getLabel();

    @Property(description = "Label font name.")
    public abstract String getFontName();

    @Property(description = "Label font color.")
    public abstract String getFontColor();
}
