/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.gchart;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartType;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named
@RequestScoped
public class OrganizationalGChartController implements Serializable {

    private static final long serialVersionUID = 253762400419864192L;

    private GChartModel standardModel = null;
    private GChartModel treeModel = null;

    public GChartModel getStandardModel() {
        return standardModel;
    }

    public GChartModel getTreeModel() {
        return treeModel;
    }

    @PostConstruct
    public void generateModel() {
        standardModel = new GChartModelBuilder().setChartType(GChartType.ORGANIZATIONAL).addColumns("Name", "Manager")
                    .addRow("Mike", "").addRow("Alice", "Mike").addRow("Jim", "Mike").addRow("Bob", "Alice")
                    .addOption("size", "large").build();

        final TreeNode mike = new DefaultTreeNode("Mike");

        treeModel = new GChartModelBuilder().setChartType(GChartType.ORGANIZATIONAL).addColumns("Name", "Manager")
                    .importTreeNode(mike).addOption("size", "large").build();
    }

}