/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.webapp;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.primefaces.extensions.model.layout.LayoutOptions;

/**
 * ShowcaseLayout
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@ApplicationScoped
@Named
public class ShowcaseLayout {

    private String options;

    @PostConstruct
    protected void initialize() {
        final LayoutOptions layoutOptions = new LayoutOptions();

        // for all panes
        final LayoutOptions panes = new LayoutOptions();
        panes.addOption("resizable", true);
        panes.addOption("closable", true);
        panes.addOption("slidable", false);
        panes.addOption("resizeWithWindow", false);
        panes.addOption("resizeWhileDragging", true);
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

        // center pane
        final LayoutOptions center = new LayoutOptions();
        center.addOption("resizable", false);
        center.addOption("closable", false);
        center.addOption("resizeWhileDragging", false);
        center.addOption("minWidth", 200);
        center.addOption("minHeight", 60);
        layoutOptions.setCenterOptions(center);

        // west pane
        final LayoutOptions west = new LayoutOptions();
        west.addOption("size", 210);
        west.addOption("minSize", 180);
        west.addOption("maxSize", 500);
        layoutOptions.setWestOptions(west);

        // east pane
        final LayoutOptions east = new LayoutOptions();
        east.addOption("size", 448);
        east.addOption("minSize", 180);
        east.addOption("maxSize", 650);
        layoutOptions.setEastOptions(east);

        // nested east layout
        final LayoutOptions childEastOptions = new LayoutOptions();
        east.setChildOptions(childEastOptions);

        // east-center pane
        final LayoutOptions eastCenter = new LayoutOptions();
        eastCenter.addOption("minHeight", 60);
        childEastOptions.setCenterOptions(eastCenter);

        // south-center pane
        final LayoutOptions southCenter = new LayoutOptions();
        southCenter.addOption("size", "70%");
        southCenter.addOption("minSize", 60);
        childEastOptions.setSouthOptions(southCenter);

        // serialize options to JSON string (increase perf.)
        options = layoutOptions.toJson();
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(final String options) {
        this.options = options;
    }
}
