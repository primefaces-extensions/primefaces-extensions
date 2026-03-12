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
 * CompactOrgchartController
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 */
@Named("compactOrgchartController")
@ViewScoped
public class CompactOrgchartController implements Serializable {

    private static final long serialVersionUID = 1L;

    private OrgChartNode orgChartNode;

    public CompactOrgchartController() {
        init();
    }

    public void init() {
        orgChartNode = new DefaultOrgChartNode("root", "Lao Lao", "General Manager");

        final DefaultOrgChartNode boMiao = new DefaultOrgChartNode("n1", "Bo Miao", "Department Manager");
        boMiao.setCompact(true);
        boMiao.addChild(new DefaultOrgChartNode("n1a", "Fei Xuan", "Engineer"));
        boMiao.addChild(new DefaultOrgChartNode("n1b", "Er Xuan", "Engineer"));
        boMiao.addChild(new DefaultOrgChartNode("n1c", "San Xuan", "Engineer"));

        final DefaultOrgChartNode siXuan = new DefaultOrgChartNode("n1d", "Si Xuan", "Engineer");
        siXuan.setCompact(true);
        siXuan.addChild(new DefaultOrgChartNode("n1d1", "Feng Shou", "Engineer"));
        siXuan.addChild(new DefaultOrgChartNode("n1d2", "Er Shou", "Engineer"));
        siXuan.addChild(new DefaultOrgChartNode("n1d3", "San Shou", "Engineer"));
        siXuan.addChild(new DefaultOrgChartNode("n1d4", "Si Shou", "Engineer"));
        boMiao.addChild(siXuan);

        boMiao.addChild(new DefaultOrgChartNode("n1e", "Wu Xuan", "Engineer"));

        final DefaultOrgChartNode suMiao = new DefaultOrgChartNode("n2", "Su Miao", "Department Manager");
        suMiao.addChild(new DefaultOrgChartNode("n2a", "Tie Hua", "Senior Engineer"));
        final DefaultOrgChartNode heiHei = new DefaultOrgChartNode("n2b", "Hei Hei", "Senior Engineer");
        heiHei.addChild(new DefaultOrgChartNode("n2b1", "Dan Dan", "Engineer"));
        heiHei.addChild(new DefaultOrgChartNode("n2b2", "Er Dan", "Engineer"));
        heiHei.addChild(new DefaultOrgChartNode("n2b3", "San Dan", "Engineer"));
        suMiao.addChild(heiHei);
        suMiao.addChild(new DefaultOrgChartNode("n2c", "Pang Pang", "Senior Engineer"));

        final DefaultOrgChartNode hongMiao = new DefaultOrgChartNode("n3", "Hong Miao", "Department Manager");

        orgChartNode.addChild(boMiao);
        orgChartNode.addChild(suMiao);
        orgChartNode.addChild(hongMiao);
    }

    public OrgChartNode getOrgChartNode() {
        return orgChartNode;
    }

    public void setOrgChartNode(final OrgChartNode orgChartNode) {
        this.orgChartNode = orgChartNode;
    }

}
