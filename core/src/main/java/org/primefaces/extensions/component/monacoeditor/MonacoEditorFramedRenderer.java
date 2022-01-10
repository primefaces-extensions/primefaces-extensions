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

import java.io.IOException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.shaded.json.JSONWriter;
import org.primefaces.util.WidgetBuilder;

/**
 * Default renderer for the inline monaco editor that creates a new instance in an iframe for better scoping.
 *
 * @since 10.0.0
 */
public class MonacoEditorFramedRenderer extends MonacoEditorBaseRenderer<MonacoEditorFramed> {

    /**
     * Default no-arg constructor for this widget renderer invoked by the framework.
     */
    public MonacoEditorFramedRenderer() {
        super(MonacoEditorFramed.class);
    }

    @Override
    protected void addWidgetProperties(FacesContext context, WidgetBuilder wb, MonacoEditorFramed monacoEditor) throws IOException {
        wb.attr("extender", monacoEditor.getExtender(), null);
        if (monacoEditor.getIframeUrlParams() instanceof Map<?, ?>) {
            wb.nativeAttr("iframeUrlParams", JSONWriter.valueToString(monacoEditor.getIframeUrlParams()));
        }
        else if (monacoEditor.getIframeUrlParams() instanceof String) {
            wb.nativeAttr("iframeUrlParams", (String) monacoEditor.getIframeUrlParams());
        }
    }

    @Override
    protected final void encodeMonacoEditor(final FacesContext context, final MonacoEditorFramed monacoEditor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = monacoEditor.getClientId();
        writer.startElement("iframe", null);
        writer.writeAttribute("id", clientId + "_editor", null);
        writer.writeAttribute("class", "ui-monaco-editor-ed", null);
        writer.writeAttribute("style", "width:100%;height:100%;border:0;margin:0;padding:0;overflow:hidden;", null);
        writer.endElement("iframe");
    }

    @Override
    protected String getMainStyleClass() {
        return MonacoEditorFramed.STYLE_CLASS;
    }

    @Override
    protected String getWidgetName() {
        return MonacoEditorFramed.WIDGET_NAME;
    }
}
