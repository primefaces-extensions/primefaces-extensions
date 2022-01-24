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
package org.primefaces.extensions.component.monacoeditor;

import java.util.Locale;
import java.util.Map;

import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.model.monacoeditor.EditorOptions;
import org.primefaces.extensions.model.monacoeditor.EditorStandaloneTheme;
import org.primefaces.util.LocaleUtils;

/**
 * Base component for both the standalone and diff monaco code editor widget, in its framed and inline variants.
 *
 * @since 11.1.0
 */
@SuppressWarnings("java:S110")
public abstract class MonacoEditorCommon<TEditorOpts> extends HtmlInputTextarea implements ClientBehaviorHolder, PrimeClientBehaviorHolder, Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    static final boolean DEFAULT_AUTO_RESIZE = false;
    static final String DEFAULT_BASENAME = "";
    static final String DEFAULT_DIRECTORY = "";
    static final boolean DEFAULT_DISABLED = false;
    static final String DEFAULT_EXTENSION = "";
    static final String DEFAULT_HEIGHT = "600px";
    static final String DEFAULT_LANGUAGE = "plaintext";
    static final boolean DEFAULT_READONLY = false;
    static final String DEFAULT_SCHEME = "inmemory";
    static final String DEFAULT_TABINDEX = null;
    static final String DEFAULT_WIDTH = "200px";

    private Locale appropriateLocale;
    private final Class<TEditorOpts> editorOptionsClass;

    protected MonacoEditorCommon(String rendererType, Class<TEditorOpts> editorOptionsClass) {
        this.editorOptionsClass = editorOptionsClass;
        setRendererType(rendererType);
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext context = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(context, getLocale(), getClientId(context));
        }
        return appropriateLocale;
    }

    public final String getBasename() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.basename, DEFAULT_BASENAME);
    }

    @SuppressWarnings("unchecked")
    public final Map<String, EditorStandaloneTheme> getCustomThemes() {
        return (Map<String, EditorStandaloneTheme>) getStateHelper().eval(BaseEditorPropertyKeys.customThemes, null);
    }

    public final String getDirectory() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.directory, DEFAULT_DIRECTORY);
    }

    public final TEditorOpts getEditorOptions() {
        final TEditorOpts editorOptions = editorOptionsClass.cast(getStateHelper().eval(BaseEditorPropertyKeys.editorOptions, null));
        return editorOptions != null ? editorOptions : newEditorOptions();
    }

    public final String getExtension() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.extension, DEFAULT_EXTENSION);
    }

    @Override
    public final String getFamily() {
        return COMPONENT_FAMILY;
    }

    public final String getHeight() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.height, DEFAULT_HEIGHT);
    }

    public final String getOninitialized() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.oninitialized, null);
    }

    public final String getOnpaste() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.onpaste, null);
    }

    public final String getScheme() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.scheme, DEFAULT_SCHEME);
    }

    public Object getLocale() {
        return getStateHelper().eval(BaseEditorPropertyKeys.locale, null);
    }

    public final String getLocaleUrl() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.localeUrl, null);
    }

    public final String getWidgetVar() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.widgetVar, null);
    }

    public final String getWidth() {
        return (String) getStateHelper().eval(BaseEditorPropertyKeys.width, DEFAULT_WIDTH);
    }

    public final boolean isAutoResize() {
        return (Boolean) getStateHelper().eval(BaseEditorPropertyKeys.autoResize, DEFAULT_AUTO_RESIZE);
    }

    public final void setAutoResize(boolean autoResize) {
        getStateHelper().put(BaseEditorPropertyKeys.autoResize, autoResize);
    }

    public final void setBasename(String basename) {
        getStateHelper().put(BaseEditorPropertyKeys.basename, basename);
    }

    public final void setCustomThemes(Map<String, EditorStandaloneTheme> customThemes) {
        getStateHelper().put(BaseEditorPropertyKeys.customThemes, customThemes);
    }

    public final void setDirectory(String directory) {
        getStateHelper().put(BaseEditorPropertyKeys.directory, directory);
    }

    public final void setEditorOptions(EditorOptions editorOptions) {
        getStateHelper().put(BaseEditorPropertyKeys.editorOptions, editorOptions);
    }

    public final void setExtension(String extension) {
        getStateHelper().put(BaseEditorPropertyKeys.extension, extension);
    }

    public final void setHeight(String height) {
        getStateHelper().put(BaseEditorPropertyKeys.height, height);
    }

    public final void setOninitialized(String oninitialized) {
        getStateHelper().put(BaseEditorPropertyKeys.oninitialized, oninitialized);
    }

    public final void setOnpaste(String onpaste) {
        getStateHelper().put(BaseEditorPropertyKeys.onpaste, onpaste);
    }

    public final void setScheme(String scheme) {
        getStateHelper().put(BaseEditorPropertyKeys.scheme, scheme);
    }

    public final void setLocale(Object locale) {
        getStateHelper().put(BaseEditorPropertyKeys.locale, locale);
    }

    public final void setLocaleUrl(String localeUrl) {
        getStateHelper().put(BaseEditorPropertyKeys.localeUrl, localeUrl);
    }

    public final void setWidgetVar(String widgetVar) {
        getStateHelper().put(BaseEditorPropertyKeys.widgetVar, widgetVar);
    }

    public final void setWidth(String width) {
        getStateHelper().put(BaseEditorPropertyKeys.width, width);
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }

    private TEditorOpts newEditorOptions() {
        try {
            return editorOptionsClass.getConstructor().newInstance();
        }
        catch (final Exception e) {
            return null;
        }
    }
}
