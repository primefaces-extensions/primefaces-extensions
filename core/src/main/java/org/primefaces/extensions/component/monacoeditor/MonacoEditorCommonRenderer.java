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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.context.PrimeRequestContext;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.shaded.json.JSONWriter;
import org.primefaces.shaded.owasp.encoder.Encode;
import org.primefaces.util.WidgetBuilder;

/**
 * Base renderer for both the {@link MonacoEditorFramed framed} and {@link MonacoEditorInline inline} monaco editor as well as the diff editor.
 *
 * @since 11.1.0
 */
abstract class MonacoEditorCommonRenderer<TEditor extends MonacoEditorCommon<TEditorOpts>, TEditorOpts> extends InputRenderer {

    protected static final String CALLBACK_SIGNATURE = "function()";

    protected final Class<TEditor> componentClass;

    protected MonacoEditorCommonRenderer(Class<TEditor> clazz) {
        this.componentClass = clazz;
    }

    protected abstract void addBaseWidgetProperties(FacesContext context, WidgetBuilder wb, TEditor monacoEditor) throws IOException;

    protected final void array(WidgetBuilder wb, String key, Iterable<String> values) throws IOException {
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
    public final void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final TEditor monacoEditor = componentClass.cast(component);
        encodeMarkup(context, monacoEditor);
        encodeScript(context, monacoEditor);
    }

    protected final void encodeMarkup(final FacesContext context, final TEditor monacoEditor) throws IOException {
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
        if (isEntireEditorDisabled(monacoEditor)) {
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

    protected abstract boolean isEntireEditorDisabled(TEditor monacoEditor);

    protected abstract void encodeHiddenInput(FacesContext context, TEditor monacoEditor) throws IOException;

    protected abstract void encodeMonacoEditor(final FacesContext context, final TEditor monacoEditor) throws IOException;

    protected final void encodeScript(final FacesContext context, final TEditor monacoEditor) throws IOException {
        final WidgetBuilder wb = PrimeRequestContext.getCurrentInstance(context).getWidgetBuilder();

        wb.init(getWidgetName(), monacoEditor);

        array(wb, "availableEvents", monacoEditor.getEventNames());

        if (monacoEditor.getCustomThemes() != null && !monacoEditor.getCustomThemes().isEmpty()) {
            wb.nativeAttr("customThemes", JSONWriter.valueToString(monacoEditor.getCustomThemes()));
        }

        wb.attr("autoResize", monacoEditor.isAutoResize(), MonacoEditorCommon.DEFAULT_AUTO_RESIZE);
        wb.attr("basename", monacoEditor.getBasename(), MonacoEditorCommon.DEFAULT_BASENAME);
        wb.attr("directory", monacoEditor.getDirectory(), MonacoEditorCommon.DEFAULT_DIRECTORY);
        wb.attr("disabled", monacoEditor.isDisabled(), MonacoEditorCommon.DEFAULT_DISABLED);
        wb.attr("editorOptions", monacoEditor.getEditorOptions().toString());
        wb.attr("extension", monacoEditor.getExtension(), MonacoEditorCommon.DEFAULT_EXTENSION);
        wb.attr("language", getLanguage(monacoEditor), MonacoEditorCommon.DEFAULT_LANGUAGE);
        wb.attr("locale", monacoEditor.calculateLocale().toString());
        wb.attr("localeUrl", monacoEditor.getLocaleUrl());
        wb.attr("readonly", monacoEditor.isReadonly(), MonacoEditorCommon.DEFAULT_READONLY);
        wb.attr("scheme", monacoEditor.getScheme(), MonacoEditorCommon.DEFAULT_SCHEME);
        wb.attr("tabIndex", monacoEditor.getTabindex(), MonacoEditorCommon.DEFAULT_TABINDEX);
        wb.attr("height", monacoEditor.getHeight(), MonacoEditorCommon.DEFAULT_HEIGHT);
        wb.attr("width", monacoEditor.getWidth(), MonacoEditorCommon.DEFAULT_WIDTH);

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

        addBaseWidgetProperties(context, wb, monacoEditor);

        encodeClientBehaviors(context, monacoEditor);
        wb.finish();
    }

    protected abstract String getLanguage(TEditor monacoEditor);

    protected abstract String getMainStyleClass();

    protected abstract String getWidgetName();
}
