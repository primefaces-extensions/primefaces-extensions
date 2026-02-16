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
package org.primefaces.extensions.component.suneditor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.convert.Converter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>SunEditor</code> component.
 *
 * @author Matthieu Valente
 * @since 12.0.6
 */
@FacesRenderer(rendererType = SunEditor.DEFAULT_RENDERER, componentFamily = SunEditor.COMPONENT_FAMILY)
public class SunEditorRenderer extends InputRenderer<SunEditor> {

    @Override
    public void decode(final FacesContext context, final SunEditor component) {
        if (!shouldDecode(component)) {
            return;
        }

        // set value
        final String clientId = component.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            String value = component.sanitizeHtml(context, params.get(clientId));
            component.setSubmittedValue(value);
        }

        // decode behaviors
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final SunEditor component) throws IOException {
        component.checkSecurity(context);
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    protected void encodeMarkup(final FacesContext context, final SunEditor component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        String style = component.getStyle();
        String styleClass = createStyleClass(component, SunEditor.EDITOR_CLASS);

        writer.startElement("textarea", component);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        renderAccessibilityAttributes(context, component);
        renderPassThruAttributes(context, component, HTML.TEXTAREA_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, component, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, component);

        String valueToRender = component.sanitizeHtml(context, ComponentUtils.getValueToRender(context, component));
        if (valueToRender != null) {

            // #1886 bug in sanitizer for font-family that has to be fixed
            if (component.isSecure()) {
                valueToRender = fixFontFamily(valueToRender);
            }
            // #1639 do not escape as its already been sanitized above, and we want to keep exact formatting
            writer.writeText(valueToRender, "value");
        }

        writer.endElement("textarea");
    }

    /**
     * DIRTY HACK: Fixes the font-family declaration in the given HTML/CSS string by replacing any font name enclosed in `&#39;...&#39;` with its properly
     * capitalized version.
     * <p>
     * Example:
     *
     * <pre>
     * Input:  font-family:&#39;courier new&#39;
     * Output: font-family: Courier New
     * </pre>
     *
     * @param valueToRender the input string containing font-family declarations
     * @return the modified string with font names properly capitalized and single quotes replaced
     * @see <a href="https://github.com/OWASP/java-html-sanitizer/issues/232">OWASP Bug #232</a>
     */
    protected String fixFontFamily(String valueToRender) {
        // Regex to match font-family declarations with any font name inside &#39;...&#39;
        String regex = "font-family:\\s*&#39;([^&#]+)&#39;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(valueToRender);

        // StringBuilder for efficient string manipulation
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            // Extract the font name inside &#39;...&#39;
            String fontName = matcher.group(1);

            // Capitalize each word in the font name separately
            String correctedFont = Arrays.stream(fontName.split("\\s+")) // Split by space
                        .map(LangUtils::capitalize) // Capitalize each word
                        .collect(Collectors.joining(" ")); // Join back with spaces

            if (correctedFont.endsWith(" Ms")) {
                correctedFont = correctedFont.replace(" Ms", " MS");
            }
            if ("tahoma".equalsIgnoreCase(correctedFont)) {
                correctedFont = correctedFont.replace("Tahoma", "tahoma");
            }

            // Replace the matched pattern with the corrected font-family declaration
            matcher.appendReplacement(result, "font-family: " + correctedFont);
        }

        // Append any remaining part of the input string
        matcher.appendTail(result);

        return result.toString();
    }

    protected void encodeScript(final FacesContext context, final SunEditor component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtSunEditor", component)
                    .attr("width", component.getWidth())
                    .attr("height", component.getHeight())
                    .attr("mode", component.getMode(), "classic")
                    .attr("rtl", ComponentUtils.isRTL(context, component), false)
                    .attr("locale", component.calculateLocale().toString())
                    .attr("readOnly", component.isReadonly(), false)
                    .attr("disabled", component.isDisabled(), false)
                    .attr("strictMode", component.isStrictMode(), true)
                    .attr("toolbar", component.getToolbar())
                    .nativeAttr("extender", component.getExtender());

        encodeClientBehaviors(context, component);
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
}