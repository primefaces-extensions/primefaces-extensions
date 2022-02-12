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

import javax.faces.event.BehaviorEvent;

import org.primefaces.util.MapBuilder;

/**
 * Base component for both the framed and inline monaco code editor widget.
 *
 * @since 10.0.0
 */
@SuppressWarnings("java:S110")
public abstract class MonacoEditorBase
                                       extends MonacoEditorCommon<org.primefaces.extensions.model.monacoeditor.EditorOptions> {
    static final String DEFAULT_EVENT = "change";

    static final Map<String, Class<? extends BehaviorEvent>> BASE_BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>> builder() //
                .put(DEFAULT_EVENT, null) //
                .put("blur", null) //
                .put("focus", null) //
                .put("initialized", null) //
                .put("keydown", null) //
                .put("keyup", null) //
                .put("mousedown", null) //
                .put("mousemove", null) //
                .put("mouseup", null) //
                .put("paste", null) //
                .build();

    static final Collection<String> BASE_EVENT_NAMES = BASE_BEHAVIOR_EVENT_MAPPING.keySet();

    protected MonacoEditorBase(final String rendererType) {
        super(rendererType, org.primefaces.extensions.model.monacoeditor.EditorOptions.class);
    }

    @Override
    public final String getDefaultEventName() {
        return DEFAULT_EVENT;
    }
}
