package org.primefaces.extensions.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesListener;

@SuppressWarnings("serial")
public class RotateEvent extends AjaxBehaviorEvent {

	private int degree;

	public RotateEvent(UIComponent component,
			Behavior behavior,
			int degree) {
		super(component, behavior);
		this.degree = degree;
	}

	@Override
	public boolean isAppropriateListener(FacesListener listener) {
		return false;
	}

	@Override
	public void processListener(FacesListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}

	/**
	 * @param degree the degree to set
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}
}
