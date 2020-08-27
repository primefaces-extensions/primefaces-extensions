package org.primefaces.extensions.showcase.controller.gchart;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartType;

@Named
@RequestScoped
public class MultipleSeriesGChartController implements Serializable {

	private static final long serialVersionUID = 253762400419864192L;

	private GChartModel chartModel = null;

	public GChartModel getChart() {
		return chartModel;
	}

	@PostConstruct
	public void generateModel() {
		chartModel = new GChartModelBuilder().setChartType(GChartType.COLUMN).addColumns("Year", "Salves", "Expenses")
				.addRow("2010", 1000, 400).addRow("2011", 1200, 800).addRow("2012", 2000, 1800)
				.addRow("2013", 1500, 1800).addRow("2014", 1300, 1000).build();
	}
}