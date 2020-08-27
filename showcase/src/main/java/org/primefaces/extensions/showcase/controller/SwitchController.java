package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class SwitchController implements Serializable {

	private static final long serialVersionUID = 1L;
	private String value = "";

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public void listener(final String value) {
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Listener called",
				"Listener with value '" + value + "' called.");

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
}
