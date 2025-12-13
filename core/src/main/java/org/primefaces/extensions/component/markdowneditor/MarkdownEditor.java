/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import jakarta.faces.application.ResourceDependency;

import org.primefaces.extensions.component.api.AbstractEditorInputTextArea;
import org.primefaces.extensions.util.Constants;

/**
 * Markdown Editor.
 *
 * @since 14.0.0
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "markdowneditor/markdowneditor.css")
@ResourceDependency(library = Constants.LIBRARY, name = "markdowneditor/markdowneditor.js")
public class MarkdownEditor extends AbstractEditorInputTextArea {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MarkdownEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MarkdownEditorRenderer";

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        indentWithTabs,
        lineNumbers,
        maxHeight,
        minHeight,
        mode,
        placeholder,
        promptURLs,
        sideBySideFullscreen,
        tabSize,
    }
    // @formatter:on

    public MarkdownEditor() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public String getMode() {
        return (String) getStateHelper().eval(PropertyKeys.mode, "wysiwyg");
    }

    public void setMode(String mode) {
        getStateHelper().put(PropertyKeys.mode, mode);
    }

    public String getMinHeight() {
        return (String) getStateHelper().eval(PropertyKeys.minHeight, "300px");
    }

    public void setMinHeight(String minHeight) {
        getStateHelper().put(PropertyKeys.minHeight, minHeight);
    }

    public String getMaxHeight() {
        return (String) getStateHelper().eval(PropertyKeys.maxHeight, null);
    }

    public void setMaxHeight(String maxHeight) {
        getStateHelper().put(PropertyKeys.maxHeight, maxHeight);
    }

    public boolean getSideBySideFullscreen() {
        return (boolean) getStateHelper().eval(PropertyKeys.sideBySideFullscreen, true);
    }

    public void setSideBySideFullscreen(boolean sideBySideFullscreen) {
        getStateHelper().put(PropertyKeys.sideBySideFullscreen, sideBySideFullscreen);
    }

    public boolean getIndentWithTabs() {
        return (boolean) getStateHelper().eval(PropertyKeys.indentWithTabs, true);
    }

    public void setIndentWithTabs(boolean indentWithTabs) {
        getStateHelper().put(PropertyKeys.indentWithTabs, indentWithTabs);
    }

    public boolean getLineNumbers() {
        return (boolean) getStateHelper().eval(PropertyKeys.lineNumbers, false);
    }

    public void setLineNumbers(boolean lineNumbers) {
        getStateHelper().put(PropertyKeys.lineNumbers, lineNumbers);
    }

    public boolean getPromptURLs() {
        return (boolean) getStateHelper().eval(PropertyKeys.promptURLs, false);
    }

    public void setPromptURLs(boolean promptURLs) {
        getStateHelper().put(PropertyKeys.promptURLs, promptURLs);
    }

    public int getTabSize() {
        return (int) getStateHelper().eval(PropertyKeys.tabSize, 2);
    }

    public void setTabSize(boolean tabSize) {
        getStateHelper().put(PropertyKeys.tabSize, tabSize);
    }
}