/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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

import javax.faces.application.ResourceDependency;

import org.primefaces.component.api.AbstractPrimeHtmlInputTextArea;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;
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
public class MarkdownEditor extends AbstractPrimeHtmlInputTextArea implements Widget, RTLAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MarkdownEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MarkdownEditorRenderer";

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        minHeight,
        maxHeight,
        placeholder,
        extender,
        sideBySideFullscreen,
        allowBlocks,
        allowFormatting,
        allowLinks,
        allowStyles,
        allowImages,
        allowTables,
        allowMedia,
        secure,
        toolbar,
        widgetVar
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

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
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

    public String getToolbar() {
        return (String) getStateHelper().eval(PropertyKeys.toolbar, null);
    }

    public void setToolbar(String toolbar) {
        getStateHelper().put(PropertyKeys.toolbar, toolbar);
    }

    public boolean isSecure() {
        return (Boolean) getStateHelper().eval(PropertyKeys.secure, true);
    }

    public void setSecure(boolean secure) {
        getStateHelper().put(PropertyKeys.secure, secure);
    }

    public boolean isAllowBlocks() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowBlocks, true);
    }

    public void setAllowBlocks(boolean allowBlocks) {
        getStateHelper().put(PropertyKeys.allowBlocks, allowBlocks);
    }

    public boolean isAllowFormatting() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowFormatting, true);
    }

    public void setAllowFormatting(boolean allowFormatting) {
        getStateHelper().put(PropertyKeys.allowFormatting, allowFormatting);
    }

    public boolean isAllowLinks() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowLinks, true);
    }

    public void setAllowLinks(boolean allowLinks) {
        getStateHelper().put(PropertyKeys.allowLinks, allowLinks);
    }

    public boolean isAllowStyles() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowStyles, true);
    }

    public void setAllowStyles(boolean allowStyles) {
        getStateHelper().put(PropertyKeys.allowStyles, allowStyles);
    }

    public boolean isAllowImages() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowImages, true);
    }

    public void setAllowImages(boolean allowImages) {
        getStateHelper().put(PropertyKeys.allowImages, allowImages);
    }

    public boolean isAllowTables() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowTables, true);
    }

    public void setAllowMedia(boolean allowMedia) {
        getStateHelper().put(PropertyKeys.allowMedia, allowMedia);
    }

    public boolean isAllowMedia() {
        return (Boolean) getStateHelper().eval(PropertyKeys.allowMedia, true);
    }

    public void setAllowTables(boolean allowTables) {
        getStateHelper().put(PropertyKeys.allowTables, allowTables);
    }
}