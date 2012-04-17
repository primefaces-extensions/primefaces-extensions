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

/**
 * Default implementation of {@link DynaFormElement}.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DefaultDynaFormElement implements DynaFormElement, Serializable {

	private static final long serialVersionUID = 20120417L;

	public static final String DEFAULT_TYPE = "default";

	private Object data;

	private String type;

	private String label;

	private boolean required;

	private int colspan = 1;

	private int row = 1;

	private int column = 1;

	public DefaultDynaFormElement() {
		this.type = DEFAULT_TYPE;
	}

	public DefaultDynaFormElement(final Object data) {
		this.data = data;
		this.type = DEFAULT_TYPE;
	}

	public DefaultDynaFormElement(final Object data, final String type) {
		this.data = data;
		this.type = type;
	}

	public DefaultDynaFormElement(final Object data, final String type, final String label, final boolean required,
	                              final int colspan, final int row, final int column) {
		this.data = data;
		this.type = type;
		this.label = label;
		this.required = required;
		this.colspan = colspan;
		this.row = row;
		this.column = column;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
