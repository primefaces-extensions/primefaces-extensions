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
package org.primefaces.extensions.component.monacoeditor;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.util.Constants;

/**
 * Framed Monaco code editor component (iframe).
 *
 * @since 10.0.0
 */
@FacesComponent(value = MonacoEditorFramedBase.COMPONENT_TYPE, namespace = MonacoEditorCommonBase.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Framed Monaco code editor (iframe).")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "monacoeditor/widget-framed.js")
@ResourceDependency(library = Constants.LIBRARY, name = "monacoeditor/monacoeditor.css")
public class MonacoEditorFramed extends MonacoEditorFramedBaseImpl {

    public static final String COMPONENT_TYPE = MonacoEditorFramedBase.COMPONENT_TYPE;
    public static final String STYLE_CLASS = "ui-monaco-editor ui-monaco-editor-code ui-monaco-editor-framed ";
    public static final String WIDGET_NAME = "ExtMonacoCodeEditorFramed";

}
