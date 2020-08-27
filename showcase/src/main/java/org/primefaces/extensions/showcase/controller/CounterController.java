package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

/**
 * Counter Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class CounterController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    public void startListener(final SelectEvent<Double> event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Start fired",
                    "Value: " + event.getObject());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void endListener(final SelectEvent<Double> event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "End fired",
                    "Value: " + event.getObject());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
