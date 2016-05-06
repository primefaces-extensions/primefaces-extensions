/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

import javax.faces.component.html.HtmlOutputText;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.rowexpansion.RowExpansion;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datalist.DataList;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.util.Constants;
import org.primefaces.expression.SearchExpressionFacade;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIPanel;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.Integer;
import java.lang.String;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Iterator;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
public class ExcelExporter extends Exporter {

    private CellStyle cellStyle;
    private CellStyle facetStyle;
    private CellStyle titleStyle;
    private Color facetBackground;
    private Short facetFontSize;
    private Color facetFontColor;
    private String facetFontStyle;
    private String fontName;
    private Short cellFontSize;
    private Color cellFontColor;
    private String cellFontStyle;
    private String datasetPadding;

    private CellStyle facetStyleLeftAlign;
    private CellStyle facetStyleCenterAlign;
    private CellStyle facetStyleRightAlign;
    private CellStyle cellStyleLeftAlign;
    private CellStyle cellStyleCenterAlign;
    private CellStyle cellStyleRightAlign;
    XSSFWorkbook wb;

    @Override
    public void export(ActionEvent event, String tableId, FacesContext context, String filename, String tableTitle, boolean pageOnly, boolean selectionOnly, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor, boolean subTable) throws IOException {

        wb = new XSSFWorkbook();
        String safeName = WorkbookUtil.createSafeSheetName(filename);
        Sheet sheet = wb.createSheet(safeName);

        cellStyle = wb.createCellStyle();
        facetStyle = wb.createCellStyle();
        titleStyle = wb.createCellStyle();

        facetStyleLeftAlign = wb.createCellStyle();
        facetStyleCenterAlign = wb.createCellStyle();
        facetStyleRightAlign = wb.createCellStyle();
        cellStyleLeftAlign = wb.createCellStyle();
        cellStyleCenterAlign = wb.createCellStyle();
        cellStyleRightAlign = wb.createCellStyle();

        createCustomFonts();

        int maxColumns = 0;
        StringTokenizer st = new StringTokenizer(tableId, ",");
        while (st.hasMoreElements()) {
            String tableName = (String) st.nextElement();
            UIComponent component = SearchExpressionFacade.resolveComponent(context, event.getComponent(), tableName);
            if (component == null) {
                throw new FacesException("Cannot find component \"" + tableName + "\" in view.");
            }
            if (!(component instanceof DataTable || component instanceof DataList)) {
                throw new FacesException("Unsupported datasource target:\"" + component.getClass().getName() + "\", exporter must target a PrimeFaces DataTable/DataList.");
            }

            DataList list = null;
            DataTable table = null;
            int cols = 0;
            if (preProcessor != null) {
                preProcessor.invoke(context.getELContext(), new Object[]{wb});
            }
            if (tableTitle != null && !tableTitle.isEmpty() && !tableId.contains("" + ",")) {
                Row titleRow = sheet.createRow(sheet.getLastRowNum());
                int cellIndex = titleRow.getLastCellNum() == -1 ? 0 : titleRow.getLastCellNum();
                Cell cell = titleRow.createCell(cellIndex);
                cell.setCellValue(new XSSFRichTextString(tableTitle));
                Font titleFont = wb.createFont();
                titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                titleStyle.setFont(titleFont);
                cell.setCellStyle(titleStyle);
                sheet.createRow(sheet.getLastRowNum() + 3);

            }
            if (component instanceof DataList) {
                list = (DataList) component;

                if (list.getHeader() != null) {
                    tableFacet(context, sheet, list, "header");
                }
                if (pageOnly) {
                    exportPageOnly(context, list, sheet);
                } else {
                    exportAll(context, list, sheet);
                }
                cols = list.getRowCount();
            } else {

                table = (DataTable) component;
                int columnsCount = getColumnsCount(table);

                if (table.getHeader() != null && !subTable) {
                    tableFacet(context, sheet, table, columnsCount, "header");

                }
                if (!subTable) {
                    tableColumnGroup(sheet, table, "header");
                }

                addColumnFacets(table, sheet, ColumnType.HEADER);

                if (pageOnly) {
                    exportPageOnly(context, table, sheet);
                } else if (selectionOnly) {
                    exportSelectionOnly(context, table, sheet);
                } else {
                    exportAll(context, table, sheet, subTable);
                }

                if (table.hasFooterColumn() && !subTable) {
                    addColumnFacets(table, sheet, ColumnType.FOOTER);
                }
                if (!subTable) {
                    tableColumnGroup(sheet, table, "footer");
                }
                table.setRowIndex(-1);
                if (postProcessor != null) {
                    postProcessor.invoke(context.getELContext(), new Object[]{wb});
                }
                cols = table.getColumnsCount();

                if (maxColumns < cols) {
                    maxColumns = cols;
                }
            }
            sheet.createRow(sheet.getLastRowNum() + Integer.parseInt(datasetPadding));
        }

        if (!subTable)
            for (int i = 0; i < maxColumns; i++) {
                sheet.autoSizeColumn((short) i);
            }

        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.setPrintGridlines(true);

        writeExcelToResponse(context.getExternalContext(), wb, filename);

    }

