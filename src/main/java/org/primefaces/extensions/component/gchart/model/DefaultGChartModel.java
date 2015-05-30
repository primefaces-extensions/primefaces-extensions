package org.primefaces.extensions.component.gchart.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.primefaces.extensions.util.json.GsonConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

class DefaultGChartModel implements GChartModel{

	private static final long serialVersionUID = -4757917806522708660L;
	
	public DefaultGChartModel(List<GChartModelRow> rows, GChartType gChartType,
			Map<String, Object> options, List<String> columns) {
		super();
		this.rows = rows;
		this.gChartType = gChartType;
		this.options = options;
		this.columns = columns;
	}

	private List<GChartModelRow> rows;
	private GChartType gChartType;
	private Map<String,Object> options;
	private List<String> columns;

	public GChartType getgChartType() {
		return gChartType;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public Collection<String> getColumns() {
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

        root.addProperty("type",this.getChartType().getChartName());
        root.add("options", GsonConverter.getGson().toJsonTree(this.getOptions()));
        root.add("data",extractData());

        return GsonConverter.getGson().toJson(root);
    }

    @SuppressWarnings("unchecked")
    protected JsonElement extractData() {
        Collection<Collection<Object>> dataTable = new ArrayList<Collection<Object>>(0);

        dataTable.add((Collection<Object>)(Collection<?>)this.getColumns());

        for (GChartModelRow row : this.getRows()) {
            Collection<Object> dataRow = new ArrayList<Object>(0);
            dataRow.add(row.getLabel());
            dataRow.addAll(row.getValues());

            dataTable.add(dataRow);
        }

        return GsonConverter.getGson().toJsonTree(dataTable);
    }

}