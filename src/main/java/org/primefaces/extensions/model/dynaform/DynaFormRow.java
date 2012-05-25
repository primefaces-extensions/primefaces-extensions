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

	private List<AbstractDynaFormElement> elements = new ArrayList<AbstractDynaFormElement>();

	public DynaFormRow(int row, boolean extended, DynaFormModel dynaFormModel) {
		this.row = row;
		this.extended = extended;
		this.dynaFormModel = dynaFormModel;
	}

	public DynaFormControl addControl(Object data, int colspan, int rowspan) {
		return addControl(data, DynaFormControl.DEFAULT_TYPE, colspan, rowspan);
	}

	public DynaFormControl addControl(Object data, String type, int colspan, int rowspan) {
		DynaFormControl dynaFormControl = new DynaFormControl(data, type, colspan, rowspan, row, elements.size() + 1, extended);

		elements.add(dynaFormControl);
		dynaFormModel.getControls().add(dynaFormControl);
		totalColspan = totalColspan + colspan;

		return dynaFormControl;
	}

	public DynaFormLabel addLabel(String value, int colspan, int rowspan) {
		return addLabel(value, true, colspan, rowspan);
	}

	public DynaFormLabel addLabel(String value, boolean escape, int colspan, int rowspan) {
		DynaFormLabel dynaFormLabel = new DynaFormLabel(value, escape, colspan, rowspan, row, elements.size() + 1, extended);

		elements.add(dynaFormLabel);
		dynaFormModel.getLabels().add(dynaFormLabel);
		totalColspan = totalColspan + colspan;

		return dynaFormLabel;
	}

	public List<AbstractDynaFormElement> getElements() {
		return elements;
	}

	public int getTotalColspan() {
		return totalColspan;
	}
}
