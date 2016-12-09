package org.primefaces.extensions.component.gchart.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface GChartModel extends Serializable {
   Collection<GChartModelRow> getRows();

   Collection<Object> getColumns();

   Map<String, Object> getOptions();

   GChartType getChartType();

   String toJson();
}
