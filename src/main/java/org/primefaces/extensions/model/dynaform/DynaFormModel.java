/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.model.dynaform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class for <code>DynaForm</code> component. It represents a data grid.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormModel implements Serializable {

	private Map<Integer, Map<Integer, DynaFormControl>> regularGrid;

	private Map<Integer, Map<Integer, DynaFormControl>> extendedGrid;

	private List<DynaFormControl> controls = new ArrayList<DynaFormControl>();

	private int maxRow;

	private int maxColumn;

	public Map<Integer, Map<Integer, DynaFormControl>> getRegularGrid() {
		return regularGrid;
	}

	public Map<Integer, Map<Integer, DynaFormControl>> getExtendedGrid() {
		return extendedGrid;
	}

	public List<DynaFormControl> getControls() {
		return controls;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public int getMaxColumn() {
		return maxColumn;
	}

	public String addRegular(int colspan, int rowspan, int row, int column) {
		return addRegular(null, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column);
	}

	public String addRegular(int colspan, int rowspan, int row, int column, String forValue) {
		return addRegular(null, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column, forValue);
	}

	public String addRegular(Object data, int colspan, int rowspan, int row, int column) {
		return addRegular(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column);
	}

	public String addRegular(Object data, int colspan, int rowspan, int row, int column, String forValue) {
		return addRegular(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column, forValue);
	}

	public String addRegular(Object data, String type, int colspan, int rowspan, int row, int column) {
		return addRegular(data, type, colspan, rowspan, row, column, null);
	}

	public String addExtended(int colspan, int rowspan, int row, int column) {
		return addExtended(null, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column);
	}

	public String addExtended(int colspan, int rowspan, int row, int column, String forValue) {
		return addExtended(null, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column, forValue);
	}

	public String addExtended(Object data, int colspan, int rowspan, int row, int column) {
		return addExtended(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column);
	}

	public String addExtended(Object data, int colspan, int rowspan, int row, int column, String forValue) {
		return addExtended(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column, forValue);
	}

	public String addExtended(Object data, String type, int colspan, int rowspan, int row, int column) {
		return addExtended(data, type, colspan, rowspan, row, column, null);
	}

	public String addRegular(Object data, String type, int colspan, int rowspan, int row, int column, String forValue) {
		return addControl(data, type, colspan, rowspan, row, column, forValue, false);
	}

	public String addExtended(Object data, String type, int colspan, int rowspan, int row, int column, String forValue) {
		return addControl(data, type, colspan, rowspan, row, column, forValue, true);
	}

	private String addControl(Object data, String type, int colspan, int rowspan, int row, int column, String forValue,
	                          boolean extended) {
		DynaFormControl dynaFormControl = new DynaFormControl(data, type, colspan, rowspan, row, column, forValue, extended);
		if (extended) {
			if (extendedGrid == null) {
				extendedGrid = new HashMap<Integer, Map<Integer, DynaFormControl>>();
			}

			fillGrid(extendedGrid, row, column, dynaFormControl);
		} else {
			if (regularGrid == null) {
				regularGrid = new HashMap<Integer, Map<Integer, DynaFormControl>>();
			}

			fillGrid(regularGrid, row, column, dynaFormControl);
		}

		return dynaFormControl.getKey();
	}

	private void fillGrid(Map<Integer, Map<Integer, DynaFormControl>> grid, int row, int column,
	                      DynaFormControl dynaFormControl) {
		Map<Integer, DynaFormControl> map = grid.get(row);
		if (map == null) {
			map = new HashMap<Integer, DynaFormControl>();
			grid.put(row, map);
		}

		map.put(column, dynaFormControl);

		controls.add(dynaFormControl);

		if (maxRow < row) {
			maxRow = row;
		}

		if (maxColumn < column) {
			maxColumn = column;
		}
	}
}