    protected void exportAll(FacesContext context, DataTable table, Sheet sheet, Boolean subTable) {

        int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rows = table.getRows();
        boolean lazy = table.isLazy();
        int i = 0;
        if (subTable) {
            int subTableCount = table.getRowCount();
            SubTable subtable = table.getSubTable();
            int subTableColumnsCount = getColumnsCount(subtable);

            if (table.getHeader() != null) {
                tableFacet(context, sheet, table, subTableColumnsCount, "header");
            }

            tableColumnGroup(sheet, table, "header");

            while (subTableCount > 0) {

                subTableCount--;
                table.setRowIndex(i);
                i++;
                if (subtable.getHeader() != null) {
                    tableFacet(context, sheet, subtable, subTableColumnsCount, "header");
                }

                if (hasHeaderColumn(subtable)) {
                    addColumnFacets(subtable, sheet, ColumnType.HEADER);
                }

                exportAll(context, subtable, sheet);

                if (hasFooterColumn(subtable)) {

                    addColumnFacets(subtable, sheet, ColumnType.FOOTER);
                }

                if (subtable.getFooter() != null) {
                    tableFacet(context, sheet, subtable, subTableColumnsCount, "footer");
                }

                subtable.setRowIndex(-1);
                subtable = table.getSubTable();
            }

            tableColumnGroup(sheet, table, "footer");

            if (table.hasFooterColumn()) {
                tableFacet(context, sheet, table, subTableColumnsCount, "footer");
            }
        } else {
            if (lazy) {
                if(rowCount > 0) {
                   table.setFirst(0);
                   table.setRows(rowCount);
                   table.clearLazyCache();
                   table.loadLazyData();
                }
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                   exportRow(table, sheet, rowIndex);
                }

                //restore
                table.setFirst(first);
                table.setRowIndex(-1);
                table.clearLazyCache();
                table.loadLazyData();
            } else {
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    exportRow(table, sheet, rowIndex);
                }
                //restore
                table.setFirst(first);
            }
        }
    }

    protected void exportAll(FacesContext context, SubTable table, Sheet sheet) {
        int first = table.getFirst();
        int rowCount = table.getRowCount();
        int rows = table.getRows();

        tableColumnGroup(sheet, table, "header");
        if (hasHeaderColumn(table)) {
            addColumnFacets(table, sheet, ColumnType.HEADER);
        }
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            exportRow(table, sheet, rowIndex);
        }
        if (hasFooterColumn(table)) {
            addColumnFacets(table, sheet, ColumnType.FOOTER);
        }
        tableColumnGroup(sheet, table, "footer");

    }

    protected void exportAll(FacesContext context, DataList list, Sheet sheet) {
        int first = list.getFirst();
        int rowCount = list.getRowCount();
        int rows = list.getRows();
        boolean lazy = false;

        if (lazy) {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                if (rowIndex % rows == 0) {
                    list.setFirst(rowIndex);
                    // table.loadLazyData();
                }

                exportRow(list, sheet, rowIndex);
            }

            //restore
            list.setFirst(first);
            // table.loadLazyData();
        } else {

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(list, sheet, rowIndex);
            }
            //restore
            list.setFirst(first);
        }

    }

    protected void exportPageOnly(FacesContext context, DataTable table, Sheet sheet) {
        int first = table.getFirst();
        int rowsToExport = first + table.getRows();

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, sheet, rowIndex);
        }
    }

    protected void exportPageOnly(FacesContext context, DataList list, Sheet sheet) {
        int first = list.getFirst();
        int rowsToExport = first + list.getRows();

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(list, sheet, rowIndex);
        }
    }

    protected void exportSelectionOnly(FacesContext context, DataTable table, Sheet sheet) {
        Object selection = table.getSelection();
        String var = table.getVar();

        if (selection != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));
                    exportCells(table, sheet);
                }
            }
            else if ( List.class.isAssignableFrom(selection.getClass()) ){
                 List<?> list = (List<?>) selection;
                 for(Iterator<? extends Object> it = list.iterator(); it.hasNext();) {
                 requestMap.put(var,it.next());
                 exportCells(table, sheet);
               }
             }
            else {
                requestMap.put(var, selection);
                exportCells(table, sheet);
            }
        }
    }

    protected void tableFacet(FacesContext context, Sheet sheet, DataTable table, int columnCount, String facetType) {
        Map<String, UIComponent> map = table.getFacets();
        UIComponent component = map.get(facetType);
        if (component != null) {
            String headerValue = null;
            if (component instanceof HtmlCommandButton) {
                headerValue = exportValue(context, component);
            } else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            } else if (component instanceof UIPanel) {
                 String header="";
                     for(UIComponent child:component.getChildren())  {
                            headerValue = exportValue(context, child);
                            header=header+headerValue;
                         }
                headerValue = header;
          }
            else {
                headerValue = exportFacetValue(context, component);
            }

            int sheetRowIndex = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(sheetRowIndex);
            Cell cell = row.createCell((short) 0);
            cell.setCellValue(headerValue);
            cell.setCellStyle(facetStyle);

            sheet.addMergedRegion(new CellRangeAddress(
                    sheetRowIndex, //first row (0-based)
                    sheetRowIndex, //last row  (0-based)
                    0, //first column (0-based)
                    columnCount - 1 //last column  (0-based)
            ));

        }
    }

    protected void tableFacet(FacesContext context, Sheet sheet, SubTable table, int columnCount, String facetType) {
        Map<String, UIComponent> map = table.getFacets();
        UIComponent component = map.get(facetType);
        if (component != null) {
            String headerValue = null;
            if (component instanceof HtmlCommandButton) {
                headerValue = exportValue(context, component);
            } else if (component instanceof HtmlCommandLink) {
                headerValue = exportValue(context, component);
            } else if (component instanceof UIPanel) {
            String header="";
            for(UIComponent child:component.getChildren())  {
                headerValue = exportValue(context, child);
                header=header+headerValue;
            }
            headerValue = header;
        } else {
                headerValue = exportFacetValue(context, component);
            }

            int sheetRowIndex = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(sheetRowIndex);
            Cell cell = row.createCell((short) 0);
            cell.setCellValue(headerValue);
            cell.setCellStyle(facetStyle);

            sheet.addMergedRegion(new CellRangeAddress(
                    sheetRowIndex, //first row (0-based)
                    sheetRowIndex, //last row  (0-based)
                    0, //first column (0-based)
                    columnCount - 1  //last column  (0-based)
            ));


        }
    }

    protected void tableFacet(FacesContext context, Sheet sheet, DataList list, String facetType) {
        Map<String, UIComponent> map = list.getFacets();
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

            int sheetRowIndex = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(sheetRowIndex);
            Cell cell = row.createCell((short) 0);
            cell.setCellValue(headerValue);
            cell.setCellStyle(facetStyle);

            sheet.addMergedRegion(new CellRangeAddress(
                    sheetRowIndex, //first row (0-based)
                    sheetRowIndex, //last row  (0-based)
                    0, //first column (0-based)
                    1  //last column  (0-based)
            ));


        }
    }

    protected void tableColumnGroup(Sheet sheet, DataTable table, String facetType) {
        ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null) {
            headerComponentList = cg.getChildren();
        }
        if (headerComponentList != null) {
            for (UIComponent component : headerComponentList) {
                if (component instanceof org.primefaces.component.row.Row) {
                    org.primefaces.component.row.Row row = (org.primefaces.component.row.Row) component;
                    int sheetRowIndex = sheet.getLastRowNum() + 1;
                    Row xlRow = sheet.createRow(sheetRowIndex);
                    int i = 0;
                    for (UIComponent rowComponent : row.getChildren()) {
                        UIColumn column = (UIColumn) rowComponent;
                        String value = null;
                        if (facetType.equalsIgnoreCase("header")) {
                            value = column.getHeaderText();
                        } else {
                            value = column.getFooterText();
                        }
                        int rowSpan = column.getRowspan();
                        int colSpan = column.getColspan();

                        Cell cell = xlRow.getCell(i);

                        if (rowSpan > 1 || colSpan > 1) {
                            if (rowSpan > 1) {
                                cell = xlRow.createCell((short) i);
                                Boolean rowSpanFlag = false;
                                for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                                    CellRangeAddress merged = sheet.getMergedRegion(j);
                                    if (merged.isInRange(sheetRowIndex, i)) {
                                        rowSpanFlag = true;
                                    }

                                }
                                if (!rowSpanFlag) {
                                    cell.setCellValue(value);
                                    cell.setCellStyle(facetStyle);
                                    sheet.addMergedRegion(new CellRangeAddress(
                                            sheetRowIndex, //first row (0-based)
                                            sheetRowIndex + (rowSpan - 1), //last row  (0-based)
                                            i, //first column (0-based)
                                            i  //last column  (0-based)
                                    ));
                                }
                            }
                            if (colSpan > 1) {
                                cell = xlRow.createCell((short) i);

                                for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                                    CellRangeAddress merged = sheet.getMergedRegion(j);
                                    if (merged.isInRange(sheetRowIndex, i)) {
                                        cell = xlRow.createCell((short) ++i);
                                    }
                                }
                                cell.setCellValue(value);
                                cell.setCellStyle(facetStyle);
                                sheet.addMergedRegion(new CellRangeAddress(
                                        sheetRowIndex, //first row (0-based)
                                        sheetRowIndex, //last row  (0-based)
                                        i, //first column (0-based)
                                        i + (colSpan - 1)  //last column  (0-based)
                                ));
                                i = i + colSpan - 1;
                            }
                        } else {
                            cell = xlRow.createCell((short) i);
                            for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                                CellRangeAddress merged = sheet.getMergedRegion(j);
                                if (merged.isInRange(sheetRowIndex, i)) {
                                    cell = xlRow.createCell((short) ++i);
                                }
                            }
                            cell.setCellValue(value);
                            cell.setCellStyle(facetStyle);
                        }

                        i++;
                    }
                }

            }

        }
    }

    protected void tableColumnGroup(Sheet sheet, SubTable table, String facetType) {
        ColumnGroup cg = table.getColumnGroup(facetType);
        List<UIComponent> headerComponentList = null;
        if (cg != null) {
            headerComponentList = cg.getChildren();
        }
        if (headerComponentList != null) {
            for (UIComponent component : headerComponentList) {
                if (component instanceof org.primefaces.component.row.Row) {
                    org.primefaces.component.row.Row row = (org.primefaces.component.row.Row) component;
                    int sheetRowIndex = sheet.getPhysicalNumberOfRows() > 0 ? sheet.getLastRowNum() + 1 : 0;
                    Row xlRow = sheet.createRow(sheetRowIndex);
                    int i = 0;
                    for (UIComponent rowComponent : row.getChildren()) {
                        UIColumn column = (UIColumn) rowComponent;
                        String value = null;
                        if (facetType.equalsIgnoreCase("header")) {
                            value = column.getHeaderText();
                        } else {
                            value = column.getFooterText();
                        }
                        int rowSpan = column.getRowspan();
                        int colSpan = column.getColspan();

                        Cell cell = xlRow.getCell(i);

                        if (rowSpan > 1 || colSpan > 1) {

                            if (rowSpan > 1) {
                                cell = xlRow.createCell((short) i);
                                Boolean rowSpanFlag = false;
                                for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                                    CellRangeAddress merged = sheet.getMergedRegion(j);
                                    if (merged.isInRange(sheetRowIndex, i)) {
                                        rowSpanFlag = true;
                                    }

                                }
                                if (!rowSpanFlag) {
                                    cell.setCellStyle(cellStyle);
                                    cell.setCellValue(value);
                                    sheet.addMergedRegion(new CellRangeAddress(
                                            sheetRowIndex, //first row (0-based)
                                            sheetRowIndex + rowSpan - 1, //last row  (0-based)
                                            i, //first column (0-based)
                                            i  //last column  (0-based)
                                    ));
                                }
                            }
                            if (colSpan > 1) {
                                cell = xlRow.createCell((short) i);
                                for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                                    CellRangeAddress merged = sheet.getMergedRegion(j);
                                    if (merged.isInRange(sheetRowIndex, i)) {
                                        cell = xlRow.createCell((short) ++i);
                                    }
                                }
                                cell.setCellStyle(cellStyle);
                                cell.setCellValue(value);
                                sheet.addMergedRegion(new CellRangeAddress(
                                        sheetRowIndex, //first row (0-based)
                                        sheetRowIndex, //last row  (0-based)
                                        i, //first column (0-based)
                                        i + colSpan - 1  //last column  (0-based)
                                ));
                                i = i + colSpan - 1;
                            }
                        } else {
                            cell = xlRow.createCell((short) i);
                            for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                                CellRangeAddress merged = sheet.getMergedRegion(j);
                                if (merged.isInRange(sheetRowIndex, i)) {
                                    cell = xlRow.createCell((short) ++i);
                                }
                            }
                            cell.setCellValue(value);
                            cell.setCellStyle(facetStyle);

                        }
                        i++;
                    }
                }


            }
        }

    }

    protected void exportRow(DataTable table, Sheet sheet, int rowIndex) {
        table.setRowIndex(rowIndex);
        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, sheet);
    }

    protected void exportRow(SubTable table, Sheet sheet, int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, sheet);
    }

    protected void exportRow(DataList list, Sheet sheet, int rowIndex) {
        list.setRowIndex(rowIndex);

        if (!list.isRowAvailable()) {
            return;
        }

        exportCells(list, sheet);
    }

    protected void exportCells(DataTable table, Sheet sheet) {
        int sheetRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(sheetRowIndex);

        facetStyleLeftAlign.setAlignment((short)CellStyle.ALIGN_LEFT);
        facetStyleCenterAlign.setAlignment((short)CellStyle.ALIGN_CENTER);
        facetStyleRightAlign.setAlignment((short)CellStyle.ALIGN_RIGHT);
        cellStyleLeftAlign.setAlignment((short)CellStyle.ALIGN_LEFT);
        cellStyleCenterAlign.setAlignment((short)CellStyle.ALIGN_CENTER);
        cellStyleRightAlign.setAlignment((short)CellStyle.ALIGN_RIGHT);

        for (UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (col.isRendered() && col.isExportable()) {
                addColumnValue(row, col.getChildren(), "content");
            }
        }
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
        for (UIComponent component : table.getChildren()) {
            if (component instanceof RowExpansion) {
                RowExpansion rowExpansion = (RowExpansion) component;
                if (rowExpansion.getChildren() != null) {
                   if(rowExpansion.getChildren().get(0) instanceof DataList) {
                    DataList list = (DataList) rowExpansion.getChildren().get(0);
                    if (list.getHeader() != null) {
                        tableFacet(context, sheet, list, "header");
                    }
                    exportAll(context, list, sheet);
                    }
                   if(rowExpansion.getChildren().get(0) instanceof DataTable) {
                       DataTable childTable = (DataTable) rowExpansion.getChildren().get(0);
                    int columnsCount = getColumnsCount(childTable);

                    if (childTable.getHeader() != null) {
                        tableFacet(context, sheet, childTable, columnsCount, "header");

                    }
                    tableColumnGroup(sheet, childTable, "header");

                    addColumnFacets(childTable, sheet, ColumnType.HEADER);

                    exportAll(context, childTable, sheet, false);

                    if (childTable.hasFooterColumn()) {
                        addColumnFacets(childTable, sheet, ColumnType.FOOTER);
                    }
                    tableColumnGroup(sheet, childTable, "footer");
                    childTable.setRowIndex(-1);
                }

            }
        }
       }
    }

    protected void exportCells(SubTable table, Sheet sheet) {
        int sheetRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(sheetRowIndex);

        facetStyleLeftAlign.setAlignment((short)CellStyle.ALIGN_LEFT);
        facetStyleCenterAlign.setAlignment((short)CellStyle.ALIGN_CENTER);
        facetStyleRightAlign.setAlignment((short)CellStyle.ALIGN_RIGHT);
        cellStyleLeftAlign.setAlignment((short)CellStyle.ALIGN_LEFT);
        cellStyleCenterAlign.setAlignment((short)CellStyle.ALIGN_CENTER);
        cellStyleRightAlign.setAlignment((short)CellStyle.ALIGN_RIGHT);

        for (UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (col.isRendered() && col.isExportable()) {
                addColumnValue(row, col.getChildren(), "content");
            }
        }
    }

    protected void exportCells(DataList list, Sheet sheet) {
        int sheetRowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(sheetRowIndex);

        facetStyleLeftAlign.setAlignment((short)CellStyle.ALIGN_LEFT);
        facetStyleCenterAlign.setAlignment((short)CellStyle.ALIGN_CENTER);
        facetStyleRightAlign.setAlignment((short)CellStyle.ALIGN_RIGHT);
        cellStyleLeftAlign.setAlignment((short)CellStyle.ALIGN_LEFT);
        cellStyleCenterAlign.setAlignment((short)CellStyle.ALIGN_CENTER);
        cellStyleRightAlign.setAlignment((short)CellStyle.ALIGN_RIGHT);

        for (UIComponent component : list.getChildren()) {
            if (component instanceof Column) {
                UIColumn column = (UIColumn) component;
                for (UIComponent childComponent : column.getChildren()) {
                    int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
                    Cell cell = row.createCell(cellIndex);
                    if (component.isRendered()) {
                        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), childComponent);
                        cell.setCellValue(new XSSFRichTextString(value));
                        cell.setCellStyle(cellStyle);
                    }
                }

            } else {
                int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
                Cell cell = row.createCell(cellIndex);
                if (component.isRendered()) {
                    String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
                    cell.setCellValue(new XSSFRichTextString(value));
                    cell.setCellStyle(cellStyle);
                }
            }
        }

    }

    protected void addColumnFacets(DataTable table, Sheet sheet, ColumnType columnType) {

        int sheetRowIndex = sheet.getLastRowNum() + 1;
        Row rowHeader = sheet.createRow(sheetRowIndex);

        for (UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (col.isRendered() && col.isExportable()) {
                addColumnValue(rowHeader, col.getFacet(columnType.facet()), "facet");
            }
        }

    }

    protected void addColumnFacets(SubTable table, Sheet sheet, ColumnType columnType) {

        int sheetRowIndex = sheet.getPhysicalNumberOfRows() > 0 ? sheet.getLastRowNum() + 1 : 0;
        Row rowHeader = sheet.createRow(sheetRowIndex);

        for (UIColumn col : table.getColumns()) {

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (col.isRendered() && col.isExportable()) {
                addColumnValue(rowHeader, col.getFacet(columnType.facet()), "facet");
            }
        }
    }

    protected void addColumnValue(Row row, UIComponent component, String type) {
        int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
        Cell cell = row.createCell(cellIndex);
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);
        cell.setCellValue(new XSSFRichTextString(value));
        if (type.equalsIgnoreCase("facet")) {
            addFacetAlignments(component,cell);
        } else {
            addColumnAlignments(component, cell);
        }

    }

    protected void addColumnValue(Row row, List<UIComponent> components, String type) {
        int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
        Cell cell = row.createCell(cellIndex);
        StringBuilder builder = new StringBuilder();
        FacesContext context = FacesContext.getCurrentInstance();

        for (UIComponent component : components) {
            if (component.isRendered()) {
                String value = exportValue(context, component);

                if (value != null) {
                    builder.append(value);
                }
            }
        }

        cell.setCellValue(new XSSFRichTextString(builder.toString()));

        if (type.equalsIgnoreCase("facet")) {
            for (UIComponent component : components) {
            addFacetAlignments(component,cell);
            }
        } else {
            for (UIComponent component : components) {
             addColumnAlignments(component, cell);
            }
        }

    }

    protected void addColumnAlignments(UIComponent component, Cell cell) {
        if (component instanceof HtmlOutputText) {
            HtmlOutputText output = (HtmlOutputText) component;
            if (output.getStyle() != null && output.getStyle().contains("left")) {
                cell.setCellStyle(cellStyleLeftAlign);
            }
            if (output.getStyle() != null && output.getStyle().contains("right")) {
                cell.setCellStyle(cellStyleRightAlign);
            }
            if (output.getStyle() != null && output.getStyle().contains("center")) {
                cell.setCellStyle(cellStyleCenterAlign);
            }
        }
    }

    protected void addFacetAlignments(UIComponent component, Cell cell) {
        if (component instanceof HtmlOutputText) {
            HtmlOutputText output = (HtmlOutputText) component;
            if (output.getStyle() != null && output.getStyle().contains("left")) {
                cell.setCellStyle(facetStyleLeftAlign);
            }
            else if (output.getStyle() != null && output.getStyle().contains("right")) {
                cell.setCellStyle(facetStyleRightAlign);
            }
            else  {
                cell.setCellStyle(facetStyleCenterAlign);
            }
        }
    }

    public void customFormat(String facetBackground, String facetFontSize, String facetFontColor, String facetFontStyle, String fontName, String cellFontSize, String cellFontColor, String cellFontStyle, String datasetPadding, String orientation) {
        if (facetBackground != null) {
            this.facetBackground = Color.decode(facetBackground);
        }
        if (facetFontColor != null) {
            this.facetFontColor = Color.decode(facetFontColor);
        }
        if (fontName != null) {
            this.fontName = fontName;
        }
        if (cellFontColor != null) {
            this.cellFontColor = Color.decode(cellFontColor);
        }

        this.facetFontSize = new Short(facetFontSize);
        this.facetFontStyle = facetFontStyle;
        this.cellFontSize = new Short(cellFontSize);
        this.cellFontStyle = cellFontStyle;
        this.datasetPadding = datasetPadding;

    }

    protected void createCustomFonts() {

        Font facetFont = wb.createFont();
        Font cellFont = wb.createFont();

        if (cellFontColor != null) {
            XSSFColor cellColor = new XSSFColor(cellFontColor);
            ((XSSFFont) cellFont).setColor(cellColor);
        }
        if (cellFontSize != null) {
            cellFont.setFontHeightInPoints((short) cellFontSize);
        }

        if (cellFontStyle.equalsIgnoreCase("BOLD")) {
            cellFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        if (cellFontStyle.equalsIgnoreCase("ITALIC")) {
            cellFont.setItalic(true);
        }

        if (facetFontStyle.equalsIgnoreCase("BOLD")) {
            facetFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        }
        if (facetFontStyle.equalsIgnoreCase("ITALIC")) {
            facetFont.setItalic(true);
        }

        if (fontName != null) {
            cellFont.setFontName(fontName);
            facetFont.setFontName(fontName);
        }

        if (facetBackground != null) {
            XSSFColor backgroundColor = new XSSFColor(facetBackground);
            ((XSSFCellStyle) facetStyle).setFillForegroundColor(backgroundColor);
            ((XSSFCellStyle) facetStyleLeftAlign).setFillForegroundColor(backgroundColor);
            ((XSSFCellStyle) facetStyleCenterAlign).setFillForegroundColor(backgroundColor);
            ((XSSFCellStyle) facetStyleRightAlign).setFillForegroundColor(backgroundColor);
            facetStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            facetStyleLeftAlign.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            facetStyleCenterAlign.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            facetStyleRightAlign.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

        }

        if (facetFontColor != null) {
            XSSFColor facetColor = new XSSFColor(facetFontColor);
            ((XSSFFont) facetFont).setColor(facetColor);

        }
        if (facetFontSize != null) {
            facetFont.setFontHeightInPoints((short) facetFontSize);
        }

        cellStyle.setFont(cellFont);
        cellStyleLeftAlign.setFont(cellFont);
        cellStyleCenterAlign.setFont(cellFont);
        cellStyleRightAlign.setFont(cellFont);

        facetStyle.setFont(facetFont);
        facetStyleLeftAlign.setFont(facetFont);
        facetStyleCenterAlign.setFont(facetFont);
        facetStyleRightAlign.setFont(facetFont);
        //facetStyle.setAlignment(CellStyle.ALIGN_CENTER);

    }

    protected void writeExcelToResponse(ExternalContext externalContext, org.apache.poi.ss.usermodel.Workbook generatedExcel, String filename) throws IOException {

        externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", Collections.<String, Object>emptyMap());

        OutputStream out = externalContext.getResponseOutputStream();
        generatedExcel.write(out);
        externalContext.responseFlushBuffer();
    }
}
