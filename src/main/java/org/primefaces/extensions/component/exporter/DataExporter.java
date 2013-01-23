/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
package org.primefaces.extensions.component.exporter;

import java.io.IOException;
import java.lang.System;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.primefaces.component.datatable.DataTable;
/**
 * <code>Exporter</code> component.
 *
 * @author  Sudheer Jonna / last modified by $Author$
 * @since   0.7.0
 */
public class DataExporter implements ActionListener, StateHolder {

	private ValueExpression target;
	
	private ValueExpression type;
	
	private ValueExpression fileName;

    private ValueExpression tableTitle;
	
	private ValueExpression encoding;
	
	private ValueExpression pageOnly;
    
	private ValueExpression selectionOnly;
	
	private MethodExpression preProcessor;
	
	private MethodExpression postProcessor;
	
	private ValueExpression isSubTable;

    private ValueExpression facetBackground;

    private ValueExpression facetFontSize;

    private ValueExpression facetFontColor;

    private ValueExpression facetFontStyle;

    private ValueExpression cellFontSize;

    private ValueExpression cellFontColor;

    private ValueExpression cellFontStyle;
	
	public DataExporter() {}

	public DataExporter(ValueExpression target, ValueExpression type, ValueExpression fileName,ValueExpression tableTitle, ValueExpression pageOnly, ValueExpression selectionOnly, ValueExpression encoding, MethodExpression preProcessor, MethodExpression postProcessor,ValueExpression isSubTable,ValueExpression facetBackground, ValueExpression facetFontSize, ValueExpression facetFontColor, ValueExpression facetFontStyle, ValueExpression cellFontSize, ValueExpression cellFontColor, ValueExpression cellFontStyle) {
		this.target = target;
		this.type = type;
        this.tableTitle = tableTitle;
		this.fileName = fileName;
		this.pageOnly = pageOnly;
		this.selectionOnly = selectionOnly;
		this.preProcessor = preProcessor;
		this.postProcessor = postProcessor;
		this.encoding = encoding;
		this.isSubTable=isSubTable;

        this.facetBackground = facetBackground;
        this.facetFontSize = facetFontSize;
        this.facetFontColor = facetFontColor;
        this.facetFontStyle = facetFontStyle;
        this.cellFontSize = cellFontSize;
        this.cellFontColor = cellFontColor;
        this.cellFontStyle = cellFontStyle;


	}





    public void processAction(ActionEvent event){
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		
		String tableId = (String) target.getValue(elContext);
		String exportAs = (String) type.getValue(elContext);
		String outputFileName = (String) fileName.getValue(elContext);

        String tableTitleValue = "";
        if(tableTitle != null) {
            tableTitleValue = (String) tableTitle.getValue(elContext);
        }

		String encodingType = "UTF-8";
		if(encoding != null) {
			encodingType = (String) encoding.getValue(elContext);
        }

		boolean isPageOnly = false;
		if(pageOnly != null) {
			isPageOnly = pageOnly.isLiteralText() ? Boolean.valueOf(pageOnly.getValue(context.getELContext()).toString()) : (Boolean) pageOnly.getValue(context.getELContext());
		}
		
        boolean isSelectionOnly = false;
		if(selectionOnly != null) {
			isSelectionOnly = selectionOnly.isLiteralText() ? Boolean.valueOf(selectionOnly.getValue(context.getELContext()).toString()) : (Boolean) selectionOnly.getValue(context.getELContext());
		}
		
		 boolean isSubtable = false;
			if(isSubTable != null) {
				isSubtable = isSubTable.isLiteralText() ? Boolean.valueOf(isSubTable.getValue(context.getELContext()).toString()) : (Boolean) isSubTable.getValue(context.getELContext());
			}
        String facetBackgroundValue="#FFFFFF";
        if(facetBackground!=null)
            facetBackgroundValue= (String) facetBackground.getValue(elContext);
        String facetFontSizeValue="10";
        if(facetFontSize!=null)
            facetFontSizeValue= (String) facetFontSize.getValue(elContext);
        String facetFontColorValue="#000000";
        if(facetFontColor!=null)
            facetFontColorValue= (String) facetFontColor.getValue(elContext);
        String facetFontStyleValue="BOLD";
        if(facetFontStyle!=null)
            facetFontStyleValue= (String) facetFontStyle.getValue(elContext);
        String cellFontSizeValue="8";
        if(cellFontSize!=null)
            cellFontSizeValue= (String) cellFontSize.getValue(elContext);
        String cellFontColorValue="#000000";
        if(cellFontColor!=null)
            cellFontColorValue= (String) cellFontColor.getValue(elContext);
        String cellFontStyleValue="NORMAL";
        if(cellFontStyle!=null)
            cellFontStyleValue= (String) cellFontStyle.getValue(elContext);


		try {
		
			Exporter exporter = ExporterFactory.getExporterForType(exportAs);
            exporter.customFormat(facetBackgroundValue,facetFontSizeValue,facetFontColorValue,facetFontStyleValue,cellFontSizeValue,cellFontColorValue,cellFontStyleValue);
			exporter.export(event,tableId,context,outputFileName,tableTitleValue, isPageOnly, isSelectionOnly, encodingType, preProcessor, postProcessor,isSubtable);
			context.responseComplete();
		}
        catch (IOException e) {
			throw new FacesException(e);
		}
	}

	private int[] resolveExcludedColumnIndexes(Object columnsToExclude) {
        if(columnsToExclude == null || columnsToExclude.equals("")) {
            return null;
        }
        else {
            String[] columnIndexesAsString = ((String) columnsToExclude).split(",");
            int[] indexes = new int[columnIndexesAsString.length];

            for(int i=0; i < indexes.length; i++) {
                indexes[i] = Integer.parseInt(columnIndexesAsString[i].trim());
            }

            return indexes;
        }
	}

	public boolean isTransient() {
		return false;
	}

	public void setTransient(boolean value) {
	}
	
	 public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;

		target = (ValueExpression) values[0];
		type = (ValueExpression) values[1];
		fileName = (ValueExpression) values[2];
        tableTitle=(ValueExpression) values[3];
		pageOnly = (ValueExpression) values[4];
		selectionOnly = (ValueExpression) values[5];
		preProcessor = (MethodExpression) values[6];
		postProcessor = (MethodExpression) values[7];
		encoding = (ValueExpression) values[8];
		isSubTable=(ValueExpression) values[9];
        facetBackground=(ValueExpression) values[10];
        facetFontSize=(ValueExpression) values[11];
        facetFontColor=(ValueExpression) values[12];
        facetFontStyle=(ValueExpression) values[13];
        cellFontSize=(ValueExpression) values[14];
        cellFontColor=(ValueExpression) values[15];
        cellFontStyle=(ValueExpression) values[16];
	}

	public Object saveState(FacesContext context) {
		Object values[] = new Object[9];

		values[0] = target;
		values[1] = type;
        values[2]=tableTitle;
		values[3] = fileName;
		values[4] = pageOnly;
		values[5] = selectionOnly;
		values[6] = preProcessor;
		values[7] = postProcessor;
		values[8] = encoding;
		values[9]=isSubTable;
		values[10]=facetBackground;
        values[11] = facetFontSize;
        values[12] = facetFontColor;
        values[13] = facetFontStyle;
        values[14] = cellFontSize;
        values[15] = cellFontColor;
        values[16]=cellFontStyle;

		return ((Object[]) values);
	}
}
