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
package org.primefaces.extensions.component.layout;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;

/**
 * <code>LayoutPane</code> component base class.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponentBase
public abstract class LayoutPaneBase extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.LayoutPane";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutPaneRenderer";

    public LayoutPaneBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Pane position: north, south, west, east, or center.", defaultValue = "center")
    public abstract String getPosition();

    @Property(description = "Custom pane selector.")
    public abstract String getPaneSelector();

    @Property(description = "Combined position path (set by renderer).", defaultValue = "center")
    public abstract String getCombinedPosition();

    @Property(description = "Inline style for the pane header.")
    public abstract String getStyleHeader();

    @Property(description = "Style class for the pane header.")
    public abstract String getStyleClassHeader();

    @Property(description = "Inline style for the pane content.")
    public abstract String getStyleContent();

    @Property(description = "Style class for the pane content.")
    public abstract String getStyleClassContent();

    @Property(description = "Whether the pane is resizable.", defaultValue = "true")
    public abstract boolean isResizable();

    @Property(description = "Whether the pane is slideable.", defaultValue = "true")
    public abstract boolean isSlidable();

    @Property(description = "Whether the pane is closable.", defaultValue = "true")
    public abstract boolean isClosable();

    @Property(description = "Pane size.")
    public abstract String getSize();

    @Property(description = "Minimum pane size.")
    public abstract String getMinSize();

    @Property(description = "Maximum pane size.")
    public abstract String getMaxSize();

    @Property(description = "Minimum pane width.")
    public abstract String getMinWidth();

    @Property(description = "Maximum pane width.")
    public abstract String getMaxWidth();

    @Property(description = "Minimum pane height.")
    public abstract String getMinHeight();

    @Property(description = "Maximum pane height.")
    public abstract String getMaxHeight();

    @Property(description = "Spacing when pane is open.", defaultValue = "6")
    public abstract int getSpacingOpen();

    @Property(description = "Spacing when pane is closed.", defaultValue = "6")
    public abstract int getSpacingClosed();

    @Property(description = "Pane initially closed.", defaultValue = "false")
    public abstract boolean isInitClosed();

    @Property(description = "Pane initially hidden.", defaultValue = "false")
    public abstract boolean isInitHidden();

    @Property(description = "Resize while dragging.", defaultValue = "false")
    public abstract boolean isResizeWhileDragging();

    @Property(description = "Tooltip when toggler is open.")
    public abstract String getTogglerTipOpen();

    @Property(description = "Tooltip when toggler is closed.")
    public abstract String getTogglerTipClosed();

    @Property(description = "Resizer tooltip.")
    public abstract String getResizerTip();

    @Property(description = "Slider tooltip.")
    public abstract String getSliderTip();

    @Property(description = "Mask pane contents.", defaultValue = "false")
    public abstract boolean isMaskContents();

    @Property(description = "Mask pane objects.", defaultValue = "false")
    public abstract boolean isMaskObjects();
}
