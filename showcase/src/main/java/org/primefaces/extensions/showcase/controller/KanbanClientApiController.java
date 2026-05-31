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

import org.primefaces.extensions.event.KanbanItemClickEvent;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * Controller for the Kanban client API showcase.
 */
@Named
@ViewScoped
public class KanbanClientApiController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;
    private String lastClickedItemId;
    private String lastClickedItem;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn backlog = new KanbanColumn("backlog", "Backlog");
        backlog.setCssClass("kanban-backlog");
        backlog.getItems().addAll(List.of(
                    new KanbanItem("t-1", "Research authentication", "Evaluate OAuth"),
                    new KanbanItem("t-2", "Design database schema", "Create ERD")));
        columns.add(backlog);

        KanbanColumn inprogress = new KanbanColumn("inprogress", "In Progress");
        inprogress.setCssClass("kanban-inprogress");
        inprogress.getItems().addAll(List.of(
                    new KanbanItem("t-3", "Implement REST API", "Build CRUD endpoints"),
                    new KanbanItem("t-4", "Add validation", "Server-side validation")));
        columns.add(inprogress);

        KanbanColumn done = new KanbanColumn("done", "Done");
        done.setCssClass("kanban-done");
        done.getItems().addAll(List.of(
                    new KanbanItem("t-5", "Setup CI/CD", "GitHub Actions"),
                    new KanbanItem("t-6", "Write tests", "80% coverage")));
        columns.add(done);
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }

    public String getLastClickedItemId() {
        return lastClickedItemId;
    }

    public String getLastClickedItem() {
        return lastClickedItem;
    }

    public void onItemClick(final KanbanItemClickEvent event) {
        this.lastClickedItemId = event.getItemId();
        String title = resolveItemTitle(lastClickedItemId, event.getColumnId());
        this.lastClickedItem = title != null ? title : lastClickedItemId;
        FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Item Clicked", "Task: " + lastClickedItem));
        org.primefaces.PrimeFaces.current().ajax().addCallbackParam("itemId", lastClickedItemId);
    }

    public void onClientApiCommand() {
        // no-op: used by p:remoteCommand to complete the AJAX cycle
    }

    private String resolveItemTitle(final String itemId, final String columnId) {
        for (KanbanColumn col : columns) {
            if (col.getId().equals(columnId)) {
                for (KanbanItem item : col.getItems()) {
                    if (item.getId().equals(itemId)) {
                        return item.getTitle();
                    }
                }
            }
        }
        return null;
    }
}
