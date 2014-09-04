package org.primefaces.extensions.component.gchart.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GChartModelBuilder {
	private List<GChartModelRow> rows = new ArrayList<GChartModelRow>(0);
	private GChartType gChartType;
	private Map<String,Object> options = new HashMap<String, Object>(0);
	private List<String> columns = new ArrayList<String>(0);
	
	public GChartModelBuilder setChartType(GChartType chartType){
		if(gChartType != null){
			throw new IllegalStateException("Type already setted");
		}
		this.gChartType = chartType;
		
		return this;
	}
	
	public GChartModelBuilder addColumns(String... columns){
		this.columns.addAll(Arrays.asList(columns));
		return this;
	}
	
	public GChartModelBuilder addColumns(Collection<String> columns){
		this.columns.addAll(columns);
		return this;
	}
	
	public GChartModelBuilder addRow(String label, Object...objects){
		this.rows.add(new DefaultGChartModelRow(label, Arrays.asList(objects)));
		return this;
	}
	
	public GChartModelBuilder addRow(String label, Collection<Object> objects){
		this.rows.add(new DefaultGChartModelRow(label, objects));
		return this;
	}
	
	public GChartModelBuilder addOption(String name,Object value){
		this.options.put(name, value);
		return this;
	}
	public GChartModel build(){
		//TODO generate exceptions
		return new DefaultGChartModel(rows, gChartType, options, columns);
	}
}
