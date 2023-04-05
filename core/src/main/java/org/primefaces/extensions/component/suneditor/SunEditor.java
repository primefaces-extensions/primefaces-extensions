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
package org.primefaces.extensions.component.suneditor;

import java.util.Collection;
import java.util.Locale;

import javax.faces.application.ResourceDependency;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.AbstractPrimeHtmlInputTextArea;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;
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
public class SunEditor extends AbstractPrimeHtmlInputTextArea implements ClientBehaviorHolder, Widget, RTLAware {
    public static final String EDITOR_CLASS = "ui-suneditor";
    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.SunEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.SunEditorRenderer";

    private static final Collection<String> EVENT_NAMES = LangUtils.unmodifiableList("change", "scroll", "mouseDown", "click", "input", "keyDown", "keyUp",
                "focus", "blur", "paste", "copy", "cut", "drop", "save");

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        widgetVar, dir, width, height, allowBlocks, allowFormatting, allowLinks, allowStyles, allowImages, secure, mode, locale, extender, toolbar
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

    public int getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, Integer.MIN_VALUE);
    }

    public void setWidth(int width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public int getHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.height, Integer.MIN_VALUE);
    }

    public void setHeight(int height) {
        getStateHelper().put(PropertyKeys.height, height);
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

    public String getExtender() {
        return (String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public void setExtender(final String extender) {
        getStateHelper().put(PropertyKeys.extender, extender);
    }

    public String getToolbar() {
        return (String) getStateHelper().eval(PropertyKeys.toolbar, null);
    }

    public void setToolbar(String toolbar) {
        getStateHelper().put(PropertyKeys.toolbar, toolbar);
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

}
