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

package org.primefaces.extensions.model.dynaform;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.primefaces.extensions.model.common.KeyData;

/**
 * Class representing a control inside of <code>DynaForm</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormControl extends AbstractDynaFormElement implements KeyData {

	public static final String DEFAULT_TYPE = "default";

	private String key;
	private Object data;
	private String type;

	public DynaFormControl(Object data, String type, int colspan, int rowspan, int row, int column, boolean extended) {
		super(colspan, rowspan, row, column, extended);

		this.data = data;
		if (type != null) {
			this.type = type;
		} else {
			this.type = DEFAULT_TYPE;
		}

		generateKey();
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

	void generateKey() {
		setKey(RandomStringUtils.randomAlphanumeric(8));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("key", key).append("data", data)
		                                                                  .append("type", type).append("colspan", getColspan())
		                                                                  .append("rowspan", getRowspan()).append("row", getRow())
		                                                                  .append("column", getColumn())
		                                                                  .append("extended", isExtended()).toString();
	}
}
