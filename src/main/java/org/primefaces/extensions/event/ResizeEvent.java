package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

@SuppressWarnings("serial")
public class ResizeEvent extends FacesEvent {

	private double width;
	private double height;
	
	public ResizeEvent(UIComponent component, double width, double height) {
		super(component);
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