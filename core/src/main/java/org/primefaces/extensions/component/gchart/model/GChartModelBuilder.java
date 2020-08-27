/*
 * Copyright 2011-2020 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    public GChartModelBuilder setChartType(GChartType chartType) {
        if (gChartType != null) {
            throw new IllegalStateException("GChart Type already set");
        }
        gChartType = chartType;

        return this;
    }

    public GChartModelBuilder importTreeNode(TreeNode root) {
        final String label = String.valueOf(root.getData());
        final String parentLabel = root.getParent() != null ? String.valueOf(root.getParent().getData()) : Constants.EMPTY_STRING;

        this.addRow(label, parentLabel);

        for (final TreeNode node : root.getChildren()) {
            importTreeNode(node);
        }

        return this;
    }

    public GChartModelBuilder addColumns(Object... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public GChartModelBuilder addColumns(Collection<Object> columns) {
        this.columns.addAll(columns);
        return this;
    }

    public GChartModelBuilder addRow(String label, Object... objects) {
        rows.add(new DefaultGChartModelRow(label, Arrays.asList(objects)));
        return this;
    }

    public GChartModelBuilder addRow(String label, Collection<Object> objects) {
        rows.add(new DefaultGChartModelRow(label, objects));
        return this;
    }

    public GChartModelBuilder addOption(String name, Object value) {
        options.put(name, value);
        return this;
    }

    public GChartModel build() {
        return new DefaultGChartModel(rows, gChartType, options, columns);
    }
}
