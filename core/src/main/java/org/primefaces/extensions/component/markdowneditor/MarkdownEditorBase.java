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
package org.primefaces.extensions.component.markdowneditor;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.extensions.component.api.AbstractEditorInputTextArea;

@FacesComponentBase
public abstract class MarkdownEditorBase extends AbstractEditorInputTextArea {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MarkdownEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MarkdownEditorRenderer";

    public MarkdownEditorBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Placeholder text shown when the editor is empty.")
    public abstract String getPlaceholder();

    @Property(description = "The mode to use: 'wysiwyg', 'standard', etc.", defaultValue = "wysiwyg")
    public abstract String getMode();

    @Property(description = "Minimum height of the editor.", defaultValue = "300px")
    public abstract String getMinHeight();

    @Property(description = "Maximum height of the editor.")
    public abstract String getMaxHeight();

    @Property(description = "Whether to use side-by-side fullscreen mode.", defaultValue = "true")
    public abstract boolean isSideBySideFullscreen();

    @Property(description = "If set true, indent using tabs instead of spaces.", defaultValue = "true")
    public abstract boolean isIndentWithTabs();

    @Property(description = "Whether to show line numbers.", defaultValue = "false")
    public abstract boolean isLineNumbers();

    @Property(description = "Whether to prompt for URLs when inserting links.", defaultValue = "false")
    public abstract boolean isPromptURLs();

    @Property(description = "The width of a tab character.", defaultValue = "2")
    public abstract Integer getTabSize();
}
