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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

@Named
@ViewScoped
public class KanbanDragHandleController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;

    private boolean dragHandle = true;
    private String handleStyle = "default";

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn todoColumn = new KanbanColumn("todo", "To Do");
        todoColumn.setCssClass("kanban-todo");
        todoColumn.addItem(new KanbanItem("item1", "Add support for Kanban",
                    "Create a new Kanban component for PrimeFaces Extensions"));
        todoColumn.addItem(new KanbanItem("item2", "Write documentation",
                    "Document the new component"));

        KanbanColumn inProgressColumn = new KanbanColumn("inprogress", "In Progress");
        inProgressColumn.setCssClass("kanban-inprogress");
        inProgressColumn.addItem(new KanbanItem("item3", "Design component",
                    "Design the Kanban board component"));

        KanbanColumn doneColumn = new KanbanColumn("done", "Done");
        doneColumn.setCssClass("kanban-done");
        doneColumn.addItem(new KanbanItem("item4", "Research libraries",
                    "Research available Kanban libraries"));

        columns.add(todoColumn);
        columns.add(inProgressColumn);
        columns.add(doneColumn);
    }

    public void onDrop(KanbanDragEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Moved",
                    "Item " + event.getItemId() + " moved from " + event.getSourceColumnId()
                                + " to " + event.getTargetColumnId() + " at position " + event.getNewPosition());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }

    public boolean isDragHandle() {
        return dragHandle;
    }

    public void setDragHandle(boolean dragHandle) {
        this.dragHandle = dragHandle;
    }

    public String getHandleStyle() {
        return handleStyle;
    }

    public void setHandleStyle(String handleStyle) {
        this.handleStyle = handleStyle;
    }

    public String getExtender() {
        if (!dragHandle) {
            return null;
        }
        if ("grip".equals(handleStyle)) {
            return "kanbanDragHandleGrip";
        }
        return "kanbanDragHandleDefault";
    }
}
