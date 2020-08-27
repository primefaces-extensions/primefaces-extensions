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