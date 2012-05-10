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

/**
 * POJO for exposed "varStatus" attribute.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormVarStatus {

	private int colspan;

	private int rowspan;

	private int row;

	private int column;

	private boolean extended;

	private String forValue;

	public DynaFormVarStatus(int colspan, int rowspan, int row, int column, boolean extended, String forValue) {
		this.colspan = colspan;
		this.rowspan = rowspan;
		this.row = row;
		this.column = column;
		this.extended = extended;
		this.forValue = forValue;
	}

	public int getColspan() {
		return colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isExtended() {
		return extended;
	}

	public String getForValue() {
		return forValue;
	}
}
