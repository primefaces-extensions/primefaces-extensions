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
package org.primefaces.extensions.component.orgchart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class OrgChartHelper {

    public static List<OrgChartNode> getAllNodesTraverseFromRoot(final OrgChartNode root) {
        final List<OrgChartNode> orgChartNodes = new ArrayList<>();

        treeTraversal(root, orgChartNodes);

        return orgChartNodes;
    }

    public static HashMap<String, OrgChartNode> parseOrgChartNodesIntoHashMap(
                final List<OrgChartNode> orgChartNodes) {

        final HashMap<String, OrgChartNode> hashMap = new HashMap<>();

        if (orgChartNodes != null && !orgChartNodes.isEmpty()) {

            if (null == orgChartNodes.get(0).getId() || orgChartNodes.get(0).getId().isEmpty()) {
                return hashMap;
            }
            else {
                for (final OrgChartNode o : orgChartNodes) {
                    hashMap.put(o.getId(), o);
                }
            }
        }

        return hashMap;
    }

    private static void treeTraversal(final OrgChartNode orgChartNode, final List<OrgChartNode> orgChartNodes) {
        if (!orgChartNode.getChildren().isEmpty()) {
            for (final OrgChartNode o : orgChartNode.getChildren()) {
                treeTraversal(o, orgChartNodes);
            }
        }
        orgChartNodes.add(orgChartNode);
    }

}
