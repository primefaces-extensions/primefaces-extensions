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
import java.nio.charset.StandardCharsets;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.sheet.Sheet;

public class SheetCSVExporter extends SheetExporter<PrintWriter> {

    public static final char DELIMITER_CHAR = ',';
    public static final char QUOTE_CHAR = '"';
    public static final String QUOTE_STRING = "\"";
    public static final String DOUBLE_QUOTE_STRING = "\"\"";
    public static final String END_OF_LINE_SYMBOLS = "\r\n";

    @Override
    public String getContentType() {
        return "text/csv";
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }

    @Override
    protected PrintWriter createDocument(FacesContext context) throws IOException {
        try {
            String encoding = exportConfiguration.getEncodingType();
            OutputStreamWriter osw = new OutputStreamWriter(exportConfiguration.getOutputStream(), encoding);
            PrintWriter writer = new PrintWriter(osw);
            if (StandardCharsets.UTF_8.name().equals(encoding)) {
                writer.write("\ufeff");
            }
            return writer;
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
    protected void exportHeaders(FacesContext context, Sheet sheet) throws IOException {
        super.exportHeaders(context, sheet);
        endRow(context, sheet);
    }

    @Override
    protected void exportColumnHeader(FacesContext context, Sheet sheet, String header, int columnIndex)
                throws IOException {
        if (columnIndex > 0) {
            document.write(DELIMITER_CHAR);
        }
        document.write(QUOTE_CHAR);
        document.write(escapeQuotes(header));
        document.write(QUOTE_CHAR);
    }

    @Override
    protected void exportCellValue(FacesContext context, Sheet sheet, String value, int columnIndex)
                throws IOException {
        if (columnIndex > 0) {
            document.write(DELIMITER_CHAR);
        }
        document.write(QUOTE_CHAR);
        document.write(escapeQuotes(value));
        document.write(QUOTE_CHAR);
    }

    @Override
    protected void endRow(FacesContext context, Sheet sheet) throws IOException {
        document.write(END_OF_LINE_SYMBOLS);
    }

    protected String escapeQuotes(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(QUOTE_STRING, DOUBLE_QUOTE_STRING);
    }
}
