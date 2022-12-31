/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import java.util.Locale;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.primefaces.util.LocaleUtils;

/**
 * <code>Localized</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 11.0.3
 */
public class Localized extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Localized";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LocalizedRenderer";

    // @formatter:off
    @SuppressWarnings("java:S115")
    public enum PropertyKeys {
        name,
        folder,
        locale,
        escape,
        evalEl,
        markdown
    }
    // @formatter:on

    public Localized() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Locale calculateLocale(FacesContext facesContext) {
        return LocaleUtils.resolveLocale(facesContext, getLocale(), getClientId(facesContext));
    }

    public String getName() {
        return (String) getStateHelper().eval(PropertyKeys.name, null);
    }

    public void setName(final String name) {
        getStateHelper().put(PropertyKeys.name, name);
    }

    public String getFolder() {
        return (String) getStateHelper().eval(PropertyKeys.folder, null);
    }

    public void setFolder(final String folder) {
        getStateHelper().put(PropertyKeys.folder, folder);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public boolean isEscape() {
        return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public void setEscape(final boolean escape) {
        getStateHelper().put(PropertyKeys.escape, escape);
    }

    public boolean isEvalEl() {
        return (Boolean) getStateHelper().eval(PropertyKeys.evalEl, false);
    }

    public void setEvalEl(final boolean evalEl) {
        getStateHelper().put(PropertyKeys.evalEl, evalEl);
    }

    public boolean isMarkdown() {
        return (Boolean) getStateHelper().eval(PropertyKeys.markdown, false);
    }

    public void setMarkdown(final boolean markdown) {
        getStateHelper().put(PropertyKeys.markdown, markdown);
    }

}