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
package org.primefaces.extensions.component.suneditor;

import java.util.Collection;
import java.util.Locale;

import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.component.base.AbstractEditorInputTextArea;
import org.primefaces.util.LangUtils;
import org.primefaces.util.LocaleUtils;

/**
 * <code>SunEditor</code> component.
 *
 * @author Matthieu Valente
 * @since 12.0.6
 */

@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "suneditor/suneditor.css")
@ResourceDependency(library = "primefaces-extensions", name = "suneditor/suneditor.js")
public class SunEditor extends AbstractEditorInputTextArea implements ClientBehaviorHolder {
    public static final String EDITOR_CLASS = "ui-suneditor";
    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SunEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SunEditorRenderer";

    private static final Collection<String> EVENT_NAMES = LangUtils.unmodifiableList("change", "scroll", "mousedown", "click", "input", "keydown", "keyup",
                "focus", "blur", "paste", "copy", "cut", "drop", "save", "initialize");

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        // @formatter:off
        width,
        height,
        mode,
        locale,
        strictMode
        // @formatter:on
    }

    private Locale appropriateLocale;

    public SunEditor() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return "change";
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width, "100%");
    }

    public void setWidth(String width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public String getHeight() {
        return (String) getStateHelper().eval(PropertyKeys.height, "auto");
    }

    public void setHeight(String height) {
        getStateHelper().put(PropertyKeys.height, height);
    }

    public String getMode() {
        return (String) getStateHelper().eval(PropertyKeys.mode, "classic");
    }

    public void setMode(String mode) {
        getStateHelper().put(PropertyKeys.mode, mode);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public boolean isStrictMode() {
        return (boolean) getStateHelper().eval(PropertyKeys.strictMode, true);
    }

    public void setStrictMode(boolean strictMode) {
        getStateHelper().put(PropertyKeys.strictMode, strictMode);
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }

}