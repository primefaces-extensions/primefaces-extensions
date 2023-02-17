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
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.config.PrimeConfiguration;
import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.util.WidgetBuilder;

/**
 * Base renderer for both the {@link MonacoEditorFramed framed} and {@link MonacoEditorInline inline} monaco editor.
 *
 * @since 11.1.0
 */
abstract class MonacoDiffEditorBaseRenderer<TEditor extends MonacoDiffEditorBase>
                                           extends
                                           MonacoEditorCommonRenderer<TEditor, org.primefaces.extensions.model.monacoeditor.DiffEditorOptions> {
    private static final String INPUT_SUFFIX = "_input";
    private static final String INPUT_ORIGINAL_SUFFIX = "_input_original";

    private static final List<String> PASSTHROUGH_ATTRS = Arrays.asList(//
                "alt", "accesskey", "autocomplete", //
                "cols", "dir", "lang", //
                "maxlength", "placeholder", "rows", //
                "size", "title" //
    );

    protected MonacoDiffEditorBaseRenderer(final Class<TEditor> clazz) {
        super(clazz);
    }

    @Override
    public final void decode(final FacesContext context, final UIComponent component) {
        final TEditor monacoEditor = componentClass.cast(component);

        final String clientId = monacoEditor.getClientId() + INPUT_SUFFIX;
        final String originalClientId = monacoEditor.getClientId() + INPUT_ORIGINAL_SUFFIX;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        boolean update = false;
        String originalValue = null;
        String modifiedValue = null;

        // Do not allow modifications if component is not allowed to submit values.
        // Read-only is fine, we should still accept the submitted value when read-only
        if (!monacoEditor.isDisabled()) {
            if (params.containsKey(clientId)) {
                modifiedValue = params.get(clientId);
                update = true;
            }
        }
        if (!monacoEditor.isOriginalDisabled()) {
            if (params.containsKey(originalClientId)) {
                originalValue = params.get(originalClientId);
                update = true;
            }
        }

        if (update) {
            final Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(originalValue, modifiedValue);
            monacoEditor.setSubmittedValue(entry);
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
        renderPassThruAttributes(context, monacoEditor, PASSTHROUGH_ATTRS);

        final org.primefaces.extensions.model.monaco.MonacoDiffEditorModel valueToRender;
        valueToRender = (org.primefaces.extensions.model.monaco.MonacoDiffEditorModel) getValueToRender(
                    context, monacoEditor);

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
        if (valueToRender != null && valueToRender.getModifiedValue() != null) {
            writer.writeText(valueToRender.getModifiedValue(), null);
        }
        writer.endElement("textarea");

        writer.startElement("textarea", null);
        writer.writeAttribute("id", clientId + INPUT_ORIGINAL_SUFFIX, null);
        writer.writeAttribute("name", clientId + INPUT_ORIGINAL_SUFFIX, null);
        writer.writeAttribute("autocomplete", "off", null);
        if (monacoEditor.isOriginalDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        if (monacoEditor.isOriginalReadonly()) {
            writer.writeAttribute("readonly", "readonly", null);
        }
        if (valueToRender != null && valueToRender.getOriginalValue() != null) {
            writer.writeText(valueToRender.getOriginalValue(), null);
        }
        writer.endElement("textarea");

        writer.endElement("div");
    }

    @Override
    public final Object getConvertedValue(final FacesContext context, final UIComponent component,
                final Object submittedValue) {
        return convertedSubmittedValue(component, submittedValue);
    }

    @Override
    protected final void addBaseWidgetProperties(final FacesContext context, final WidgetBuilder wb,
                final TEditor monacoEditor)
                throws IOException {
        wb.attr("originalDisabled", monacoEditor.isOriginalDisabled(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_DISABLED);
        wb.attr("originalReadonly", monacoEditor.isOriginalReadonly(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_READONLY);
        wb.attr("originalLanguage", monacoEditor.getOriginalLanguage(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_LANGUAGE);
        wb.attr("originalRequired", monacoEditor.isOriginalRequired(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_REQUIRED);

        wb.attr("originalScheme", monacoEditor.getScheme(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_SCHEME);
        wb.attr("originalDirectory", monacoEditor.getDirectory(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_DIRECTORY);
        wb.attr("originalBasename", monacoEditor.getBasename(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_BASENAME);
        wb.attr("originalExtension", monacoEditor.getExtension(), MonacoDiffEditorBase.DEFAULT_ORIGINAL_EXTENSION);

        wb.callback("onoriginalblur", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalblur());
        wb.callback("onoriginalchange", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalchange());
        wb.callback("onoriginalfocus", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalfocus());
        wb.callback("onoriginalkeyup", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalkeyup());
        wb.callback("onoriginalmousedown", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalmousedown());
        wb.callback("onoriginalmousemove", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalmousemove());
        wb.callback("onoriginalmouseup", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalmouseup());
        wb.callback("onoriginalkeydown", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalkeydown());
        wb.callback("onoriginalpaste", CALLBACK_SIGNATURE, monacoEditor.getOnoriginalpaste());

        addWidgetProperties(context, wb, monacoEditor);
    }

    protected abstract void addWidgetProperties(FacesContext context, WidgetBuilder wb, TEditor monacoEditor)
                throws IOException;

    @Override
    protected String getLanguage(final TEditor monacoEditor) {
        return monacoEditor.getLanguage();
    }

    @Override
    protected boolean isEntireEditorDisabled(final TEditor monacoEditor) {
        return monacoEditor.isDisabled() && monacoEditor.isOriginalDisabled();
    }

    private static Object getValueToRender(final FacesContext context, final MonacoDiffEditorBase component) {
        final Object submittedValue = component.getSubmittedValue();
        final PrimeConfiguration config = PrimeApplicationContext.getCurrentInstance(context).getConfig();
        if (config.isInterpretEmptyStringAsNull() && submittedValue == null && !component.isLocalValueSet() &&
                    context.isValidationFailed()
                    && !component.isValid()) {
            return null;
        }
        else if (submittedValue != null) {
            return convertedSubmittedValue(component, submittedValue);
        }
        else {
            final Object value = component.getValue();
            return value instanceof org.primefaces.extensions.model.monaco.MonacoDiffEditorModel
                        ? (org.primefaces.extensions.model.monaco.MonacoDiffEditorModel) value
                        : org.primefaces.extensions.model.monaco.MonacoDiffEditorModel.empty();
        }
    }

    public static org.primefaces.extensions.model.monaco.MonacoDiffEditorModel convertedSubmittedValue(
                final UIComponent component, final Object submittedValue) {
        if (submittedValue == null) {
            return org.primefaces.extensions.model.monaco.MonacoDiffEditorModel.empty();
        }
        if (submittedValue instanceof org.primefaces.extensions.model.monaco.MonacoDiffEditorModel) {
            return (org.primefaces.extensions.model.monaco.MonacoDiffEditorModel) submittedValue;
        }
        final MonacoDiffEditorBase editor = (MonacoDiffEditorBase) component;
        final org.primefaces.extensions.model.monaco.MonacoDiffEditorModel currentModel = editor.getValue() != null
                    //
                    ? (org.primefaces.extensions.model.monaco.MonacoDiffEditorModel) editor.getValue()
                    //
                    : org.primefaces.extensions.model.monaco.MonacoDiffEditorModel.empty();
        @SuppressWarnings("unchecked")
        final Entry<String, String> value = (Map.Entry<String, String>) submittedValue;
        final String originalValue = value.getKey() != null ? value.getKey() : currentModel.getOriginalValue();
        final String modifiedValue = value.getValue() != null ? value.getValue() : currentModel.getModifiedValue();
        return new org.primefaces.extensions.model.monaco.MonacoDiffEditorModel(originalValue, modifiedValue);
    }
}
