/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.primefaces.extensions.util.json.GsonConverter;

class DefaultGChartModel implements GChartModel {

    private static final long serialVersionUID = -4757917806522708660L;

    private final List<GChartModelRow> rows;
    private final GChartType gChartType;
    private final transient Map<String, Object> options;
    private final transient List<Object> columns;

    public DefaultGChartModel(final List<GChartModelRow> rows, final GChartType gChartType,
                final Map<String, Object> options, final List<Object> columns) {
        super();
        this.rows = rows;
        this.gChartType = gChartType;
        this.options = options;
        this.columns = columns;
    }

    @Override
    public Map<String, Object> getOptions() {
        return options;
    }

    @Override
    public Collection<Object> getColumns() {
        return columns;
    }

    @Override
    public GChartType getChartType() {
        return gChartType;
    }

    @Override
    public Collection<GChartModelRow> getRows() {
        return rows;
    }

    @Override
    public String toJson() {
        final com.google.gson.JsonObject root = new com.google.gson.JsonObject();

        root.addProperty("type", getChartType().getChartName());
        root.add("options", GsonConverter.getGson().toJsonTree(getOptions()));
        root.add("data", extractData());

        return GsonConverter.getGson().toJson(root);
    }

    protected com.google.gson.JsonElement extractData() {
        final Collection<Collection<Object>> dataTable = new ArrayList<>(0);

        dataTable.add(getColumns());

        for (final GChartModelRow row : getRows()) {
            final Collection<Object> dataRow = new ArrayList<>(0);
            dataRow.add(row.getLabel());
            dataRow.addAll(row.getValues());

            dataTable.add(dataRow);
        }

        return GsonConverter.getGson().toJsonTree(dataTable);
    }

}
