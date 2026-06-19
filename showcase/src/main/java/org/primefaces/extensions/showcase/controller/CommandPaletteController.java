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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.component.commandpalette.CommandPaletteSelection;

@Named
@ViewScoped
public class CommandPaletteController implements Serializable {

    private static final long serialVersionUID = 1L;

    public void onItemSelect(final SelectEvent<CommandPaletteSelection> event) {
        final CommandPaletteSelection selection = event.getObject();
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Item Selected",
                    "Group: " + selection.getGroup()
                                + ", Value: " + selection.getValue()
                                + ", Label: " + selection.getLabel());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
