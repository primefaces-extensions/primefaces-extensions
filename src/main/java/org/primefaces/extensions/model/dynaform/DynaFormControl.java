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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.primefaces.extensions.model.common.KeyData;

/**
 * Class representing a control (typically input element or label) inside of <code>DynaForm</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormControl implements KeyData, Serializable {

	private static final long serialVersionUID = 20120417L;

	protected static final String KEY_SEPARATOR = "_";

	protected static final String KEY_SUFFIX_REGULAR = "_reg";

	protected static final String KEY_SUFFIX_EXTENDED = "_ext";

	public static final String DEFAULT_TYPE = "default";

	protected Object data;

	protected String key;

	protected String refKey;

	protected String type;

	protected int colspan = 1;

	protected int rowspan = 1;

	public DynaFormControl(Object data, String type, int colspan, int rowspan, int row, int column, String refKey,
	                       boolean extended) {
		this.data = data;
		if (type != null) {
			this.type = type;
		} else {
			this.type = DEFAULT_TYPE;
		}

		this.colspan = colspan;
		this.rowspan = rowspan;
		this.refKey = refKey;

		setKey(generateKey(row, column, extended));
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRefKey() {
		return refKey;
	}

	public String getType() {
		return type;
	}

	public int getColspan() {
		return colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	protected String generateKey(int row, int column, boolean extended) {
		StringBuilder sb = new StringBuilder();
		sb.append(row).append(KEY_SEPARATOR).append(column);
		if (extended) {
			sb.append(KEY_SUFFIX_EXTENDED);
		} else {
			sb.append(KEY_SUFFIX_REGULAR);
		}

		return sb.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getKey()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (obj.getClass() != getClass()) {
			return false;
		}

		DynaFormControl ddfe = (DynaFormControl) obj;

		return new EqualsBuilder().append(getKey(), ddfe.getKey()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("data", getData()).append("key", getKey())
		                                                                  .append("refKey", refKey).append("type", type)
		                                                                  .append("colspan", colspan).append("rowspan", rowspan)
		                                                                  .toString();
	}
}
