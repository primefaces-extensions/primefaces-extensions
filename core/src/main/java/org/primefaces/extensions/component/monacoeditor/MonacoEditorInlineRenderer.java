/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Default renderer for the inline monaco editor that creates a new instance without iframes.
 *
 * @since 10.0.0
 */
public class MonacoEditorInlineRenderer extends MonacoEditorBaseRenderer<MonacoEditorInline> {

    /**
     * Default no-arg constructor for this widget renderer invoked by the framework.
     */
    public MonacoEditorInlineRenderer() {
        super(MonacoEditorInline.class);
    }

    @Override
    protected void addWidgetProperties(FacesContext context, WidgetBuilder wb, MonacoEditorInline monacoEditor) throws IOException {
        final String overflowWidgetsDomNode = SearchExpressionFacade.resolveClientId( //
                    context, //
                    monacoEditor, //
                    monacoEditor.getOverflowWidgetsDomNode(), //
                    SearchExpressionUtils.SET_RESOLVE_CLIENT_SIDE //
        );
        wb.attr("overflowWidgetsDomNode", overflowWidgetsDomNode, null);
        wb.returnCallback("extender", "function()", monacoEditor.getExtender());
    }

    @Override
    protected final void encodeMonacoEditor(final FacesContext context, final MonacoEditorInline monacoEditor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = monacoEditor.getClientId();

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_editor", null);
        writer.writeAttribute("class", "ui-monaco-editor-ed", null);
        writer.writeAttribute("style", "width:100%;height:100%;", null);
        writer.endElement("div");
    }

    @Override
    protected String getMainStyleClass() {
        return MonacoEditorInline.STYLE_CLASS;
    }

    @Override
    protected String getWidgetName() {
        return MonacoEditorInline.WIDGET_NAME;
    }
}
