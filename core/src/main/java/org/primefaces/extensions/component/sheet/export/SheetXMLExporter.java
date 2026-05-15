/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.sheet.Sheet;
import org.primefaces.util.EscapeUtils;

public class SheetXMLExporter extends SheetExporter<PrintWriter> {

    private int rowIndex;

    @Override
    public String getContentType() {
        return "application/xml";
    }

    @Override
    public String getFileExtension() {
        return ".xml";
    }

    @Override
    protected PrintWriter createDocument(FacesContext context) throws IOException {
        try {
            String encoding = exportConfiguration.getEncodingType();
            return new PrintWriter(new OutputStreamWriter(exportConfiguration.getOutputStream(), encoding));
        }
        catch (UnsupportedEncodingException e) {
            throw new FacesException(e);
        }
    }

    @Override
    protected void closeDocument(FacesContext context) throws IOException {
        if (document != null) {
            document.flush();
        }
    }

    @Override
    protected void exportSheet(FacesContext context, Sheet sheet, int index) throws IOException {
        document.write("<?xml version=\"1.0\" encoding=\"" + exportConfiguration.getEncodingType() + "\"?>");
        document.write("<sheet>");

        super.exportSheet(context, sheet, index);

        document.write("</sheet>");
    }

    @Override
    protected void exportHeaders(FacesContext context, Sheet sheet) throws IOException {
        if (!exportConfiguration.isExportHeader()) {
            return;
        }
        document.write("<headers>");
        super.exportHeaders(context, sheet);
        document.write("</headers>");
    }

    @Override
    protected void exportAllData(FacesContext context, Sheet sheet) throws IOException {
        document.write("<rows>");
        rowIndex = 0;
        super.exportAllData(context, sheet);
        document.write("</rows>");
    }

    @Override
    protected void exportColumnHeader(FacesContext context, Sheet sheet, String header, int columnIndex)
                throws IOException {
        document.write("<column>");
        document.write(EscapeUtils.forXml(header));
        document.write("</column>");
    }

    @Override
    protected void exportCellValue(FacesContext context, Sheet sheet, String value, int columnIndex)
                throws IOException {
        if (columnIndex == 0) {
            document.write("<row index=\"" + rowIndex + "\">");
        }
        document.write("<cell index=\"" + columnIndex + "\">");
        document.write(EscapeUtils.forXml(value));
        document.write("</cell>");
    }

    @Override
    protected void endRow(FacesContext context, Sheet sheet) throws IOException {
        document.write("</row>");
        rowIndex++;
    }
}
