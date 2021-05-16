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
package org.primefaces.extensions.component.monacoeditor;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.model.monacoeditor.EditorOptions;
import org.primefaces.extensions.model.monacoeditor.EditorStandaloneTheme;
import org.primefaces.util.LocaleUtils;
import org.primefaces.util.MapBuilder;

/**
 * Base component for both the framed and inline monaco code editor widget.
 *
 * @since 10.0.0
 */
@SuppressWarnings("java:S110")
public abstract class MonacoEditorBase extends HtmlInputTextarea implements ClientBehaviorHolder, PrimeClientBehaviorHolder, Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    static final String DEFAULT_EVENT = "change";

    static final Map<String, Class<? extends BehaviorEvent>> BASE_BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>> builder() //
                .put(DEFAULT_EVENT, null) //
                .put("blur", null) //
                .put("focus", null) //
                .put("initialized", null) //
                .put("keydown", null) //
                .put("keyup", null) //
                .put("mousedown", null) //
                .put("mousemove", null) //
                .put("mouseup", null) //
                .put("paste", null) //
                .build();

    static final Collection<String> BASE_EVENT_NAMES = BASE_BEHAVIOR_EVENT_MAPPING.keySet();

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

    protected MonacoEditorBase(String rendererType) {
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
        return (String) getStateHelper().eval(BasePropertyKeys.basename, DEFAULT_BASENAME);
    }

    @SuppressWarnings("unchecked")
    public final Map<String, EditorStandaloneTheme> getCustomThemes() {
        return (Map<String, EditorStandaloneTheme>) getStateHelper().eval(BasePropertyKeys.customThemes, null);
    }

    @Override
    public final String getDefaultEventName() {
        return DEFAULT_EVENT;
    }

    public final String getDirectory() {
        return (String) getStateHelper().eval(BasePropertyKeys.directory, DEFAULT_DIRECTORY);
    }

    public final EditorOptions getEditorOptions() {
        final EditorOptions editorOptions = (EditorOptions) getStateHelper().eval(BasePropertyKeys.editorOptions, null);
        return editorOptions != null ? editorOptions : new EditorOptions();
    }

    public final String getExtension() {
        return (String) getStateHelper().eval(BasePropertyKeys.extension, DEFAULT_EXTENSION);
    }

    @Override
    public final String getFamily() {
        return COMPONENT_FAMILY;
    }

    public final String getHeight() {
        return (String) getStateHelper().eval(BasePropertyKeys.height, DEFAULT_HEIGHT);
    }

    public final String getOninitialized() {
        return (String) getStateHelper().eval(BasePropertyKeys.oninitialized, null);
    }

    public final String getOnpaste() {
        return (String) getStateHelper().eval(BasePropertyKeys.onpaste, null);
    }

    public final String getScheme() {
        return (String) getStateHelper().eval(BasePropertyKeys.scheme, DEFAULT_SCHEME);
    }

    public Object getLocale() {
        return getStateHelper().eval(BasePropertyKeys.locale, null);
    }

    public final String getLocaleUrl() {
        return (String) getStateHelper().eval(BasePropertyKeys.localeUrl, null);
    }

    public final String getWidgetVar() {
        return (String) getStateHelper().eval(BasePropertyKeys.widgetVar, null);
    }

    public final String getWidth() {
        return (String) getStateHelper().eval(BasePropertyKeys.width, DEFAULT_WIDTH);
    }

    public final boolean isAutoResize() {
        return (Boolean) getStateHelper().eval(BasePropertyKeys.autoResize, DEFAULT_AUTO_RESIZE);
    }

    public final void setAutoResize(boolean autoResize) {
        getStateHelper().put(BasePropertyKeys.autoResize, autoResize);
    }

    public final void setBasename(String basename) {
        getStateHelper().put(BasePropertyKeys.basename, basename);
    }

    public final void setCustomThemes(Map<String, EditorStandaloneTheme> customThemes) {
        getStateHelper().put(BasePropertyKeys.customThemes, customThemes);
    }

    public final void setDirectory(String directory) {
        getStateHelper().put(BasePropertyKeys.directory, directory);
    }

    public final void setEditorOptions(EditorOptions editorOptions) {
        getStateHelper().put(BasePropertyKeys.editorOptions, editorOptions);
    }

    public final void setExtension(String extension) {
        getStateHelper().put(BasePropertyKeys.extension, extension);
    }

    public final void setHeight(String height) {
        getStateHelper().put(BasePropertyKeys.height, height);
    }

    public final void setOninitialized(String oninitialized) {
        getStateHelper().put(BasePropertyKeys.oninitialized, oninitialized);
    }

    public final void setOnpaste(String onpaste) {
        getStateHelper().put(BasePropertyKeys.onpaste, onpaste);
    }

    public final void setScheme(String scheme) {
        getStateHelper().put(BasePropertyKeys.scheme, scheme);
    }

    public final void setLocale(Object locale) {
        getStateHelper().put(BasePropertyKeys.locale, locale);
    }

    public final void setLocaleUrl(String localeUrl) {
        getStateHelper().put(BasePropertyKeys.localeUrl, localeUrl);
    }

    public final void setWidgetVar(String widgetVar) {
        getStateHelper().put(BasePropertyKeys.widgetVar, widgetVar);
    }

    public final void setWidth(String width) {
        getStateHelper().put(BasePropertyKeys.width, width);
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }
}
