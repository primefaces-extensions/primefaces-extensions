package org.primefaces.extensions.component.gchart.model;

import java.io.Serializable;
import java.util.Collection;

public interface GChartModelRow extends Serializable {
   Collection<Object> getValues();

   String getLabel();
}
