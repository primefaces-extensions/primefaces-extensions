/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

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