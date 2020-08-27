/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

package org.primefaces.extensions.showcase.model.dynaform;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * InventoryProperty
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class InventoryProperty implements Serializable {

	private static final long serialVersionUID = 20120521L;

	private String position;
	private Object value;
	private boolean required;

	public InventoryProperty(String position, boolean required) {
		this.position = position;
		this.required = required;
	}

	public InventoryProperty(String position, Object value, boolean required) {
		this.position = position;
		this.value = value;
		this.required = required;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Object getValue() {
		return value;
	}

	public Object getFormattedValue() {
		if (value instanceof Date) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");

			return simpleDateFormat.format(value);
		}

		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
