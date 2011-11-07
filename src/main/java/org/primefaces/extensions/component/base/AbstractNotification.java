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

package org.primefaces.extensions.component.base;

import java.util.ArrayList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponentBase;

import org.primefaces.extensions.application.TargetableFacesMessage;

/**
 * Base class for all notification components.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public abstract class AbstractNotification extends UIComponentBase {

	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		escape,
		showSummary,
		showDetail;

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

	@SuppressWarnings("boxing")
	public boolean isEscape() {
		return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
	}

	@SuppressWarnings("boxing")
	public void setEscape(final boolean escape) {
		setAttribute(PropertyKeys.escape, escape);
	}

	@SuppressWarnings("boxing")
	public boolean isShowSummary() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showSummary, true);
	}

	@SuppressWarnings("boxing")
	public void setShowSummary(final boolean showSummary) {
		setAttribute(PropertyKeys.showSummary, showSummary);
	}

	@SuppressWarnings("boxing")
	public boolean isShowDetail() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showDetail, false);
	}

	@SuppressWarnings("boxing")
	public void setShowDetail(final boolean showDetail) {
		setAttribute(PropertyKeys.showDetail, showDetail);
	}

	@SuppressWarnings("unchecked")
	public void setAttribute(final PropertyKeys property, final Object value) {
		getStateHelper().put(property, value);

		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
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

	/**
	 * Checks if the component is target of the given {@link FacesMessage}.
	 *
	 * @param message The {@link FacesMessage}.
	 * @param targets The targets which are supported by the component.
	 * @return If the component is target of the given {@link FacesMessage}.
	 */
	public boolean isTarget(final FacesMessage message, final TargetableFacesMessage.Target... targets) {
		boolean isTarget = false;

		if (message instanceof TargetableFacesMessage) {
			final TargetableFacesMessage targetableMessage = (TargetableFacesMessage) message;

			for (TargetableFacesMessage.Target target : targets) {
				if (!isTarget) {
					isTarget = targetableMessage.getTarget() == target;
				}
			}
		} else {
			isTarget = true;
		}

		return isTarget;
	}
}
