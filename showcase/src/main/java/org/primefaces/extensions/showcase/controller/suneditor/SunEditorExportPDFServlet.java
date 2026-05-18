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
package org.primefaces.extensions.showcase.controller.suneditor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * Servlet for exporting SunEditor HTML content to PDF. Called by the SunEditor exportPDF plugin.
 */
@WebServlet("/api/suneditor/export-pdf")
public class SunEditorExportPDFServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");

        String json;
        try (Reader reader = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
            json = sb.toString();
        }

        String fileName = "suneditor.pdf";
        String htmlContent = "";

        // simple JSON parsing to avoid extra dependencies
        String fileNameKey = "\"fileName\":\"";
        int fnStart = json.indexOf(fileNameKey);
        if (fnStart >= 0) {
            int fnValStart = fnStart + fileNameKey.length();
            int fnValEnd = json.indexOf("\"", fnValStart);
            if (fnValEnd > fnValStart) {
                fileName = json.substring(fnValStart, fnValEnd) + ".pdf";
            }
        }

        String htmlKey = "\"htmlContent\":\"";
        int hcStart = json.indexOf(htmlKey);
        if (hcStart >= 0) {
            int hcValStart = hcStart + htmlKey.length();
            int hcValEnd = json.lastIndexOf("\"}");
            if (hcValEnd > hcValStart) {
                htmlContent = json.substring(hcValStart, hcValEnd)
                            .replace("\\\"", "\"")
                            .replace("\\n", "\n")
                            .replace("\\t", "\t")
                            .replace("\\\\", "\\");
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            String xhtml = wrapHTML(htmlContent);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(xhtml);
            renderer.layout();
            renderer.createPDF(os);
            os.flush();

            response.setContentLength(os.size());
            response.getOutputStream().write(os.toByteArray());
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PDF generation failed: " + e.getMessage());
        }
    }

    private String wrapHTML(String content) {
        int styleEnd = content.indexOf("</style>");
        String body = styleEnd >= 0 ? content.substring(styleEnd + "</style>".length()) : content;

        return "<!DOCTYPE html>\n<html>\n<head>\n"
                    + "<meta charset=\"UTF-8\"/>\n"
                    + "<style>\n"
                    + "body { font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #333; }\n"
                    + "h1 { font-size: 2em; margin: 0.67em 0; }\n"
                    + "h2 { font-size: 1.5em; margin: 0.83em 0; }\n"
                    + "h3 { font-size: 1.17em; margin: 1em 0; }\n"
                    + "p { margin: 0 0 10px; }\n"
                    + "strong { font-weight: bold; }\n"
                    + "em { font-style: italic; }\n"
                    + "table { border-collapse: collapse; width: 100%; }\n"
                    + "td, th { border: 1px solid #ddd; padding: 8px; }\n"
                    + "img { max-width: 100%; }\n"
                    + "</style>\n</head>\n<body>\n"
                    + body
                    + "\n</body>\n</html>";
    }
}
