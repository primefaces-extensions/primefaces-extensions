package org.primefaces.extensions.showcase.controller.timer;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class JsApiTimerController implements Serializable {

	private static final long serialVersionUID = 1L;

	public void listener() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Stopped", null));
	}

}
