package org.primefaces.extensions.component.gchart.model;

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

}