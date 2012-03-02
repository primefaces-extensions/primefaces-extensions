/*
 * Copyright 2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.tristatecheckbox;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * TriStateCheckboxRenderer
 *
 * @author  Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since   0.3
 */
public class TriStateCheckboxRenderer extends InputRenderer {

	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		TriStateCheckbox checkbox = (TriStateCheckbox) component;

		if (checkbox.isDisabled()) {
			return;
		}

		decodeBehaviors(context, checkbox);

		String clientId = checkbox.getClientId(context);
		String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");

		if (submittedValue != null) {
			checkbox.setSubmittedValue(submittedValue);
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		TriStateCheckbox checkbox = (TriStateCheckbox) component;

		encodeMarkup(context, checkbox);
		encodeScript(context, checkbox);
	}

	protected void encodeMarkup(final FacesContext context, final TriStateCheckbox checkbox) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = checkbox.getClientId(context);

		int valCheck = 0;
		try {
			valCheck = Integer.valueOf(ComponentUtils.getValueToRender(context, checkbox));
		} catch (Exception ex) {
			valCheck = 0;
		}

		if (valCheck > 2 || valCheck < 0) {
			valCheck = 0;
		}

		boolean disabled = checkbox.isDisabled();

		String style = checkbox.getStyle();
		String styleClass = checkbox.getStyleClass();
		styleClass = styleClass == null ? HTML.CHECKBOX_CLASS : HTML.CHECKBOX_CLASS + " " + styleClass;

		writer.startElement("div", checkbox);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		if (style != null) {
			writer.writeAttribute("style", style, "style");
		}

		encodeInput(context, checkbox, clientId, valCheck, disabled);
		encodeOutput(context, checkbox, valCheck, disabled);
		encodeItemLabel(context, checkbox);

		writer.endElement("div");
	}

	protected void encodeInput(final FacesContext context, final TriStateCheckbox checkbox, final String clientId,
	                           final int valCheck, final boolean disabled) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String inputId = clientId + "_input";

		writer.startElement("div", checkbox);
		writer.writeAttribute("class", HTML.CHECKBOX_INPUT_WRAPPER_CLASS, null);

		writer.startElement("input", null);
		writer.writeAttribute("id", inputId, "id");
		writer.writeAttribute("name", inputId, null);

		writer.writeAttribute("value", valCheck, null);

		if (disabled) {
			writer.writeAttribute("disabled", "disabled", null);
		}

		if (checkbox.getOnchange() != null) {
			writer.writeAttribute("onchange", checkbox.getOnchange(), null);
		}

		writer.endElement("input");

		writer.endElement("div");
	}

	protected void encodeOutput(final FacesContext context, final TriStateCheckbox checkbox, final int valCheck,
	                            final boolean disabled) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String styleClass = HTML.CHECKBOX_BOX_CLASS;
		styleClass = (valCheck == 1 || valCheck == 2) ? styleClass + " ui-state-active" : styleClass;
		styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

		String iconClass = HTML.CHECKBOX_ICON_CLASS;

		//if stateIcon is defined use it insted of default icons.
		String stateOneIconClass = checkbox.getStateOneIcon() != null ? "ui-icon " + checkbox.getStateOneIcon() : " ";
		String stateTwoIconClass =
		    checkbox.getStateTwoIcon() != null ? "ui-icon " + checkbox.getStateTwoIcon() : "ui-icon ui-icon-check";
		String stataThreeIconClass =
		    checkbox.getStateThreeIcon() != null ? "ui-icon " + checkbox.getStateThreeIcon() : "ui-icon ui-icon-closethick";

		String statesIconsClasses = stateOneIconClass + ";" + stateTwoIconClass + ";" + stataThreeIconClass;

		iconClass = valCheck == 0 ? iconClass + " " + stateOneIconClass : iconClass;
		iconClass = valCheck == 1 ? iconClass + " " + stateTwoIconClass : iconClass;
		iconClass = valCheck == 2 ? iconClass + " " + stataThreeIconClass : iconClass;

		writer.startElement("div", null);
		writer.writeAttribute("class", styleClass, null);
		writer.writeAttribute("statesIcons", statesIconsClasses, null);

		writer.startElement("span", null);
		writer.writeAttribute("class", iconClass, null);
		writer.endElement("span");

		writer.endElement("div");
	}

	protected void encodeItemLabel(final FacesContext context, final TriStateCheckbox checkbox) throws IOException {
		String label = checkbox.getItemLabel();

		if (label != null) {
			ResponseWriter writer = context.getResponseWriter();

			writer.startElement("span", null);
			writer.writeAttribute("class", HTML.CHECKBOX_LABEL_CLASS, null);
			writer.writeText(label, "itemLabel");
			writer.endElement("span");
		}
	}

	protected void encodeScript(final FacesContext context, final TriStateCheckbox checkbox) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = checkbox.getClientId(context);

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('TriStateCheckbox','" + checkbox.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		encodeClientBehaviors(context, checkbox);
		writer.write("});});");

		endScript(writer);
	}
}
