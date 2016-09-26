package org.primefaces.extensions.component.gchart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultGChartModelRow implements GChartModelRow {
   
   private static final long serialVersionUID = -4757917806522708660L;
	
	private final String label;
	private final List<Object> values;
	
	public DefaultGChartModelRow(String label, Collection<Object> values) {
		super();
		this.label = label;
		this.values = new ArrayList<Object>(values);
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	@Override
	public Collection<Object> getValues() {
		return values;
	}
}
