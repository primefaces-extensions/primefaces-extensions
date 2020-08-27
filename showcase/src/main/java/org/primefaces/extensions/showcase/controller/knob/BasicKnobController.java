package org.primefaces.extensions.showcase.controller.knob;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
public class BasicKnobController implements Serializable {

	private static final long serialVersionUID = 1L;
	private int value = 50;

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public void onChange() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "You have selected: " + value, null));
	}
}
