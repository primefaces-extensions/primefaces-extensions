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
package org.primefaces.extensions.showcase.controller.orgchart;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.orgchart.DefaultOrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartNode;

/**
 * ColorCodedOrgchartController
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 */
@Named("colorCodedOrgchartController")
@ViewScoped
public class ColorCodedOrgchartController implements Serializable {

    private static final long serialVersionUID = 1648477595853984820L;

    public OrgChartNode getColorCodedChart() {
        DefaultOrgChartNode root = new DefaultOrgChartNode("1", "Leadership", "Executive Team");
        root.setClassName("gold-level");

        DefaultOrgChartNode mgmt = new DefaultOrgChartNode("2", "Management", "Middle Management");
        mgmt.setClassName("green-level");

        DefaultOrgChartNode staff1 = new DefaultOrgChartNode("3", "Engineering", "Tech Staff");
        staff1.setClassName("blue-level");

        DefaultOrgChartNode staff2 = new DefaultOrgChartNode("4", "Sales", "Sales Staff");
        staff2.setClassName("red-level");

        DefaultOrgChartNode staff3 = new DefaultOrgChartNode("5", "Support", "Support Staff");
        staff3.setClassName("purple-level");

        mgmt.addChild(staff1);
        mgmt.addChild(staff2);
        mgmt.addChild(staff3);
        root.addChild(mgmt);

        return root;
    }

}