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
package org.primefaces.extensions.component.orgchart;

import jakarta.faces.component.UIData;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.OrgChartClickEvent;
import org.primefaces.extensions.event.OrgChartDropEvent;

/**
 * <code>OrgChart</code> component base class.
 *
 * @author @jxmai / last modified by Melloware
 * @since 6.3
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "click", event = OrgChartClickEvent.class, description = "Fires when a node is clicked.",
                        defaultEvent = true),
            @FacesBehaviorEvent(name = "drop", event = OrgChartDropEvent.class, description = "Fires when a node is dropped after dragging.")
})
public abstract class OrgChartBase extends UIData implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.OrgChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.OrgChartRenderer";

    public OrgChartBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "The node id property name.", defaultValue = "id")
    public abstract String getNodeId();

    @Property(description = "The node content property name.", defaultValue = "title")
    public abstract String getNodeContent();

    @Property(description = "The direction of the chart. 't2b' (top to bottom), 'b2t' (bottom to top), 'l2r' (left to right), 'r2l' (right to left).",
                defaultValue = "t2b")
    public abstract String getDirection();

    @Property(description = "Enable panning on the chart.", defaultValue = "false")
    public abstract Boolean getPan();

    @Property(description = "Toggle sibling nodes responsively.", defaultValue = "false")
    public abstract Boolean getToggleSiblingsResp();

    @Property(description = "The depth of the chart to initially display.", defaultValue = "999")
    public abstract Integer getDepth();

    @Property(description = "Enable export button on the chart.", defaultValue = "false")
    public abstract Boolean getExportButton();

    @Property(description = "The filename for the exported chart.", defaultValue = "OrgChart")
    public abstract String getExportFilename();

    @Property(description = "The file extension for the exported chart.", defaultValue = "png")
    public abstract String getExportFileextension();

    @Property(description = "The symbol to indicate a node has children.", defaultValue = "fa-users")
    public abstract String getParentNodeSymbol();

    @Property(description = "Enable dragging nodes on the chart.", defaultValue = "false")
    public abstract Boolean getDraggable();

    @Property(description = "CSS class to apply to the chart container.")
    public abstract String getChartClass();

    @Property(description = "Enable zooming on the chart.", defaultValue = "false")
    public abstract Boolean getZoom();

    @Property(description = "The maximum zoom-in limit.", defaultValue = "7")
    public abstract Number getZoominLimit();

    @Property(description = "The maximum zoom-out limit.", defaultValue = "0.5")
    public abstract Number getZoomoutLimit();

    @Property(description = "The depth at which the chart switches to vertical layout.")
    public abstract Integer getVerticalDepth();

    @Property(description = "The node title property name.", defaultValue = "name")
    public abstract String getNodeTitle();

    @Property(description = "Name of javascript function to extend the widget.")
    public abstract String getExtender();
}