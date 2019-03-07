package org.primefaces.extensions.component.countdown;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@FacesComponent(value = Countdown.COMPONENT_TYPE)
@ResourceDependencies({
    @ResourceDependency(library = "jquery", name = "jquery.min.js")
    ,@ResourceDependency(library = "countdown", name = "jquery.countdown.min.js")
})
public class Countdown extends UIComponentBase {

    public static final String COMPONENT_TYPE = "com.aripd.extensions.component.Countdown";
    public static final String COMPONENT_FAMILY = "com.aripd.extensions.component";
    public static final String DEFAULT_RENDERER = "com.aripd.extensions.component.CountdownRenderer";

    protected static enum PropertyKeys {
        style, styleClass,
        value,
        text, fast;
    }

    public Countdown() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getValue() {
        return (String) getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(String value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getText() {
        return (String) getStateHelper().eval(PropertyKeys.text, null);
    }

    public void setText(String text) {
        getStateHelper().put(PropertyKeys.text, text);
    }

    public Boolean isFast() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fast, false);
    }

    public void setFast(Boolean fast) {
        getStateHelper().put(PropertyKeys.fast, fast);
    }

    public final static String COMPONENT_CLASS = "ui-countdown";

}
