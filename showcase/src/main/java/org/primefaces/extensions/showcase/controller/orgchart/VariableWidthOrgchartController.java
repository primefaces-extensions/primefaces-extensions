/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.extensions.component.orgchart.DefaultOrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartNode;

/**
 * VariableWidthOrgchartController
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 */
@Named("variableWidthOrgchartController")
@ViewScoped
public class VariableWidthOrgchartController implements Serializable {

    private static final long serialVersionUID = 1648477595853984821L;

    public OrgChartNode getOrgChartModel() {
        DefaultOrgChartNode root = new DefaultOrgChartNode("1", "Sarah Johnson", "Chief Executive Officer & Board Member");
        root.setClassName("wide-node");

        DefaultOrgChartNode cto = new DefaultOrgChartNode("2", "Michael Chen", "Chief Technology Officer");
        cto.setClassName("standard-node");

        DefaultOrgChartNode cfo = new DefaultOrgChartNode("3", "Emily Rodriguez", "Chief Financial Officer & VP of Strategic Planning");
        cfo.setClassName("wide-node");

        DefaultOrgChartNode cmo = new DefaultOrgChartNode("4", "David Kim", "CMO");
        cmo.setClassName("narrow-node");

        DefaultOrgChartNode engManager = new DefaultOrgChartNode("5", "Robert Williams", "Engineering Manager");
        engManager.setClassName("standard-node");

        DefaultOrgChartNode finAnalyst = new DefaultOrgChartNode("6", "Lisa Brown", "Senior Analyst");
        finAnalyst.setClassName("narrow-node");

        DefaultOrgChartNode accountant = new DefaultOrgChartNode("7", "James Taylor", "Accountant");
        accountant.setClassName("narrow-node");

        root.addChild(cto);
        root.addChild(cfo);
        root.addChild(cmo);
        cto.addChild(engManager);
        cfo.addChild(finAnalyst);
        cfo.addChild(accountant);

        return root;
    }

}
