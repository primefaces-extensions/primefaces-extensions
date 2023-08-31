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
package org.primefaces.extensions.component.suneditor;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.extensions.util.HtmlSanitizer;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>SunEditor</code> component.
 *
 * @author Matthieu Valente
 * @since 12.0.6
 */
public class SunEditorRenderer extends InputRenderer {

    private static final Logger LOGGER = Logger.getLogger(SunEditorRenderer.class.getName());

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final SunEditor editor = (SunEditor) component;

        if (!shouldDecode(editor)) {
            return;
        }

        // set value
        final String clientId = editor.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            String value = sanitizeHtml(context, editor, params.get(clientId));
            editor.setSubmittedValue(value);
        }

        // decode behaviors
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SunEditor editor = (SunEditor) component;
        checkSecurity(context, editor);
        encodeMarkup(context, editor);
        encodeScript(context, editor);
    }

    protected void encodeMarkup(final FacesContext context, final SunEditor editor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = editor.getClientId(context);
        String style = editor.getStyle();
        String styleClass = createStyleClass(editor, SunEditor.EDITOR_CLASS);

        writer.startElement("textarea", editor);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        renderAccessibilityAttributes(context, editor);
        renderPassThruAttributes(context, editor, HTML.TEXTAREA_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, editor, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, editor);

        String valueToRender = sanitizeHtml(context, editor, ComponentUtils.getValueToRender(context, editor));
        if (valueToRender != null) {
            writer.write(valueToRender);
        }

        writer.endElement("textarea");
    }

    protected void encodeScript(final FacesContext context, final SunEditor editor) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtSunEditor", editor)
                    .attr("width", editor.getWidth())
                    .attr("height", editor.getHeight())
                    .attr("mode", editor.getMode(), "classic")
                    .attr("rtl", ComponentUtils.isRTL(context, editor), false)
                    .attr("locale", editor.calculateLocale().toString())
                    .attr("readOnly", editor.isReadonly(), false)
                    .attr("disabled", editor.isDisabled(), false)
                    .attr("toolbar", editor.getToolbar())
                    .nativeAttr("extender", editor.getExtender());

        encodeClientBehaviors(context, editor);
        wb.finish();
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component,
                final Object submittedValue) {
        final SunEditor editor = (SunEditor) component;
        final String value = (String) submittedValue;
        final Converter converter = ComponentUtils.getConverter(context, component);

        if (converter != null) {
            return converter.getAsObject(context, editor, value);
        }

        return value;
    }

    /**
     * Enforce security by default requiring the OWASP sanitizer on the classpath. Only if a user marks the editor with secure="false" will they opt-out of
     * security.
     *
     * @param context the FacesContext
     * @param editor the editor to check for security
     */
    private void checkSecurity(FacesContext context, SunEditor editor) {
        boolean sanitizerAvailable = PrimeApplicationContext.getCurrentInstance(context).getEnvironment()
                    .isHtmlSanitizerAvailable();
        if (editor.isSecure() && !sanitizerAvailable) {
            throw new FacesException(
                        "SunEditor component is marked secure='true' but the HTML Sanitizer was not found on the classpath. "
                                    + "Either add the HTML sanitizer to the classpath per the documentation"
                                    + " or mark secure='false' if you would like to use the component without the sanitizer.");
        }
    }

    /**
     * If security is enabled sanitize the HTML string to prevent XSS.
     *
     * @param context the FacesContext
     * @param editor the TextEditor instance
     * @param value the value to sanitize
     * @return the sanitized value
     */
    protected String sanitizeHtml(FacesContext context, SunEditor editor, String value) {
        String result = value;
        if (editor.isSecure()
                    && PrimeApplicationContext.getCurrentInstance(context).getEnvironment().isHtmlSanitizerAvailable()) {
            result = HtmlSanitizer.sanitizeHtml(value, editor.isAllowBlocks(), editor.isAllowFormatting(),
                        editor.isAllowLinks(), editor.isAllowStyles(), editor.isAllowImages(), editor.isAllowTables(), editor.isAllowMedia());
        }
        else {
            if (!editor.isAllowBlocks() || !editor.isAllowFormatting() || !editor.isAllowLinks()
                        || !editor.isAllowStyles() || !editor.isAllowImages() || !editor.isAllowTables()) {
                LOGGER.warning("HTML sanitizer not available - skip sanitizing....");
            }
        }
        return result;
    }
}
