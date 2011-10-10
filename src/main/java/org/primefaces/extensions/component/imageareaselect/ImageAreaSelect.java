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

package org.primefaces.extensions.component.imageareaselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.util.Constants;

/**
 * Component class for the <code>ImageAreaSelect</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@ResourceDependencies({
	@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
	@ResourceDependency(library = "primefaces", name = "core/core.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "core/core.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "imageareaselect/imageareaselect.js"),
	@ResourceDependency(library = "primefaces-extensions", name = "imageareaselect/imageareaselect.css")
})
public class ImageAreaSelect extends UIComponentBase implements Widget, ClientBehaviorHolder {

	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageAreaSelectRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

	public static final String EVENT_SELECT = "select";

	private static final Collection<String> EVENT_NAMES =
			Collections.unmodifiableCollection(Arrays.asList(EVENT_SELECT));

	protected enum PropertyKeys {
		widgetVar,
		forValue("for"),
		aspectRatio,
		autoHide,
		fadeSpeed,
		handles,
		hide,
		imageHeight,
		imageWidth,
		movable,
		persistent,
		resizable,
		show,
		zIndex,
		maxHeight,
		maxWidth,
		minHeight,
		minWidth,
		imageAreaSelectParent,
		keyboardSupport;

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

	public ImageAreaSelect() {
		setRendererType(DEFAULT_RENDERER);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

    @Override
    public String getDefaultEventName() {
        return EVENT_SELECT;
    }

	public String getAspectRatio() {
		return (String) getStateHelper().eval(PropertyKeys.aspectRatio, null);
	}

	public void setAspectRatio(final String aspectRatio) {
		setAttribute(PropertyKeys.aspectRatio, aspectRatio);
	}

	public String getImageAreaSelectParent() {
		return (String) getStateHelper().eval(PropertyKeys.imageAreaSelectParent, null);
	}

	public void setImageAreaSelectParent(final String parent) {
		setAttribute(PropertyKeys.imageAreaSelectParent, parent);
	}

	public Boolean isAutoHide() {
		return (Boolean) getStateHelper().eval(PropertyKeys.autoHide, null);
	}

	public void setAutoHide(final Boolean autoHide) {
		setAttribute(PropertyKeys.autoHide, autoHide);
	}

	public Integer getFadeSpeed() {
		return (Integer) getStateHelper().eval(PropertyKeys.fadeSpeed, null);
	}

	public void setFadeSpeed(final Integer fadeSpeed) {
		setAttribute(PropertyKeys.fadeSpeed, fadeSpeed);
	}

	public Boolean isHandles() {
		return (Boolean) getStateHelper().eval(PropertyKeys.handles, null);
	}

	public void setHandles(final Boolean handles) {
		setAttribute(PropertyKeys.handles, handles);
	}

	public Boolean isHide() {
		return (Boolean) getStateHelper().eval(PropertyKeys.hide, null);
	}

	public void setHide(final Boolean hide) {
		setAttribute(PropertyKeys.hide, hide);
	}

	public Integer getImageHeight() {
		return (Integer) getStateHelper().eval(PropertyKeys.imageHeight, null);
	}

	public void setImageHeight(final Integer imageHeight) {
		setAttribute(PropertyKeys.imageHeight, imageHeight);
	}

	public Integer getImageWidth() {
		return (Integer) getStateHelper().eval(PropertyKeys.imageWidth, null);
	}

	public void setImageWidth(final Integer imageWidth) {
		setAttribute(PropertyKeys.imageWidth, imageWidth);
	}

	public Boolean isMovable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.movable, null);
	}

	public void setMovable(final Boolean movable) {
		setAttribute(PropertyKeys.movable, movable);
	}

	public Boolean isKeyboardSupport() {
		return (Boolean) getStateHelper().eval(PropertyKeys.keyboardSupport, null);
	}

	public void setKeyboardSupport(final Boolean keyboardSupport) {
		setAttribute(PropertyKeys.keyboardSupport, keyboardSupport);
	}

	public Boolean isPersistent() {
		return (Boolean) getStateHelper().eval(PropertyKeys.persistent, null);
	}

	public void setPersistent(final Boolean persistent) {
		setAttribute(PropertyKeys.persistent, persistent);
	}

	public Boolean isResizable() {
		return (Boolean) getStateHelper().eval(PropertyKeys.resizable, null);
	}

	public void setResizable(final Boolean resizable) {
		setAttribute(PropertyKeys.resizable, resizable);
	}

	public Boolean isShow() {
		return (Boolean) getStateHelper().eval(PropertyKeys.show, null);
	}

	public void setShow(final Boolean show) {
		setAttribute(PropertyKeys.show, show);
	}

	public Integer getZIndex() {
		return (Integer) getStateHelper().eval(PropertyKeys.zIndex, null);
	}

	public void setZIndex(final Integer zIndex) {
		setAttribute(PropertyKeys.zIndex, zIndex);
	}

	public Integer getMaxHeight() {
		return (Integer) getStateHelper().eval(PropertyKeys.maxHeight, null);
	}

	public void setMaxHeight(final Integer maxHeight) {
		setAttribute(PropertyKeys.maxHeight, maxHeight);
	}

	public Integer getMaxWidth() {
		return (Integer) getStateHelper().eval(PropertyKeys.maxWidth, null);
	}

	public void setMaxWidth(final Integer maxWidth) {
		setAttribute(PropertyKeys.maxWidth, maxWidth);
	}

	public Integer getMinHeight() {
		return (Integer) getStateHelper().eval(PropertyKeys.minHeight, null);
	}

	public void setMinHeight(final Integer minHeight) {
		setAttribute(PropertyKeys.minHeight, minHeight);
	}

	public Integer getMinWidth() {
		return (Integer) getStateHelper().eval(PropertyKeys.minWidth, null);
	}

	public void setMinWidth(final Integer minWidth) {
		setAttribute(PropertyKeys.minWidth, minWidth);
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(final String widgetVar) {
		setAttribute(PropertyKeys.widgetVar, widgetVar);
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(final String forValue) {
		setAttribute(PropertyKeys.forValue, forValue);
	}

	@Override
	public String resolveWidgetVar() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

		if (userWidgetVar != null) {
			return userWidgetVar;
		}

		return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
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

	@Override
	public void queueEvent(final FacesEvent event) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String clientId = getClientId(context);

		if (isRequestSource(clientId, params)) {
			final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

			if (eventName.equals(EVENT_SELECT)) {
				final BehaviorEvent behaviorEvent = (BehaviorEvent) event;

				final int x1 = Integer.parseInt(params.get(clientId + "_x1"));
				final int x2 = Integer.parseInt(params.get(clientId + "_x2"));
				final int y1 = Integer.parseInt(params.get(clientId + "_y1"));
				final int y2 = Integer.parseInt(params.get(clientId + "_y2"));
				final int height = Integer.parseInt(params.get(clientId + "_height"));
				final int width = Integer.parseInt(params.get(clientId + "_width"));
				final int imgHeight = Integer.parseInt(params.get(clientId + "_imgHeight"));
				final int imgWidth = Integer.parseInt(params.get(clientId + "_imgWidth"));
				final String imgSrc = params.get(clientId + "_imgSrc");

				final ImageAreaSelectEvent selectEvent =
						new ImageAreaSelectEvent(this,
								behaviorEvent.getBehavior(),
								height,
								width,
								x1,
								x2,
								y1,
								y2,
								imgHeight,
								imgWidth,
								imgSrc);

				super.queueEvent(selectEvent);
			}
		} else {
			super.queueEvent(event);
		}
	}

	private boolean isRequestSource(final String clientId, final Map<String, String> params) {
		return clientId.equals(params.get(Constants.PARTIAL_SOURCE_PARAM));
	}
}
