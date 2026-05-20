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

import org.primefaces.extensions.event.KanbanAddEvent;
import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.event.KanbanItemClickEvent;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * KanbanController
 *
 * @author jxmai
 * @since 15.0.16
 */
@Named
@ViewScoped
public class KanbanController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;
    private KanbanItem selectedItem;
    private String selectedColumnId;

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

    public void onAddItem(KanbanAddEvent event) {
        for (KanbanColumn column : columns) {
            if (column.getId().equals(event.getColumnId())) {
                int count = column.getItems().size() + 1;
                column.addItem(new KanbanItem("new-" + count, "New Task " + count,
                            "Click to edit this task"));
                break;
            }
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Added",
                    "New item added to column " + event.getColumnId());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onItemClick(KanbanItemClickEvent event) {
        String itemId = event.getItemId();
        String columnId = event.getColumnId();

        for (KanbanColumn col : columns) {
            if (col.getId().equals(columnId)) {
                for (KanbanItem item : col.getItems()) {
                    if (item.getId().equals(itemId)) {
                        this.selectedItem = item;
                        this.selectedColumnId = columnId;
                        break;
                    }
                }
            }
        }

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Clicked",
                    "Loaded task: " + (selectedItem != null ? selectedItem.getTitle() : itemId));
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void saveItem() {
        if (selectedItem != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Updated",
                        "Updated task: " + selectedItem.getTitle());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<KanbanColumn> columns) {
        this.columns = columns;
    }

    public KanbanItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(KanbanItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getSelectedColumnId() {
        return selectedColumnId;
    }

    public void setSelectedColumnId(String selectedColumnId) {
        this.selectedColumnId = selectedColumnId;
    }
}
