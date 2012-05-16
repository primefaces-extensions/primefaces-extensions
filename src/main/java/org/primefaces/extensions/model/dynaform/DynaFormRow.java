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

/**
 * Class representing a row inside of <code>DynaForm</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormRow implements Serializable {

	private static final long serialVersionUID = 20120514L;

	private int row;

	private boolean extended;

	private int totalColspan = 0;

	private DynaFormModel dynaFormModel;

	private List<DynaFormControl> dynaFormControls = new ArrayList<DynaFormControl>();

	public DynaFormRow(int row, boolean extended, DynaFormModel dynaFormModel) {
		this.row = row;
		this.extended = extended;
		this.dynaFormModel = dynaFormModel;
	}

	public DynaFormControl addControl(Object data, int colspan, int rowspan) {
		return addControl(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, false);
	}

	public DynaFormControl addControl(Object data, int colspan, int rowspan, boolean applyLabelStyle) {
		return addControl(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan, applyLabelStyle);
	}

	public DynaFormControl addControl(Object data, String type, int colspan, int rowspan) {
		return addControl(data, type, colspan, rowspan, false);
	}

	public DynaFormControl addControl(Object data, String type, int colspan, int rowspan, boolean applyLabelStyle) {
		DynaFormControl dynaFormControl =
		    new DynaFormControl(data, type, colspan, rowspan, row, dynaFormControls.size() + 1, extended, applyLabelStyle);

		dynaFormControls.add(dynaFormControl);
		dynaFormModel.getControls().add(dynaFormControl);

		totalColspan = totalColspan + colspan;

		return dynaFormControl;
	}

	public List<DynaFormControl> getDynaFormControls() {
		return dynaFormControls;
	}

	public int getTotalColspan() {
		return totalColspan;
	}
}
