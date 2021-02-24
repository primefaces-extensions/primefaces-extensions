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
package org.primefaces.extensions.component.documentviewer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.model.StreamedContent;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.DynamicContentSrcBuilder;
import org.primefaces.util.LangUtils;
import org.primefaces.util.Lazy;

/**
 * Renderer for the {@link DocumentViewer} component.
 *
 * @author f.strazzullo
 * @author Melloware mellowaredev@gmail.com
 * @since 3.0.0
 */
public class DocumentViewerRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final DocumentViewer documentViewer = (DocumentViewer) component;
        encodeMarkup(context, documentViewer);
    }

    private void encodeMarkup(final FacesContext context, final DocumentViewer documentViewer) throws IOException {
        // Section 508 frame title for assisted technology
        String title = documentViewer.getTitle() != null ? documentViewer.getTitle() : documentViewer.getName();
        title = ExtLangUtils.defaultString(title, "Document Viewer");

        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("iframe", documentViewer);
        writer.writeAttribute("id", documentViewer.getClientId(), null);
        writer.writeAttribute(Attrs.STYLE, documentViewer.getStyle(), null);
        writer.writeAttribute("title", title, null);
        writer.writeAttribute("width", documentViewer.getWidth() != null ? documentViewer.getWidth() : "100%", null);
        writer.writeAttribute("height", documentViewer.getHeight(), null);
        writer.writeAttribute("allowfullscreen", Constants.EMPTY_STRING, null);
        writer.writeAttribute("webkitallowfullscreen", Constants.EMPTY_STRING, null);
        writer.writeAttribute("src", generateSrc(context, documentViewer), null);
        writer.endElement("iframe");
    }

    private String generateSrc(final FacesContext context, final DocumentViewer documentViewer) throws IOException {
        final String imageSrc;
        try {
            imageSrc = URLEncoder.encode(getDocumentSource(context, documentViewer), "UTF-8");
        }
        catch (final Exception ex) {
            throw new IOException(ex);
        }

        return getResourceURL(context) +
                    "&file=" +
                    imageSrc +
                    generateHashString(documentViewer);
    }

    private String generateHashString(final DocumentViewer documentViewer) {
        final List<String> params = new ArrayList<>(4);
        params.add("locale=" + documentViewer.calculateLocale().toString().replace('_', '-'));

        // page: page number. Example: page=2
        if (documentViewer.getPage() != null) {
            params.add("page=" + documentViewer.getPage());
        }

        // zoom level. Example: zoom=200 (accepted formats: '[zoom],[left],[top]',
        // 'page-width', 'page-height', 'page-fit', 'auto')
        if (!LangUtils.isValueBlank(documentViewer.getZoom())) {
            params.add("zoom=" + documentViewer.getZoom());
        }

        // nameddest: go to a named destination
        if (!LangUtils.isValueBlank(documentViewer.getNameddest())) {
            params.add("nameddest=" + documentViewer.getNameddest());
        }

        // pagemode: either "thumbs" or "bookmarks". Example: pagemode=thumbs
        if (!LangUtils.isValueBlank(documentViewer.getPagemode())) {
            params.add("pagemode=" + documentViewer.getPagemode());
        }

        if (!params.isEmpty()) {
            return "#" + String.join("&", params.toArray(new String[params.size()]));
        }
        else {
            return Constants.EMPTY_STRING;
        }
    }

    private String getResourceURL(final FacesContext context) {
        final ResourceHandler handler = context.getApplication().getResourceHandler();
        return context.getExternalContext().encodeResourceURL(
                    handler.createResource("documentviewer/pdfviewer.html", "primefaces-extensions").getRequestPath());
    }

    protected String getDocumentSource(final FacesContext context, final DocumentViewer documentViewer) {
        final String name = documentViewer.getName();

        if (name != null) {
            final String libName = documentViewer.getLibrary();
            final ResourceHandler handler = context.getApplication().getResourceHandler();
            final Resource res = handler.createResource(name, libName);

            if (res == null) {
                return "RES_NOT_FOUND";
            }
            else {
                final String requestPath = res.getRequestPath();
                return context.getExternalContext().encodeResourceURL(requestPath);
            }
        }
        else {
            final Object value = documentViewer.getValue();
            String downloadName = documentViewer.getDownload();
            if (value instanceof StreamedContent) {
                final StreamedContent streamedContent = (StreamedContent) value;
                downloadName = streamedContent.getName();
            }
            return DynamicContentSrcBuilder.build(context,
                        documentViewer,
                        documentViewer.getValueExpression("value"),
                        new Lazy<>(() -> value),
                        documentViewer.isCache(),
                        true) + "&download=" + downloadName;
        }
    }
}
