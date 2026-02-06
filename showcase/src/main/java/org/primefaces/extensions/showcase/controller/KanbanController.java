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

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * KanbanController
 *
 * @author @jxmai / Melloware
 * @since 16.0.0
 */
@Named
@ViewScoped
public class KanbanController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        // To Do column
        KanbanColumn todoColumn = new KanbanColumn("todo", "To Do");
        todoColumn.addItem(new KanbanItem("item1", "Add support for Kanban"));
        todoColumn.getItems().get(0).setDescription("Create a new Kanban component for PrimeFaces Extensions");
        todoColumn.addItem(new KanbanItem("item2", "Write documentation"));
        todoColumn.getItems().get(1).setDescription("Document the new component");

        // In Progress column
        KanbanColumn inProgressColumn = new KanbanColumn("inprogress", "In Progress");
        inProgressColumn.addItem(new KanbanItem("item3", "Design component"));
        inProgressColumn.getItems().get(0).setDescription("Design the Kanban board component");

        // Done column
        KanbanColumn doneColumn = new KanbanColumn("done", "Done");
        doneColumn.addItem(new KanbanItem("item4", "Research libraries"));
        doneColumn.getItems().get(0).setDescription("Research available Kanban libraries");

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

    public void setColumns(List<KanbanColumn> columns) {
        this.columns = columns;
    }
}
