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
package org.primefaces.extensions.component.markdowneditor;

import java.io.IOException;
import java.util.Map;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.render.FacesRenderer;

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
@FacesRenderer(rendererType = MarkdownEditor.DEFAULT_RENDERER, componentFamily = MarkdownEditor.COMPONENT_FAMILY)
public class MarkdownEditorRenderer extends InputRenderer<MarkdownEditor> {

    @Override
    public void decode(FacesContext context, MarkdownEditor component) {
        if (!shouldDecode(component)) {
            return;
        }

        // set value
        String clientId = component.getClientId(context);
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            String value = component.sanitizeHtml(context, params.get(clientId));
            component.setSubmittedValue(value);
        }

        // decode behaviors
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, MarkdownEditor component) throws IOException {
        component.checkSecurity(context);
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeScript(FacesContext context, MarkdownEditor component) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtMarkdownEditor", component)
                    .attr("minHeight", component.getMinHeight(), "300px")
                    .attr("maxHeight", component.getMaxHeight(), null)
                    .attr("mode", component.getMode())
                    .attr("toolbar", component.getToolbar())
                    .attr("placeholder", component.getPlaceholder())
                    .attr("direction", ComponentUtils.isRTL(context, component) ? "rtl" : "ltr", "ltr")
                    .attr("sideBySideFullscreen", component.isSideBySideFullscreen(), true)
                    .attr("indentWithTabs", component.isIndentWithTabs(), true)
                    .attr("lineNumbers", component.isLineNumbers(), false)
                    .attr("promptURLs", component.isPromptURLs(), false)
                    .attr("tabSize", component.getTabSize(), 2)
                    .nativeAttr("extender", component.getExtender());

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    protected void encodeMarkup(FacesContext context, MarkdownEditor component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);

        writer.startElement("textarea", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("class", createStyleClass(component, InputTextarea.STYLE_CLASS), "styleClass");
        writer.writeAttribute("style", "display:none", "style");

        renderAccessibilityAttributes(context, component);
        renderRTLDirection(context, component);
        renderPassThruAttributes(context, component, HTML.TEXTAREA_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, component, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, component);

        String valueToRender = ComponentUtils.getValueToRender(context, component);
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
