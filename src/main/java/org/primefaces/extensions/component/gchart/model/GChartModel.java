package org.primefaces.extensions.component.gchart.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface GChartModel extends Serializable{
	public abstract Collection<GChartModelRow> getRows();
	public abstract Collection<String> getColumns();
	public abstract Map<String,Object> getOptions();
	public abstract GChartType getChartType();
}
