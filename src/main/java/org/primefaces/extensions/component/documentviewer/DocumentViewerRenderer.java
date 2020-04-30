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

import org.apache.commons.lang3.StringUtils;
import org.primefaces.application.resource.DynamicContentType;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.DynamicContentSrcBuilder;
import org.primefaces.util.LangUtils;

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
        title = StringUtils.defaultString(title, "Document Viewer");

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
            return "#" + StringUtils.join(params, "&");
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
            return DynamicContentSrcBuilder.build(context, documentViewer.getValue(), documentViewer,
                        documentViewer.isCache(), DynamicContentType.STREAMED_CONTENT, true) + "&download="
                        + documentViewer.getDownload();
        }
    }
}
