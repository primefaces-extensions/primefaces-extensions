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
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * KanbanLayoutController
 *
 * @since 16.0.0
 */
@Named
@ViewScoped
public class KanbanLayoutController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;

    private String gutter = "15px";
    private String widthBoard = "250px";
    private boolean responsivePercentage;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn backlogColumn = new KanbanColumn("backlog", "Backlog");
        backlogColumn.setCssClass("kanban-backlog");
        backlogColumn.addItem(new KanbanItem("item1", "Gather requirements",
                    "Collect and document all project requirements"));
        backlogColumn.addItem(new KanbanItem("item2", "Create wireframes",
                    "Design initial wireframes for the new feature"));

        KanbanColumn designColumn = new KanbanColumn("design", "Design");
        designColumn.setCssClass("kanban-design");
        designColumn.addItem(new KanbanItem("item3", "UI mockups",
                    "Create high-fidelity UI mockups"));
        designColumn.addItem(new KanbanItem("item4", "Review designs",
                    "Review designs with stakeholders"));

        KanbanColumn developmentColumn = new KanbanColumn("development", "Development");
        developmentColumn.setCssClass("kanban-development");
        developmentColumn.addItem(new KanbanItem("item5", "Set up project",
                    "Initialize project and configure build tools"));
        developmentColumn.addItem(new KanbanItem("item6", "Implement core",
                    "Implement core functionality"));

        KanbanColumn testingColumn = new KanbanColumn("testing", "Testing");
        testingColumn.setCssClass("kanban-testing");
        testingColumn.addItem(new KanbanItem("item7", "Write unit tests",
                    "Write unit tests for all modules"));
        testingColumn.addItem(new KanbanItem("item8", "Integration tests",
                    "Run and validate integration tests"));

        columns.add(backlogColumn);
        columns.add(designColumn);
        columns.add(developmentColumn);
        columns.add(testingColumn);
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<KanbanColumn> columns) {
        this.columns = columns;
    }

    public String getGutter() {
        return gutter;
    }

    public void setGutter(String gutter) {
        this.gutter = gutter;
    }

    public String getWidthBoard() {
        return widthBoard;
    }

    public void setWidthBoard(String widthBoard) {
        this.widthBoard = widthBoard;
    }

    public boolean isResponsivePercentage() {
        return responsivePercentage;
    }

    public void setResponsivePercentage(boolean responsivePercentage) {
        this.responsivePercentage = responsivePercentage;
    }
}
