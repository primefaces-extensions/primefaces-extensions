package org.primefaces.extensions.component.imagerotateandresize;

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
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="imagerotateandresize/imagerotateandresize.js")
})
public class ImageRotateAndResize extends UIComponentBase implements Widget, ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageRotateAndResize";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageRotateAndResizeRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";	

    private static final Collection<String> EVENT_NAMES =
    	Collections.unmodifiableCollection(Arrays.asList("rotate", "resize"));

	protected enum PropertyKeys {
		widgetVar,
		forValue("for");

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

	public ImageRotateAndResize() {
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

			BehaviorEvent behaviorEvent = (BehaviorEvent) event;

			String clientId = getClientId(context);

			if (eventName.equals("resize")) {
				double width = Double.parseDouble(params.get(clientId + "_width"));
	            double height = Double.parseDouble(params.get(clientId + "_height"));

	            ResizeEvent resizeEvent = new ResizeEvent(
	            		this,
	            		behaviorEvent.getBehavior(),
	            		width,
	            		height);
	            super.queueEvent(resizeEvent);
			} else if (eventName.equals("rotate")) {
				int degree = Integer.parseInt(params.get(clientId + "_degree"));

				RotateEvent rotateEvent = new RotateEvent(
						this,
						behaviorEvent.getBehavior(),
						degree);

	            super.queueEvent(rotateEvent);
			}
		} else {
			super.queueEvent(event);
		}
	}

	private boolean isRequestSource(FacesContext context) {
		return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
	}
}
