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
package org.primefaces.extensions.component.documentviewer;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.context.PrimeRequestContext;
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
@FacesRenderer(rendererType = DocumentViewer.DEFAULT_RENDERER, componentFamily = DocumentViewer.COMPONENT_FAMILY)
public class DocumentViewerRenderer extends CoreRenderer<DocumentViewer> {

    @Override
    public void encodeEnd(final FacesContext context, final DocumentViewer component) throws IOException {
        if (PrimeRequestContext.getCurrentInstance(context).isHideResourceVersion()) {
            logDevelopmentWarning(context, this, "DocumentViewer requires a resource version to work properly and '"
                        + Constants.ContextParams.HIDE_RESOURCE_VERSION + "' is currently configured.");
        }

        encodeMarkup(context, component);
    }

    private void encodeMarkup(final FacesContext context, final DocumentViewer component) throws IOException {
        String title = component.getTitle() != null ? component.getTitle() : component.getName();
        title = ExtLangUtils.defaultString(title, "Document Viewer");

        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("iframe", component);
        writer.writeAttribute("id", component.getClientId(), null);
        writer.writeAttribute(Attrs.STYLE, component.getStyle(), null);
        writer.writeAttribute("title", title, null);
        writer.writeAttribute("width", component.getWidth(), null);
        writer.writeAttribute("height", component.getHeight(), null);
        writer.writeAttribute("allowfullscreen", Constants.EMPTY_STRING, null);
        writer.writeAttribute("webkitallowfullscreen", Constants.EMPTY_STRING, null);
        writer.writeAttribute("src", generateSrc(context, component), null);
        writer.endElement("iframe");
    }

    private String generateSrc(final FacesContext context, final DocumentViewer component) throws IOException {
        final String imageSrc;
        try {
            imageSrc = URLEncoder.encode(getDocumentSource(context, component), StandardCharsets.UTF_8);
        }
        catch (final Exception ex) {
            throw new IOException(ex);
        }

        return getResourceURL(context) +
                    "&file=" +
                    imageSrc +
                    generateHashString(component);
    }

    private String generateHashString(final DocumentViewer component) {
        final List<String> params = new ArrayList<>(4);
        params.add("locale=" + component.calculateLocale().toString().replace('_', '-'));

        if (component.getPage() != null) {
            params.add("page=" + component.getPage());
        }

        if (LangUtils.isNotBlank(component.getZoom())) {
            params.add("zoom=" + component.getZoom());
        }

        if (LangUtils.isNotBlank(component.getNameddest())) {
            params.add("nameddest=" + component.getNameddest());
        }

        if (LangUtils.isNotBlank(component.getPagemode())) {
            params.add("pagemode=" + component.getPagemode());
        }

        params.add("disableFontFace=" + component.isDisableFontFace());

        return "#" + String.join("&", params.toArray(new String[0]));
    }

    private String getResourceURL(final FacesContext context) {
        final ResourceHandler handler = context.getApplication().getResourceHandler();
        return context.getExternalContext().encodeResourceURL(
                    handler.createResource("documentviewer/pdfviewer.html", "primefaces-extensions").getRequestPath());
    }

    protected String getDocumentSource(final FacesContext context, final DocumentViewer component) {
        final String name = component.getName();

        if (name != null) {
            final String libName = component.getLibrary();
            final ResourceHandler handler = context.getApplication().getResourceHandler();
            final Resource res = handler.createResource(name, libName);

            if (res == null) {
                return "RES_NOT_FOUND";
            }
            final String requestPath = res.getRequestPath();
            return context.getExternalContext().encodeResourceURL(requestPath);
        }

        final Object value = component.getValue();
        String downloadName = component.getDownload();
        if (value instanceof StreamedContent) {
            final StreamedContent streamedContent = (StreamedContent) value;
            downloadName = Objects.toString(streamedContent.getName(), downloadName);
        }
        return DynamicContentSrcBuilder.build(context,
                    component,
                    component.getValueExpression("value"),
                    new Lazy<>(() -> value),
                    component.isCache(),
                    true) + "&download=" + downloadName;
    }
}
