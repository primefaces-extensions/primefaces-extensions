/**
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
 */
package org.primefaces.extensions.component.orgchart;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class DefaultOrgChartNode implements OrgChartNode {

    private String id;

    private String name;

    private String title;

    private String className;

    private Object nodeData;

    private List<OrgChartNode> children = new ArrayList<OrgChartNode>();

    private int childCount;

    private OrgChartNode parent;

    public DefaultOrgChartNode() {
        super();
    }

    public DefaultOrgChartNode(String name, String title) {
        super();
        this.name = name;
        this.title = title;
    }

    public DefaultOrgChartNode(String id, String name, String title) {
        super();
        this.id = id;
        this.name = name;
        this.title = title;
    }

    public DefaultOrgChartNode(String id, String name, String title, Object nodeData) {
        super();
        this.id = id;
        this.name = name;
        this.title = title;
        this.nodeData = nodeData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OrgChartNode> getChildren() {
        return children;
    }

    public void setChildren(List<OrgChartNode> children) {
        this.children = children;
    }

    public OrgChartNode getParent() {
        return parent;
    }

    public void setParent(OrgChartNode parent) {
        this.parent = parent;
    }

    public void addChild(OrgChartNode child) {
        this.children.add(child);
    }

    public void clearChildren() {
        this.children.clear();
    }

    public void clearParent() {
        this.parent = null;
    }

    public Object getNodeData() {
        return nodeData;
    }

    public void setNodeData(Object nodeData) {
        this.nodeData = nodeData;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getChildCount() {
        childCount = this.getChildren().size();
        return childCount;
    }

}
