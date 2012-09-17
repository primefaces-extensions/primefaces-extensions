package org.primefaces.extensions.component.switchcomponent;

import javax.faces.component.UIComponentBase;

public class DefaultCase extends UIComponentBase {
	
	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DefaultCase";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	
	public DefaultCase() {
		setRendered(false);
		setRendererType(null);
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
}
