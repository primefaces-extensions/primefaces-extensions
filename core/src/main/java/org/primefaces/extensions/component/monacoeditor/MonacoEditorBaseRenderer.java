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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.shaded.json.JSONWriter;
import org.primefaces.shaded.owasp.encoder.Encode;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Base renderer for both the {@link MonacoEditorFramed framed} and {@link MonacoEditorInline inline} monaco editor.
 * 
 * @since 10.0.0
 */
abstract class MonacoEditorBaseRenderer<T extends MonacoEditorBase> extends InputRenderer {
    private static final String CALLBACK_SIGNATURE = "function()";

    private static final List<String> PASSTHROUGH_ATTRS = Arrays.asList(//
                "alt", "accesskey", "autocomplete", //
                "cols", "dir", "lang", //
                "maxlength", "placeholder", "rows", //
                "size", "title" //
    );

    private final Class<T> componentClass;

    protected MonacoEditorBaseRenderer(Class<T> clazz) {
        this.componentClass = clazz;
    }

    protected abstract void addWidgetProperties(WidgetBuilder wb, T monacoEditor) throws IOException;

    protected void array(WidgetBuilder wb, String key, Iterable<String> values) throws IOException {
        final StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (final String item : values) {
            builder.append('"');
            builder.append(Encode.forJavaScript(item));
            builder.append('"');
            builder.append(',');
        }
        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append(']');
        wb.nativeAttr(key, builder.toString());
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final T monacoEditor = componentClass.cast(component);

        // Do not allow modifications if component is not editable.
        if (monacoEditor.isDisabled() || monacoEditor.isReadonly()) {
            return;
        }

        // Decode value
        final String clientId = monacoEditor.getClientId() + "_input";
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            monacoEditor.setSubmittedValue(params.get(clientId));
        }

        // Decode behaviors
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final T monacoEditor = componentClass.cast(component);
        encodeMarkup(context, monacoEditor);
        encodeScript(context, monacoEditor);
    }

    protected void encodeHiddenInput(final FacesContext context, final T monacoEditor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = monacoEditor.getClientId();

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("textarea", monacoEditor);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("name", clientId + "_input", null);
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

    protected void encodeMarkup(final FacesContext context, final T monacoEditor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = monacoEditor.getClientId();

        String style = monacoEditor.getStyle() != null ? monacoEditor.getStyle() : "";
        style = style.concat(";");
        if (monacoEditor.getWidth() != null && !monacoEditor.getWidth().isEmpty()) {
            style = style.concat("width:" + monacoEditor.getWidth() + ";");
        }
        if (monacoEditor.getHeight() != null && !monacoEditor.getHeight().isEmpty()) {
            style = style.concat("height:" + monacoEditor.getHeight() + ";");
        }
        final StringBuilder styleClass = new StringBuilder();
        styleClass.append(getMainStyleClass() + " ui-hidden-container ");
        if (monacoEditor.isDisabled() || monacoEditor.isReadonly()) {
            styleClass.append("ui-state-disabled ");
        }
        if (monacoEditor.getStyleClass() != null) {
            styleClass.append(monacoEditor.getStyleClass());
        }

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("data-widget-var", monacoEditor.resolveWidgetVar(), null);
        writer.writeAttribute("class", styleClass.toString(), null);
        writer.writeAttribute("style", style, null);

        encodeHiddenInput(context, monacoEditor);
        encodeMonacoEditor(context, monacoEditor);

        writer.endElement("div");
    }

    protected abstract void encodeMonacoEditor(final FacesContext context, final T monacoEditor) throws IOException;

    protected void encodeScript(final FacesContext context, final T monacoEditor) throws IOException {
        final WidgetBuilder wb = PrimeRequestContext.getCurrentInstance(context).getWidgetBuilder();

        wb.init(getWidgetName(), monacoEditor);

        array(wb, "availableEvents", monacoEditor.getEventNames());

        if (monacoEditor.getCustomThemes() != null && !monacoEditor.getCustomThemes().isEmpty()) {
            wb.nativeAttr("customThemes", JSONWriter.valueToString(monacoEditor.getCustomThemes()));
        }

        wb.attr("autoResize", monacoEditor.isAutoResize(), MonacoEditorBase.DEFAULT_AUTO_RESIZE);
        wb.attr("basename", monacoEditor.getBasename(), MonacoEditorBase.DEFAULT_BASENAME);
        wb.attr("directory", monacoEditor.getDirectory(), MonacoEditorBase.DEFAULT_DIRECTORY);
        wb.attr("disabled", monacoEditor.isDisabled(), MonacoEditorBase.DEFAULT_DISABLED);
        wb.attr("editorOptions", monacoEditor.getEditorOptions().toString());
        wb.attr("extension", monacoEditor.getExtension(), MonacoEditorBase.DEFAULT_EXTENSION);
        wb.attr("language", monacoEditor.getEditorOptions().getLanguage(), MonacoEditorBase.DEFAULT_LANGUAGE);
        wb.attr("locale", monacoEditor.calculateLocale().toString());
        wb.attr("localeUrl", monacoEditor.getLocaleUrl());
        wb.attr("readonly", monacoEditor.isReadonly(), MonacoEditorBase.DEFAULT_READONLY);
        wb.attr("scheme", monacoEditor.getScheme(), MonacoEditorBase.DEFAULT_SCHEME);
        wb.attr("tabIndex", monacoEditor.getTabindex(), MonacoEditorBase.DEFAULT_TABINDEX);
        wb.attr("height", monacoEditor.getHeight(), MonacoEditorBase.DEFAULT_HEIGHT);
        wb.attr("width", monacoEditor.getWidth(), MonacoEditorBase.DEFAULT_WIDTH);

        wb.callback("onblur", CALLBACK_SIGNATURE, monacoEditor.getOnblur());
        wb.callback("onchange", CALLBACK_SIGNATURE, monacoEditor.getOnchange());
        wb.callback("onfocus", CALLBACK_SIGNATURE, monacoEditor.getOnfocus());
        wb.callback("oninitialized", CALLBACK_SIGNATURE, monacoEditor.getOninitialized());
        wb.callback("onkeyup", CALLBACK_SIGNATURE, monacoEditor.getOnkeyup());
        wb.callback("onmousedown", CALLBACK_SIGNATURE, monacoEditor.getOnmousedown());
        wb.callback("onmousemove", CALLBACK_SIGNATURE, monacoEditor.getOnmousemove());
        wb.callback("onmouseup", CALLBACK_SIGNATURE, monacoEditor.getOnmouseup());
        wb.callback("onkeydown", CALLBACK_SIGNATURE, monacoEditor.getOnkeydown());
        wb.callback("onpaste", CALLBACK_SIGNATURE, monacoEditor.getOnpaste());

        addWidgetProperties(wb, monacoEditor);

        encodeClientBehaviors(context, monacoEditor);
        wb.finish();
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue) {
        final T monacoEditor = componentClass.cast(component);
        final String value = (String) submittedValue;
        final Converter<?> converter = ComponentUtils.getConverter(context, monacoEditor);

        if (converter != null) {
            return converter.getAsObject(context, monacoEditor, value);
        }

        return value;
    }

    protected abstract String getMainStyleClass();

    protected abstract String getWidgetName();
}
