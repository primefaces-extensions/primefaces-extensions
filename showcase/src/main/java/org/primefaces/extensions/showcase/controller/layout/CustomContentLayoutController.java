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

import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * CustomContentLayoutController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class CustomContentLayoutController implements Serializable {

    private static final long serialVersionUID = 20120925L;

    private final String src = "/sections/layout/example-customContentOne.xhtml";
    private LayoutOptions layoutOptions;

    @PostConstruct
    protected void initialize() {
        layoutOptions = new LayoutOptions();

        // options for all panes
        final LayoutOptions panes = new LayoutOptions();
        panes.addOption("slidable", false);
        layoutOptions.setPanesOptions(panes);

        // north pane
        final LayoutOptions north = new LayoutOptions();
        north.addOption("resizable", false);
        north.addOption("closable", false);
        north.addOption("size", 60);
        north.addOption("spacing_open", 0);
        layoutOptions.setNorthOptions(north);

        // south pane
        final LayoutOptions south = new LayoutOptions();
        south.addOption("resizable", false);
        south.addOption("closable", false);
        south.addOption("size", 28);
        south.addOption("spacing_open", 0);
        layoutOptions.setSouthOptions(south);

        // west pane
        final LayoutOptions west = new LayoutOptions();
        west.addOption("size", 340);
        west.addOption("minSize", 150);
        west.addOption("maxSize", 500);
        layoutOptions.setWestOptions(west);

        final LayoutOptions center = new LayoutOptions();
        center.addOption("resizable", false);
        center.addOption("closable", false);
        center.addOption("minWidth", 200);
        center.addOption("minHeight", 60);
        layoutOptions.setCenterOptions(center);

        // set options for nested center layout
        final LayoutOptions optionsNested = new LayoutOptions();
        center.setChildOptions(optionsNested);

        // options for center-north pane
        final LayoutOptions centerNorth = new LayoutOptions();
        centerNorth.addOption("size", "50%");
        optionsNested.setNorthOptions(centerNorth);

        // options for center-center pane
        final LayoutOptions centerCenter = new LayoutOptions();
        centerCenter.addOption("minHeight", 60);
        optionsNested.setCenterOptions(centerCenter);
    }

    public LayoutOptions getLayoutOptions() {
        return layoutOptions;
    }

    public String getSrc() {
        return src;
    }

    public void showMessages() {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This is the first error message",
                    null);
        final FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "This is the second error message",
                    null);

        fc.addMessage(null, msg1);
        fc.addMessage(null, msg2);
    }

    public void hideMessages() {
        // nothing to do
    }
}
