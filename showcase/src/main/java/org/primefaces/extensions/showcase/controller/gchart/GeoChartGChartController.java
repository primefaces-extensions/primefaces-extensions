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
        colorAxis.put("colors", new String[] {"white", "orange"});

        chartModel = new GChartModelBuilder().setChartType(GChartType.GEO).addColumns("Country", "Popularity")
                    .addRow(Locale.GERMANY.getDisplayCountry(englishLocale), 1200)
                    .addRow(Locale.FRANCE.getDisplayCountry(englishLocale), 1800).addRow("Russia", 1800)
                    .addRow(Locale.ITALY.getDisplayCountry(englishLocale), 2000)
                    .addRow(Locale.CHINA.getDisplayCountry(englishLocale), 2200)
                    .addRow(Locale.US.getDisplayCountry(englishLocale), 2500).addOption("colorAxis", colorAxis).build();
    }
}