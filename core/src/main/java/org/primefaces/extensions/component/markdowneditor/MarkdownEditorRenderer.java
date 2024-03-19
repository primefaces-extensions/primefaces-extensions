/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
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
package org.primefaces.extensions.component.markdowneditor;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Markdown Editor.
 *
 * @since 14.0.0
 */
public class MarkdownEditorRenderer extends InputRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        MarkdownEditor editor = (MarkdownEditor) component;

        if (!shouldDecode(editor)) {
            return;
        }

        // set value
        String clientId = editor.getClientId(context);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            String value = editor.sanitizeHtml(context, params.get(clientId));
            editor.setSubmittedValue(value);
        }

        // decode behaviors
        decodeBehaviors(context, editor);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        MarkdownEditor editor = (MarkdownEditor) component;
        editor.checkSecurity(context);
        encodeMarkup(context, editor);
        encodeScript(context, editor);
    }

    protected void encodeScript(FacesContext context, MarkdownEditor editor) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtMarkdownEditor", editor)
                    .attr("minHeight", editor.getMinHeight(), "300px")
                    .attr("maxHeight", editor.getMaxHeight(), null)
                    .attr("mode", editor.getMode())
                    .attr("toolbar", editor.getToolbar())
                    .attr("placeholder", editor.getPlaceholder())
                    .attr("direction", ComponentUtils.isRTL(context, editor) ? "rtl" : "ltr", "ltr")
                    .attr("sideBySideFullscreen", editor.getSideBySideFullscreen(), true)
                    .attr("indentWithTabs", editor.getIndentWithTabs(), true)
                    .attr("lineNumbers", editor.getLineNumbers(), false)
                    .attr("promptURLs", editor.getPromptURLs(), false)
                    .attr("tabSize", editor.getTabSize(), 2)
                    .nativeAttr("extender", editor.getExtender());

        encodeClientBehaviors(context, editor);

        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, MarkdownEditor editor) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = editor.getClientId(context);

        writer.startElement("textarea", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("class", createStyleClass(editor, InputTextarea.STYLE_CLASS), "styleClass");
        writer.writeAttribute("style", "display:none", "style");

        renderAccessibilityAttributes(context, editor);
        renderRTLDirection(context, editor);
        renderPassThruAttributes(context, editor, HTML.TEXTAREA_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, editor, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, editor);

        String valueToRender = ComponentUtils.getValueToRender(context, editor);
        if (valueToRender != null) {
            writer.writeText(valueToRender, "value");
        }

        writer.endElement("textarea");
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component,
                final Object submittedValue) {
        final MarkdownEditor editor = (MarkdownEditor) component;
        final String value = (String) submittedValue;
        final Converter converter = ComponentUtils.getConverter(context, component);

        if (converter != null) {
            return converter.getAsObject(context, editor, value);
        }

        return value;
    }
}