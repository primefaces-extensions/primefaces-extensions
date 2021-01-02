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
package org.primefaces.extensions.showcase.controller.layout;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * LayoutController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ElementLayoutController implements Serializable {

    private static final long serialVersionUID = 20120925L;

    private String stateOne;
    private String stateTwo;
    private boolean layoutOneShown = true;

    private LayoutOptions layoutOptionsOne;
    private LayoutOptions layoutOptionsTwo;

    @PostConstruct
    protected void initialize() {
        // 1. set options for first layout
        layoutOptionsOne = new LayoutOptions();

        // options for all panes (center and west)
        LayoutOptions panes = new LayoutOptions();
        panes.addOption("slidable", false);
        panes.addOption("resizeWhileDragging", true);
        layoutOptionsOne.setPanesOptions(panes);

        // options for west pane
        final LayoutOptions west = new LayoutOptions();
        west.addOption("size", 150);
        west.addOption("minSize", 40);
        west.addOption("maxSize", 300);
        layoutOptionsOne.setWestOptions(west);

        // 2. set options for second layout
        layoutOptionsTwo = new LayoutOptions();

        // options for all panes
        panes = new LayoutOptions();
        panes.addOption("slidable", false);
        panes.addOption("resizeWhileDragging", true);
        layoutOptionsTwo.setPanesOptions(panes);

        // options for east pane
        final LayoutOptions east = new LayoutOptions();
        panes.addOption("resizable", false);
        panes.addOption("closable", false);
        east.addOption("size", "50%");
        layoutOptionsTwo.setEastOptions(east);

        // options for nested east layout
        final LayoutOptions childEastOptions = new LayoutOptions();
        east.setChildOptions(childEastOptions);

        // options for east-south pane
        final LayoutOptions eastSouth = new LayoutOptions();
        eastSouth.addOption("size", "50%");
        childEastOptions.setSouthOptions(eastSouth);
    }

    public String getStateOne() {
        return stateOne;
    }

    public void setStateOne(final String stateOne) {
        this.stateOne = stateOne;
    }

    public String getStateTwo() {
        return stateTwo;
    }

    public void setStateTwo(final String stateTwo) {
        this.stateTwo = stateTwo;
    }

    public void toogleLayout(final ActionEvent event) {
        layoutOneShown = !layoutOneShown;
    }

    public boolean isLayoutOneShown() {
        return layoutOneShown;
    }

    public LayoutOptions getLayoutOptionsOne() {
        return layoutOptionsOne;
    }

    public LayoutOptions getLayoutOptionsTwo() {
        return layoutOptionsTwo;
    }
}
