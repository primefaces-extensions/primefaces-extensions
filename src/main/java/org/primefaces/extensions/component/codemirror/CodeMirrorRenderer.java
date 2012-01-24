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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.InputRenderer;

/**
 * Renderer for the {@link CodeMirror} component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ListenerFor(sourceClass = CodeMirror.class, systemEventClass = PostAddToViewEvent.class)
public class CodeMirrorRenderer extends InputRenderer implements ComponentSystemEventListener {

	private static final List<String> MODES_WITH_STYLESHEET = new ArrayList<String>();
	private static final String MODE_DIRECTORY = "codemirror/mode/";

	static {
		MODES_WITH_STYLESHEET.add("diff");
		MODES_WITH_STYLESHEET.add("tiddlywiki");
	}

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

		writer.write(widgetVar + " = new PrimeFacesExt.widget.CodeMirror('" + clientId + "', {");

		// options
		writer.write("readOnly:" + codeMirror.isReadOnly());

		if (codeMirror.getMode() != null) {
			writer.write(",mode:'" + codeMirror.getMode() + "'");
		}

		if (codeMirror.getFirstLineNumber() != null) {
			writer.write(",firstLineNumber:" + codeMirror.getFirstLineNumber());
		}

		if (codeMirror.getIndentUnit() != null) {
			writer.write(",indentUnit:" + codeMirror.getIndentUnit());
		}

		if (codeMirror.getKeyMap() != null) {
			writer.write(",keyMap:'" + codeMirror.getKeyMap() + "'");
		}

		if (codeMirror.getPollInterval() != null) {
			writer.write(",pollInterval:" + codeMirror.getPollInterval());
		}

		if (codeMirror.getTabSize() != null) {
			writer.write(",tabSize:" + codeMirror.getTabSize());
		}

		if (codeMirror.getTheme() != null) {
			writer.write(",theme:'" + codeMirror.getTheme() + "'");
		}

		if (codeMirror.getUndoDepth() != null) {
			writer.write(",undoDepth:" + codeMirror.getUndoDepth());
		}

		if (codeMirror.getWorkDelay() != null) {
			writer.write(",workDelay:" + codeMirror.getWorkDelay());
		}

		if (codeMirror.getWorkTime() != null) {
			writer.write(",workTime:" + codeMirror.getWorkTime());
		}

		if (codeMirror.isElectricChars() != null) {
			writer.write(",electricChars:" + codeMirror.isElectricChars());
		}

		if (codeMirror.isFixedGutter() != null) {
			writer.write(",fixedGutter:" + codeMirror.isFixedGutter());
		}

		if (codeMirror.isGutter() != null) {
			writer.write(",gutter:" + codeMirror.isGutter());
		}

		if (codeMirror.isIndentWithTabs() != null) {
			writer.write(",indentWithTabs:" + codeMirror.isIndentWithTabs());
		}

		if (codeMirror.isLineNumbers() != null) {
			writer.write(",lineNumbers:" + codeMirror.isLineNumbers());
		}

		if (codeMirror.isLineWrapping() != null) {
			writer.write(",lineWrapping:" + codeMirror.isLineWrapping());
		}

		if (codeMirror.isMatchBrackets() != null) {
			writer.write(",matchBrackets:" + codeMirror.isMatchBrackets());
		}

		if (codeMirror.isSmartIndent() != null) {
			writer.write(",smartIndent:" + codeMirror.isSmartIndent());
		}

		if (codeMirror.getExtraKeys() != null) {
			writer.write(",extraKeys:" + codeMirror.getExtraKeys());
		}

		encodeClientBehaviors(context, codeMirror);

		writer.write("});});");

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

	@Override
	public void processEvent(final ComponentSystemEvent event) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final CodeMirror codeMirror = (CodeMirror) event.getComponent();

		addScript(codeMirror, context);
		addStylesheet(codeMirror, context);
	}

	protected UIComponent addScript(final CodeMirror codeMirror, final FacesContext context) {
		if (codeMirror.getMode() != null) {
			if ("spec".equals(codeMirror.getMode()) || "changes".equals(codeMirror.getMode())) {
				ComponentUtils.addComponentResource(
						context,
						MODE_DIRECTORY + "rpm/" + codeMirror.getMode() + "/" + codeMirror.getMode() + ".js");
			} else {
				ComponentUtils.addComponentResource(
						context,
						MODE_DIRECTORY + codeMirror.getMode() + "/" + codeMirror.getMode() + ".js");
			}
		}

		return null;
	}

	protected void addStylesheet(final CodeMirror codeMirror, final FacesContext context) {
		if (codeMirror.getMode() != null) {
			if (MODES_WITH_STYLESHEET.contains(codeMirror.getMode())) {
				ComponentUtils.addComponentResource(
						context,
						MODE_DIRECTORY + codeMirror.getMode() + "/" + codeMirror.getMode() + ".css");
			}

			if ("spec".equals(codeMirror.getMode())) {
				ComponentUtils.addComponentResource(
						context,
						MODE_DIRECTORY + "rpm/" + codeMirror.getMode() + "/" + codeMirror.getMode() + ".css");
			}
		}
	}
}
