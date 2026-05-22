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
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.extensions.event.KanbanBoardDragEvent;
import org.primefaces.extensions.event.KanbanDragEvent;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * KanbanBoardDragController
 *
 * @since 16.0.0
 */
@Named
@ViewScoped
public class KanbanBoardDragController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;

    private boolean dragBoards = true;

    private List<String> boardOrder;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn discoveryColumn = new KanbanColumn("discovery", "Discovery");
        discoveryColumn.setCssClass("kanban-discovery");
        discoveryColumn.addItem(new KanbanItem("item1", "User research",
                    "Conduct user interviews and surveys"));
        discoveryColumn.addItem(new KanbanItem("item2", "Market analysis",
                    "Analyze competitors and market trends"));

        KanbanColumn designColumn = new KanbanColumn("design", "Design");
        designColumn.setCssClass("kanban-design");
        designColumn.addItem(new KanbanItem("item3", "Wireframes",
                    "Create low-fidelity wireframes"));
        designColumn.addItem(new KanbanItem("item4", "Prototype",
                    "Build interactive prototype"));

        KanbanColumn developmentColumn = new KanbanColumn("development", "Development");
        developmentColumn.setCssClass("kanban-development");
        developmentColumn.addItem(new KanbanItem("item5", "Frontend",
                    "Implement UI components"));
        developmentColumn.addItem(new KanbanItem("item6", "Backend",
                    "Build REST API endpoints"));

        KanbanColumn testingColumn = new KanbanColumn("testing", "Testing");
        testingColumn.setCssClass("kanban-testing");
        testingColumn.addItem(new KanbanItem("item7", "Unit tests",
                    "Write unit tests for all modules"));
        testingColumn.addItem(new KanbanItem("item8", "E2E tests",
                    "Run end-to-end test suite"));

        columns.add(discoveryColumn);
        columns.add(designColumn);
        columns.add(developmentColumn);
        columns.add(testingColumn);

        boardOrder = new ArrayList<>();
        for (KanbanColumn col : columns) {
            boardOrder.add(col.getId());
        }
    }

    public void onDragBoard(KanbanBoardDragEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Board Drag Started",
                    "Board \"" + event.getBoardId() + "\" is being dragged");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onDragendBoard(KanbanBoardDragEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Board Drag Ended",
                    "Board \"" + event.getBoardId() + "\" moved to position " + event.getNewPosition());
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public List<String> getBoardOrder() {
        return boardOrder;
    }

    public void setBoardOrder(List<String> boardOrder) {
        this.boardOrder = boardOrder;
    }

    public boolean isDragBoards() {
        return dragBoards;
    }

    public void setDragBoards(boolean dragBoards) {
        this.dragBoards = dragBoards;
    }
}
