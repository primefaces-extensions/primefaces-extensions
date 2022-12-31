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
package org.primefaces.extensions.showcase.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.orgchart.DefaultOrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartNode;
import org.primefaces.extensions.event.OrgChartClickEvent;
import org.primefaces.extensions.event.OrgChartDropEvent;

/**
 * OrgchartController
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class OrgchartController implements Serializable {

    private static final long serialVersionUID = 1648477595853984820L;

    private OrgChartNode orgChartNode;

    private String direction = "t2b";

    public OrgchartController() {
        super();
        init();
    }

    public void init() {
        orgChartNode = new DefaultOrgChartNode("id1", "Node1", "content1");
        orgChartNode.addChild(new DefaultOrgChartNode("id2", "Node2", "Content2"));
        orgChartNode.addChild(new DefaultOrgChartNode("id3", "Node3", "Content3"));
        final OrgChartNode node = new DefaultOrgChartNode("id4", "Node4", "Content4");
        orgChartNode.addChild(node);
        node.addChild(new DefaultOrgChartNode("id5", "Node5", "Content5"));
        node.addChild(new DefaultOrgChartNode("id6", "Node6", "Content6"));
    }

    public static void onClick(final OrgChartClickEvent event) {
        System.out.println("clicked ID: " + event.getId());
        System.out.println("hierachy: " + event.getHierarchy().toString());
    }

    public static void onDropOver(final OrgChartDropEvent event) {

        System.out.println("hierachy: " + event.getHierarchy().toString());
        System.out.println("dragged node id " + event.getDraggedNodeId());
        System.out.println("dropped node id " + event.getDroppedZoneId());
    }

    public OrgChartNode getOrgChartNode() {
        return orgChartNode;
    }

    public void setOrgChartNode(final OrgChartNode orgChartNode) {
        this.orgChartNode = orgChartNode;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(final String direction) {
        this.direction = direction;
    }

}
