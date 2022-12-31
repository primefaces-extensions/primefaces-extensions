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
package org.primefaces.extensions.orgchart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.primefaces.extensions.component.orgchart.DefaultOrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartHelper;
import org.primefaces.extensions.component.orgchart.OrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class OrgChartHelperTest {

    private OrgChartNode root = new DefaultOrgChartNode("root", "root");

    public OrgChartNode getRoot() {
        return root;
    }

    public void setRoot(final OrgChartNode root) {
        this.root = root;
    }

    @Before
    public void before() {

    }

    @After
    public void after() {
        root.clearChildren();
    }

    @Test
    public void testGetAllNodesTraverseFromRoot() {

        final OrgChartNode child1 = new DefaultOrgChartNode("children 1", "children 1");
        final OrgChartNode child2 = new DefaultOrgChartNode("children 2", "children 2");
        final OrgChartNode child3 = new DefaultOrgChartNode("children 3", "children 3");

        child1.setParent(root);
        child2.setParent(root);

        root.addChild(child1);
        root.addChild(child2);

        final List<OrgChartNode> orgChartNodes = OrgChartHelper.getAllNodesTraverseFromRoot(root);

        assertEquals(true, orgChartNodes.contains(child1));
        assertEquals(true, orgChartNodes.contains(child2));
        assertEquals(false, orgChartNodes.contains(child3));
        assertEquals(true, orgChartNodes.contains(root));
        assertEquals(3, orgChartNodes.size());
    }

    @Test
    public void testGetAllNodesTraverseFromRootTwo() {

        final OrgChartNode child1 = new DefaultOrgChartNode("children 1", "children 1");
        final OrgChartNode child2 = new DefaultOrgChartNode("children 2", "children 2");
        final OrgChartNode child3 = new DefaultOrgChartNode("children 3", "children 3");
        final OrgChartNode grandChild1 = new DefaultOrgChartNode("grand child1", "grand child1");

        child1.setParent(root);
        child2.setParent(root);

        root.addChild(child1);
        root.addChild(child2);

        child1.addChild(grandChild1);

        final List<OrgChartNode> orgChartNodes = OrgChartHelper.getAllNodesTraverseFromRoot(root);
        assertEquals(true, orgChartNodes.contains(child1));
        assertEquals(true, orgChartNodes.contains(child2));
        assertEquals(false, orgChartNodes.contains(child3));
        assertEquals(true, orgChartNodes.contains(root));
        assertEquals(true, orgChartNodes.contains(grandChild1));
        assertEquals(4, orgChartNodes.size());
    }

    @Test
    public void testParseOrgChartNodesIntoHashMap() {
        final List<OrgChartNode> orgChartNodes = new ArrayList<>();
        final OrgChartNode node1 = new DefaultOrgChartNode("id1", "name1", "title1");
        final OrgChartNode node2 = new DefaultOrgChartNode("id2", "name2", "title2");
        final OrgChartNode node3 = new DefaultOrgChartNode("id3", "name3", "title3");
        orgChartNodes.add(node1);
        orgChartNodes.add(node2);
        orgChartNodes.add(node3);

        final Map<String, OrgChartNode> hashMap = OrgChartHelper.parseOrgChartNodesIntoHashMap(orgChartNodes);

        assertEquals(node1, hashMap.get("id1"));
        assertEquals(node2, hashMap.get("id2"));
        assertEquals(node3, hashMap.get("id3"));
        assertNotSame(node2, hashMap.get("id3"));
        assertEquals(3, hashMap.size());

    }

    @Test
    public void testBuildNodesFromJSON() {

        final OrgChartRenderer orgChartRenderer = new OrgChartRenderer();

        final List<OrgChartNode> orgChartNodes = new ArrayList<>();
        final OrgChartNode orgChartNode = new DefaultOrgChartNode("id1", "name1", "title1");
        final OrgChartNode orgChartNode2 = new DefaultOrgChartNode("id2", "name2", "title2");
        orgChartNodes.add(orgChartNode);
        orgChartNodes.add(orgChartNode2);

        final Map<String, OrgChartNode> hashMap = OrgChartHelper.parseOrgChartNodesIntoHashMap(orgChartNodes);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "id1");
        final JSONArray jsonArray = new JSONArray();
        final JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id", "id2");
        jsonArray.put(jsonObject2);
        jsonObject.put("children", jsonArray);

        final OrgChartNode chartNode = OrgChartRenderer.buildNodesFromJSON(hashMap, jsonObject, null);

        assertEquals("id1", chartNode.getId());
        assertEquals(1, chartNode.getChildren().size());

    }

    @Test
    public void testBuildNodesFromJSONTwo() {

        final OrgChartRenderer orgChartRenderer = new OrgChartRenderer();

        final List<OrgChartNode> orgChartNodes = new ArrayList<>();
        final OrgChartNode orgChartNode = new DefaultOrgChartNode("id1", "name1", "title1");
        final OrgChartNode orgChartNode2 = new DefaultOrgChartNode("id2", "name2", "title2");
        final OrgChartNode orgChartNode3 = new DefaultOrgChartNode("id3", "name3", "title3");
        orgChartNodes.add(orgChartNode);
        orgChartNodes.add(orgChartNode2);
        orgChartNodes.add(orgChartNode3);

        final Map<String, OrgChartNode> hashMap = OrgChartHelper.parseOrgChartNodesIntoHashMap(orgChartNodes);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "id1");
        final JSONArray jsonArray = new JSONArray();
        final JSONObject jsonObject2 = new JSONObject();
        final JSONObject jsonObject3 = new JSONObject();
        jsonObject2.put("id", "id2");
        jsonObject3.put("id", "id3");
        jsonArray.put(jsonObject2);
        jsonArray.put(jsonObject3);
        jsonObject.put("children", jsonArray);

        final OrgChartNode chartNode = OrgChartRenderer.buildNodesFromJSON(hashMap, jsonObject, null);

        assertEquals("id1", chartNode.getId());
        assertEquals(2, chartNode.getChildren().size());
        assertEquals("id2", chartNode.getChildren().get(0).getId());
        assertEquals("id3", chartNode.getChildren().get(1).getId());
    }

    @Test
    public void testBuildNodesFromJSONThree() {

        final OrgChartRenderer orgChartRenderer = new OrgChartRenderer();

        final List<OrgChartNode> orgChartNodes = new ArrayList<>();
        final OrgChartNode orgChartNode = new DefaultOrgChartNode("id1", "name1", "title1");
        final OrgChartNode orgChartNode2 = new DefaultOrgChartNode("id2", "name2", "title2");
        final OrgChartNode orgChartNode3 = new DefaultOrgChartNode("id3", "name3", "title3");
        orgChartNodes.add(orgChartNode);
        orgChartNodes.add(orgChartNode2);
        orgChartNodes.add(orgChartNode3);

        final Map<String, OrgChartNode> hashMap = OrgChartHelper.parseOrgChartNodesIntoHashMap(orgChartNodes);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "id1");
        final JSONArray jsonArray = new JSONArray();
        final JSONArray jsonArray2 = new JSONArray();
        final JSONObject jsonObject2 = new JSONObject();
        final JSONObject jsonObject3 = new JSONObject();
        jsonObject2.put("id", "id2");
        jsonObject2.put("children", jsonArray2);
        jsonObject3.put("id", "id3");
        jsonArray.put(jsonObject2);
        jsonArray2.put(jsonObject3);
        jsonObject.put("children", jsonArray);

        final OrgChartNode chartNode = OrgChartRenderer.buildNodesFromJSON(hashMap, jsonObject, null);

        assertEquals("id1", chartNode.getId());
        assertEquals(1, chartNode.getChildren().size());
        assertEquals(1, chartNode.getChildren().get(0).getChildren().size());
        assertEquals("id2", chartNode.getChildren().get(0).getId());
        assertEquals("id3", chartNode.getChildren().get(0).getChildren().get(0).getId());
    }
}