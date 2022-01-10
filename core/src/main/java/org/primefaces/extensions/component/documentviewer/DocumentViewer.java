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
package org.primefaces.extensions.component.documentviewer;

import java.util.Locale;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;

import org.primefaces.util.LocaleUtils;

/**
 * <code>DocumentViewer</code> component.
 *
 * @author f.strazzullo
 * @author Melloware mellowaredev@gmail.com
 * @since 3.0.0
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
public class DocumentViewer extends UIGraphic {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DocumentViewer";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DocumentViewerRenderer";

    private Locale appropriateLocale;

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        //@formatter:off
        width,
        height,
        style,
        title,
        name,
        library,
        cache,
        page,
        download,
        locale,
        nameddest,
        pagemode,
        zoom;
       //@formatter:on
    }

    public DocumentViewer() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    public String getWidth() {
        return (String) this.getStateHelper().eval(PropertyKeys.width, null);
    }

    public void setWidth(final String width) {
        this.getStateHelper().put(PropertyKeys.width, width);
    }

    public String getHeight() {
        return (String) this.getStateHelper().eval(PropertyKeys.height, null);
    }

    public void setHeight(final String height) {
        this.getStateHelper().put(PropertyKeys.height, height);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String style) {
        this.getStateHelper().put(PropertyKeys.style, style);
    }

    public String getName() {
        return (String) getStateHelper().eval(PropertyKeys.name, null);
    }

    public void setName(final String _name) {
        getStateHelper().put(PropertyKeys.name, _name);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(final String _title) {
        getStateHelper().put(PropertyKeys.title, _title);
    }

    public String getLibrary() {
        return (String) getStateHelper().eval(PropertyKeys.library, null);
    }

    public void setLibrary(final String _library) {
        getStateHelper().put(PropertyKeys.library, _library);
    }

    public boolean isCache() {
        return (Boolean) getStateHelper().eval(PropertyKeys.cache, false);
    }

    public void setCache(final boolean _cache) {
        getStateHelper().put(PropertyKeys.cache, _cache);
    }

    public Integer getPage() {
        return (Integer) getStateHelper().eval(PropertyKeys.page);
    }

    public void setPage(final Integer page) {
        this.getStateHelper().put(PropertyKeys.page, page);
    }

    public void setDownload(final String download) {
        getStateHelper().put(PropertyKeys.download, download);
    }

    public String getDownload() {
        return (String) getStateHelper().eval(PropertyKeys.download, null);
    }

    public void setNameddest(final String nameddest) {
        getStateHelper().put(PropertyKeys.nameddest, nameddest);
    }

    public String getNameddest() {
        return (String) getStateHelper().eval(PropertyKeys.nameddest, null);
    }

    public void setPagemode(final String pagemode) {
        getStateHelper().put(PropertyKeys.pagemode, pagemode);
    }

    public String getPagemode() {
        return (String) getStateHelper().eval(PropertyKeys.pagemode, null);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public void setZoom(final String zoom) {
        getStateHelper().put(PropertyKeys.zoom, zoom);
    }

    public String getZoom() {
        return (String) getStateHelper().eval(PropertyKeys.zoom, null);
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }
}
