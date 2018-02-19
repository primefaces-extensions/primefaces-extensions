/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
    private List<GChartModelRow> rows = new ArrayList<GChartModelRow>(0);
    private GChartType gChartType;
    private Map<String, Object> options = new HashMap<String, Object>(0);
    private List<Object> columns = new ArrayList<Object>(0);

    public GChartModelBuilder setChartType(GChartType chartType) {
        if (gChartType != null) {
            throw new IllegalStateException("GChart Type already set");
        }
        this.gChartType = chartType;

        return this;
    }

    public GChartModelBuilder importTreeNode(TreeNode root) {
        String label = String.valueOf(root.getData());
        String parentLabel = root.getParent() != null ? String.valueOf(root.getParent().getData()) : Constants.EMPTY_STRING;

        this.addRow(label, parentLabel);

        for (TreeNode node : root.getChildren()) {
            this.importTreeNode(node);
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
        this.rows.add(new DefaultGChartModelRow(label, Arrays.asList(objects)));
        return this;
    }

    public GChartModelBuilder addRow(String label, Collection<Object> objects) {
        this.rows.add(new DefaultGChartModelRow(label, objects));
        return this;
    }

    public GChartModelBuilder addOption(String name, Object value) {
        this.options.put(name, value);
        return this;
    }

    public GChartModel build() {
        return new DefaultGChartModel(rows, gChartType, options, columns);
    }
}
