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

    /**
     * @param root
     * @return
     */
    public static List<OrgChartNode> getAllNodesTraverseFromRoot(final OrgChartNode root) {
        final List<OrgChartNode> orgChartNodes = new ArrayList<OrgChartNode>();

        treeTraversal(root, orgChartNodes);

        return orgChartNodes;
    }

    /**
     * @param orgChartNodes
     * @return
     */
    public static HashMap<String, OrgChartNode> parseOrgChartNodesIntoHashMap(
                final List<OrgChartNode> orgChartNodes) {

        final HashMap<String, OrgChartNode> hashMap = new HashMap<String, OrgChartNode>();

        if (orgChartNodes != null && !orgChartNodes.isEmpty()) {

            if (null == orgChartNodes.get(0).getId() || orgChartNodes.get(0).getId().isEmpty()) {

            }
            else {
                for (final OrgChartNode o : orgChartNodes) {
                    hashMap.put(o.getId(), o);
                }
            }
        }

        return hashMap;
    }

    /**
     * @param orgChartNode
     * @param orgChartNodes
     */
    private static void treeTraversal(final OrgChartNode orgChartNode, final List<OrgChartNode> orgChartNodes) {
        if (orgChartNode.getChildren().isEmpty()) {
            orgChartNodes.add(orgChartNode);
        }
        else {
            for (final OrgChartNode o : orgChartNode.getChildren()) {
                treeTraversal(o, orgChartNodes);
            }
            // This line will be executed on backtrack
            orgChartNodes.add(orgChartNode);
        }
    }

}
