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

import org.primefaces.extensions.application.TargetableFacesMessage;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all notification components.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public abstract class AbstractNotification extends UIMessage {

	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Thomas Andraschko / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		escape,
		level,
		minLevel,
		maxLevel;

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

	public String getLevel() {
		return (String) getStateHelper().eval(PropertyKeys.level, null);
	}

	public void setLevel(final String level) {
		setAttribute(PropertyKeys.level, level);
	}

	public String getMinLevel() {
		return (String) getStateHelper().eval(PropertyKeys.minLevel, null);
	}

	public void setMinLevel(final String minLevel) {
		setAttribute(PropertyKeys.minLevel, minLevel);
	}

	public String getMaxLevel() {
		return (String) getStateHelper().eval(PropertyKeys.maxLevel, null);
	}

	public void setMaxLevel(final String maxLevel) {
		setAttribute(PropertyKeys.maxLevel, maxLevel);
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
	 * Checks if the given {@link FacesMessage} should be rendered.
	 *
	 * @param message The {@link FacesMessage}.
	 * @param targets The targets which are supported by the component.
	 * @return If the message should be rendered or not.
	 */
	public boolean shouldRender(final FacesMessage message, final TargetableFacesMessage.Target... targets) {
		return checkSeverity(message) && checkTarget(message, targets);
	}

	/**
	 * Checks if the component is target of the given {@link FacesMessage}.
	 *
	 * @param message The {@link FacesMessage}.
	 * @param targets The targets which are supported by the component.
	 * @return If the component is target of the given {@link FacesMessage}.
	 */
	protected boolean checkTarget(final FacesMessage message, final TargetableFacesMessage.Target... targets) {
        if (!(message instanceof TargetableFacesMessage)) {
            return true;
        }

        final TargetableFacesMessage targetableMessage = (TargetableFacesMessage) message;

        for (TargetableFacesMessage.Target target : targets) {
            if (targetableMessage.getTarget() == target) {
                return true;
            }
        }

		return false;
	}

	/**
	 * Check the {@link Severity} of the message and if it should be rendered.
	 *
	 * @param message The {@link FacesMessage}.
	 * @return If the given {@link FacesMessage} should be rendered.
	 */
	protected boolean checkSeverity(final FacesMessage message) {
		// level has priority over min/max level
		if (getLevel() != null) {
			return getLevel().equals(message.getSeverity().toString());
		}

		final FacesMessage.Severity minLevelSeverity = toSeverity(getMinLevel());
		final FacesMessage.Severity maxLevelSeverity = toSeverity(getMaxLevel());

		int minOrdinal;
		int maxOrdinal;

		if (minLevelSeverity == null) {
			minOrdinal = Integer.MIN_VALUE;
		} else {
			minOrdinal = minLevelSeverity.getOrdinal();
		}

		if (maxLevelSeverity == null) {
			maxOrdinal = Integer.MAX_VALUE;
		} else {
			maxOrdinal = maxLevelSeverity.getOrdinal();
		}

		return message.getSeverity().getOrdinal() >= minOrdinal && message.getSeverity().getOrdinal() <= maxOrdinal;
	}

	private FacesMessage.Severity toSeverity(final String severity) {
		if (severity == null) {
			return null;
		}

		if (FacesMessage.SEVERITY_ERROR.toString().equals(severity)) {
			return FacesMessage.SEVERITY_ERROR;
		}
		if (FacesMessage.SEVERITY_FATAL.toString().equals(severity)) {
			return FacesMessage.SEVERITY_FATAL;
		}
		if (FacesMessage.SEVERITY_INFO.toString().equals(severity)) {
			return FacesMessage.SEVERITY_INFO;
		}
		if (FacesMessage.SEVERITY_WARN.toString().equals(severity)) {
			return FacesMessage.SEVERITY_WARN;
		}

		return null;
	}
}
