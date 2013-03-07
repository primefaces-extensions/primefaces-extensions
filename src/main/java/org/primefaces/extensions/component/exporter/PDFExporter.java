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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.Float;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.reflect.Array;
import java.util.Map;
import java.awt.Color;


import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datalist.DataList;
import org.primefaces.component.row.Row;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.component.summaryrow.SummaryRow;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.columns.Columns;
import org.primefaces.util.Constants;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;


/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
public class PDFExporter extends Exporter {

    private Font cellFont;
    private Font facetFont;
    private Color facetBackground;
    private Float facetFontSize;
    private Color facetFontColor;
    private String facetFontStyle;
    private Float cellFontSize;
    private Color cellFontColor;
    private String cellFontStyle;


    @Override
    public void export(ActionEvent event, String tableId, FacesContext context, String filename, String tableTitle, boolean pageOnly, boolean selectionOnly, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor, boolean isSubTable) throws IOException {
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            StringTokenizer st = new StringTokenizer(tableId, ",");
            while (st.hasMoreElements()) {
                String tableName = (String) st.nextElement();
                UIComponent component = event.getComponent().findComponent(tableName);
                if (component == null) {
                    throw new FacesException("Cannot find component \"" + tableName + "\" in view.");
                }
                if (!(component instanceof DataTable || component instanceof DataList)) {
                    throw new FacesException("Unsupported datasource target:\"" + component.getClass().getName() + "\", exporter must target a PrimeFaces DataTable/DataList.");
                }

                if (preProcessor != null) {
                    preProcessor.invoke(context.getELContext(), new Object[]{document});
                }

                if (!document.isOpen()) {
                    document.open();
                }
                if (tableTitle != null && !tableTitle.isEmpty() && !tableId.contains("" + ",")) {

                    Font tableTitleFont = FontFactory.getFont(FontFactory.TIMES, encodingType, Font.DEFAULTSIZE, Font.BOLD);
                    Paragraph title = new Paragraph(tableTitle, tableTitleFont);
                    document.add(title);

                    Paragraph preface = new Paragraph();
                    addEmptyLine(preface, 3);
                    document.add(preface);
                }
                PdfPTable pdf;
                DataList list = null;
                DataTable table = null;
                if (component instanceof DataList) {
                    list = (DataList) component;
                    pdf = exportPDFTable(context, list, pageOnly, encodingType);
                }
                else {
                    table = (DataTable) component;
                    pdf = exportPDFTable(context, table, pageOnly, selectionOnly, encodingType, isSubTable);
                }

                if (pdf != null){
                    document.add(pdf);
                }
                // add a couple of blank lines
                Paragraph preface = new Paragraph();
                addEmptyLine(preface, 7);
                document.add(preface);


                if (postProcessor != null) {
                    postProcessor.invoke(context.getELContext(), new Object[]{document});
                }
            }
            document.close();

            writePDFToResponse(context.getExternalContext(), baos, filename);

        } catch (DocumentException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    public void customFormat(String facetBackground, String facetFontSize, String facetFontColor, String facetFontStyle, String cellFontSize, String cellFontColor, String cellFontStyle) {

        this.facetBackground = Color.decode(facetBackground);
        this.facetFontSize = new Float(facetFontSize);
        this.facetFontColor = Color.decode(facetFontColor);
        this.cellFontSize = new Float(cellFontSize);
        this.cellFontColor = Color.decode(cellFontColor);

        if (facetFontStyle.equalsIgnoreCase("NORMAL")) {
            this.facetFontStyle = "" + facetFont.NORMAL;
        }
        if (facetFontStyle.equalsIgnoreCase("BOLD"))  {
            this.facetFontStyle = "" + facetFont.BOLD;
        }
        if (facetFontStyle.equalsIgnoreCase("ITALIC")) {
            this.facetFontStyle = "" + facetFont.ITALIC;
        }

        if (cellFontStyle.equalsIgnoreCase("NORMAL")) {
            this.cellFontStyle = "" + cellFont.NORMAL;
        }
        if (cellFontStyle.equalsIgnoreCase("BOLD"))  {
            this.cellFontStyle = "" + cellFont.BOLD;
        }
        if (cellFontStyle.equalsIgnoreCase("ITALIC")) {
            this.cellFontStyle = "" + cellFont.ITALIC;
        }

    }

    protected void createCustomFonts(String encoding) {

        this.cellFont = FontFactory.getFont(FontFactory.TIMES, encoding);
        this.facetFont = FontFactory.getFont(FontFactory.TIMES, encoding, Font.DEFAULTSIZE, Font.BOLD);
        if (facetFontColor != null) {
            this.facetFont.setColor(facetFontColor);
        }
        if (facetFontSize != null) {
            this.facetFont.setSize(facetFontSize);
        }
        if (facetFontStyle != null) {
            this.facetFont.setStyle(facetFontStyle);
        }
        if (cellFontColor != null) {
            this.cellFont.setColor(cellFontColor);
        }
        if (cellFontSize != null) {
            this.cellFont.setSize(cellFontSize);
        }
        if (cellFontStyle != null) {
            this.cellFont.setStyle(cellFontStyle);
        }
    }

    protected PdfPTable exportPDFTable(FacesContext context, DataTable table, boolean pageOnly, boolean selectionOnly, String encoding, boolean isSubTable) {

        createCustomFonts(encoding);
        int columnsCount = getColumnsCount(table);
        PdfPTable pdfTable = null;
        if (isSubTable) {
            int subTableCount = table.getRowCount();
            SubTable subtable = table.getSubTable();
            int subTableColumnsCount = getColumnsCount(subtable);
            pdfTable = new PdfPTable(subTableColumnsCount);

            if (table.getHeader() != null) {
                tableFacet(context, pdfTable, table, subTableColumnsCount, "header");
            }

            tableColumnGroup(pdfTable, table, "header");

            int i = 0;
            while (subTableCount > 0) {

                subTableCount--;
                table.setRowIndex(i);
                i++;
                subtable = table.getSubTable();

                if (subtable.getHeader() != null) {
                    tableFacet(context, pdfTable, subtable, subTableColumnsCount, "header");
                }


                if (hasHeaderColumn(subtable)) {
                    addColumnFacets(subtable, pdfTable, ColumnType.HEADER);
                }

                if (pageOnly) {
                    exportPageOnly(context, table, pdfTable);
                } else if (selectionOnly) {
                    exportSelectionOnly(context, table, pdfTable);
                } else {
                    subTableExportAll(context, subtable, pdfTable);
                }

                if (hasFooterColumn(subtable)) {

                    addColumnFacets(subtable, pdfTable, ColumnType.FOOTER);
                }

                if (subtable.getFooter() != null) {
                    tableFacet(context, pdfTable, subtable, subTableColumnsCount, "footer");
                }

                subtable.setRowIndex(-1);
            }

            tableColumnGroup(pdfTable, table, "footer");


            if (table.hasFooterColumn()) {
                tableFacet(context, pdfTable, table, subTableColumnsCount, "footer");
            }


            return pdfTable;
        } else if (columnsCount != 0) {
            pdfTable = new PdfPTable(columnsCount);

            if (table.getHeader() != null) {
                tableFacet(context, pdfTable, table, columnsCount, "header");
            }

            if (hasHeaderColumn(table)) {
                addColumnFacets(table, pdfTable, ColumnType.HEADER);
            }
            if (pageOnly) {
                exportPageOnly(context, table, pdfTable);
            } else if (selectionOnly) {
                exportSelectionOnly(context, table, pdfTable);
            } else {
                exportAll(context, table, pdfTable);
            }

            if (table.hasFooterColumn()) {
                addColumnFacets(table, pdfTable, ColumnType.FOOTER);
            }
            if (table.getFooter() != null) {

                tableFacet(context, pdfTable, table, columnsCount, "footer");
            }

            table.setRowIndex(-1);

            return pdfTable;
        } else {
            return null;
        }

    }

    protected PdfPTable exportPDFTable(FacesContext context, DataList list, boolean pageOnly, String encoding) {

        createCustomFonts(encoding);
        int first = list.getFirst();
        int rowCount = list.getRowCount();
        int rowsToExport = first + list.getRows();

        PdfPTable pdfTable = new PdfPTable(1);
        if (list.getHeader() != null) {
            String value = exportValue(FacesContext.getCurrentInstance(), list.getHeader());
            PdfPCell cell = new PdfPCell(new Paragraph((value), this.facetFont));
            if (facetBackground != null){
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }

        StringBuilder builder = new StringBuilder();
        String output = null;
        UIComponent datalist = (UIComponent) list;

        if (pageOnly) {
            output = exportPageOnly(first, list, rowsToExport, builder);
        } else {
            output = exportAll(list, rowCount, builder);
        }

        pdfTable.addCell(new Paragraph(output, cellFont));
        pdfTable.completeRow();

        if (list.getFooter() != null) {
            String value = exportValue(FacesContext.getCurrentInstance(), list.getFooter());
            PdfPCell cell = new PdfPCell(new Paragraph((value), this.facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }

        return pdfTable;
    }

    protected void exportPageOnly(FacesContext context, DataTable table, PdfPTable pdfTable) {
        int first = table.getFirst();
        int rowsToExport = first + table.getRows();

        tableColumnGroup(pdfTable, table, "header");

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, pdfTable, rowIndex);
        }

        tableColumnGroup(pdfTable, table, "footer");
    }

    protected String exportPageOnly(int first, DataList list, int rowsToExport, StringBuilder input) {
        String output = "";
        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            output = addColumnValues(list, input);
        }
        return output;

    }

    protected void exportSelectionOnly(FacesContext context, DataTable table, PdfPTable pdfTable) {
        Object selection = table.getSelection();
        String var = table.getVar();

        if (selection != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));

                    exportCells(table, pdfTable);
                }
            } else {
                requestMap.put(var, selection);

                exportCells(table, pdfTable);
            }
        }
    }

    protected void exportAll(FacesContext context, DataTable table, PdfPTable pdfTable) {
        int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rows = table.getRows();
        boolean lazy = table.isLazy();

        if (lazy) {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                if (rowIndex % rows == 0) {
                    table.setFirst(rowIndex);
                    table.loadLazyData();
                }

                exportRow(table, pdfTable, rowIndex);
            }

            //restore
            table.setFirst(first);
            table.loadLazyData();
        } else {
            tableColumnGroup(pdfTable, table, "header");
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, pdfTable, rowIndex);
            }
            tableColumnGroup(pdfTable, table, "footer");
            //restore
            table.setFirst(first);
        }
    }

    protected void subTableExportAll(FacesContext context, SubTable table, PdfPTable pdfTable) {
        int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rows = table.getRows();
        boolean lazy = false;

        if (lazy) {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                if (rowIndex % rows == 0) {
                    table.setFirst(rowIndex);
                }
            }

            //restore
            table.setFirst(first);

        } else {
            tableColumnGroup(pdfTable, table, "header");

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                subTableExportRow(table, pdfTable, rowIndex);
            }

            tableColumnGroup(pdfTable, table, "footer");
            //restore
            table.setFirst(first);
        }
    }

    protected String exportAll(DataList list, int rowCount, StringBuilder input) {
        String output = "";
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            list.setRowIndex(rowIndex);
            output = addColumnValues(list, input);
        }

        return output;
    }

    protected void tableFacet(FacesContext context, PdfPTable pdfTable, DataTable table, int columnCount, String facetType) {
        Map<String, UIComponent> map = table.getFacets();
        UIComponent component = map.get(facetType);
        if (component != null) {
            String headerValue = null;
            if (component instanceof HtmlCommandButton) {
                headerValue = exportValue(context, component);
            } else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            } else {
                headerValue = exportFacetValue(context, component);
            }
            PdfPCell cell = new PdfPCell(new Paragraph((headerValue), this.facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(columnCount);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }
    }

    protected void tableFacet(FacesContext context, PdfPTable pdfTable, SubTable table, int columnCount, String facetType) {
        Map<String, UIComponent> map = table.getFacets();
        UIComponent component = map.get(facetType);
        if (component != null) {
            String headerValue = null;
            if (component instanceof HtmlCommandButton) {
                headerValue = exportValue(context, component);
            } else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            } else {
                headerValue = exportFacetValue(context, component);
            }
            PdfPCell cell = new PdfPCell(new Paragraph((headerValue), this.facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(columnCount);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }
    }

    protected void tableColumnGroup(PdfPTable pdfTable, DataTable table, String facetType) {
        ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null) {
            headerComponentList = cg.getChildren();
        }
        if (headerComponentList != null)
            for (UIComponent component : headerComponentList) {
                if (component instanceof Row) {
                    Row row = (Row) component;
                    for (UIComponent rowComponent : row.getChildren()) {
                        UIColumn column = (UIColumn) rowComponent;
                        String value = null;
                        if (facetType.equalsIgnoreCase("header")) {
                            value = column.getHeaderText();
                        }
                        else
                            value = column.getFooterText();
                        int rowSpan = column.getRowspan();
                        int colSpan = column.getColspan();
                        PdfPCell cell = new PdfPCell(new Paragraph(value, this.facetFont));
                        if (facetBackground != null) {
                            cell.setBackgroundColor(facetBackground);
                        }
                        if (rowSpan > 1) {
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setRowspan(rowSpan);


                        }
                        if (colSpan > 1) {
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setColspan(colSpan);


                        }
                        pdfTable.addCell(cell);

                    }
                }

            }
        pdfTable.completeRow();

    }

    protected void tableColumnGroup(PdfPTable pdfTable, SubTable table, String facetType) {
        ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null){
            headerComponentList = cg.getChildren();
        }
        if (headerComponentList != null)
            for (UIComponent component : headerComponentList) {
                if (component instanceof Row) {
                    Row row = (Row) component;
                    for (UIComponent rowComponent : row.getChildren()) {
                        UIColumn column = (UIColumn) rowComponent;
                        String value = null;
                        if (facetType.equalsIgnoreCase("header")) {
                            value = column.getHeaderText();
                        }
                        else
                            value = column.getFooterText();
                        int rowSpan = column.getRowspan();
                        int colSpan = column.getColspan();
                        PdfPCell cell = new PdfPCell(new Paragraph(value, this.facetFont));
                        if (facetBackground != null) {
                            cell.setBackgroundColor(facetBackground);
                        }
                        if (rowSpan > 1) {
                            cell.setVerticalAlignment(Element.ALIGN_CENTER);
                            cell.setRowspan(rowSpan);


                        }
                        if (colSpan > 1) {
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setColspan(colSpan);


                        }
                        pdfTable.addCell(cell);

                    }
                }


            }
        pdfTable.completeRow();

    }

    protected String exportFacetValue(FacesContext context, UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object value = valueHolder.getValue();
            if (value == null) {
                return "";
            }

            //first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            }
            return value.toString();
        } else {
            //This would get the plain texts on UIInstructions when using Facelets
            String value = component.toString();

            if (value != null) {
                return value.trim();
            }
            else {
                return "";
            }
        }

    }

    public boolean hasHeaderColumn(DataTable table) {
        for (UIComponent child : table.getChildren()) {
            if (child.isRendered() && (child instanceof UIColumn)) {
                UIColumn column = (UIColumn) child;

                if (column.getFacet("header") != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    public boolean hasHeaderColumn(SubTable table) {
        for (UIComponent child : table.getChildren()) {
            if (child.isRendered() && (child instanceof UIColumn)) {
                UIColumn column = (UIColumn) child;

                if (column.getFacet("header") != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    public boolean hasFooterColumn(SubTable table) {
        for (UIComponent child : table.getChildren()) {
            if (child.isRendered() && (child instanceof UIColumn)) {
                UIColumn column = (UIColumn) child;

                if (column.getFacet("footer") != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    protected void exportRow(DataTable table, PdfPTable pdfTable, int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, pdfTable);
        SummaryRow sr = table.getSummaryRow();

        if (sr != null && sr.isInView()) {
            for (UIComponent summaryComponent : sr.getChildren()) {
                UIColumn column = (UIColumn) summaryComponent;
                StringBuilder builder = new StringBuilder();

                for (UIComponent component : column.getChildren()) {
                    if (component.isRendered()) {
                        String value = exportValue(FacesContext.getCurrentInstance(), component);

                        if (value != null) {
                            builder.append(value);
                        }
                    }
                }
                int rowSpan = column.getRowspan();
                int colSpan = column.getColspan();
                PdfPCell cell = new PdfPCell(new Paragraph(builder.toString(), this.facetFont));
                if (facetBackground != null) {
                    cell.setBackgroundColor(facetBackground);
                }
                if (rowSpan > 1) {
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setRowspan(rowSpan);


                }
                if (colSpan > 1) {
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setColspan(colSpan);


                }
                pdfTable.addCell(cell);
            }
        }
    }

    protected void subTableExportRow(SubTable table, PdfPTable pdfTable, int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        subTableExportCells(table, pdfTable);
    }

    protected void exportCells(DataTable table, PdfPTable pdfTable) {
        for (UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                if (col.getSelectionMode() != null) {
                    pdfTable.addCell(new Paragraph(col.getSelectionMode(), this.cellFont));
                    continue;
                }
                addColumnValue(pdfTable, col.getChildren(), this.cellFont);
            }

        }

    }

    protected void subTableExportCells(SubTable table, PdfPTable pdfTable) {
        for (UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(pdfTable, col.getChildren(), this.cellFont);
            }
        }
    }


    protected void addColumnFacets(DataTable table, PdfPTable pdfTable, ColumnType columnType) {
        for (UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }
            PdfPCell cell = null;
            if (col.isExportable()) {
                if (col.getHeaderText() != null && columnType.name().equalsIgnoreCase("header")) {
                    cell = new PdfPCell(new Paragraph(col.getHeaderText(), this.facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }
                    pdfTable.addCell(cell);
                } else if (col.getFooterText() != null && columnType.name().equalsIgnoreCase("footer")) {
                    cell = new PdfPCell(new Paragraph(col.getFooterText(), this.facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }
                    pdfTable.addCell(cell);
                } else {

                    addColumnValue(pdfTable, col.getFacet(columnType.facet()), this.facetFont);
                }
            }
        }
    }

    protected void addColumnFacets(SubTable table, PdfPTable pdfTable, ColumnType columnType) {
        for (UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }
            PdfPCell cell = null;
            if (col.isExportable()) {
                if (col.getHeaderText() != null && columnType.name().equalsIgnoreCase("header")) {
                    cell = new PdfPCell(new Paragraph(col.getHeaderText(), this.facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }
                    pdfTable.addCell(cell);
                } else if (col.getFooterText() != null && columnType.name().equalsIgnoreCase("footer")) {
                    cell = new PdfPCell(new Paragraph(col.getFooterText(), this.facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }
                    pdfTable.addCell(cell);
                } else {

                    addColumnValue(pdfTable, col.getFacet(columnType.facet()), this.facetFont);
                }
            }
        }
    }

    protected void addColumnValue(PdfPTable pdfTable, UIComponent component, Font font) {
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
        PdfPCell cell = new PdfPCell(new Paragraph(value, font));
        if (facetBackground != null) {
            cell.setBackgroundColor(facetBackground);
        }
        pdfTable.addCell(cell);
    }

    protected void addColumnValue(PdfPTable pdfTable, List<UIComponent> components, Font font) {
        StringBuilder builder = new StringBuilder();

        for (UIComponent component : components) {
            if (component.isRendered()) {
                String value = exportValue(FacesContext.getCurrentInstance(), component);

                if (value != null) {
                    builder.append(value);
                }
            }
        }

        pdfTable.addCell(new Paragraph(builder.toString(), font));
    }

    protected String addColumnValues(DataList dataList, StringBuilder input) {
        for (UIComponent component : dataList.getChildren()) {
            if (component instanceof Column) {
                UIColumn column = (UIColumn) component;
                for (UIComponent childComponent : column.getChildren()) {
                    if (component.isRendered()) {
                        String value = exportValue(FacesContext.getCurrentInstance(), childComponent);

                        if (value != null) {
                            input.append(value + "\n \n");
                        }
                    }
                }
                return input.toString();
            } else {
                if (component.isRendered()) {
                    String value = exportValue(FacesContext.getCurrentInstance(), component);

                    if (value != null) {
                        input.append(value + "\n \n");
                    }
                }
                return input.toString();
            }


        }
        return null;
    }

    protected void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) throws IOException, DocumentException {
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
        OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    protected int getColumnsCount(DataTable table) {
        int count = 0;

        for (UIComponent child : table.getChildren()) {
            if (!child.isRendered()) {
                continue;
            }

            if (child instanceof Column) {
                Column column = (Column) child;

                if (column.isExportable()) {
                    count++;
                }
            } else if (child instanceof Columns) {
                Columns columns = (Columns) child;

                if (columns.isExportable()) {
                    count += columns.getRowCount();
                }
            }
        }

        return count;
    }

    protected int getColumnsCount(SubTable table) {
        int count = 0;

        for (UIComponent child : table.getChildren()) {
            if (!child.isRendered()) {
                continue;
            }

            if (child instanceof Column) {
                Column column = (Column) child;

                if (column.isExportable()) {
                    count++;
                }
            } else if (child instanceof Columns) {
                Columns columns = (Columns) child;

                if (columns.isExportable()) {
                    count += columns.getRowCount();
                }
            }
        }

        return count;
    }


}