/*
 * Copyright 2011-2021 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
