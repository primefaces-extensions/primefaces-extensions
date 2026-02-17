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
package org.primefaces.extensions.event;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.Behavior;
import jakarta.faces.event.AjaxBehaviorEvent;

/**
 * Event for Kanban drag-and-drop operations.
 *
 * @author @jxmai
 * @since 16.0.0
 */
public class KanbanDragEvent extends AjaxBehaviorEvent {

    public static final String NAME = "drop";
    private static final long serialVersionUID = 1L;

    private String itemId;
    private String sourceColumnId;
    private String targetColumnId;
    private int newPosition;

    public KanbanDragEvent(UIComponent component, Behavior behavior, String itemId,
                String sourceColumnId, String targetColumnId, int newPosition) {
        super(component, behavior);
        this.itemId = itemId;
        this.sourceColumnId = sourceColumnId;
        this.targetColumnId = targetColumnId;
        this.newPosition = newPosition;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSourceColumnId() {
        return sourceColumnId;
    }

    public void setSourceColumnId(String sourceColumnId) {
        this.sourceColumnId = sourceColumnId;
    }

    public String getTargetColumnId() {
        return targetColumnId;
    }

    public void setTargetColumnId(String targetColumnId) {
        this.targetColumnId = targetColumnId;
    }

    public int getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(int newPosition) {
        this.newPosition = newPosition;
    }
}