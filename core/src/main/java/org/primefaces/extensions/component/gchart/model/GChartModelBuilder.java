/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.gchart.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.TreeNode;
import org.primefaces.util.Constants;

public class GChartModelBuilder {
    private final List<GChartModelRow> rows = new ArrayList<>(0);
    private GChartType gChartType;
    private final Map<String, Object> options = new HashMap<>(0);
    private final List<Object> columns = new ArrayList<>(0);

    public GChartModelBuilder setChartType(final GChartType chartType) {
        if (gChartType != null) {
            throw new IllegalStateException("GChart Type already set");
        }
        gChartType = chartType;

        return this;
    }

    public GChartModelBuilder importTreeNode(final TreeNode root) {
        final String label = String.valueOf(root.getData());
        final String parentLabel = root.getParent() != null ? String.valueOf(root.getParent().getData()) : Constants.EMPTY_STRING;

        this.addRow(label, parentLabel);

        final List<TreeNode> nodes = root.getChildren();
        for (final TreeNode node : nodes) {
            importTreeNode(node);
        }

        return this;
    }

    public GChartModelBuilder addColumns(final Object... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public GChartModelBuilder addColumns(final Collection<Object> columns) {
        this.columns.addAll(columns);
        return this;
    }

    public GChartModelBuilder addRow(final String label, final Object... objects) {
        rows.add(new DefaultGChartModelRow(label, Arrays.asList(objects)));
        return this;
    }

    public GChartModelBuilder addRow(final String label, final Collection<Object> objects) {
        rows.add(new DefaultGChartModelRow(label, objects));
        return this;
    }

    public GChartModelBuilder addOption(final String name, final Object value) {
        options.put(name, value);
        return this;
    }

    public GChartModel build() {
        return new DefaultGChartModel(rows, gChartType, options, columns);
    }
}
