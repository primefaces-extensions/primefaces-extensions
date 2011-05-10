package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesListener;

@SuppressWarnings("serial")
public class ResizeEvent extends AjaxBehaviorEvent {

	private double width;
	private double height;
	
	public ResizeEvent(UIComponent component, 
			Behavior behavior, 
			double width, 
			double height) {
		super(component, behavior);
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return false;
	}

	@Override
	public void processListener(FacesListener faceslistener) {
		throw new UnsupportedOperationException();
	}
	
	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}