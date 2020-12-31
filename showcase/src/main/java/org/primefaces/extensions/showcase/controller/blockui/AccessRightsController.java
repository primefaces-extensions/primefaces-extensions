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
package org.primefaces.extensions.showcase.controller.blockui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * AccessRightsController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class AccessRightsController implements Serializable {

    private static final long serialVersionUID = 20111229L;

    private List<SelectItem> accessRights = new ArrayList<SelectItem>();
    private List<SelectItem> selectedAccessRights = new ArrayList<SelectItem>();

    public AccessRightsController() {
        accessRights.add(new SelectItem("View", "View"));
        accessRights.add(new SelectItem("Read", "Read"));
        accessRights.add(new SelectItem("Write", "Write"));
        accessRights.add(new SelectItem("Execute", "Execute"));
        accessRights.add(new SelectItem("Delete", "Delete"));
        accessRights.add(new SelectItem("Full", "Full"));
    }

    public List<SelectItem> getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(List<SelectItem> accessRights) {
        this.accessRights = accessRights;
    }

    public List<SelectItem> getSelectedAccessRights() {
        try {
            // simulate a long running request
            Thread.sleep(1200);
        }
        catch (Exception e) {
            // ignore
        }

        return selectedAccessRights;
    }

    public void setSelectedAccessRights(List<SelectItem> selectedAccessRights) {
        this.selectedAccessRights = selectedAccessRights;
    }
}
