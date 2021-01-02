/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
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
