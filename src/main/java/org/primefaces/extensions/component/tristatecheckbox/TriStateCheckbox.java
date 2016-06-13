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

package org.primefaces.extensions.component.tristatecheckbox;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.html.HtmlInputText;
import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * TriStateCheckbox
 *
 * @author  Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
@ResourceDependencies({
        @ResourceDependency(library = "primefaces", name = "components.css"),
		@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
        @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
		@ResourceDependency(library = "primefaces", name = "core.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class TriStateCheckbox extends HtmlInputText implements Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TriStateCheckbox";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.";

	public static final String UI_ICON = "ui-icon ";

	/**
	 * PropertyKeys
	 *
	 * @author  ova / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		widgetVar,
		itemLabel,
		stateOneIcon,
		stateTwoIcon,
		stateThreeIcon,
                stateOneTitle,
                stateTwoTitle,
                stateThreeTitle;

		String toString;

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

	public TriStateCheckbox() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

        @Override
        public String getDefaultEventName() {
            return "valueChange";   
        }
        
	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(final String widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}

	public String getItemLabel() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.itemLabel, null);
	}

	public void setItemLabel(final String itemLabel) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.itemLabel, itemLabel);
	}

	public String getStateOneIcon() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateOneIcon, null);
	}

	public void setStateOneIcon(final String stateOneIcon) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.stateOneIcon, stateOneIcon);
	}

	public String getStateTwoIcon() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateTwoIcon, null);
	}

	public void setStateTwoIcon(final String stateTwoIcon) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.stateTwoIcon, stateTwoIcon);
	}

	public String getStateThreeIcon() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateThreeIcon, null);
	}

	public void setStateThreeIcon(final String stateThreeIcon) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.stateThreeIcon, stateThreeIcon);
	}
        
        public String getStateOneTitle() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateOneTitle, "");
	}

	public void setStateOneTitle(final String stateOneTitle) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.stateOneTitle, stateOneTitle);
	}
        
        public String getStateTwoTitle() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateTwoTitle, "");
	}

	public void setStateTwoTitle(final String stateTwoTitle) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.stateTwoTitle, stateTwoTitle);
	}
        
        public String getStateThreeTitle() {
		return (String) getStateHelper().eval(TriStateCheckbox.PropertyKeys.stateThreeTitle, "");
	}

	public void setStateThreeTitle(final String stateThreeTitle) {
		getStateHelper().put(TriStateCheckbox.PropertyKeys.stateThreeTitle, stateThreeTitle);
	}

    @Override
	public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}
}
