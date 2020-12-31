/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
