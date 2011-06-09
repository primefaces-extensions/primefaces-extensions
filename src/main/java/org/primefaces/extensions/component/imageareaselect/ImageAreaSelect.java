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

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="imageareaselect/imageareaselect.js"),
	@ResourceDependency(library="primefaces-extensions", name="imageareaselect/imageareaselect.css")
})
public class ImageAreaSelect extends UIComponentBase implements Widget, ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageAreaSelect";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageAreaSelectRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

    private static final Collection<String> EVENT_NAMES =
    	Collections.unmodifiableCollection(Arrays.asList("select"));

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

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {}

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

	public java.lang.String getAspectRatio() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.aspectRatio, null);
	}

	public void setAspectRatio(java.lang.String _aspectRatio) {
		getStateHelper().put(PropertyKeys.aspectRatio, _aspectRatio);
		handleAttribute("aspectRatio", _aspectRatio);
	}

	public java.lang.String getImageAreaSelectParent() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.imageAreaSelectParent, null);
	}

	public void setImageAreaSelectParent(java.lang.String _parent) {
		getStateHelper().put(PropertyKeys.imageAreaSelectParent, _parent);
		handleAttribute("imageAreaSelectParent", _parent);
	}

	public java.lang.Boolean isAutoHide() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoHide, null);
	}

	public void setAutoHide(java.lang.Boolean _autoHide) {
		getStateHelper().put(PropertyKeys.autoHide, _autoHide);
		handleAttribute("autoHide", _autoHide);
	}

	public java.lang.Integer getFadeSpeed() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.fadeSpeed, null);
	}

	public void setFadeSpeed(java.lang.Integer _fadeSpeed) {
		getStateHelper().put(PropertyKeys.fadeSpeed, _fadeSpeed);
		handleAttribute("fadeSpeed", _fadeSpeed);
	}

	public java.lang.Boolean isHandles() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.handles, null);
	}

	public void setHandles(java.lang.Boolean _handles) {
		getStateHelper().put(PropertyKeys.handles, _handles);
		handleAttribute("handles", _handles);
	}

	public java.lang.Boolean isHide() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.hide, null);
	}

	public void setHide(java.lang.Boolean _hide) {
		getStateHelper().put(PropertyKeys.hide, _hide);
		handleAttribute("hide", _hide);
	}

	public java.lang.Integer getImageHeight() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.imageHeight, null);
	}

	public void setImageHeight(java.lang.Integer _imageHeight) {
		getStateHelper().put(PropertyKeys.imageHeight, _imageHeight);
		handleAttribute("imageHeight", _imageHeight);
	}

	public java.lang.Integer getImageWidth() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.imageWidth, null);
	}

	public void setImageWidth(java.lang.Integer _imageWidth) {
		getStateHelper().put(PropertyKeys.imageWidth, _imageWidth);
		handleAttribute("imageWidth", _imageWidth);
	}

	public java.lang.Boolean isMovable() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.movable, null);
	}

	public void setMovable(java.lang.Boolean _movable) {
		getStateHelper().put(PropertyKeys.movable, _movable);
		handleAttribute("movable", _movable);
	}

	public java.lang.Boolean isKeyboardSupport() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.keyboardSupport, null);
	}

	public void setKeyboardSupport(java.lang.Boolean _keyboardSupport) {
		getStateHelper().put(PropertyKeys.keyboardSupport, _keyboardSupport);
		handleAttribute("keyboardSupport", _keyboardSupport);
	}

	public java.lang.Boolean isPersistent() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.persistent, null);
	}

	public void setPersistent(java.lang.Boolean _persistent) {
		getStateHelper().put(PropertyKeys.persistent, _persistent);
		handleAttribute("persistent", _persistent);
	}

	public java.lang.Boolean isResizable() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resizable, null);
	}

	public void setResizable(java.lang.Boolean _resizable) {
		getStateHelper().put(PropertyKeys.resizable, _resizable);
		handleAttribute("resizable", _resizable);
	}

	public java.lang.Boolean isShow() {
		return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.show, null);
	}

	public void setShow(java.lang.Boolean _show) {
		getStateHelper().put(PropertyKeys.show, _show);
		handleAttribute("show", _show);
	}

	public java.lang.Integer getZIndex() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.zIndex, null);
	}

	public void setZIndex(java.lang.Integer _zIndex) {
		getStateHelper().put(PropertyKeys.zIndex, _zIndex);
		handleAttribute("zIndex", _zIndex);
	}

	public java.lang.Integer getMaxHeight() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxHeight, null);
	}

	public void setMaxHeight(java.lang.Integer _maxHeight) {
		getStateHelper().put(PropertyKeys.maxHeight, _maxHeight);
		handleAttribute("maxHeight", _maxHeight);
	}

	public java.lang.Integer getMaxWidth() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxWidth, null);
	}

	public void setMaxWidth(java.lang.Integer _maxWidth) {
		getStateHelper().put(PropertyKeys.maxWidth, _maxWidth);
		handleAttribute("maxWidth", _maxWidth);
	}

	public java.lang.Integer getMinHeight() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minHeight, null);
	}

	public void setMinHeight(java.lang.Integer _minHeight) {
		getStateHelper().put(PropertyKeys.minHeight, _minHeight);
		handleAttribute("minHeight", _minHeight);
	}

	public java.lang.Integer getMinWidth() {
		return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minWidth, null);
	}

	public void setMinWidth(java.lang.Integer _minWidth) {
		getStateHelper().put(PropertyKeys.minWidth, _minWidth);
		handleAttribute("minWidth", _minWidth);
	}

	public java.lang.String getWidgetVar() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(java.lang.String _widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
		handleAttribute("widgetVar", _widgetVar);
	}

	public java.lang.String getFor() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
	}

	public void setFor(java.lang.String _for) {
		getStateHelper().put(PropertyKeys.forValue, _for);
		handleAttribute("forValue", _for);
	}

	public String resolveWidgetVar() {
		FacesContext context = FacesContext.getCurrentInstance();
		String userWidgetVar = (String) getAttributes().get("widgetVar");

		if (userWidgetVar != null) {
			return userWidgetVar;
		} else {
			return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
		}
	}

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

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		if (isRequestSource(context)) {
			Map<String,String> params = context.getExternalContext().getRequestParameterMap();
			String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

			if (eventName.equals("select")) {
				BehaviorEvent behaviorEvent = (BehaviorEvent) event;

				String clientId = getClientId(context);

				int x1 = Integer.parseInt(params.get(clientId + "_x1"));
		        int x2 = Integer.parseInt(params.get(clientId + "_x2"));
		        int y1 = Integer.parseInt(params.get(clientId + "_y1"));
		        int y2 = Integer.parseInt(params.get(clientId + "_y2"));
		        int height = Integer.parseInt(params.get(clientId + "_height"));
		        int width = Integer.parseInt(params.get(clientId + "_width"));
		        int imgHeight = Integer.parseInt(params.get(clientId + "_imgHeight"));
		        int imgWidth = Integer.parseInt(params.get(clientId + "_imgWidth"));
		        String imgSrc = params.get(clientId + "_imgSrc");

		        ImageAreaSelectEvent selectEvent =
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

	private boolean isRequestSource(FacesContext context) {
		return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
	}
}
