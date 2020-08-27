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
 * BookProperty
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class BookProperty implements Serializable {

	private static final long serialVersionUID = 20120521L;

	private String name;
	private Object value;
	private boolean required;

	public BookProperty(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public BookProperty(String name, Object value, boolean required) {
		this.name = name;
		this.value = value;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
