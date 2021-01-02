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
package org.primefaces.extensions.showcase.util;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datalist.DataList;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.row.Row;
import org.primefaces.component.rowexpansion.RowExpansion;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.component.summaryrow.SummaryRow;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.component.exporter.Exporter;
import org.primefaces.util.Constants;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @version $Revision: 1.0 $
 * @since 0.7.0
 */
public class PDFCustomExporter extends Exporter {

    private Font cellFont;
    private Font facetFont;
    private Color facetBackground;
    private Float facetFontSize;
    private Color facetFontColor;
    private String facetFontStyle;
    private String fontName;
    private Float cellFontSize;
    private Color cellFontColor;
    private String cellFontStyle;
    private int datasetPadding;
    private String orientation;

    @Override
    public void export(ActionEvent event, String tableId, FacesContext context, String filename, String tableTitle,
                boolean pageOnly, boolean selectionOnly, String encodingType, MethodExpression preProcessor,
                MethodExpression postProcessor, boolean subTable) throws IOException {
        try {
            Document document = new Document();
            if ("Landscape".equalsIgnoreCase(orientation)) {
                document.setPageSize(PageSize.A4.rotate());
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            StringTokenizer st = new StringTokenizer(tableId, ",");
            while (st.hasMoreElements()) {
                String tableName = (String) st.nextElement();
                UIComponent component = SearchExpressionFacade.resolveComponent(context, event.getComponent(), tableName);
                if (component == null) {
                    throw new FacesException("Cannot find component \"" + tableName + "\" in view.");
                }

                if (!(component instanceof DataTable || component instanceof DataList)) {
                    throw new FacesException("Unsupported datasource target:\"" + component.getClass().getName()
                                + "\", exporter must target a PrimeFaces DataTable/DataList.");
                }

                if (preProcessor != null) {
                    preProcessor.invoke(context.getELContext(), new Object[] {document});
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
                    pdf = exportPDFTable(context, table, pageOnly, selectionOnly, encodingType, subTable);
                }

                if (pdf != null) {
                    document.add(pdf);
                }

                // add a couple of blank lines
                Paragraph preface = new Paragraph();
                addEmptyLine(preface, datasetPadding);
                document.add(preface);

                if (postProcessor != null) {
                    postProcessor.invoke(context.getELContext(), new Object[] {document});
                }
            }

            document.close();

            writePDFToResponse(context.getExternalContext(), baos, filename);
        }
        catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }

    protected PdfPTable exportPDFTable(FacesContext context, DataTable table, boolean pageOnly, boolean selectionOnly,
                String encoding, boolean subTable) {
        if (!("-".equalsIgnoreCase(encoding))) {
            createCustomFonts(encoding);
        }

        int columnsCount = Exporter.getColumnsCount(table);
        PdfPTable pdfTable = null;
        if (subTable) {
            int subTableCount = table.getRowCount();
            SubTable subtable = table.getSubTable();
            int subTableColumnsCount = Exporter.getColumnsCount(subtable);
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

                if (Exporter.hasHeaderColumn(subtable)) {
                    addColumnFacets(subtable, pdfTable, ColumnType.HEADER);
                }

                if (pageOnly) {
                    exportPageOnly(context, table, pdfTable);
                }
                else if (selectionOnly) {
                    exportSelectionOnly(context, table, pdfTable);
                }
                else {
                    subTableExportAll(context, subtable, pdfTable);
                }

                if (Exporter.hasFooterColumn(subtable)) {
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
        }
        else {
            if (columnsCount == 0) {
                return null;
            }

            pdfTable = new PdfPTable(columnsCount + 1);

            if (table.getHeader() != null) {
                tableFacet(context, pdfTable, table, columnsCount + 1, "header");
            }

            if (Exporter.hasHeaderColumn(table)) {
                addColumnFacets(table, pdfTable, ColumnType.HEADER);
            }

            if (pageOnly) {
                exportPageOnly(context, table, pdfTable);
            }
            else if (selectionOnly) {
                exportSelectionOnly(context, table, pdfTable);
            }
            else {
                exportAll(context, table, pdfTable);
            }

            if (table.hasFooterColumn()) {
                addColumnFacets(table, pdfTable, ColumnType.FOOTER);
            }

            if (table.getFooter() != null) {
                tableFacet(context, pdfTable, table, columnsCount + 1, "footer");
            }

            table.setRowIndex(-1);

            return pdfTable;
        }
    }

    protected PdfPTable exportPDFTable(FacesContext context, DataList list, boolean pageOnly, String encoding) {
        if (!("-".equalsIgnoreCase(encoding))) {
            createCustomFonts(encoding);
        }

        int first = list.getFirst();
        int rowCount = list.getRowCount();
        int rowsToExport = first + list.getRows();

        PdfPTable pdfTable = new PdfPTable(1);
        if (list.getHeader() != null) {
            String value = exportValue(FacesContext.getCurrentInstance(), list.getHeader());
            PdfPCell cell = new PdfPCell(new Paragraph((value), facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
            pdfTable.completeRow();
        }

        StringBuilder builder = new StringBuilder();
        String output = null;

        if (pageOnly) {
            output = exportPageOnly(first, list, rowsToExport, builder);
        }
        else {
            output = exportAll(list, rowCount, builder);
        }

        pdfTable.addCell(new Paragraph(output, cellFont));
        pdfTable.completeRow();

        if (list.getFooter() != null) {
            String value = exportValue(FacesContext.getCurrentInstance(), list.getFooter());
            PdfPCell cell = new PdfPCell(new Paragraph((value), facetFont));
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

                    exportCells(table, pdfTable, i);
                }
            }
            else {
                requestMap.put(var, selection);

                exportCells(table, pdfTable, 0);
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

            // restore
            table.setFirst(first);
            table.loadLazyData();
        }
        else {
            tableColumnGroup(pdfTable, table, "header");
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, pdfTable, rowIndex);
            }

            tableColumnGroup(pdfTable, table, "footer");

            // restore
            table.setFirst(first);
        }
    }

    protected void subTableExportAll(FacesContext context, SubTable table, PdfPTable pdfTable) {
        int first = table.getFirst();
        int rowCount = table.getRowCount();

        tableColumnGroup(pdfTable, table, "header");

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            subTableExportRow(table, pdfTable, rowIndex);
        }

        tableColumnGroup(pdfTable, table, "footer");

        // restore
        table.setFirst(first);
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
            }
            else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            }
            else if (component instanceof UIPanel || component instanceof OutputPanel) {
                String header = "";
                for (UIComponent child : component.getChildren()) {
                    headerValue = exportValue(context, child);
                    header = header + headerValue;
                }

                PdfPCell cell = new PdfPCell(new Paragraph((header), facetFont));
                if (facetBackground != null) {
                    cell.setBackgroundColor(facetBackground);
                }

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                // addColumnAlignments(component,cell);
                cell.setColspan(columnCount);
                pdfTable.addCell(cell);
                pdfTable.completeRow();

                return;
            }
            else {
                headerValue = Exporter.exportFacetValue(context, component);
            }

            PdfPCell cell = new PdfPCell(new Paragraph((headerValue), facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            // addColumnAlignments(component,cell);
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
            }
            else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            }
            else if (component instanceof UIPanel || component instanceof OutputPanel) {
                String header = "";
                for (UIComponent child : component.getChildren()) {
                    headerValue = exportValue(context, child);
                    header = header + headerValue;
                }

                PdfPCell cell = new PdfPCell(new Paragraph((header), facetFont));
                if (facetBackground != null) {
                    cell.setBackgroundColor(facetBackground);
                }

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                // addColumnAlignments(component,cell);
                cell.setColspan(columnCount);
                pdfTable.addCell(cell);
                pdfTable.completeRow();

                return;
            }
            else {
                headerValue = Exporter.exportFacetValue(context, component);
            }

            PdfPCell cell = new PdfPCell(new Paragraph((headerValue), facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }

            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            // addColumnAlignments(component,cell);
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

        if (headerComponentList != null) {
            for (UIComponent component : headerComponentList) {
                if (component instanceof Row) {
                    Row row = (Row) component;
                    for (UIComponent rowComponent : row.getChildren()) {
                        UIColumn column = (UIColumn) rowComponent;
                        String value = null;
                        if ("header".equalsIgnoreCase(facetType)) {
                            value = column.getHeaderText();
                        }
                        else {
                            value = column.getFooterText();
                        }

                        int rowSpan = column.getRowspan();
                        int colSpan = column.getColspan();
                        PdfPCell cell = new PdfPCell(new Paragraph(value, facetFont));
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

                        // addColumnAlignments(component,cell);
                        if ("header".equalsIgnoreCase(facetType)) {
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        }

                        pdfTable.addCell(cell);
                    }
                }
            }
        }

        pdfTable.completeRow();
    }

    protected void tableColumnGroup(PdfPTable pdfTable, SubTable table, String facetType) {
        ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null) {
            headerComponentList = cg.getChildren();
        }

        if (headerComponentList != null) {
            for (UIComponent component : headerComponentList) {
                if (component instanceof Row) {
                    Row row = (Row) component;
                    for (UIComponent rowComponent : row.getChildren()) {
                        UIColumn column = (UIColumn) rowComponent;
                        String value = null;
                        if ("header".equalsIgnoreCase(facetType)) {
                            value = column.getHeaderText();
                        }
                        else {
                            value = column.getFooterText();
                        }

                        int rowSpan = column.getRowspan();
                        int colSpan = column.getColspan();
                        PdfPCell cell = new PdfPCell(new Paragraph(value, facetFont));
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

                        // addColumnAlignments(component,cell);
                        if ("header".equalsIgnoreCase(facetType)) {
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        }

                        pdfTable.addCell(cell);
                    }
                }
            }
        }

        pdfTable.completeRow();
    }

    protected void exportRow(DataTable table, PdfPTable pdfTable, int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, pdfTable, rowIndex);
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
                PdfPCell cell = new PdfPCell(new Paragraph(builder.toString(), facetFont));
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

    protected void exportCells(DataTable table, PdfPTable pdfTable, int rowIndex) {
        for (UIColumn col : table.getColumns()) {
            UIComponent component = (UIComponent) col;

            //// Adding RowIndex for custom Export
            if (component.getId().equalsIgnoreCase("subject")) {
                int value = rowIndex;
                PdfPCell cell = new PdfPCell(new Paragraph(value + ""));
                // addColumnAlignments(component, cell);

                if (facetBackground != null) {
                    cell.setBackgroundColor(facetBackground);
                }

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                System.out.println("value is" + value);
                pdfTable.addCell(cell);
            }

            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                if (col.getSelectionMode() != null) {
                    pdfTable.addCell(new Paragraph(col.getSelectionMode(), cellFont));

                    continue;
                }

                addColumnValue(pdfTable, col.getChildren(), cellFont, "data");
            }
        }

        pdfTable.completeRow();
        FacesContext context = null;
        if (table.getRowIndex() == 0) {
            for (UIComponent component : table.getChildren()) {
                if (component instanceof RowExpansion) {
                    RowExpansion rowExpansion = (RowExpansion) component;
                    if (rowExpansion.getChildren() != null) {
                        if (rowExpansion.getChildren().get(0) instanceof DataTable) {
                            DataTable childTable = (DataTable) rowExpansion.getChildren().get(0);
                            childTable.setRowIndex(-1);
                        }

                        if (rowExpansion.getChildren().get(0) instanceof DataList) {
                            DataList childList = (DataList) rowExpansion.getChildren().get(0);
                            childList.setRowIndex(-1);
                        }
                    }
                }
            }
        }

        table.setRowIndex(table.getRowIndex() + 1);
        for (UIComponent component : table.getChildren()) {
            if (component instanceof RowExpansion) {
                RowExpansion rowExpansion = (RowExpansion) component;
                if (rowExpansion.getChildren() != null) {
                    if (rowExpansion.getChildren().get(0) instanceof DataTable) {
                        DataTable childTable = (DataTable) rowExpansion.getChildren().get(0);
                        PdfPTable pdfTableChild = exportPDFTable(context, childTable, false, false, "-", false);
                        PdfPCell cell = new PdfPCell();
                        cell.addElement(pdfTableChild);
                        cell.setColspan(pdfTable.getNumberOfColumns());
                        pdfTable.addCell(cell);
                    }

                    if (rowExpansion.getChildren().get(0) instanceof DataList) {
                        DataList list = (DataList) rowExpansion.getChildren().get(0);
                        PdfPTable pdfTableChild = exportPDFTable(context, list, false, "-");
                        pdfTableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                        PdfPCell cell = new PdfPCell();
                        cell.addElement(pdfTableChild);
                        cell.setColspan(pdfTable.getNumberOfColumns());
                    }
                }
            }

            pdfTable.completeRow();
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
                addColumnValue(pdfTable, col.getChildren(), cellFont, "data");
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
                    cell = new PdfPCell(new Paragraph(col.getHeaderText(), facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }

                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
                else if (col.getFooterText() != null && columnType.name().equalsIgnoreCase("footer")) {
                    cell = new PdfPCell(new Paragraph(col.getFooterText(), facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }

                    pdfTable.addCell(cell);
                }
                else {
                    UIComponent component = (UIComponent) col;

                    // Adding RowIndex for Custom Exporter
                    if (component.getId().equalsIgnoreCase("subject")) {
                        String value = "Index";
                        PdfPCell cellIndex = new PdfPCell(new Paragraph(value));
                        // addColumnAlignments(component, cell);

                        if (facetBackground != null) {
                            cellIndex.setBackgroundColor(facetBackground);
                        }

                        cellIndex.setHorizontalAlignment(Element.ALIGN_CENTER);

                        pdfTable.addCell(cellIndex);
                    }

                    addColumnValue(pdfTable, col.getFacet(columnType.facet()), facetFont, columnType.name());
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
                    cell = new PdfPCell(new Paragraph(col.getHeaderText(), facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }

                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
                else if (col.getFooterText() != null && columnType.name().equalsIgnoreCase("footer")) {
                    cell = new PdfPCell(new Paragraph(col.getFooterText(), facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }

                    pdfTable.addCell(cell);
                }
                else {
                    addColumnValue(pdfTable, col.getFacet(columnType.facet()), facetFont, columnType.name());
                }
            }
        }
    }

    protected void addColumnValue(PdfPTable pdfTable, UIComponent component, Font font, String columnType) {
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
        PdfPCell cell = new PdfPCell(new Paragraph(value, font));
        // addColumnAlignments(component, cell);

        if (facetBackground != null) {
            cell.setBackgroundColor(facetBackground);
        }

        if ("header".equalsIgnoreCase(columnType)) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }

        pdfTable.addCell(cell);
    }

    protected void addColumnValue(PdfPTable pdfTable, List<UIComponent> components, Font font, String columnType) {
        StringBuilder builder = new StringBuilder();

        for (UIComponent component : components) {
            if (component.isRendered()) {
                String value = exportValue(FacesContext.getCurrentInstance(), component);

                if (value != null) {
                    builder.append(value);
                }
            }
        }

        PdfPCell cell = new PdfPCell(new Paragraph(builder.toString(), font));

        // addColumnAlignments(components, cell);
        if ("header".equalsIgnoreCase(columnType)) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }

        pdfTable.addCell(cell);
    }

    protected static void addColumnAlignments(UIComponent component, PdfPCell cell) {
        if (component instanceof HtmlOutputText) {
            HtmlOutputText output = (HtmlOutputText) component;
            if (output.getStyle() != null && output.getStyle().contains("left")) {
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            }

            if (output.getStyle() != null && output.getStyle().contains("right")) {
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }

            if (output.getStyle() != null && output.getStyle().contains("center")) {
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
        }
    }

    protected static void addColumnAlignments(List<UIComponent> components, PdfPCell cell) {
        for (UIComponent component : components) {
            if (component instanceof HtmlOutputText) {
                HtmlOutputText output = (HtmlOutputText) component;
                if (output.getStyle() != null && output.getStyle().contains("left")) {
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                }

                if (output.getStyle() != null && output.getStyle().contains("right")) {
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                }

                if (output.getStyle() != null && output.getStyle().contains("center")) {
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
            }
        }
    }

    @Override
    public void customFormat(String facetBackground, String facetFontSize, String facetFontColor, String facetFontStyle,
                String fontName, String cellFontSize, String cellFontColor, String cellFontStyle,
                String datasetPadding, String orientation) {
        this.facetFontSize = new Float(facetFontSize);
        this.cellFontSize = new Float(cellFontSize);
        this.datasetPadding = Integer.parseInt(datasetPadding);
        this.orientation = orientation;

        if (facetBackground != null) {
            this.facetBackground = Color.decode(facetBackground);
        }

        if (facetFontColor != null) {
            this.facetFontColor = Color.decode(facetFontColor);
        }

        if (cellFontColor != null) {
            this.cellFontColor = Color.decode(cellFontColor);
        }

        if (fontName != null) {
            this.fontName = fontName;
        }

        if ("NORMAL".equalsIgnoreCase(facetFontStyle)) {
            this.facetFontStyle = "" + Font.NORMAL;
        }

        if ("BOLD".equalsIgnoreCase(facetFontStyle)) {
            this.facetFontStyle = "" + Font.BOLD;
        }

        if ("ITALIC".equalsIgnoreCase(facetFontStyle)) {
            this.facetFontStyle = "" + Font.ITALIC;
        }

        if ("NORMAL".equalsIgnoreCase(cellFontStyle)) {
            this.cellFontStyle = "" + Font.NORMAL;
        }

        if ("BOLD".equalsIgnoreCase(cellFontStyle)) {
            this.cellFontStyle = "" + Font.BOLD;
        }

        if ("ITALIC".equalsIgnoreCase(cellFontStyle)) {
            this.cellFontStyle = "" + Font.ITALIC;
        }
    }

    protected void createCustomFonts(String encoding) {
        cellFont = FontFactory.getFont(FontFactory.TIMES, encoding);
        facetFont = FontFactory.getFont(FontFactory.TIMES, encoding, Font.DEFAULTSIZE, Font.BOLD);
        if (facetFontColor != null) {
            facetFont.setColor(facetFontColor);
        }

        if (facetFontSize != null) {
            facetFont.setSize(facetFontSize);
        }

        if (facetFontStyle != null) {
            facetFont.setStyle(facetFontStyle);
        }

        if (cellFontColor != null) {
            cellFont.setColor(cellFontColor);
        }

        if (cellFontSize != null) {
            cellFont.setSize(cellFontSize);
        }

        if (cellFontStyle != null) {
            cellFont.setStyle(cellFontStyle);
        }

        if (fontName != null) {
            cellFont.setFamily(fontName);
            facetFont.setFamily(fontName);
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    protected static void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName)
                throws IOException, DocumentException {
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", Collections.<String, Object> emptyMap());
        OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }
}
