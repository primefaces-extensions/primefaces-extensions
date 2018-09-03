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

import java.util.List;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public interface OrgChartNode {

    public String getId();

    public void setId(String id);

    String getName();

    void setName(String name);

    String getTitle();

    void setTitle(String title);

    List<OrgChartNode> getChildren();

    void setChildren(List<OrgChartNode> children);

    OrgChartNode getParent();

    void setParent(OrgChartNode parent);

    public void addChild(OrgChartNode child);

    public void clearChildren();

    public void clearParent();

    public String getClassName();

    public void setClassName(String className);

    public int getChildCount();

}
