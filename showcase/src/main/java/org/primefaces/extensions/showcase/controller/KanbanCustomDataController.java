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
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

@Named
@ViewScoped
public class KanbanCustomDataController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<KanbanColumn> columns;

    @PostConstruct
    public void init() {
        columns = new ArrayList<>();

        KanbanColumn todo = new KanbanColumn();
        todo.setId("todo");
        todo.setTitle("To Do");
        todo.setCssClass("kanban-todo");
        todo.getItems().addAll(List.of(
                    new KanbanItem("td-1", "Design new landing page")
                                .putData("priority", "high")
                                .putData("assignee", "Alice")
                                .putData("dueDate", "2026-06-10")
                                .putData("labels", "design,frontend")
                                .putData("storyPoints", 5),
                    new KanbanItem("td-2", "Research competitor features")
                                .putData("priority", "medium")
                                .putData("assignee", "Bob")
                                .putData("dueDate", "2026-06-15")
                                .putData("labels", "research")
                                .putData("storyPoints", 3),
                    new KanbanItem("td-3", "Write unit tests")
                                .putData("priority", "low")
                                .putData("assignee", "Carol")
                                .putData("dueDate", "2026-06-20")
                                .putData("labels", "testing")
                                .putData("storyPoints", 8)));
        columns.add(todo);

        KanbanColumn inprogress = new KanbanColumn();
        inprogress.setId("inprogress");
        inprogress.setTitle("In Progress");
        inprogress.setCssClass("kanban-inprogress");
        inprogress.getItems().addAll(List.of(
                    new KanbanItem("ip-1", "Implement OAuth integration")
                                .putData("priority", "high")
                                .putData("assignee", "Alice")
                                .putData("dueDate", "2026-06-05")
                                .putData("labels", "backend,security")
                                .putData("storyPoints", 13),
                    new KanbanItem("ip-2", "API rate limiting")
                                .putData("priority", "high")
                                .putData("assignee", "Bob")
                                .putData("dueDate", "2026-06-08")
                                .putData("labels", "backend,performance")
                                .putData("storyPoints", 5)));
        columns.add(inprogress);

        KanbanColumn done = new KanbanColumn();
        done.setId("done");
        done.setTitle("Done");
        done.setCssClass("kanban-done");
        done.getItems().addAll(List.of(
                    new KanbanItem("dn-1", "Setup CI/CD pipeline")
                                .putData("priority", "high")
                                .putData("assignee", "Carol")
                                .putData("dueDate", "2026-05-28")
                                .putData("labels", "devops")
                                .putData("storyPoints", 8),
                    new KanbanItem("dn-2", "Database migration script")
                                .putData("priority", "medium")
                                .putData("assignee", "Bob")
                                .putData("dueDate", "2026-05-30")
                                .putData("labels", "backend,database")
                                .putData("storyPoints", 3)));
        columns.add(done);
    }

    public List<KanbanColumn> getColumns() {
        return columns;
    }
}
