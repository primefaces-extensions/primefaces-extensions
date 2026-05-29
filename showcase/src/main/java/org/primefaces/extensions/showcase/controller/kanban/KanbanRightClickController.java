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
package org.primefaces.extensions.showcase.controller.kanban;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import org.primefaces.extensions.event.KanbanAddEvent;
import org.primefaces.extensions.event.KanbanItemRightClickEvent;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * Controller for the Kanban right-click context menu showcase.
 */
@Named
@jakarta.faces.view.ViewScoped
public class KanbanRightClickController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;
    private KanbanItem selectedItem;
    private String selectedColumnId;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn todo = new KanbanColumn();
        todo.setId("todo");
        todo.setTitle("To Do");
        todo.setCssClass("kanban-todo");
        todo.getItems().addAll(List.of(
                    new KanbanItem("task-1", "Design new landing page", "Create wireframes and mockups for the new product landing page"),
                    new KanbanItem("task-2", "Research competitor features", "Analyze top 3 competitor products and summarize feature differences"),
                    new KanbanItem("task-3", "Write unit tests", "Increase code coverage to 80% for the payment module")));
        columns.add(todo);

        KanbanColumn inprogress = new KanbanColumn();
        inprogress.setId("inprogress");
        inprogress.setTitle("In Progress");
        inprogress.setCssClass("kanban-inprogress");
        inprogress.getItems().addAll(List.of(
                    new KanbanItem("task-4", "Implement OAuth integration", "Add Google and GitHub OAuth providers"),
                    new KanbanItem("task-5", "API rate limiting", "Implement rate limiter for public API endpoints")));
        columns.add(inprogress);

        KanbanColumn done = new KanbanColumn();
        done.setId("done");
        done.setTitle("Done");
        done.setCssClass("kanban-done");
        done.getItems().addAll(List.of(
                    new KanbanItem("task-6", "Setup CI/CD pipeline", "Configure GitHub Actions for automated builds and deployment"),
                    new KanbanItem("task-7", "Database migration script", "Write migration scripts for schema updates")));
        columns.add(done);
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }

    public KanbanItem getSelectedItem() {
        return selectedItem;
    }

    public String getSelectedColumnId() {
        return selectedColumnId;
    }

    public void onItemRightClick(final KanbanItemRightClickEvent event) {
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

        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Right-clicked",
                                "Item: " + (selectedItem != null ? selectedItem.getTitle() : itemId) + ", Column: " + columnId));
    }

    public void onItemAdd(final KanbanAddEvent event) {
        String columnId = event.getColumnId();
        for (KanbanColumn column : columns) {
            if (column.getId().equals(columnId)) {
                int count = column.getItems().size() + 1;
                column.addItem(new KanbanItem(columnId + "-new-" + count, "New Task " + count,
                            "Click to edit this task"));
                break;
            }
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Added",
                    "New item added to column " + columnId);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void deleteSelectedItem() {
        if (selectedItem != null) {
            for (KanbanColumn col : columns) {
                col.getItems().removeIf(item -> item.getId().equals(selectedItem.getId()));
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Deleted",
                        "Deleted task: " + selectedItem.getTitle());
            FacesContext.getCurrentInstance().addMessage(null, message);
            selectedItem = null;
            selectedColumnId = null;
        }
    }
}
