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
package org.primefaces.extensions.component.base;

import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.AbstractPrimeHtmlInputTextArea;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;
import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.extensions.util.HtmlSanitizer;

/**
 * Abstract Editor for SunEditor and Markdown Editor.
 */
public class AbstractEditorInputTextArea extends AbstractPrimeHtmlInputTextArea implements Widget, RTLAware {

    private static final Logger LOGGER = Logger.getLogger(AbstractEditorInputTextArea.class.getName());

    protected enum PropertyKeys {
        // @formatter:off
        widgetVar,
        dir,
        allowBlocks,
        allowFormatting,
        allowLinks,
        allowStyles,
        allowImages,
        allowTables,
        allowMedia,
        secure,
        extender,
        toolbar
        // @formatter:on
    }

    public void setDir(final String _dir) {
        getStateHelper().put(PropertyKeys.dir, _dir);
    }

    @Override
    public String getDir() {
        return (String) getStateHelper().eval(PropertyKeys.dir, "ltr");
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
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

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
    }

    public String getToolbar() {
        return (String) getStateHelper().eval(PropertyKeys.toolbar, null);
    }

    public void setToolbar(String toolbar) {
        getStateHelper().put(PropertyKeys.toolbar, toolbar);
    }

    /**
     * Enforce security by default requiring the OWASP sanitizer on the classpath. Only if a user marks the editor with secure="false" will they opt-out of
     * security.
     *
     * @param context the FacesContext
     */
    public void checkSecurity(FacesContext context) {
        boolean sanitizerAvailable = PrimeApplicationContext.getCurrentInstance(context).getEnvironment()
                    .isHtmlSanitizerAvailable();
        if (this.isSecure() && !sanitizerAvailable) {
            throw new FacesException(
                        "Editor component is marked secure='true' but the HTML Sanitizer was not found on the classpath. "
                                    + "Either add the HTML sanitizer to the classpath per the documentation"
                                    + " or mark secure='false' if you would like to use the component without the sanitizer.");
        }
    }

    /**
     * If security is enabled sanitize the Markdown string to prevent XSS.
     *
     * @param context the FacesContext
     * @param value the value to sanitize
     * @return the sanitized value
     */
    public String sanitizeHtml(FacesContext context, String value) {
        String result = value;
        if (this.isSecure()
                    && PrimeApplicationContext.getCurrentInstance(context).getEnvironment().isHtmlSanitizerAvailable()) {
            result = HtmlSanitizer.sanitizeHtml(value, this.isAllowBlocks(), this.isAllowFormatting(),
                        this.isAllowLinks(), this.isAllowStyles(), this.isAllowImages(), this.isAllowTables(), this.isAllowMedia());
        }
        else {
            if (!this.isAllowBlocks() || !this.isAllowFormatting() || !this.isAllowLinks()
                        || !this.isAllowStyles() || !this.isAllowImages() || !this.isAllowTables()) {
                LOGGER.warning("HTML sanitizer not available - skip sanitizing....");
            }
        }
        return result;
    }
}