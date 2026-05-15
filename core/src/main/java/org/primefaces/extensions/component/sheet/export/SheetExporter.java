/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.sheet.export;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.faces.context.FacesContext;

import org.primefaces.component.export.ExportConfiguration;
import org.primefaces.component.export.Exporter;
import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.extensions.component.sheet.SheetColumn;
import org.primefaces.util.Constants;

public abstract class SheetExporter<D> implements Exporter<Sheet> {

    protected D document;
    protected ExportConfiguration exportConfiguration;

    @Override
    public void export(FacesContext context, List<Sheet> sheets, ExportConfiguration config) throws IOException {
        this.exportConfiguration = config;
        try {
            document = createDocument(context);
            if (exportConfiguration.getPreProcessor() != null) {
                exportConfiguration.getPreProcessor().invoke(context.getELContext(), new Object[] {document});
            }
            for (int i = 0; i < sheets.size(); i++) {
                exportSheet(context, sheets.get(i), i);
            }
            if (exportConfiguration.getPostProcessor() != null) {
                exportConfiguration.getPostProcessor().invoke(context.getELContext(), new Object[] {document});
            }
        }
        finally {
            closeDocument(context);
        }
    }

    protected void exportSheet(FacesContext context, Sheet sheet, int index) throws IOException {
        if (exportConfiguration.getOnTableRender() != null) {
            exportConfiguration.getOnTableRender().invoke(context.getELContext(), new Object[] {document});
        }

        if (exportConfiguration.isExportHeader()) {
            exportHeaders(context, sheet);
        }

        exportAllData(context, sheet);
    }

    protected void exportHeaders(FacesContext context, Sheet sheet) throws IOException {
        int renderCol = 0;
        for (int col = 0; col < sheet.getColumns().size(); col++) {
            SheetColumn column = sheet.getColumns().get(col);
            if (!column.isRendered()) {
                continue;
            }
            if (exportConfiguration.isVisibleOnly() && !column.isVisible()) {
                continue;
            }
            String headerText = column.getHeaderText();
            if (headerText == null) {
                headerText = Constants.EMPTY_STRING;
            }
            exportColumnHeader(context, sheet, headerText, renderCol);
            renderCol++;
        }
    }

    protected void exportAllData(FacesContext context, Sheet sheet) throws IOException {
        List<?> values = sheet.getSortedValues();
        if (values == null || values.isEmpty()) {
            return;
        }

        String var = sheet.getVar();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        for (Object value : values) {
            requestMap.put(var, value);
            String rowKey = sheet.getRowKeyValueAsString(context);
            int renderCol = 0;
            for (int col = 0; col < sheet.getColumns().size(); col++) {
                SheetColumn column = sheet.getColumns().get(col);
                if (!column.isRendered()) {
                    continue;
                }
                if (exportConfiguration.isVisibleOnly() && !column.isVisible()) {
                    continue;
                }
                String cellValue = sheet.getRenderValueForCell(context, rowKey, col);
                if (cellValue == null) {
                    cellValue = Constants.EMPTY_STRING;
                }
                exportCellValue(context, sheet, cellValue, renderCol);
                renderCol++;
            }

            if (exportConfiguration.getOnRowExport() != null) {
                exportConfiguration.getOnRowExport().invoke(context.getELContext(), new Object[] {document});
            }

            endRow(context, sheet);
        }
        requestMap.remove(var);
    }

    protected abstract D createDocument(FacesContext context) throws IOException;

    protected abstract void closeDocument(FacesContext context) throws IOException;

    protected abstract void exportColumnHeader(FacesContext context, Sheet sheet, String header, int columnIndex)
                throws IOException;

    protected abstract void exportCellValue(FacesContext context, Sheet sheet, String value, int columnIndex)
                throws IOException;

    protected abstract void endRow(FacesContext context, Sheet sheet) throws IOException;
}
