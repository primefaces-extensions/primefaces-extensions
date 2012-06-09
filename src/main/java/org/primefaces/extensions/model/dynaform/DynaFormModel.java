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
import java.util.List;
import java.util.UUID;

/**
 * Model class for <code>DynaForm</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormModel implements Serializable {

	private static final long serialVersionUID = 20120514L;

	private String uuid;

	private List<DynaFormRow> regularRows = new ArrayList<DynaFormRow>();

	private List<DynaFormRow> extendedRows = null;

	private List<DynaFormLabel> labels = new ArrayList<DynaFormLabel>();

	private List<DynaFormControl> controls = new ArrayList<DynaFormControl>();

	public DynaFormModel() {
		uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return uuid;
	}

	public List<DynaFormRow> getRegularRows() {
		return regularRows;
	}

	public List<DynaFormRow> getExtendedRows() {
		return extendedRows;
	}

	public List<DynaFormControl> getControls() {
		return controls;
	}

	public List<DynaFormLabel> getLabels() {
		return labels;
	}

	public DynaFormRow createRegularRow() {
		DynaFormRow dynaFormRow = new DynaFormRow(regularRows.size() + 1, false, this);
		regularRows.add(dynaFormRow);

		return dynaFormRow;
	}

	public DynaFormRow createExtendedRow() {
		if (extendedRows == null) {
			extendedRows = new ArrayList<DynaFormRow>();
		}

		DynaFormRow dynaFormRow = new DynaFormRow(extendedRows.size() + 1, true, this);
		extendedRows.add(dynaFormRow);

		return dynaFormRow;
	}

	public boolean isExistExtendedGrid() {
		return (getExtendedRows() != null && !getExtendedRows().isEmpty());
	}
}
