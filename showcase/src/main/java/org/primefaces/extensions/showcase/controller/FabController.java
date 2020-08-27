package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;

/**
 * Floating Action Button Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class FabController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    public void closeListener(final CloseEvent closeEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Close fired",
                    "Component id: " + closeEvent.getComponent().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void openListener(final OpenEvent openEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Open fired",
                    "Component id: " + openEvent.getComponent().getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
