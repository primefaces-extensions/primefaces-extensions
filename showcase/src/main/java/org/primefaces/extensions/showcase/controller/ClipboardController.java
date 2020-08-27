package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.event.ClipboardErrorEvent;
import org.primefaces.extensions.event.ClipboardSuccessEvent;

/**
 * Clipboard Controller.
 *
 * @author Melloware mellowaredev@gmail.com
 */
@Named
@ViewScoped
public class ClipboardController implements Serializable {

    private static final long serialVersionUID = 20120224L;

    private String copyInput = "Test Copy!";
    private String cutInput = "Cut Me!";
    private String lineBreaks = "PrimeFaces Clipboard\nRocks Ajax!";

    public void successListener(final ClipboardSuccessEvent successEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success",
                    "Component id: " + successEvent.getComponent().getId() + " Action: " + successEvent.getAction()
                                + " Text: " + successEvent.getText());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void errorListener(final ClipboardErrorEvent errorEvent) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    "Component id: " + errorEvent.getComponent().getId() + " Action: " + errorEvent.getAction());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String getCopyInput() {
        return copyInput;
    }

    public void setCopyInput(final String copyInput) {
        this.copyInput = copyInput;
    }

    public String getCutInput() {
        return cutInput;
    }

    public void setCutInput(final String cutInput) {
        this.cutInput = cutInput;
    }

    public String getLineBreaks() {
        return lineBreaks;
    }

    public void setLineBreaks(final String lineBreaks) {
        this.lineBreaks = lineBreaks;
    }
}
