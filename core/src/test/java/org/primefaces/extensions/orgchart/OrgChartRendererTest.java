/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.primefaces.extensions.component.orgchart.DefaultOrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartNode;
import org.primefaces.extensions.component.orgchart.OrgChartRenderer;

/**
 * <code>orgchart</code> component.
 *
 * @author @jxmai / last modified by $Author$
 * @version $Revision$
 * @since 6.3
 */
public class OrgChartRendererTest {

    private OrgChartRenderer renderer = new OrgChartRenderer();

    private OrgChartNode root = new DefaultOrgChartNode("root", "root");

    public OrgChartRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(OrgChartRenderer renderer) {
        this.renderer = renderer;
    }

    public OrgChartNode getRoot() {
        return root;
    }

    public void setRoot(OrgChartNode root) {
        this.root = root;
    }

    @Before
    public void before() {
        final OrgChartNode child1 = new DefaultOrgChartNode("children 1", "children 1");
        final OrgChartNode child2 = new DefaultOrgChartNode("children 2", "children 2");

        child1.setParent(root);
        child2.setParent(root);

        root.addChild(child1);
        root.addChild(child2);
    }

    @After
    public void after() {

    }

    @Test
    public void testParent() {

        assertEquals(root, root.getChildren().get(0).getParent());
    }

}