/*
 * Copyright 2011 Thomas Andraschko.
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
package org.primefaces.extensions.component.dnd;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="primefaces", name="core/core.js"),
	@ResourceDependency(library="primefaces", name="dnd/dragdrop.js"),
	@ResourceDependency(library="primefaces-extensions", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="dnd/dragdrop.js")
})
public class Droppable extends org.primefaces.component.dnd.Droppable {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Droppable";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DroppableRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";	
	
	protected enum PropertyKeys {
		oncomplete;

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {}

		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
	}
	
	public Droppable() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}	
	
	public java.lang.String getOncomplete() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
	}

	public void setOncomplete(java.lang.String _oncomplete) {
		getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
		handleAttribute("oncomplete", _oncomplete);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void handleAttribute(String name, Object value) {
		List<String> setAttributes = (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if(setAttributes == null) {
			String cname = this.getClass().getName();
			if(cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}
		if(setAttributes != null) {
			if(value == null) {
				ValueExpression ve = getValueExpression(name);
				if(ve == null) {
					setAttributes.remove(name);
				} else if(!setAttributes.contains(name)) {
					setAttributes.add(name);
				}
			}
		}
	}
}
