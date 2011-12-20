/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.behavior.blockui;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorBase;

/**
 * Client Behavior class for the <code>BlockUI</code> behavior.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ResourceDependencies({
                          @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
                          @ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.js")
                      })
public class BlockUIBehavior extends ClientBehaviorBase {

	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.BlockUIBehaviorRenderer";

	private boolean disabled = false;
	private String forValue;
	private String forSelector;
	private String content;

	@Override
	public String getRendererType() {
		return DEFAULT_RENDERER;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(final boolean disabled) {
		this.disabled = disabled;
	}

	public String getFor() {
		return forValue;
	}

	public void setFor(final String forValue) {
		this.forValue = forValue;
	}

	public String getForSelector() {
		return forSelector;
	}

	public void setForSelector(final String forSelector) {
		this.forSelector = forSelector;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}
}
