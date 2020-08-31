/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.masterdetail;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.showcase.model.masterdetail.User;

/**
 * WizardMasterDetailController.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class WizardMasterDetailController implements Serializable {

    private static final long serialVersionUID = 20120209L;

    private User user = new User();
    private int currentLevel = 1;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(final int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void save(final ActionEvent actionEvent) {
        final FacesMessage msg = new FacesMessage("Saved successful", "Welcome :" + user.getFirstname());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        // create new empty user
        user = new User();
        currentLevel = 1;
    }
}
