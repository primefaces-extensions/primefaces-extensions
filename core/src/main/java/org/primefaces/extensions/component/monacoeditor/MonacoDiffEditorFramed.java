/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import java.util.Collection;
import java.util.Map;

import javax.faces.application.ResourceDependency;
import javax.faces.event.BehaviorEvent;

/**
 * Component for the Monaco code diff editor JavaScript library .This is the framed monaco editor that creates a new instance in a separate iframe to allow for
 * better scoping, i.e. loading types etc. without affecting other editors. There is also an inline widget when this scoping is not required as iframes create
 * additional overhead.
 *
 * @see MonacoDiffEditorInline
 * @since 11.1.0
 */
@SuppressWarnings("java:S110")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "monacoeditor/widget-framed.js")
@ResourceDependency(library = "primefaces-extensions", name = "monacoeditor/monacoeditor.css")
public class MonacoDiffEditorFramed extends MonacoDiffEditorBase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MonacoDiffEditorFramed";

    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MonacoDiffEditorFramedRenderer";

    public static final String STYLE_CLASS = "ui-monaco-editor ui-monaco-editor-diff ui-monaco-editor-framed ";

    public static final String WIDGET_NAME = "ExtMonacoDiffEditorFramed";

    /**
     * Default no-arg constructor for this widget invoked by the framework.
     */
    public MonacoDiffEditorFramed() {
        super(DEFAULT_RENDERER);
    }

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        // no additional events for now
        return BASE_BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        // no additional events for now
        return BASE_EVENT_NAMES;
    }

    public String getExtender() {
        return (String) getStateHelper().eval(DiffEditorFramedPropertyKeys.extender, null);
    }

    public void setExtender(final String extender) {
        getStateHelper().put(DiffEditorFramedPropertyKeys.extender, extender);
    }

    public Object getIframeUrlParams() {
        return getStateHelper().eval(DiffEditorFramedPropertyKeys.iframeUrlParams, null);
    }

    public void setIframeUrlParams(final Object iframeUrlParams) {
        getStateHelper().put(DiffEditorFramedPropertyKeys.iframeUrlParams, iframeUrlParams);
    }
}
