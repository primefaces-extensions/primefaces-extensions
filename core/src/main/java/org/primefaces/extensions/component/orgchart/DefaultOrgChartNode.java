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

    private List<OrgChartNode> children = new ArrayList<>();

    private int childCount;

    private OrgChartNode parent;

    public DefaultOrgChartNode() {
        super();
    }

    public DefaultOrgChartNode(final String name, final String title) {
        super();
        this.name = name;
        this.title = title;
    }

    public DefaultOrgChartNode(final String id, final String name, final String title) {
        super();
        this.id = id;
        this.name = name;
        this.title = title;
    }

    public DefaultOrgChartNode(final String id, final String name, final String title, final Object nodeData) {
        super();
        this.id = id;
        this.name = name;
        this.title = title;
        this.nodeData = nodeData;
    }

    @Override
    public String toString() {
        return "DefaultOrgChartNode [id=" + id + ", name=" + name + ", nodeData=" + nodeData + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (nodeData == null ? 0 : nodeData.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefaultOrgChartNode other = (DefaultOrgChartNode) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        if (nodeData == null) {
            if (other.nodeData != null) {
                return false;
            }
        }
        else if (!nodeData.equals(other.nodeData)) {
            return false;
        }
        return true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public List<OrgChartNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(final List<OrgChartNode> children) {
        this.children = children;
    }

    @Override
    public OrgChartNode getParent() {
        return parent;
    }

    @Override
    public void setParent(final OrgChartNode parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(final OrgChartNode child) {
        children.add(child);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

    @Override
    public void clearParent() {
        parent = null;
    }

    public Object getNodeData() {
        return nodeData;
    }

    public void setNodeData(final Object nodeData) {
        this.nodeData = nodeData;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(final String className) {
        this.className = className;
    }

    @Override
    public int getChildCount() {
        childCount = getChildren().size();
        return childCount;
    }

}
