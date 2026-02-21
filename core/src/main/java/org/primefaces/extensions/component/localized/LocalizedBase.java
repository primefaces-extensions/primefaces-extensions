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
package org.primefaces.extensions.component.localized;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Facet;
import org.primefaces.cdk.api.Property;

/**
 * <code>Localized</code> component base class.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 11.0.3
 */
@FacesComponentBase
public abstract class LocalizedBase extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Localized";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LocalizedRenderer";

    public LocalizedBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Facet(description = "Localized content for the default locale.")
    public abstract UIComponent getDefaultFacet();

    @Property(description = "Name of the localized content file (without locale suffix).")
    public abstract String getName();

    @Property(description = "Optional subfolder for the content file.")
    public abstract String getFolder();

    @Property(description = "Locale for the content (Locale instance or string).")
    public abstract Object getLocale();

    @Property(description = "Whether to HTML-escape the content.", defaultValue = "true")
    public abstract boolean isEscape();

    @Property(description = "Whether to evaluate EL in the content.", defaultValue = "false")
    public abstract boolean isEvalEl();

    @Property(description = "Whether to render content as Markdown.", defaultValue = "false")
    public abstract boolean isMarkdown();
}
