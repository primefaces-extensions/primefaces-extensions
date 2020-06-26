/*
 * Copyright 2011-2020 PrimeFaces Extensions
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

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.List;
import java.util.*;

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

import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datalist.DataList;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.row.Row;
import org.primefaces.component.rowexpansion.RowExpansion;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.component.summaryrow.SummaryRow;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.util.Constants;

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
    private String fontName;
    private Float cellFontSize;
    private Color cellFontColor;
    private String cellFontStyle;
    private int datasetPadding;
    private String orientation;

    @Override
    public void export(final ActionEvent event, final String tableId, final FacesContext context, final String filename, final String tableTitle,
                final boolean pageOnly, final boolean selectionOnly,
                final String encodingType, final MethodExpression preProcessor, final MethodExpression postProcessor, final boolean subTable)
                throws IOException {
        try {
            final Document document = new Document();
            if (orientation.equalsIgnoreCase("Landscape")) {
                document.setPageSize(PageSize.A4.rotate());
            }
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            if (preProcessor != null) {
                preProcessor.invoke(context.getELContext(), new Object[] {document});
            }

            final String tokenString = ExtLangUtils.normalizeSpace(tableId.replace(',', ' '));
            final StringTokenizer st = new StringTokenizer(tokenString, " ");
            while (st.hasMoreElements()) {
                final String tableName = (String) st.nextElement();
                final UIComponent component = SearchExpressionFacade.resolveComponent(context, event.getComponent(), tableName);
                if (component == null) {
                    throw new FacesException("Cannot find component \"" + tableName + "\" in view.");
                }
                if (!(component instanceof DataTable || component instanceof DataList)) {
                    throw new FacesException("Unsupported datasource target:\"" + component.getClass().getName()
                                + "\", exporter must target a PrimeFaces DataTable/DataList.");
                }
                if (!component.isRendered()) {
                    continue;
                }

                if (!document.isOpen()) {
                    document.open();
                }
                if (tableTitle != null && !tableTitle.isEmpty() && !tableId.contains(",")) {

                    final Font tableTitleFont = FontFactory.getFont(FontFactory.TIMES, encodingType, Font.DEFAULTSIZE, Font.BOLD);
                    final Paragraph title = new Paragraph(tableTitle, tableTitleFont);
                    document.add(title);

                    final Paragraph preface = new Paragraph();
                    addEmptyLine(preface, 3);
                    document.add(preface);
                }
                final PdfPTable pdf;
                final DataList list;
                final DataTable table;
                if (component instanceof DataList) {
                    list = (DataList) component;
                    pdf = exportPDFTable(list, pageOnly, encodingType);
                }
                else {
                    table = (DataTable) component;
                    pdf = exportPDFTable(context, table, pageOnly, selectionOnly, encodingType, subTable);
                }

                if (pdf != null) {
                    document.add(pdf);
                }
                // add a couple of blank lines
                final Paragraph preface = new Paragraph();
                addEmptyLine(preface, datasetPadding);
                document.add(preface);
            }

            if (postProcessor != null) {
                postProcessor.invoke(context.getELContext(), new Object[] {document});
            }

            document.close();

            writePDFToResponse(context.getExternalContext(), baos, filename);

        }
        catch (final DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }

    protected PdfPTable exportPDFTable(final FacesContext context, final DataTable table, final boolean pageOnly, final boolean selectionOnly,
                final String encoding, final boolean subTable) {
        if (!"-".equalsIgnoreCase(encoding)) {
            createCustomFonts(encoding);
        }
        final int columnsCount = getColumnsCount(table);
        final PdfPTable pdfTable;
        if (subTable) {
            int subTableCount = table.getRowCount();
            SubTable subtable = table.getSubTable();
            final int subTableColumnsCount = getColumnsCount(subtable);
            pdfTable = new PdfPTable(subTableColumnsCount);

            if (table.getHeader() != null) {
                tableFacet(context, pdfTable, table, subTableColumnsCount, ColumnType.HEADER.facet());
            }

            tableColumnGroup(pdfTable, table, ColumnType.HEADER.facet());

            int i = 0;
            while (subTableCount > 0) {

                subTableCount--;
                table.setRowIndex(i);
                i++;
                subtable = table.getSubTable();

                if (subtable.getHeader() != null) {
                    tableFacet(context, pdfTable, subtable, subTableColumnsCount, ColumnType.HEADER.facet());
                }

                if (hasHeaderColumn(subtable)) {
                    addColumnFacets(subtable, pdfTable, ColumnType.HEADER);
                }

                if (pageOnly) {
                    exportPageOnly(table, pdfTable);
                }
                else if (selectionOnly) {
                    exportSelectionOnly(context, table, pdfTable);
                }
                else {
                    subTableExportAll(subtable, pdfTable);
                }

                if (hasFooterColumn(subtable)) {
                    addColumnFacets(subtable, pdfTable, ColumnType.FOOTER);
                }

                if (subtable.getFooter() != null) {
                    tableFacet(context, pdfTable, subtable, subTableColumnsCount, ColumnType.FOOTER.facet());
                }

                subtable.setRowIndex(-1);
            }

            tableColumnGroup(pdfTable, table, ColumnType.FOOTER.facet());

            if (table.hasFooterColumn()) {
                tableFacet(context, pdfTable, table, subTableColumnsCount, ColumnType.FOOTER.facet());
            }

        }
        else {

            if (columnsCount == 0) {
                return null;
            }

            pdfTable = new PdfPTable(columnsCount);

            if (table.getHeader() != null) {
                tableFacet(context, pdfTable, table, columnsCount, ColumnType.HEADER.facet());
            }

            if (hasHeaderColumn(table)) {
                addColumnFacets(table, pdfTable, ColumnType.HEADER);
            }
            if (pageOnly) {
                exportPageOnly(table, pdfTable);
            }
            else if (selectionOnly) {
                exportSelectionOnly(context, table, pdfTable);
            }
            else {
                exportAll(table, pdfTable);
            }

            if (table.hasFooterColumn()) {
                addColumnFacets(table, pdfTable, ColumnType.FOOTER);
            }
            if (table.getFooter() != null) {

                tableFacet(context, pdfTable, table, columnsCount, ColumnType.FOOTER.facet());
            }

            table.setRowIndex(-1);

        }
        return pdfTable;

    }

    protected PdfPTable exportPDFTable(final DataList list, final boolean pageOnly, final String encoding) {

        if (!"-".equalsIgnoreCase(encoding)) {
            createCustomFonts(encoding);
        }
        final int first = list.getFirst();
        final int rowCount = list.getRowCount();
        final int rowsToExport = first + list.getRows();

        final PdfPTable pdfTable = new PdfPTable(1);
        if (list.getHeader() != null) {
            final String value = exportValue(FacesContext.getCurrentInstance(), list.getHeader());
            final PdfPCell cell = new PdfPCell(new Paragraph(value, facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }

        final StringBuilder builder = new StringBuilder();
        final String output;

        if (pageOnly) {
            output = exportPageOnly(first, list, rowsToExport, builder);
        }
        else {
            output = exportAll(list, rowCount, builder);
        }

        pdfTable.addCell(new Paragraph(output, cellFont));
        pdfTable.completeRow();

        if (list.getFooter() != null) {
            final String value = exportValue(FacesContext.getCurrentInstance(), list.getFooter());
            final PdfPCell cell = new PdfPCell(new Paragraph(value, facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }

        return pdfTable;
    }

    protected void exportPageOnly(final DataTable table, final PdfPTable pdfTable) {
        final int first = table.getFirst();
        final int rowsToExport = first + table.getRows();

        tableColumnGroup(pdfTable, table, ColumnType.HEADER.facet());

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, pdfTable, rowIndex);
        }

        tableColumnGroup(pdfTable, table, ColumnType.FOOTER.facet());
    }

    protected String exportPageOnly(final int first, final DataList list, final int rowsToExport, final StringBuilder input) {
        String output = Constants.EMPTY_STRING;
        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            output = addColumnValues(list, input);
        }
        return output;

    }

    protected void exportSelectionOnly(final FacesContext context, final DataTable table, final PdfPTable pdfTable) {
        final Object selection = table.getSelection();
        final String var = table.getVar();

        if (selection != null) {
            final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                final int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));
                    exportCells(table, pdfTable);
                }
            }
            else if (Collection.class.isAssignableFrom(selection.getClass())) {
                final Collection<?> collection = (Collection<?>) selection;
                for (final Object o : collection) {
                    requestMap.put(var, o);
                    exportCells(table, pdfTable);
                }
            }
            else {
                requestMap.put(var, selection);
                exportCells(table, pdfTable);
            }
        }
    }

    protected void exportAll(final DataTable table, final PdfPTable pdfTable) {
        final int first = table.getFirst();
        final int rowCount = table.getRowCount();
        final boolean lazy = table.isLazy();

        if (lazy) {
            if (rowCount > 0) {
                table.setFirst(0);
                table.setRows(rowCount);
                table.clearLazyCache();
                table.loadLazyData();
            }

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, pdfTable, rowIndex);
            }

            // restore
            table.setFirst(first);
            table.setRowIndex(-1);
            table.clearLazyCache();
            table.loadLazyData();
        }
        else {
            tableColumnGroup(pdfTable, table, ColumnType.HEADER.facet());
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, pdfTable, rowIndex);
            }
            tableColumnGroup(pdfTable, table, ColumnType.FOOTER.facet());
            // restore
            table.setFirst(first);
        }

    }

    protected void subTableExportAll(final SubTable table, final PdfPTable pdfTable) {
        final int first = table.getFirst();
        final int rowCount = table.getRowCount();

        tableColumnGroup(pdfTable, table, ColumnType.HEADER.facet());

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            subTableExportRow(table, pdfTable, rowIndex);
        }

        tableColumnGroup(pdfTable, table, ColumnType.FOOTER.facet());
        // restore
        table.setFirst(first);
    }

    protected String exportAll(final DataList list, final int rowCount, final StringBuilder input) {
        String output = Constants.EMPTY_STRING;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            list.setRowIndex(rowIndex);
            output = addColumnValues(list, input);
        }

        return output;
    }

    protected void tableFacet(final FacesContext context, final PdfPTable pdfTable, final DataTable table, final int columnCount, final String facetType) {
        final Map<String, UIComponent> map = table.getFacets();
        final UIComponent component = map.get(facetType);
        if (component != null) {
            String headerValue;
            if (component instanceof HtmlCommandButton) {
                headerValue = exportValue(context, component);
            }
            else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            }
            else if (component instanceof UIPanel) {
                final StringBuilder header = new StringBuilder(Constants.EMPTY_STRING);
                for (final UIComponent child : component.getChildren()) {
                    headerValue = exportValue(context, child);
                    header.append(headerValue);
                }
                final PdfPCell cell = new PdfPCell(new Paragraph(header.toString(), facetFont));
                if (facetBackground != null) {
                    cell.setBackgroundColor(facetBackground);
                }
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(columnCount);
                pdfTable.addCell(cell);
                pdfTable.completeRow();
                return;
            }
            else {
                headerValue = exportFacetValue(context, component);
            }
            final PdfPCell cell = new PdfPCell(new Paragraph(headerValue, facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(columnCount);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }
    }

    protected void tableFacet(final FacesContext context, final PdfPTable pdfTable, final SubTable table, final int columnCount, final String facetType) {
        final Map<String, UIComponent> map = table.getFacets();
        final UIComponent component = map.get(facetType);
        if (component != null) {
            String headerValue;
            if (component instanceof HtmlCommandButton) {
                headerValue = exportValue(context, component);
            }
            else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            }
            else if (component instanceof UIPanel) {
                final StringBuilder header = new StringBuilder(Constants.EMPTY_STRING);
                for (final UIComponent child : component.getChildren()) {
                    headerValue = exportValue(context, child);
                    header.append(headerValue);
                }
                final PdfPCell cell = new PdfPCell(new Paragraph(header.toString(), facetFont));
                if (facetBackground != null) {
                    cell.setBackgroundColor(facetBackground);
                }
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(columnCount);
                pdfTable.addCell(cell);
                pdfTable.completeRow();
                return;
            }
            else {
                headerValue = exportFacetValue(context, component);
            }
            final PdfPCell cell = new PdfPCell(new Paragraph(headerValue, facetFont));
            if (facetBackground != null) {
                cell.setBackgroundColor(facetBackground);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(columnCount);
            pdfTable.addCell(cell);
            pdfTable.completeRow();

        }
    }

    protected void tableColumnGroup(final PdfPTable pdfTable, final DataTable table, final String facetType) {
        final ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null) {
            headerComponentList = cg.getChildren();
        }
        if (headerComponentList != null) {
            for (final UIComponent component : headerComponentList) {
                if (component instanceof Row) {
                    final Row row = (Row) component;
                    for (final UIComponent rowComponent : row.getChildren()) {
                        final UIColumn column = (UIColumn) rowComponent;
                        final String value;
                        if (column.isRendered() && column.isExportable()) {
                            if (facetType.equalsIgnoreCase(ColumnType.HEADER.facet())) {
                                value = column.getHeaderText();
                            }
                            else {
                                value = column.getFooterText();
                            }
                            final int rowSpan = column.getRowspan();
                            final int colSpan = column.getColspan();
                            final PdfPCell cell = new PdfPCell(new Paragraph(value, facetFont));
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
                            if (facetType.equalsIgnoreCase(ColumnType.HEADER.facet())) {
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            }
                            pdfTable.addCell(cell);
                        }
                    }
                }

            }
        }
        pdfTable.completeRow();

    }

    protected void tableColumnGroup(final PdfPTable pdfTable, final SubTable table, final String facetType) {
        final ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null) {
            headerComponentList = cg.getChildren();
        }
        if (headerComponentList != null) {
            for (final UIComponent component : headerComponentList) {
                if (component instanceof Row) {
                    final Row row = (Row) component;
                    for (final UIComponent rowComponent : row.getChildren()) {
                        final UIColumn column = (UIColumn) rowComponent;
                        final String value;
                        if (facetType.equalsIgnoreCase(ColumnType.HEADER.facet())) {
                            value = column.getHeaderText();
                        }
                        else {
                            value = column.getFooterText();
                        }
                        final int rowSpan = column.getRowspan();
                        final int colSpan = column.getColspan();
                        final PdfPCell cell = new PdfPCell(new Paragraph(value, facetFont));
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
                        if (facetType.equalsIgnoreCase(ColumnType.HEADER.facet())) {
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        }
                        pdfTable.addCell(cell);

                    }
                }

            }
        }
        pdfTable.completeRow();

    }

    protected void exportRow(final DataTable table, final PdfPTable pdfTable, final int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, pdfTable);
        final SummaryRow sr = table.getSummaryRow();

        if (sr != null && sr.isInView()) {
            for (final UIComponent summaryComponent : sr.getChildren()) {
                final UIColumn column = (UIColumn) summaryComponent;
                final StringBuilder builder = new StringBuilder();

                for (final UIComponent component : column.getChildren()) {
                    if (component.isRendered()) {
                        final String value = exportValue(FacesContext.getCurrentInstance(), component);

                        if (value != null) {
                            builder.append(value);
                        }
                    }
                }
                final int rowSpan = column.getRowspan();
                final int colSpan = column.getColspan();
                final PdfPCell cell = new PdfPCell(new Paragraph(builder.toString(), facetFont));
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

    protected void subTableExportRow(final SubTable table, final PdfPTable pdfTable, final int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        subTableExportCells(table, pdfTable);
    }

    protected void exportCells(final DataTable table, final PdfPTable pdfTable) {
        for (final UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (col.isRendered() && col.isExportable()) {
                if (col.getSelectionMode() != null) {
                    pdfTable.addCell(new Paragraph(col.getSelectionMode(), cellFont));
                    continue;
                }
                addColumnValue(pdfTable, col.getChildren(), cellFont, col);
            }

        }
        pdfTable.completeRow();
        if (table.getRowIndex() == 0) {
            for (final UIComponent component : table.getChildren()) {
                if (component instanceof RowExpansion) {
                    final RowExpansion rowExpansion = (RowExpansion) component;
                    if (rowExpansion.getChildren() != null) {
                        if (rowExpansion.getChildren().get(0) instanceof DataTable) {
                            final DataTable childTable = (DataTable) rowExpansion.getChildren().get(0);
                            childTable.setRowIndex(-1);
                        }
                        if (rowExpansion.getChildren().get(0) instanceof DataList) {
                            final DataList childList = (DataList) rowExpansion.getChildren().get(0);
                            childList.setRowIndex(-1);
                        }
                    }

                }
            }
        }
        for (final UIComponent component : table.getChildren()) {
            if (component instanceof RowExpansion) {
                final RowExpansion rowExpansion = (RowExpansion) component;
                if (rowExpansion.getChildren() != null) {
                    for (int i = 0; i < rowExpansion.getChildren().size(); i++) {
                        final UIComponent child = rowExpansion.getChildren().get(i);
                        if (child instanceof DataTable) {
                            final DataTable childTable = (DataTable) child;
                            final PdfPTable pdfTableChild = exportPDFTable(null, childTable, false, false, "-", false);
                            final PdfPCell cell = new PdfPCell();
                            cell.addElement(pdfTableChild);
                            cell.setColspan(pdfTable.getNumberOfColumns());
                            pdfTable.addCell(cell);
                        }
                        if (child instanceof DataList) {
                            final DataList list = (DataList) child;
                            final PdfPTable pdfTableChild = exportPDFTable(list, false, "-");
                            pdfTableChild.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                            final PdfPCell cell = new PdfPCell();
                            cell.addElement(pdfTableChild);
                            cell.setColspan(pdfTable.getNumberOfColumns());
                        }
                    }
                }

            }
            pdfTable.completeRow();
        }

    }

    protected void subTableExportCells(final SubTable table, final PdfPTable pdfTable) {
        for (final UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (col.isRendered() && col.isExportable()) {
                addColumnValue(pdfTable, col.getChildren(), cellFont, col);
            }
        }
    }

    protected void addColumnFacets(final DataTable table, final PdfPTable pdfTable, final ColumnType columnType) {
        for (final UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }
            final PdfPCell cell;
            if (col.isRendered() && col.isExportable()) {
                if (col.getHeaderText() != null && columnType.name().equalsIgnoreCase(ColumnType.HEADER.facet())) {
                    cell = new PdfPCell(new Paragraph(col.getHeaderText(), facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
                else if (col.getFooterText() != null && columnType.name().equalsIgnoreCase(ColumnType.FOOTER.facet())) {
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

    protected void addColumnFacets(final SubTable table, final PdfPTable pdfTable, final ColumnType columnType) {
        for (final UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }
            final PdfPCell cell;
            if (col.isRendered() && col.isExportable()) {
                if (col.getHeaderText() != null && columnType.name().equalsIgnoreCase(ColumnType.HEADER.facet())) {
                    cell = new PdfPCell(new Paragraph(col.getHeaderText(), facetFont));
                    if (facetBackground != null) {
                        cell.setBackgroundColor(facetBackground);
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
                else if (col.getFooterText() != null && columnType.name().equalsIgnoreCase(ColumnType.FOOTER.facet())) {
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

    protected void addColumnValue(final PdfPTable pdfTable, final UIComponent component, final Font font, final String columnType) {
        final String value = component == null ? Constants.EMPTY_STRING : exportValue(FacesContext.getCurrentInstance(), component);
        PdfPCell cell = new PdfPCell(new Paragraph(value, font));

        if (facetBackground != null) {
            cell.setBackgroundColor(facetBackground);
        }
        if (columnType.equalsIgnoreCase(ColumnType.HEADER.facet())) {
            cell = addFacetAlignments(component, cell);
        }
        else {
            cell = addColumnAlignments(component, cell);
        }
        pdfTable.addCell(cell);
    }

    protected void addColumnValue(final PdfPTable pdfTable, final List<UIComponent> components, final Font font,
                final UIColumn column) {
        final FacesContext context = FacesContext.getCurrentInstance();
        PdfPCell cell;

        if (column.getExportFunction() != null) {
            cell = new PdfPCell(new Paragraph(exportColumnByFunction(context, column), font));
        }
        else {
            final StringBuilder builder = new StringBuilder();
            for (final UIComponent component : components) {
                if (component.isRendered()) {
                    final String value = exportValue(context, component);

                    if (value != null) {
                        builder.append(value);
                    }
                }
            }
            cell = new PdfPCell(new Paragraph(builder.toString(), font));
            for (final UIComponent component : components) {
                cell = addColumnAlignments(component, cell);
            }
        }

        if (cell != null) {
            pdfTable.addCell(cell);
        }
    }

    protected PdfPCell addColumnAlignments(final UIComponent component, final PdfPCell cell) {
        if (component instanceof HtmlOutputText) {
            final HtmlOutputText output = (HtmlOutputText) component;
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
        return cell;
    }

    protected PdfPCell addFacetAlignments(final UIComponent component, final PdfPCell cell) {
        if (component instanceof HtmlOutputText) {
            final HtmlOutputText output = (HtmlOutputText) component;
            if (output.getStyle() != null && output.getStyle().contains("left")) {
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            }
            else if (output.getStyle() != null && output.getStyle().contains("right")) {
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            }
            else {
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
        }
        return cell;
    }

    @Override
    public void customFormat(final String facetBackground, final String facetFontSize, final String facetFontColor, final String facetFontStyle,
                final String fontName, final String cellFontSize,
                final String cellFontColor, final String cellFontStyle, final String datasetPadding, final String orientation) {

        this.facetFontSize = Float.parseFloat(facetFontSize);
        this.cellFontSize = Float.parseFloat(cellFontSize);
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
        if (facetFontStyle.equalsIgnoreCase("NORMAL")) {
            this.facetFontStyle = Constants.EMPTY_STRING + Font.NORMAL;
        }
        if (facetFontStyle.equalsIgnoreCase("BOLD")) {
            this.facetFontStyle = Constants.EMPTY_STRING + Font.BOLD;
        }
        if (facetFontStyle.equalsIgnoreCase("ITALIC")) {
            this.facetFontStyle = Constants.EMPTY_STRING + Font.ITALIC;
        }

        if (cellFontStyle.equalsIgnoreCase("NORMAL")) {
            this.cellFontStyle = Constants.EMPTY_STRING + Font.NORMAL;
        }
        if (cellFontStyle.equalsIgnoreCase("BOLD")) {
            this.cellFontStyle = Constants.EMPTY_STRING + Font.BOLD;
        }
        if (cellFontStyle.equalsIgnoreCase("ITALIC")) {
            this.cellFontStyle = Constants.EMPTY_STRING + Font.ITALIC;
        }

    }

    protected void createCustomFonts(final String encoding) {

        if (fontName != null && FontFactory.getFont(fontName).getBaseFont() != null) {
            cellFont = FontFactory.getFont(fontName, encoding);
            facetFont = FontFactory.getFont(fontName, encoding, Font.DEFAULTSIZE, Font.BOLD);
        }
        else {
            cellFont = FontFactory.getFont(FontFactory.TIMES, encoding);
            facetFont = FontFactory.getFont(FontFactory.TIMES, encoding, Font.DEFAULTSIZE, Font.BOLD);
        }
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
    }

    private static void addEmptyLine(final Paragraph paragraph, final int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    protected void writePDFToResponse(final ExternalContext externalContext, final ByteArrayOutputStream baos, final String fileName)
                throws IOException {
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", Collections.emptyMap());
        final OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }

}
