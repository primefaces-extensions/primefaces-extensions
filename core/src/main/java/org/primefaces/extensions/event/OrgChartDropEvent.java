/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.shaded.json.JSONObject;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class OrgChartDropEvent extends AjaxBehaviorEvent {

    public static final String NAME = "drop";
    private static final long serialVersionUID = 1L;

    private transient JSONObject hierarchy;

    private String draggedNodeId;

    private String droppedZoneId;

    public OrgChartDropEvent(UIComponent component, Behavior behavior, String hierarchyStr,
                String draggedNodeId, String droppedZoneId) {
        super(component, behavior);
        hierarchy = new JSONObject(hierarchyStr);
        this.draggedNodeId = draggedNodeId;
        this.droppedZoneId = droppedZoneId;
    }

    public JSONObject getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(JSONObject hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getDraggedNodeId() {
        return draggedNodeId;
    }

    public void setDraggedNodeId(String draggedNodeId) {
        this.draggedNodeId = draggedNodeId;
    }

    public String getDroppedZoneId() {
        return droppedZoneId;
    }

    public void setDroppedZoneId(String droppedZoneId) {
        this.droppedZoneId = droppedZoneId;
    }

}
