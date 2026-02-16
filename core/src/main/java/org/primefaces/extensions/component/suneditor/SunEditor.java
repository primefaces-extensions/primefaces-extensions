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
package org.primefaces.extensions.component.suneditor;

import java.util.Locale;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * <code>SunEditor</code> component.
 *
 * @author Matthieu Valente
 * @since 12.0.6
 */
@FacesComponent(value = SunEditor.COMPONENT_TYPE, namespace = SunEditor.COMPONENT_FAMILY)
@FacesComponentInfo(description = "SunEditor WYSIWYG editor component.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "suneditor/suneditor.css")
@ResourceDependency(library = Constants.LIBRARY, name = "suneditor/suneditor.js")
public class SunEditor extends SunEditorBaseImpl {

    public static final String EDITOR_CLASS = "ui-suneditor";

    private Locale appropriateLocale;

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    @Override
    public Object saveState(final FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }
}
