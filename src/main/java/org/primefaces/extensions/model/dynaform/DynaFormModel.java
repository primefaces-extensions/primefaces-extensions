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

	public String addEditControl(Object data, int colspan, int rowspan, int row, int column, boolean extended) {
		return addEditControl(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column, extended);
	}

	public String addEditControl(Object data, String type, int colspan, int rowspan, int row, int column, boolean extended) {
		DynaFormControl dynaFormControl = new DynaFormEditControl(data, type, colspan, rowspan, row, column, extended);

		return addControl(dynaFormControl, row, column, extended);
	}

	public void addLabelControl(Object data, int colspan, int rowspan, int row, int column, boolean extended, String forValue) {
		addLabelControl(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, row, column, extended, forValue);
	}

	public void addLabelControl(Object data, String type, int colspan, int rowspan, int row, int column, boolean extended,
	                            String forValue) {
		DynaFormControl dynaFormControl = new DynaFormLabelControl(data, type, colspan, rowspan, row, column, extended, forValue);

		addControl(dynaFormControl, row, column, extended);
	}

	private String addControl(DynaFormControl dynaFormControl, int row, int column, boolean extended) {
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
