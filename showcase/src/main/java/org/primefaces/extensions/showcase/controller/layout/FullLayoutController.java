/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.layout.LayoutPane;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * LayoutController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class FullLayoutController implements Serializable {

    private static final long serialVersionUID = 20120925L;

    private LayoutOptions layoutOptions;

    @PostConstruct
    protected void initialize() {
        layoutOptions = new LayoutOptions();

        // options for all panes
        final LayoutOptions panes = new LayoutOptions();
        panes.addOption("slidable", false);
        panes.addOption("resizeWhileDragging", false);
        layoutOptions.setPanesOptions(panes);

        // options for center pane
        final LayoutOptions center = new LayoutOptions();
        layoutOptions.setCenterOptions(center);

        // options for nested center layout
        final LayoutOptions childCenterOptions = new LayoutOptions();
        center.setChildOptions(childCenterOptions);

        // options for center-north pane
        final LayoutOptions centerNorth = new LayoutOptions();
        centerNorth.addOption("size", "50%");
        childCenterOptions.setNorthOptions(centerNorth);

        // options for center-center pane
        final LayoutOptions centerCenter = new LayoutOptions();
        centerCenter.addOption("minHeight", 60);
        childCenterOptions.setCenterOptions(centerCenter);

        // options for west pane
        final LayoutOptions west = new LayoutOptions();
        west.addOption("size", 200);
        layoutOptions.setWestOptions(west);

        // options for nested west layout
        final LayoutOptions childWestOptions = new LayoutOptions();
        west.setChildOptions(childWestOptions);

        // options for west-north pane
        final LayoutOptions westNorth = new LayoutOptions();
        westNorth.addOption("size", "33%");
        childWestOptions.setNorthOptions(westNorth);

        // options for west-center pane
        final LayoutOptions westCenter = new LayoutOptions();
        westCenter.addOption("minHeight", "60");
        childWestOptions.setCenterOptions(westCenter);

        // options for west-south pane
        final LayoutOptions westSouth = new LayoutOptions();
        westSouth.addOption("size", "33%");
        childWestOptions.setSouthOptions(westSouth);

        // options for east pane
        final LayoutOptions east = new LayoutOptions();
        east.addOption("size", 200);
        layoutOptions.setEastOptions(east);

        // options for south pane
        final LayoutOptions south = new LayoutOptions();
        south.addOption("size", 80);
        layoutOptions.setSouthOptions(south);
    }

    public LayoutOptions getLayoutOptions() {
        return layoutOptions;
    }

    public void handleClose(final CloseEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane closed",
                    "Position:" + ((LayoutPane) event.getComponent()).getPosition());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleOpen(final OpenEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane opened",
                    "Position:" + ((LayoutPane) event.getComponent()).getPosition());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void handleResize(final ResizeEvent event) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Layout Pane resized",
                    "Position:" + ((LayoutPane) event.getComponent()).getPosition() + ", new width = " + event.getWidth()
                                + "px, new height = " + event.getHeight() + "px");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
