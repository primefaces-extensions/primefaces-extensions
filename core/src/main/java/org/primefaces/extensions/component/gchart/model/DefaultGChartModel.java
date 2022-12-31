/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
