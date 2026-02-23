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

import jakarta.faces.component.html.HtmlInputTextarea;
import jakarta.faces.validator.Validator;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.model.monacoeditor.EditorStandaloneTheme;

/**
 * CDK base for Monaco editor common behavior (shared by code and diff editors).
 *
 * @since 10.0.0
 */
@FacesComponentBase
public abstract class MonacoEditorCommonBase extends HtmlInputTextarea
            implements Widget {

    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Whether the editor auto-resizes.", defaultValue = "false")
    public abstract boolean isAutoResize();

    @Property(description = "Base name for the editor model.")
    public abstract String getBasename();

    @Property(description = "Custom themes for the editor.")
    public abstract Map<String, EditorStandaloneTheme> getCustomThemes();

    @Property(description = "Directory for the editor model.")
    public abstract String getDirectory();

    @Property(description = "Editor options object.cd ")
    public abstract Object getEditorOptions();

    @Property(description = "File extension for the editor model.")
    public abstract String getExtension();

    @Property(description = "Height of the editor.", defaultValue = "600px")
    public abstract String getHeight();

    @Property(description = "Language mode.", defaultValue = "plaintext")
    public abstract String getLanguage();

    @Property(description = "Client-side script to run when editor is initialized.")
    public abstract String getOninitialized();

    @Property(description = "Client-side script to run on paste.")
    public abstract String getOnpaste();

    @Property(description = "Placeholder text.")
    public abstract String getPlaceholder();

    @Property(description = "URI scheme for the model.", defaultValue = "inmemory")
    public abstract String getScheme();

    @Property(description = "Locale for the editor.")
    public abstract Object getLocale();

    @Property(description = "URL to load locale from.")
    public abstract String getLocaleUrl();

    @Property(description = "Width of the editor.", defaultValue = "200px")
    public abstract String getWidth();

    @Property(description = "A method expression referring to a method validating the input.")
    public Validator<?> getValidator() {
        throw new UnsupportedOperationException("Only for documentation purpose.");
    }
}
