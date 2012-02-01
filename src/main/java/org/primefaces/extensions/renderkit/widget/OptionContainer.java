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

package org.primefaces.extensions.renderkit.widget;

/**
 * Container for all required informations to render an widget option.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class OptionContainer {

	private String name;
	private boolean escape;
	private boolean useDoubleQuotes;

	public OptionContainer() {

	}

	public OptionContainer(final String name) {
		this.name = name;
	}

	public OptionContainer(final String name, final boolean escape) {
		this.name = name;
		this.escape = escape;
	}

	public OptionContainer(final String name, final boolean escape, final boolean useDoubleQuotes) {
		this.name = name;
		this.escape = escape;
		this.useDoubleQuotes = useDoubleQuotes;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isEscape() {
		return escape;
	}

	public void setEscape(final boolean escape) {
		this.escape = escape;
	}

	public boolean isUseDoubleQuotes() {
		return useDoubleQuotes;
	}

	public void setUseDoubleQuotes(final boolean useDoubleQuotes) {
		this.useDoubleQuotes = useDoubleQuotes;
	}
}
