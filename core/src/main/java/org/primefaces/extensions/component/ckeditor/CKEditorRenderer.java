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
package org.primefaces.extensions.component.ckeditor;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link CKEditor} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class CKEditorRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final CKEditor ckEditor = (CKEditor) component;

        if (!shouldDecode(ckEditor)) {
            return;
        }

        // set value
        final String clientId = ckEditor.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            ckEditor.setSubmittedValue(params.get(clientId));
        }

        // decode behaviors
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final CKEditor ckEditor = (CKEditor) component;

        encodeMarkup(context, ckEditor);
        encodeScript(context, ckEditor);
    }

    protected void encodeMarkup(final FacesContext context, final CKEditor ckEditor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ckEditor.getClientId(context);

        writer.startElement("textarea", ckEditor);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);

        if (ckEditor.getTabindex() != null) {
            writer.writeAttribute(Attrs.TABINDEX, ckEditor.getTabindex(), null);
        }

        renderAccessibilityAttributes(context, ckEditor);
        renderPassThruAttributes(context, ckEditor, HTML.TEXTAREA_ATTRS_WITHOUT_EVENTS);
        renderDomEvents(context, ckEditor, HTML.INPUT_TEXT_EVENTS);
        renderValidationMetadata(context, ckEditor);

        final String valueToRender = ComponentUtils.getValueToRender(context, ckEditor);
        if (valueToRender != null) {
            if (ckEditor.isEscape()) {
                writer.writeText(valueToRender, null);
            }
            else {
                writer.write(valueToRender);
            }
        }

        writer.endElement("textarea");
    }

    protected void encodeScript(final FacesContext context, final CKEditor ckEditor) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCKEditor", ckEditor);
        wb.attr("height", ckEditor.getHeight())
                    .attr("width", ckEditor.getWidth())
                    .attr("skin", ckEditor.getSkin())
                    .attr("toolbar", ckEditor.getToolbar())
                    .attr("readOnly", ckEditor.isReadonly())
                    .attr("interfaceColor", ckEditor.getInterfaceColor())
                    .attr("language", ckEditor.getLanguage())
                    .attr("defaultLanguage", ckEditor.getDefaultLanguage())
                    .attr("customConfig", ckEditor.getCustomConfig())
                    .attr("advancedContentFilter", ckEditor.isAdvancedContentFilter())
                    .attr("disableNativeSpellChecker", ckEditor.isDisableNativeSpellChecker())
                    .attr(Attrs.TABINDEX, ckEditor.getTabindex())
                    .attr("font", ckEditor.getFont())
                    .attr("fontSize", ckEditor.getFontSize())
                    .attr("enterMode", ckEditor.getEnterMode())
                    .attr("shiftEnterMode", ckEditor.getShiftEnterMode());

        if (ckEditor.getContentsCss() != null && ckEditor.getContentsCss().startsWith("[")) {
            // new :: Array of CSS-Files :: ['/path/css1.css','/path/css2.css']
            wb.nativeAttr("contentsCss", ckEditor.getContentsCss());
        }
        else {
            // default behaviour
            wb.attr("contentsCss", ckEditor.getContentsCss());
        }
        encodeClientBehaviors(context, ckEditor);
        wb.finish();
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component,
                final Object submittedValue) {
        final CKEditor ckEditor = (CKEditor) component;
        final String value = (String) submittedValue;
        final Converter<?> converter = ComponentUtils.getConverter(context, component);

        if (converter != null) {
            return converter.getAsObject(context, ckEditor, value);
        }

        return value;
    }
}
