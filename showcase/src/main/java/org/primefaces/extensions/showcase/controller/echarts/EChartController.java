/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.echarts;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import org.primefaces.extensions.event.EChartEvent;

@Named
@RequestScoped
public class EChartController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String json;

    @PostConstruct
    public void init() {
        createJsonModel();
    }

    public void createJsonModel() {
        json = "{\n" +
                    "    \"title\": {\n" +
                    "        \"text\": \"Apache ECharts Line Chart\"\n" +
                    "    },\n" +
                    "    \"xAxis\": {\n" +
                    "        \"type\": \"category\",\n" +
                    "        \"data\": [\"Mon\", \"Tue\", \"Wed\", \"Thu\", \"Fri\", \"Sat\", \"Sun\"]\n" +
                    "    },\n" +
                    "    \"yAxis\": {\n" +
                    "        \"type\": \"value\"\n" +
                    "    },\n" +
                    "    \"series\": [\n" +
                    "        {\n" +
                    "            \"name\": \"Some data\",\n" +
                    "            \"data\": [150, 230, 224, 218, 135, 147, 260],\n" +
                    "            \"type\": \"line\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"name\": \"Other data\",\n" +
                    "            \"data\": [110, 260, 124, 118, 235, 100, 200],\n" +
                    "            \"type\": \"line\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"legend\": {},\n" +
                    "    \"dataZoom\": [\n" +
                    "        {\n" +
                    "            \"type\": \"slider\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
    }

    public void itemSelect(EChartEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String message = "Clicked " + event.getName()
                    + ", series index " + event.getSeriesIndex()
                    + ", data index " + event.getDataIndex()
                    + ", value " + event.getData();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
        facesContext.addMessage(null, facesMessage);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
