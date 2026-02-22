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
package org.primefaces.extensions.component.monacoeditor;

import java.util.Locale;

import jakarta.faces.context.FacesContext;

import org.primefaces.util.LocaleUtils;

/**
 * Base component for both the standalone and diff monaco code editor widget, in its framed and inline variants.
 *
 * @since 11.1.0
 */
public abstract class MonacoEditorCommon<TEditorOpts> extends MonacoEditorCommonBaseImpl {

    static final boolean DEFAULT_AUTO_RESIZE = false;
    static final String DEFAULT_BASENAME = "";
    static final String DEFAULT_DIRECTORY = "";
    static final boolean DEFAULT_DISABLED = false;
    static final String DEFAULT_EXTENSION = "";
    static final String DEFAULT_LANGUAGE = "plaintext";
    static final boolean DEFAULT_READONLY = false;
    static final String DEFAULT_PLACEHOLDER = null;
    static final String DEFAULT_SCHEME = "inmemory";
    static final int DEFAULT_TABINDEX = 0;
    static final String DEFAULT_HEIGHT = "600px";
    static final String DEFAULT_WIDTH = "200px";

    private Locale appropriateLocale;
    private final Class<TEditorOpts> editorOptionsClass;

    protected MonacoEditorCommon(final String rendererType, final Class<TEditorOpts> editorOptionsClass) {
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

    public TEditorOpts getEditorOptions() {
        final TEditorOpts editorOptions = editorOptionsClass.cast(getStateHelper().eval(PropertyKeys.editorOptions, null));
        return editorOptions != null ? editorOptions : newEditorOptions();
    }

    public void setEditorOptions(final org.primefaces.extensions.model.monacoeditor.EditorOptions editorOptions) {
        getStateHelper().put(PropertyKeys.editorOptions, editorOptions);
    }

    @Override
    public Object saveState(final FacesContext context) {
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
