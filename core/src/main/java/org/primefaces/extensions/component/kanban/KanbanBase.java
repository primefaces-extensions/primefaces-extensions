/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import java.util.List;

import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * Component base class for the <code>Kanban</code> component.
 *
 * @author jxmai
 * @since 15.0.16
 */
public abstract class KanbanBase extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Kanban";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.kanban.KanbanRenderer";

    public static final String STYLE_CLASS = "ui-kanban";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        value, style, styleClass, draggable, addItemButton, extender, gutter, widthBoard, responsivePercentage, dragBoards, bindContextMenu, dragHandle
    }

    public KanbanBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public List getValue() {
        return (List) getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(List value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public boolean isDraggable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggable, true);
    }

    public void setDraggable(boolean draggable) {
        getStateHelper().put(PropertyKeys.draggable, draggable);
    }

    public boolean isAddItemButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.addItemButton, false);
    }

    public void setAddItemButton(boolean addItemButton) {
        getStateHelper().put(PropertyKeys.addItemButton, addItemButton);
    }

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
    }

    public String getGutter() {
        return (String) getStateHelper().eval(PropertyKeys.gutter, "15px");
    }

    public void setGutter(String gutter) {
        getStateHelper().put(PropertyKeys.gutter, gutter);
    }

    public String getWidthBoard() {
        return (String) getStateHelper().eval(PropertyKeys.widthBoard, "250px");
    }

    public void setWidthBoard(String widthBoard) {
        getStateHelper().put(PropertyKeys.widthBoard, widthBoard);
    }

    public boolean isResponsivePercentage() {
        return (Boolean) getStateHelper().eval(PropertyKeys.responsivePercentage, false);
    }

    public void setResponsivePercentage(boolean responsivePercentage) {
        getStateHelper().put(PropertyKeys.responsivePercentage, responsivePercentage);
    }

    public boolean isDragBoards() {
        return (Boolean) getStateHelper().eval(PropertyKeys.dragBoards, true);
    }

    public void setDragBoards(boolean dragBoards) {
        getStateHelper().put(PropertyKeys.dragBoards, dragBoards);
    }

    public String getBindContextMenu() {
        return (String) getStateHelper().eval(PropertyKeys.bindContextMenu, null);
    }

    public void setBindContextMenu(String bindContextMenu) {
        getStateHelper().put(PropertyKeys.bindContextMenu, bindContextMenu);
    }

    public boolean isDragHandle() {
        return (Boolean) getStateHelper().eval(PropertyKeys.dragHandle, false);
    }

    public void setDragHandle(boolean dragHandle) {
        getStateHelper().put(PropertyKeys.dragHandle, dragHandle);
    }
}
