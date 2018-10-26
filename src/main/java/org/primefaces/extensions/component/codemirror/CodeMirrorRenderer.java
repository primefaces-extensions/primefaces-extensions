/**
 * Copyright 2011-2018 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.component.codemirror;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseId;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.event.CompleteEvent;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link CodeMirror} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class CodeMirrorRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext facesContext, final UIComponent component) {
        final CodeMirror codeMirror = (CodeMirror) component;

        if (codeMirror.isReadonly()) {
            return;
        }

        // set value
        final String clientId = codeMirror.getClientId(facesContext);
        final Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
            codeMirror.setSubmittedValue(params.get(clientId));
        }

        // decode behaviors
        decodeBehaviors(facesContext, component);

        // complete event
        final String token = params.get(clientId + "_token");
        if (token != null) {
            final String context = params.get(clientId + "_context");
            final int line = Integer.parseInt(params.get(clientId + "_line"));
            final int column = Integer.parseInt(params.get(clientId + "_column"));

            final CompleteEvent autoCompleteEvent = new CompleteEvent(
                        codeMirror, token, context, line, column);
            autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            codeMirror.queueEvent(autoCompleteEvent);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final CodeMirror codeMirror = (CodeMirror) component;

        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String token = params.get(codeMirror.getClientId(context) + "_token");

        if (token != null) {
            encodeSuggestions(context, codeMirror, codeMirror.getSuggestions());
        }
        else {
            encodeMarkup(context, codeMirror);
            encodeScript(context, codeMirror);
        }
    }

    protected void encodeMarkup(final FacesContext context, final CodeMirror codeMirror) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = codeMirror.getClientId(context);

        writer.startElement("textarea", codeMirror);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);

        renderPassThruAttributes(context, codeMirror, HTML.INPUT_TEXTAREA_ATTRS);
        renderDomEvents(context, codeMirror, HTML.INPUT_TEXT_EVENTS);

        final String valueToRender = ComponentUtils.getValueToRender(context, codeMirror);
        if (valueToRender != null) {
            if (codeMirror.isEscape()) {
                writer.writeText(valueToRender, null);
            }
            else {
                writer.write(valueToRender);
            }
        }

        writer.endElement("textarea");
    }

    protected void encodeScript(final FacesContext context, final CodeMirror codeMirror) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.initWithDomReady("ExtCodeMirror", codeMirror.resolveWidgetVar(), codeMirror.getClientId());
        wb.attr("theme", codeMirror.getTheme())
                    .attr("mode", codeMirror.getMode())
                    .attr("indentUnit", codeMirror.getIndentUnit())
                    .attr("smartIndent", codeMirror.isSmartIndent())
                    .attr("tabSize", codeMirror.getTabSize())
                    .attr("indentWithTabs", codeMirror.isIndentWithTabs())
                    .attr("electricChars", codeMirror.isElectricChars())
                    .attr("keyMap", codeMirror.getKeyMap())
                    .attr("lineWrapping", codeMirror.isLineWrapping())
                    .attr("lineNumbers", codeMirror.isLineNumbers())
                    .attr("firstLineNumber", codeMirror.getFirstLineNumber())
                    .attr("gutter", codeMirror.isGutter())
                    .attr("fixedGutter", codeMirror.isFixedGutter())
                    .attr("readOnly", codeMirror.isReadonly())
                    .attr("matchBrackets", codeMirror.isMatchBrackets())
                    .attr("workTime", codeMirror.getWorkTime())
                    .attr("workDelay", codeMirror.getWorkDelay())
                    .attr("pollInterval", codeMirror.getPollInterval())
                    .attr("tabindex", codeMirror.getTabindex())
                    .attr("undoDepth", codeMirror.getUndoDepth());

        if (codeMirror.getExtraKeys() != null) {
            wb.append(",extraKeys:" + codeMirror.getExtraKeys());
        }
        if (!codeMirror.isGlobal()) {
            wb.attr("global", false);
        }
        if (codeMirror.isAsync()) {
            wb.attr("async", true);
        }
        if (codeMirror.getProcess() != null) {
            wb.attr("process", SearchExpressionFacade.resolveClientIds(context, codeMirror, codeMirror.getProcess()));
        }
        if (codeMirror.getOnstart() != null) {
            wb.callback("onstart", "function(request)", codeMirror.getOnstart());
        }
        if (codeMirror.getOncomplete() != null) {
            wb.callback("oncomplete", "function(xhr, status, args)", codeMirror.getOncomplete());
        }
        if (codeMirror.getOnsuccess() != null) {
            wb.callback("onsuccess", "function(data, status, xhr)", codeMirror.getOnsuccess());
        }
        if (codeMirror.getOnerror() != null) {
            wb.callback("onerror", "function(xhr, status, error)", codeMirror.getOnerror());
        }

        encodeClientBehaviors(context, codeMirror);

        wb.finish();
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue) {
        final CodeMirror codeMirror = (CodeMirror) component;
        final String value = (String) submittedValue;
        final Converter converter = ComponentUtils.getConverter(context, component);

        if (converter != null) {
            return converter.getAsObject(context, codeMirror, value);
        }

        return value;
    }

    protected void encodeSuggestions(final FacesContext context, final CodeMirror codeMirror, final List<String> suggestions) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("ul", codeMirror);

        for (int i = 0; i < suggestions.size(); i++) {
            final String suggestion = suggestions.get(i);

            writer.startElement("li", null);

            if (codeMirror.isEscapeSuggestions()) {
                writer.writeText(suggestion, null);
            }
            else {
                writer.write(suggestion);
            }

            writer.endElement("li");
        }

        writer.endElement("ul");
    }
}
