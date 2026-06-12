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
package org.primefaces.extensions.component.commandpalette;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;

/**
 * <code>CommandPalette</code> component base class.
 *
 * @since 16.0.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "select", event = SelectEvent.class, description = "Fires when an item is selected.", defaultEvent = true)
})
public abstract class CommandPaletteBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CommandPalette";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CommandPaletteRenderer";

    public CommandPaletteBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "ID of the target component that triggers the command palette on right-click.", required = true)
    public abstract String getFor();

    @Property(description = "Header label text displayed at the top of the palette.")
    public abstract String getLabel();

    @Property(description = "Placeholder text for the search/filter input.", defaultValue = "Search...")
    public abstract String getFilterPlaceholder();

    @Property(description = "Whether to show the header section.", defaultValue = "true")
    public abstract boolean isShowHeader();

    @Property(description = "Whether to show the filter/search input.", defaultValue = "true")
    public abstract boolean isShowFilter();

    @Property(description = "Client-side callback to execute when an item is selected. Signature: function(option).")
    public abstract String getOnSelect();

    @Property(description = "Width of the palette in pixels or a CSS value.", defaultValue = "280")
    public abstract String getWidth();

    @Property(description = "Maximum height of the palette in pixels or a CSS value.", defaultValue = "400")
    public abstract String getHeight();

    @Property(description = "Event type that triggers the palette. Default is contextmenu (right-click).", defaultValue = "contextmenu")
    public abstract String getTriggerEvent();
}
