package org.primefaces.extensions.showcase.controller.timer;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class BasicTimerController implements Serializable {

	private static final long serialVersionUID = 1L;

	public void onTimeout() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "BOOM", null));
	}

}
