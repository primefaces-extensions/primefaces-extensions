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

import jakarta.faces.component.UIGraphic;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;

/**
 * <code>DocumentViewer</code> component base class.
 *
 * @author f.strazzullo
 * @author Melloware mellowaredev@gmail.com
 * @since 3.0.0
 */
@FacesComponentBase
public abstract class DocumentViewerBase extends UIGraphic {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DocumentViewer";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DocumentViewerRenderer";

    public DocumentViewerBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Width of the viewer iframe (e.g. '100%' or '800px').", defaultValue = "100%")
    public abstract String getWidth();

    @Property(description = "Height of the viewer iframe.")
    public abstract String getHeight();

    @Property(description = "Inline style for the iframe.")
    public abstract String getStyle();

    @Property(description = "Title for the iframe (accessibility).")
    public abstract String getTitle();

    @Property(description = "Resource name when using static resource (name/library).")
    public abstract String getName();

    @Property(description = "Resource library when using static resource.")
    public abstract String getLibrary();

    @Property(description = "Whether to cache the document when using dynamic content.", defaultValue = "false")
    public abstract boolean isCache();

    @Property(description = "Initial page number to display.")
    public abstract Integer getPage();

    @Property(description = "Download filename for dynamic content.")
    public abstract String getDownload();

    @Property(description = "Locale for the viewer. Can be a string or java.util.Locale instance.")
    public abstract Object getLocale();

    @Property(description = "Whether to disable font face in PDF.js.", defaultValue = "true")
    public abstract boolean isDisableFontFace();

    @Property(description = "Named destination to navigate to in the PDF.")
    public abstract String getNameddest();

    @Property(description = "Page mode: 'thumbs' or 'bookmarks'.")
    public abstract String getPagemode();

    @Property(description = "Zoom level (e.g. 'page-width', 'page-fit', 'auto', or numeric).")
    public abstract String getZoom();

    @Property(description = "URL of the document to view.")
    public abstract String getUrl();
}
