/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
 * Event which is triggered by the {@link org.primefaces.extensions.component.imageareaselect.ImageAreaSelect} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
public class ImageAreaSelectEvent extends AbstractAjaxBehaviorEvent {

    private static final long serialVersionUID = 1L;

    private final int height;
    private final int width;
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;
    private final int imgHeight;
    private final int imgWidth;
    private final String imgSrc;

    public ImageAreaSelectEvent(final UIComponent component, final Behavior behavior, final int height, final int width,
                final int x1, final int x2, final int y1, final int y2, final int imgHeight, final int imgWidth,
                final String imgSrc) {
        super(component, behavior);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.height = height;
        this.width = width;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.imgSrc = imgSrc;
    }

    public final int getHeight() {
        return height;
    }

    public final int getWidth() {
        return width;
    }

    public final int getX1() {
        return x1;
    }

    public final int getX2() {
        return x2;
    }

    public final int getY1() {
        return y1;
    }

    public final int getY2() {
        return y2;
    }

    public final int getImgHeight() {
        return imgHeight;
    }

    public final int getImgWidth() {
        return imgWidth;
    }

    public final String getImgSrc() {
        return imgSrc;
    }
}
