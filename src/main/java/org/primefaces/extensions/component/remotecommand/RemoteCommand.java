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

package org.primefaces.extensions.component.remotecommand;

import javax.faces.component.UICommand;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.primefaces.component.api.AjaxSource;

import java.util.List;
import java.util.ArrayList;

/**
 * Component class for the <code>RemoteCommand</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "primefaces.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "core/core.js")
})
public class RemoteCommand extends UICommand implements AjaxSource {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.RemoteCommandRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	protected enum PropertyKeys {
		name,
		update,
		process,
		onstart,
		oncomplete,
		onerror,
		onsuccess,
		global,
		async;

		private String toString;

		PropertyKeys(final String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}

	public RemoteCommand() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getName() {
		return (String) getStateHelper().eval(PropertyKeys.name, null);
	}

	public void setName(final String name) {
		setAttribute(PropertyKeys.name, name);
	}

	@Override
	public String getUpdate() {
		return (String) getStateHelper().eval(PropertyKeys.update, null);
	}

	public void setUpdate(final String update) {
		setAttribute(PropertyKeys.update, update);
	}

	@Override
	public String getProcess() {
		return (String) getStateHelper().eval(PropertyKeys.process, null);
	}

	public void setProcess(final String process) {
		setAttribute(PropertyKeys.process, process);
	}

	@Override
	public String getOnstart() {
		return (String) getStateHelper().eval(PropertyKeys.onstart, null);
	}

	public void setOnstart(final String onstart) {
		setAttribute(PropertyKeys.onstart, onstart);
	}

	@Override
	public String getOncomplete() {
		return (String) getStateHelper().eval(PropertyKeys.oncomplete, null);
	}

	public void setOncomplete(final String oncomplete) {
		setAttribute(PropertyKeys.oncomplete, oncomplete);
	}

	@Override
	public String getOnerror() {
		return (String) getStateHelper().eval(PropertyKeys.onerror, null);
	}

	public void setOnerror(final String onerror) {
		setAttribute(PropertyKeys.onerror, onerror);
	}

	@Override
	public String getOnsuccess() {
		return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
	}

	public void setOnsuccess(final String onsuccess) {
		setAttribute(PropertyKeys.onsuccess, onsuccess);
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean isGlobal() {
		return (Boolean) getStateHelper().eval(PropertyKeys.global, true);
	}

	@SuppressWarnings("boxing")
	public void setGlobal(final boolean global) {
		setAttribute(PropertyKeys.global, global);
	}

	@SuppressWarnings("boxing")
	@Override
	public boolean isAsync() {
		return (Boolean) getStateHelper().eval(PropertyKeys.async, false);
	}

	@SuppressWarnings("boxing")
	public void setAsync(final boolean async) {
		setAttribute(PropertyKeys.async, async);
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes = (List<String>) this.getAttributes().get(
				"javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put(
						"javax.faces.component.UIComponentBase.attributesThatAreSet",
						setAttributes);
			}
		}
		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}
}
