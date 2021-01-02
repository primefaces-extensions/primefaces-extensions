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

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.Circle;

/**
 * RemoteCommandController
 *
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
@Named
@RequestScoped
public class RemoteCommandController {

    private String subject;
    private Date date;
    private Circle circle;

    public void printMethodParams(final String subject, final Date date, final Circle circle) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "ActionListener called", "Subject: "
                    + subject + ", Date: " + date + ", Circle - backgroundColor: " + circle.getBackgroundColor());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void parametersAssigned() {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "ActionListener called",
                    "Parameters assigned");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(final Circle circle) {
        this.circle = circle;
    }
}
