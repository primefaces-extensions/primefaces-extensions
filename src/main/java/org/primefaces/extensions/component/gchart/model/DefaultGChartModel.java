/**
 * Copyright 2011-2017 PrimeFaces Extensions
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class DefaultGChartModel implements GChartModel {

    private static final long serialVersionUID = -4757917806522708660L;

    private List<GChartModelRow> rows;
    private GChartType gChartType;
    private Map<String, Object> options;
    private List<Object> columns;

    public DefaultGChartModel(List<GChartModelRow> rows, GChartType gChartType,
                Map<String, Object> options, List<Object> columns) {
        super();
        this.rows = rows;
        this.gChartType = gChartType;
        this.options = options;
        this.columns = columns;
    }

    public GChartType getgChartType() {
        return gChartType;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public Collection<Object> getColumns() {
        return columns;
    }

    public GChartType getChartType() {
        return gChartType;
    }

    public Collection<GChartModelRow> getRows() {
        return rows;
    }

    public String toJson() {
        JsonObject root = new JsonObject();

        root.addProperty("type", this.getChartType().getChartName());
        root.add("options", GsonConverter.getGson().toJsonTree(this.getOptions()));
        root.add("data", extractData());

        return GsonConverter.getGson().toJson(root);
    }

    @SuppressWarnings("unchecked")
    protected JsonElement extractData() {
        Collection<Collection<Object>> dataTable = new ArrayList<Collection<Object>>(0);

        dataTable.add(this.getColumns());

        for (GChartModelRow row : this.getRows()) {
            Collection<Object> dataRow = new ArrayList<Object>(0);
            dataRow.add(row.getLabel());
            dataRow.addAll(row.getValues());

            dataTable.add(dataRow);
        }

        return GsonConverter.getGson().toJsonTree(dataTable);
    }

}
