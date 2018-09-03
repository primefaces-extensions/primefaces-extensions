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
package org.primefaces.extensions.orgchart;

import static org.junit.Assert.*;

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

	OrgChartRenderer renderer = new OrgChartRenderer();

	OrgChartNode root = new DefaultOrgChartNode("root", "root");

	@Before
	public void before() {
		OrgChartNode child1 = new DefaultOrgChartNode("children 1", "children 1");
		OrgChartNode child2 = new DefaultOrgChartNode("children 2", "children 2");

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