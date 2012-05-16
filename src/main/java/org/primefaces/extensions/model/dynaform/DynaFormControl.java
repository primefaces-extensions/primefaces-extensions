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

	private static final long serialVersionUID = 20120514L;

	public static final String DEFAULT_TYPE = "default";

	private static final String KEY_SEPARATOR = "_";

	private static final String KEY_SUFFIX_REGULAR = "_reg";

	private static final String KEY_SUFFIX_EXTENDED = "_ext";

	private String key;

	private Object data;

	private String refKey;

	private String type;

	private int colspan = 1;

	private int rowspan = 1;

	private int row;

	private int column;

	private boolean extended;

	private boolean applyLabelStyle;

	public DynaFormControl(Object data, String type, int colspan, int rowspan, int row, int column, boolean extended,
	                       boolean applyLabelStyle) {
		this.data = data;
		if (type != null) {
			this.type = type;
		} else {
			this.type = DEFAULT_TYPE;
		}

		this.colspan = colspan;
		this.rowspan = rowspan;
		this.row = row;
		this.column = column;
		this.extended = extended;
		this.applyLabelStyle = applyLabelStyle;

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

	public String getRefKey() {
		return refKey;
	}

	public void setRefKey(String refKey) {
		this.refKey = refKey;
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

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isExtended() {
		return extended;
	}

	public boolean isApplyLabelStyle() {
		return applyLabelStyle;
	}

	private void generateKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(row).append(KEY_SEPARATOR).append(column);
		if (extended) {
			sb.append(KEY_SUFFIX_EXTENDED);
		} else {
			sb.append(KEY_SUFFIX_REGULAR);
		}

		setKey(sb.toString());
	}

	@Override
	public int hashCode() {
		if (key != null) {
			return new HashCodeBuilder(17, 37).append(key).toHashCode();
		} else {
			return new HashCodeBuilder(17, 37).append(data).append(type).append(refKey).append(colspan).append(rowspan)
			                                  .append(row).append(column).append(extended).toHashCode();
		}
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

		if (key != null) {
			return new EqualsBuilder().append(key, ddfe.key).isEquals();
		} else {
			return new EqualsBuilder().append(data, ddfe.data).append(type, ddfe.type).append(refKey, ddfe.refKey)
			                          .append(colspan, ddfe.colspan).append(rowspan, ddfe.rowspan).append(row, ddfe.row)
			                          .append(column, ddfe.column).append(extended, ddfe.extended).isEquals();
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("key", key).append("data", data)
		                                                                  .append("type", type).append("refKey", refKey)
		                                                                  .append("colspan", colspan).append("rowspan", rowspan)
		                                                                  .append("row", row).append("column", column)
		                                                                  .append("extended", extended).toString();
	}
}
