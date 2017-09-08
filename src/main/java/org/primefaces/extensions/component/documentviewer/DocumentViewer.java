/**
 * Copyright 2011-2017 PrimeFaces Extensions
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

import java.util.Locale;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIGraphic;

/**
 * <code>DocumentViewer</code> component.
 *
 * @author f.strazzullo
 * @author Melloware mellowaredev@gmail.com
 * @since 3.0.0
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
})
public class DocumentViewer extends UIGraphic {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DocumentViewer";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DocumentViewerRenderer";

    private Locale appropriateLocale;

    protected static enum PropertyKeys {
        //@formatter:off
        width, 
        height, 
        style, 
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
            appropriateLocale = org.primefaces.extensions.util.LocaleUtils.resolveLocale(getLocale());
        }
        return appropriateLocale;
    }

    public Integer getWidth() {
        return (Integer) this.getStateHelper().eval(PropertyKeys.width, null);
    }

    public void setWidth(final Integer width) {
        this.getStateHelper().put(PropertyKeys.width, width);
    }

    public Integer getHeight() {
        return (Integer) this.getStateHelper().eval(PropertyKeys.height, null);
    }

    public void setHeight(final Integer width) {
        this.getStateHelper().put(PropertyKeys.height, width);
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

    public void setLocale(final Object _locale) {
        getStateHelper().put(PropertyKeys.locale, _locale);
    }

    public void setZoom(final String zoom) {
        getStateHelper().put(PropertyKeys.zoom, zoom);
    }

    public String getZoom() {
        return (String) getStateHelper().eval(PropertyKeys.zoom, null);
    }
}
