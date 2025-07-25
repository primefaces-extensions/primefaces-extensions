/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.blockui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

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

    private List<SelectItem> accessRights = new ArrayList<>();
    private List<SelectItem> selectedAccessRights = new ArrayList<>();

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
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        return selectedAccessRights;
    }

    public void setSelectedAccessRights(List<SelectItem> selectedAccessRights) {
        this.selectedAccessRights = selectedAccessRights;
    }
}
