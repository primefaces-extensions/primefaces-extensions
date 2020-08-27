package org.primefaces.extensions.showcase.controller.gchart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartType;

@Named
@RequestScoped
public class GeoChartGChartController implements Serializable {

	private static final long serialVersionUID = 253762400419864192L;

	private GChartModel chartModel = null;

	public GChartModel getChart() {
		return chartModel;
	}

	@PostConstruct
	public void generateModel() {

		final Locale englishLocale = Locale.ENGLISH;

		final Map<String, Object> colorAxis = new HashMap<String, Object>();
		colorAxis.put("colors", new String[] { "white", "orange" });

		chartModel = new GChartModelBuilder().setChartType(GChartType.GEO).addColumns("Country", "Popularity")
				.addRow(Locale.GERMANY.getDisplayCountry(englishLocale), 1200)
				.addRow(Locale.FRANCE.getDisplayCountry(englishLocale), 1800).addRow("Russia", 1800)
				.addRow(Locale.ITALY.getDisplayCountry(englishLocale), 2000)
				.addRow(Locale.CHINA.getDisplayCountry(englishLocale), 2200)
				.addRow(Locale.US.getDisplayCountry(englishLocale), 2500).addOption("colorAxis", colorAxis).build();
	}
}