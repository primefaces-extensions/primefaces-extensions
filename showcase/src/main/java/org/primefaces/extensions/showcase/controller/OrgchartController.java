/*
 * Copyright 2011-2018 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
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

	/**
	 * 
	 */
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

	public void onClick(final OrgChartClickEvent event) {

		System.out.println("clicked ID: " + event.getId());
		System.out.println("hierachy: " + event.getHierarchy().toString());
	}

	public void onDropOver(final OrgChartDropEvent event) {

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
