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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @since 16.0.0
 */
@Named
@ViewScoped
public class KanbanDropRestrictionsController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn backlogColumn = new KanbanColumn("backlog", "Backlog");
        backlogColumn.setCssClass("kanban-backlog");
        backlogColumn.setDragTo(Arrays.asList("inprogress"));
        backlogColumn.addItem(new KanbanItem("item1", "Research requirements",
                    "Gather and analyze project requirements"));
        backlogColumn.addItem(new KanbanItem("item2", "Plan sprint",
                    "Define sprint goals and tasks"));

        KanbanColumn inProgressColumn = new KanbanColumn("inprogress", "In Progress");
        inProgressColumn.setCssClass("kanban-inprogress");
        inProgressColumn.setDragTo(Arrays.asList("review"));
        inProgressColumn.addItem(new KanbanItem("item3", "Implement feature X",
                    "Core implementation of feature X"));
        inProgressColumn.addItem(new KanbanItem("item4", "Write unit tests",
                    "Add unit tests for new modules"));

        KanbanColumn reviewColumn = new KanbanColumn("review", "Review");
        reviewColumn.setCssClass("kanban-review");
        reviewColumn.setDragTo(Arrays.asList("inprogress", "done"));
        reviewColumn.addItem(new KanbanItem("item5", "Code review PR #42",
                    "Review pull request for feature X"));
        reviewColumn.addItem(new KanbanItem("item6", "QA verification",
                    "Verify bug fixes in staging"));

        KanbanColumn doneColumn = new KanbanColumn("done", "Done");
        doneColumn.setCssClass("kanban-done");
        doneColumn.setDragTo(Arrays.asList("review"));
        doneColumn.addItem(new KanbanItem("item7", "Deploy v1.0",
                    "Production deployment completed"));
        doneColumn.addItem(new KanbanItem("item8", "Documentation",
                    "API documentation updated"));

        columns.add(backlogColumn);
        columns.add(inProgressColumn);
        columns.add(reviewColumn);
        columns.add(doneColumn);
    }

    public void onDrop(KanbanDragEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Moved",
                    "Item \"" + event.getItemId() + "\" moved from " + event.getSourceColumnId()
                                + " to " + event.getTargetColumnId());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<KanbanColumn> columns) {
        this.columns = columns;
    }
}
