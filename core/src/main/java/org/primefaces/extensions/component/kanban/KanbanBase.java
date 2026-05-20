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
package org.primefaces.extensions.component.kanban;

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.KanbanAddEvent;
import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.event.KanbanItemClickEvent;

/**
 * Component base class for the <code>Kanban</code> component.
 *
 * @author jxmai
 * @since 16.0.0
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "drop", event = KanbanDragEvent.class,
                        description = "Fires when a Kanban item is dropped onto a column.", defaultEvent = true),
            @FacesBehaviorEvent(name = "itemAdd", event = KanbanAddEvent.class,
                        description = "Fires when the add item button is clicked."),
            @FacesBehaviorEvent(name = "itemClick", event = KanbanItemClickEvent.class,
                        description = "Fires when a Kanban item is clicked.")
})
public abstract class KanbanBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Kanban";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.kanban.KanbanRenderer";

    public static final String STYLE_CLASS = "ui-kanban";

    public KanbanBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "List of KanbanColumn objects representing the kanban board columns and items.", required = true)
    public abstract java.util.List getValue();

    @Property(description = "Inline CSS style of the component.")
    public abstract String getStyle();

    @Property(description = "Style class of the component.")
    public abstract String getStyleClass();

    @Property(description = "Enable drag-and-drop functionality.", defaultValue = "true")
    public abstract boolean isDraggable();

    @Property(description = "Enable add item button on each board.", defaultValue = "false")
    public abstract boolean isAddItemButton();

    @Property(description = "JavaScript function to extend the widget configuration.")
    public abstract String getExtender();
}
