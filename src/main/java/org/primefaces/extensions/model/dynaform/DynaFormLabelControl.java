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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Class representing a label control inside of <code>DynaForm</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormLabelControl extends DynaFormControl implements Serializable {

	private static final long serialVersionUID = 20120510L;

	protected String refKey;

	public DynaFormLabelControl(Object data, String type, int colspan, int rowspan, int row, int column, boolean extended,
	                            String refKey) {
		super(data, type, colspan, rowspan, row, column, extended);
		this.refKey = refKey;
	}

	public String getRefKey() {
		return refKey;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("data", getData()).append("key", getKey())
		                                                                  .append("refKey", refKey).append("type", type)
		                                                                  .append("colspan", colspan).append("rowspan", rowspan)
		                                                                  .toString();
	}
}
