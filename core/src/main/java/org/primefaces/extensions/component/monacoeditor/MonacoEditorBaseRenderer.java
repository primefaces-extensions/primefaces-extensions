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
package org.primefaces.extensions.component.monacoeditor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Base renderer for both the {@link MonacoEditorFramed framed} and {@link MonacoEditorInline inline} monaco editor.
 *
 * @since 10.0.0
 */
abstract class MonacoEditorBaseRenderer<TEditor extends MonacoEditorBase>
                                       extends MonacoEditorCommonRenderer<TEditor, org.primefaces.extensions.model.monacoeditor.EditorOptions> {
    private static final String INPUT_SUFFIX = "_input";

    private static final List<String> PASSTHROUGH_ATTRS = Arrays.asList(//
                "alt", "accesskey", "autocomplete", //
                "cols", "dir", "lang", //
                "maxlength", "placeholder", "rows", //
                "size", "title" //
    );

    protected MonacoEditorBaseRenderer(final Class<TEditor> clazz) {
        super(clazz);
    }

    protected abstract void addWidgetProperties(FacesContext context, WidgetBuilder wb, TEditor monacoEditor)
                throws IOException;

    @Override
    public final void decode(final FacesContext context, final UIComponent component) {
        final TEditor monacoEditor = componentClass.cast(component);

        // Do not allow modifications if component is not allowed to submit values.
        // Read-only is fine, we should still accept the submitted value when read-only
        if (monacoEditor.isDisabled()) {
            return;
        }

        // Decode value
        final String clientId = monacoEditor.getClientId() + INPUT_SUFFIX;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            monacoEditor.setSubmittedValue(params.get(clientId));
        }

        // Decode behaviors
        decodeBehaviors(context, component);
    }

    @Override
    protected final void encodeHiddenInput(final FacesContext context, final TEditor monacoEditor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = monacoEditor.getClientId();

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("textarea", null);
        writer.writeAttribute("id", clientId + INPUT_SUFFIX, null);
        writer.writeAttribute("name", clientId + INPUT_SUFFIX, null);
        writer.writeAttribute("autocomplete", "off", null);
        if (monacoEditor.isReadonly()) {
            writer.writeAttribute("readonly", "readonly", null);
        }
        if (monacoEditor.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        renderPassThruAttributes(context, monacoEditor, PASSTHROUGH_ATTRS);

        final String valueToRender = ComponentUtils.getValueToRender(context, monacoEditor);
        if (valueToRender != null) {
            writer.writeText(valueToRender, null);
        }
        writer.endElement("textarea");

        writer.endElement("div");
    }

    @Override
    public final Object getConvertedValue(final FacesContext context, final UIComponent component,
                final Object submittedValue) {
        final TEditor monacoEditor = componentClass.cast(component);
        final String value = (String) submittedValue;
        final Converter<?> converter = ComponentUtils.getConverter(context, monacoEditor);

        if (converter != null) {
            return converter.getAsObject(context, monacoEditor, value);
        }

        return value;
    }

    @Override
    protected final void addBaseWidgetProperties(final FacesContext context, final WidgetBuilder wb, final TEditor monacoEditor)
                throws IOException {
        addWidgetProperties(context, wb, monacoEditor);
    }

    @Override
    protected boolean isEntireEditorDisabled(final TEditor monacoEditor) {
        return monacoEditor.isDisabled();
    }

    @Override
    protected String getLanguage(final TEditor monacoEditor) {
        return monacoEditor.getEditorOptions().getLanguage();
    }
}
