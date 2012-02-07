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
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.primefaces.extensions.renderkit.widget.WidgetRenderer;
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
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final CodeMirror codeMirror = (CodeMirror) component;

		encodeMarkup(context, codeMirror);
		encodeScript(context, codeMirror);
	}

	protected void encodeMarkup(final FacesContext context, final CodeMirror codeMirror) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = codeMirror.getClientId(context);

		writer.startElement("textarea", codeMirror);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("name", clientId, null);

		if (codeMirror.getValue() != null) {
			writer.write(codeMirror.getValue().toString());
		}

		writer.endElement("textarea");
	}

	protected void encodeScript(final FacesContext context, final CodeMirror codeMirror) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = codeMirror.getClientId(context);
		final String widgetVar = codeMirror.resolveWidgetVar();

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('CodeMirror', '" + widgetVar + "', {");

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
}
