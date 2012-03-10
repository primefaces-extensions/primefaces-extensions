/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.codemirror;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseId;

import org.primefaces.extensions.event.CompleteEvent;
import org.primefaces.extensions.renderkit.widget.WidgetRenderer;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.InputRenderer;

/**
 * Renderer for the {@link CodeMirror} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class CodeMirrorRenderer extends InputRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		final CodeMirror codeMirror = (CodeMirror) component;

		if (codeMirror.isReadOnly()) {
			return;
		}

		// set value
        final String clientId = codeMirror.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        if (params.containsKey(clientId)) {
        	codeMirror.setSubmittedValue(params.get(clientId));
        }

        // decode behaviors
		decodeBehaviors(context, component);

        // Complete event
        final String query = params.get(clientId + "_query");
        if (query != null) {
            final CompleteEvent autoCompleteEvent = new CompleteEvent(codeMirror, query);
            autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);

            codeMirror.queueEvent(autoCompleteEvent);
        }
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final CodeMirror codeMirror = (CodeMirror) component;

        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String query = params.get(codeMirror.getClientId(context) + "_query");

        if (query != null) {
        	encodeSuggestions(context, codeMirror, codeMirror.getSuggestions());
        } else {
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

		if (codeMirror.getValue() != null) {
			writer.write(ComponentUtils.getValueToRender(context, codeMirror));
		}

		writer.endElement("textarea");
	}

	protected void encodeScript(final FacesContext context, final CodeMirror codeMirror) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = codeMirror.getClientId(context);
		final String widgetVar = codeMirror.resolveWidgetVar();

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('" + CodeMirror.class.getSimpleName() + "', '" + widgetVar + "', {");

		WidgetRenderer.renderOptions(clientId, writer, codeMirror);

		encodeClientBehaviors(context, codeMirror);

		writer.write("}, true);});");

		endScript(writer);
	}

    @Override
	public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue) {
    	final CodeMirror codeMirror = (CodeMirror) component;
    	final String value = (String) submittedValue;
    	final Converter converter = codeMirror.getConverter();

		if (converter != null) {
			return converter.getAsObject(context, codeMirror, value);
		}

		final ValueExpression ve = codeMirror.getValueExpression("value");

		if (ve != null) {
		    final Class<?> valueType = ve.getType(context.getELContext());
		    final Converter converterForType = context.getApplication().createConverter(valueType);

		    if (converterForType != null) {
		        return converterForType.getAsObject(context, codeMirror, value);
		    }
		}

		return value;
	}

    protected void encodeSuggestions(final FacesContext context, final CodeMirror codeMirror, final List<String> suggestions) throws IOException {
    	final ResponseWriter writer = context.getResponseWriter();

    	final StringBuilder suggestionsBuilder = new StringBuilder();

    	for (int i = 0; i < suggestions.size(); i++) {
    		final String suggestion = suggestions.get(i);

    		if (i > 0) {
    			suggestionsBuilder.append(',');
    		}

    		suggestionsBuilder.append(suggestion);
    	}

    	writer.writeText(suggestionsBuilder.toString(), codeMirror, null);


    	/*
    	writer.startElement("ul", codeMirror);

    	for (int i = 0; i < suggestions.size(); i++) {
    		final String suggestion = suggestions.get(i);

    		writer.startElement("li", null);
    		writer.writeText(suggestion, null);
    		writer.endElement("li");
    	}

    	writer.endElement("ul");
    	*/
    }
}
