/**
 * Copyright 2011-2017 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.gchart.model;

public enum GChartType {
   
	PIE("PieChart"),
	AREA("AreaChart"),
	BAR("BarChart"),
	GEO("GeoChart"),
	ORGANIZATIONAL("OrgChart"),
	COLUMN("ColumnChart"),
	LINE("LineChart");
	
	public String getChartName() {
		return chartName;
	}

	private GChartType(String chartName) {
		this.chartName = chartName;
	}

	private String chartName;
}
