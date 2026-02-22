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

import java.util.Map;

import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.model.monacoeditor.EditorStandaloneTheme;

/**
 * Base properties for both the framed and the inline Monaco editor widget.
 *
 * @since 10.0.0
 */
public interface BaseEditorProperties extends Widget {

    @Property(description = "Whether the editor auto-resizes.", defaultValue = "false")
    boolean isAutoResize();

    @Property(description = "Base name for the editor model.")
    String getBasename();

    @Property(description = "Custom themes for the editor.")
    Map<String, EditorStandaloneTheme> getCustomThemes();

    @Property(description = "Directory for the editor model.")
    String getDirectory();

    @Property(description = "Editor options object.")
    Object getEditorOptions();

    @Property(description = "File extension for the editor model.")
    String getExtension();

    @Property(description = "Height of the editor.", defaultValue = "600px")
    String getHeight();

    @Property(description = "Language mode.", defaultValue = "plaintext")
    String getLanguage();

    @Property(description = "Client-side script to run when editor is initialized.")
    String getOninitialized();

    @Property(description = "Client-side script to run on paste.")
    String getOnpaste();

    @Property(description = "Placeholder text.")
    String getPlaceholder();

    @Property(description = "URI scheme for the model.", defaultValue = "inmemory")
    String getScheme();

    @Property(description = "Locale for the editor.")
    Object getLocale();

    @Property(description = "URL to load locale from.")
    String getLocaleUrl();

    @Property(description = "Width of the editor.", defaultValue = "200px")
    String getWidth();
}
