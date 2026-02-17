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
package org.primefaces.extensions.kanban;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;

/**
 * Test for Kanban model classes.
 *
 * @author @jxmai
 * @version $Revision$
 * @since 16.0.0
 */
public class KanbanModelTest {

    private List<KanbanColumn> columns;

    @BeforeEach
    void setUp() {
        columns = new ArrayList<>();

        KanbanColumn todoColumn = new KanbanColumn("todo", "To Do");
        todoColumn.addItem(new KanbanItem("item1", "Task 1"));
        todoColumn.addItem(new KanbanItem("item2", "Task 2"));

        KanbanColumn doneColumn = new KanbanColumn("done", "Done");
        doneColumn.addItem(new KanbanItem("item3", "Task 3"));

        columns.add(todoColumn);
        columns.add(doneColumn);
    }

    @Test
    void testColumnCreation() {
        assertNotNull(columns);
        assertEquals(2, columns.size());
    }

    @Test
    void testColumnProperties() {
        KanbanColumn column = columns.get(0);
        assertEquals("todo", column.getId());
        assertEquals("To Do", column.getTitle());
        assertEquals(2, column.getItems().size());
    }

    @Test
    void testItemProperties() {
        KanbanColumn column = columns.get(0);
        KanbanItem item = column.getItems().get(0);
        assertEquals("item1", item.getId());
        assertEquals("Task 1", item.getTitle());
    }

    @Test
    void testAddItem() {
        KanbanColumn column = columns.get(1);
        KanbanItem newItem = new KanbanItem("item4", "Task 4");
        newItem.setDescription("New task description");
        column.addItem(newItem);

        assertEquals(2, column.getItems().size());
        assertEquals("Task 4", column.getItems().get(1).getTitle());
        assertEquals("New task description", column.getItems().get(1).getDescription());
    }
}