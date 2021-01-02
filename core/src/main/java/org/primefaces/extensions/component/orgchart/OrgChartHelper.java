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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class OrgChartHelper {

    private OrgChartHelper() {
        // prevent instantiation
    }

    public static List<OrgChartNode> getAllNodesTraverseFromRoot(final OrgChartNode root) {
        final List<OrgChartNode> orgChartNodes = new ArrayList<>();

        treeTraversal(root, orgChartNodes);

        return orgChartNodes;
    }

    public static Map<String, OrgChartNode> parseOrgChartNodesIntoHashMap(
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
        if (orgChartNode.getChildCount() > 0) {
            for (final OrgChartNode o : orgChartNode.getChildren()) {
                treeTraversal(o, orgChartNodes);
            }
        }
        orgChartNodes.add(orgChartNode);
    }

}
