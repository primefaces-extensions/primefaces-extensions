/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

package org.primefaces.extensions.model.fluidgrid;

import java.io.Serializable;

import org.apache.commons.lang3.RandomStringUtils;

import org.primefaces.extensions.model.common.KeyData;

/**
 * Class representing an item inside of <code>FluidGrid</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   1.1.0
 */
public class FluidGridItem implements KeyData, Serializable {

	public static final String DEFAULT_TYPE = "default";

	private String key;
	private Object data;
	private String type;

	public FluidGridItem() {
		// generate key
		setKey(RandomStringUtils.randomAlphanumeric(8));
	}

	public FluidGridItem(Object data) {
		this.data = data;
		this.type = DEFAULT_TYPE;

		// generate key
		setKey(RandomStringUtils.randomAlphanumeric(8));
	}

	public FluidGridItem(Object data, String type) {
		this.data = data;
		if (type != null) {
			this.type = type;
		} else {
			this.type = DEFAULT_TYPE;
		}

		// generate key
		setKey(RandomStringUtils.randomAlphanumeric(8));
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
}
