package org.primefaces.extensions.component.imagerotateandresize;

import java.util.ArrayList;
import java.util.List;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotationEvent;

@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="core/core.js"),
	@ResourceDependency(library="primefaces-extensions", name="imagerotateandresize/imagerotateandresize.js")
})
public class ImageRotateAndResize extends UIComponentBase implements Widget {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageRotateAndResize";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageRotateAndResizeRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";	
	
	
	protected enum PropertyKeys {
		widgetVar,
		forValue("for"),
		rotateListener,
		resizeListener,
		onResizeUpdate,
		onRotateUpdate,
		onResizeComplete,
		onRotationComplete;

		String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {}

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

	public String getOnResizeUpdate() {
		return (String) getStateHelper().eval(PropertyKeys.onResizeUpdate, null);
	}

	public void setOnResizeUpdate(java.lang.String _onResizeUpdate) {
		getStateHelper().put(PropertyKeys.onResizeUpdate, _onResizeUpdate);
		handleAttribute("onResizeUpdate", _onResizeUpdate);
	}

	public String getOnRotateUpdate() {
		return (String) getStateHelper().eval(PropertyKeys.onRotateUpdate, null);
	}

	public void setOnRotateUpdate(java.lang.String _onRotateUpdate) {
		getStateHelper().put(PropertyKeys.onRotateUpdate, _onRotateUpdate);
		handleAttribute("onRotateUpdate", _onRotateUpdate);
	}		

	public String getOnResizeComplete() {
		return (String) getStateHelper().eval(PropertyKeys.onResizeComplete, null);
	}

	public void setOnResizeComplete(java.lang.String _onResizeComplete) {
		getStateHelper().put(PropertyKeys.onResizeComplete, _onResizeComplete);
		handleAttribute("onResizeComplete", _onResizeComplete);
	}
	
	public String getOnRotationComplete() {
		return (String) getStateHelper().eval(PropertyKeys.onRotationComplete, null);
	}

	public void setOnRotationComplete(java.lang.String _onRotationComplete) {
		getStateHelper().put(PropertyKeys.onRotationComplete, _onRotationComplete);
		handleAttribute("onRotationComplete", _onRotationComplete);
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

	public MethodExpression getRotateListener() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.rotateListener, null);
	}

	public void setRotateListener(MethodExpression _rotateListener) {
		getStateHelper().put(PropertyKeys.rotateListener, _rotateListener);
		handleAttribute("rotateListener", _rotateListener);
	}
	
	public MethodExpression getResizeListener() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.resizeListener, null);
	}

	public void setResizeListener(MethodExpression _resizeListener) {
		getStateHelper().put(PropertyKeys.resizeListener, _resizeListener);
		handleAttribute("resizeListener", _resizeListener);
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

	public void broadcast(javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
		super.broadcast(event);

		FacesContext facesContext = FacesContext.getCurrentInstance();
		MethodExpression me = null;

		if (event instanceof RotationEvent) {
			me = getRotateListener();
		} else if (event instanceof ResizeEvent) {
			me = getResizeListener();
		}
		
		if (me != null) {
			me.invoke(facesContext.getELContext(), new Object[] {event});
		}
	}
	
    public boolean isRotateImageRequest(FacesContext context) {
		return context.getExternalContext().getRequestParameterMap().containsKey(getClientId(context) + "_ajaxRotate");
	}

    public boolean isResizeImageRequest(FacesContext context) {
		return context.getExternalContext().getRequestParameterMap().containsKey(getClientId(context) + "_ajaxResize");
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
}

