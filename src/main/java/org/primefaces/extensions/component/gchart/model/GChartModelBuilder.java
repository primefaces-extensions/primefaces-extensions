package org.primefaces.extensions.component.gchart.model;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GChartModelBuilder {
	private List<GChartModelRow> rows = new ArrayList<GChartModelRow>(0);
	private GChartType gChartType;
	private Map<String,Object> options = new HashMap<String, Object>(0);
	private List<String> columns = new ArrayList<String>(0);
	
	public GChartModelBuilder setChartType(GChartType chartType){
		if(gChartType != null){
			throw new IllegalStateException("Type already setted");
		}
		this.gChartType = chartType;
		
		return this;
	}

    public GChartModelBuilder importTreeNode(TreeNode root){
        String label = String.valueOf(root.getData());
        String parentLabel = root.getParent() != null ? String.valueOf(root.getParent().getData()) : StringUtils.EMPTY;

        this.addRow(label,parentLabel);

        for(TreeNode node : root.getChildren()){
            this.importTreeNode(node);
        }

        return this;
    }

	public GChartModelBuilder addColumns(String... columns){
		this.columns.addAll(Arrays.asList(columns));
		return this;
	}
	
	public GChartModelBuilder addColumns(Collection<String> columns){
		this.columns.addAll(columns);
		return this;
	}
	
	public GChartModelBuilder addRow(String label, Object...objects){
		this.rows.add(new DefaultGChartModelRow(label, Arrays.asList(objects)));
		return this;
	}
	
	public GChartModelBuilder addRow(String label, Collection<Object> objects){
		this.rows.add(new DefaultGChartModelRow(label, objects));
		return this;
	}
	
	public GChartModelBuilder addOption(String name,Object value){
		this.options.put(name, value);
		return this;
	}
	public GChartModel build(){
		return new DefaultGChartModel(rows, gChartType, options, columns);
	}
}
