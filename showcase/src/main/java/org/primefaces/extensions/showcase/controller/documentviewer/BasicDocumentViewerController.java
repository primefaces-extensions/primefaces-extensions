/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.documentviewer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Named
@SessionScoped
public class BasicDocumentViewerController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String downloadFileName = "pfe-rocks.pdf";
    private StreamedContent content;

    public void onPrerender(final ComponentSystemEvent event) {

        try {

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            final Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            for (int i = 0; i < 50; i++) {
                document.add(new Paragraph("All work and no play makes Jack a dull boy"));
            }

            document.close();
            content = DefaultStreamedContent.builder().stream(() -> new ByteArrayInputStream(out.toByteArray()))
                        .contentType("application/pdf").build();
        }
        catch (final Exception e) {

        }
    }

    public StreamedContent getContent() {
        return content;
    }

    public void setContent(final StreamedContent content) {
        this.content = content;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(final String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
}
