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
