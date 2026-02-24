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
package org.primefaces.extensions.component.api;

import java.util.logging.Logger;

import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AbstractPrimeHtmlInputTextArea;
import org.primefaces.component.api.Widget;
import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.extensions.util.HtmlSanitizer;

/**
 * Abstract Editor for SunEditor and Markdown Editor.
 */
@FacesComponentBase
public abstract class AbstractEditorInputTextArea extends AbstractPrimeHtmlInputTextArea implements Widget {

    private static final Logger LOGGER = Logger.getLogger(AbstractEditorInputTextArea.class.getName());

    @Property(description = "Secure the component with the HTML Sanitizer library on the classpath.", defaultValue = "true")
    public abstract boolean isSecure();

    @Property(description = "Whether to allow blocks to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowBlocks();

    @Property(description = "Whether to allow formatting to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowFormatting();

    @Property(description = "Whether to allow links to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowLinks();

    @Property(description = "Whether to allow styles to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowStyles();

    @Property(description = "Whether to allow images to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowImages();

    @Property(description = "Whether to allow tables to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowTables();

    @Property(description = "Whether to allow audio/video to be included when secure=true and the HTML is sanitized.", defaultValue = "true")
    public abstract boolean isAllowMedia();

    @Property(description = "The extender to use for the editor.")
    public abstract String getExtender();

    @Property(description = "The toolbar to use for the editor.")
    public abstract String getToolbar();

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
