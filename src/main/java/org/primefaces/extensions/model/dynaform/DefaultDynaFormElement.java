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

/**
 * Default implementation of {@link DynaFormElement}.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DefaultDynaFormElement extends DynaFormElement implements Serializable {

	private static final long serialVersionUID = 20120417L;

	public DefaultDynaFormElement() {
		this.type = DEFAULT_TYPE;
	}

	public DefaultDynaFormElement(final Object data) {
		setData(data);
		this.type = DEFAULT_TYPE;
	}

	public DefaultDynaFormElement(final Object data, final String type) {
		setData(data);
		this.type = type;
	}

	public DefaultDynaFormElement(final Object data, final String type, final String label, final boolean required,
	                              final boolean extended, final int colspan, final int row, final int column) {
		setData(data);
		this.type = type;
		this.label = label;
		this.required = required;
		this.extended = extended;
		this.colspan = colspan;
		this.row = row;
		this.column = column;
	}

	@Override
	public int hashCode() {
		if (getData() != null) {
			return new HashCodeBuilder(17, 37).append(getData()).toHashCode();
		} else {
			return new HashCodeBuilder(17, 37).append(type).append(label).append(required).append(extended).append(colspan)
			                                  .append(row).append(column).toHashCode();
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

		DefaultDynaFormElement ddfe = (DefaultDynaFormElement) obj;

		if (getData() != null) {
			return new EqualsBuilder().append(getData(), ddfe.getData()).isEquals();
		} else {
			return new EqualsBuilder().append(type, ddfe.type).append(label, ddfe.label).append(required, ddfe.required)
			                          .append(extended, ddfe.extended).append(colspan, ddfe.colspan).append(row, ddfe.row)
			                          .append(column, ddfe.column).isEquals();
		}
	}

	@Override
	public String toString() {
		if (getData() != null) {
			return getData().toString();
		} else {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("type", type).append("label", label)
			                                                                  .append("required", required)
			                                                                  .append("extended", extended)
			                                                                  .append("colspan", colspan).append("row", row)
			                                                                  .append("column", column).toString();
		}
	}
}
