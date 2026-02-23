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
package org.primefaces.extensions.component.blockui;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;

/**
 * <code>BlockUI</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponentBase
public abstract class BlockUIBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.BlockUI";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.BlockUIRenderer";

    public BlockUIBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "CSS to be applied to the blocking overlay.")
    public abstract String getCss();

    @Property(description = "CSS to be applied to the overlay element.")
    public abstract String getCssOverlay();

    @Property(description = "Search expression for the component(s) to block.", implicitDefaultValue = "parent")
    public abstract String getSource();

    @Property(description = "Search expression for the element to use as the blocking container.")
    public abstract String getTarget();

    @Property(description = "Search expression for the component whose markup is used as blocking content.")
    public abstract String getContent();

    @Property(description = "Comma-separated list of event names that trigger the block. If empty, all events from source are accepted.")
    public abstract String getEvent();

    @Property(description = "When true, the block is shown automatically when source triggers.", defaultValue = "false")
    public abstract boolean isAutoShow();

    @Property(description = "Timeout in milliseconds to automatically unblock. 0 means no timeout.", defaultValue = "0")
    public abstract Integer getTimeout();

    @Property(description = "Whether to center the block message horizontally.", defaultValue = "true")
    public abstract boolean isCenterX();

    @Property(description = "Whether to center the block message vertically.", defaultValue = "true")
    public abstract boolean isCenterY();

    @Property(description = "Fade-in duration in milliseconds.", defaultValue = "200")
    public abstract Integer getFadeIn();

    @Property(description = "Fade-out duration in milliseconds.", defaultValue = "400")
    public abstract Integer getFadeOut();

    @Property(description = "Whether to show the overlay.", defaultValue = "true")
    public abstract boolean isShowOverlay();

    @Property(description = "Whether to focus the first input in the block content.", defaultValue = "true")
    public abstract boolean isFocusInput();
}
