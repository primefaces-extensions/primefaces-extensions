package org.primefaces.extensions.component.knob;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;

/**
 * <code>Knob</code> component
 *
 * @author f.strazzullo
 * @since 3.0.0
 *
 */
@ResourceDependencies({
        @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
        @ResourceDependency(library = "primefaces", name = "primefaces.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
        @ResourceDependency(library = "primefaces-extensions", name = "knob/knob.js")
})
public class Knob extends UIInput implements Widget, ClientBehaviorHolder {
	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Knob";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("change"));

    protected static enum PropertyKeys {
        foregroundColor,
        backgroundColor,
        showLabel,
        labelTemplate,
        onchange,
        width,
        step,
        min,
        max,
        widgetVar,
        disabled,
        cursor,
        thickness;
    }

    public Knob(){
        setRendererType(KnobRenderer.RENDERER_TYPE);
    }

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public String getDefaultEventName() {
		return "change";
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(String _widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
	}

	public String resolveWidgetVar() {
		FacesContext context = getFacesContext();
		String userWidgetVar = (String) getAttributes().get("widgetVar");

		if (userWidgetVar != null) {
			return userWidgetVar;
		} else {
			return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
		}

	}

	public int getMin() {
		return (Integer) getStateHelper().eval(PropertyKeys.min, 0);
	}

	public void setMin(int min) {
		this.getStateHelper().put(PropertyKeys.min, min);
	}

	public int getMax() {
		return (Integer) getStateHelper().eval(PropertyKeys.max, 100);
	}

	public void setMax(int max) {
		this.getStateHelper().put(PropertyKeys.max, max);
	}

	public int getStep() {
		return (Integer) getStateHelper().eval(PropertyKeys.step, 1);
	}

	public void setStep(int step) {
		this.getStateHelper().put(PropertyKeys.step, step);
	}

	public Object getWidth() {
		return this.getStateHelper().eval(PropertyKeys.width);
	}

	public void setWidth(Object width) {
		this.getStateHelper().put(PropertyKeys.width, width);
	}

	public String getOnchange() {
		return (String) this.getStateHelper().eval(PropertyKeys.onchange);
	}

	public void setOnchange(String onchange) {
		this.getStateHelper().put(PropertyKeys.onchange, onchange);
	}

	public boolean isShowLabel() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showLabel, true);
	}

	public void setShowLabel(boolean showLabel) {
		this.getStateHelper().put(PropertyKeys.showLabel, showLabel);
	}

	public String getLabelTemplate() {
		return (String) this.getStateHelper().eval(PropertyKeys.labelTemplate, "{value}");
	}

	public void setLabelTemplate(String labelTemplate) {
		this.getStateHelper().put(PropertyKeys.labelTemplate, labelTemplate);
	}

	public boolean isDisabled() {
		return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);

	}

	public void setDisabled(boolean disabled) {
		getStateHelper().put(PropertyKeys.disabled, disabled);
	}

	public boolean isCursor() {
		return (Boolean) getStateHelper().eval(PropertyKeys.cursor, false);

	}

	public void setCursor(boolean cursor) {
		getStateHelper().put(PropertyKeys.cursor, cursor);
	}

	public Float getThickness() {
		return (Float) getStateHelper().eval(PropertyKeys.thickness);
	}

	public void setThickness(Float thickness) {
		this.getStateHelper().put(PropertyKeys.thickness, thickness);
	}

	public Object getForegroundColor() {
		return getStateHelper().eval(PropertyKeys.foregroundColor);
	}

	public void setForegroundColor(Object foregroundColor) {
		this.getStateHelper().put(PropertyKeys.foregroundColor, foregroundColor);
	}

	public Object getBackgroundColor() {
		return getStateHelper().eval(PropertyKeys.backgroundColor);
	}

	public void setBackgroundColor(Object backgroundColor) {
		this.getStateHelper().put(PropertyKeys.backgroundColor, backgroundColor);
	}
}
