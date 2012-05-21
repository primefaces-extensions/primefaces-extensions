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

package org.primefaces.extensions.component.dynaform;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for {@link DynaForm} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormRenderer extends CoreRenderer {

	private static final Logger LOGGER = Logger.getLogger(DynaFormRenderer.class.getName());

	private static final String FACET_HEADER_REGULAR = "headerRegular";
	private static final String FACET_FOOTER_REGULAR = "footerRegular";
	private static final String FACET_HEADER_EXTENDED = "headerExtended";
	private static final String FACET_FOOTER_EXTENDED = "footerExtended";
	private static final String FACET_BUTTON_BAR = "buttonBar";

	private static final String GRID_CLASS = "pe-dynaform-grid";
	private static final String CELL_CLASS = "pe-dynaform-cell";
	private static final String LABEL_CLASS = "pe-dynaform-label";
	private static final String FACET_BUTTON_BAR_TOP_CLASS = "pe-dynaform-buttonbar-top";
	private static final String FACET_BUTTON_BAR_BOTTOM_CLASS = "pe-dynaform-buttonbar-bottom";
	private static final String FACET_HEADER_CLASS = "pe-dynaform-headerfacet";
	private static final String FACET_FOOTER_CLASS = "pe-dynaform-footerfacet";
	private static final String EXTENDED_ROW_CLASS = "pe-dynaform-extendedrow";

	private static final String BUTTON_BAR_ROLE = "toolbar";
	private static final String GRID_CELL_ROLE = "gridcell";

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		DynaForm dynaForm = (DynaForm) component;
		encodeMarkup(fc, dynaForm);
		encodeScript(fc, dynaForm);
	}

	protected void encodeMarkup(FacesContext fc, DynaForm dynaForm) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = dynaForm.getClientId(fc);

		String styleClass = (dynaForm.getStyleClass() == null ? GRID_CLASS : GRID_CLASS + " " + dynaForm.getStyleClass());

		writer.startElement("table", dynaForm);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("cellspacing", "0", "cellspacing");
		writer.writeAttribute("class", styleClass, "styleClass");
		if (dynaForm.getStyle() != null) {
			writer.writeAttribute("style", dynaForm.getStyle(), "style");
		}

		writer.writeAttribute("role", "grid", null);

		DynaFormModel dynaFormModel = (DynaFormModel) dynaForm.getValue();
		int totalColspan = getTotalColspan(dynaFormModel);
		String bbPosition = dynaForm.getButtonBarPosition();

		if ("top".equals(bbPosition) || "both".equals(bbPosition)) {
			encodeFacet(fc, dynaForm, FACET_BUTTON_BAR, totalColspan, FACET_BUTTON_BAR_TOP_CLASS, BUTTON_BAR_ROLE, false, true);
		}

		encodeFacet(fc, dynaForm, FACET_HEADER_REGULAR, totalColspan, FACET_HEADER_CLASS, GRID_CELL_ROLE, false, true);

		// encode regular grid
		encodeBody(fc, dynaForm, dynaFormModel.getRegularRows(), false, true);

		encodeFacet(fc, dynaForm, FACET_FOOTER_REGULAR, totalColspan, FACET_FOOTER_CLASS, GRID_CELL_ROLE, false, true);
		encodeFacet(fc, dynaForm, FACET_HEADER_EXTENDED, totalColspan, FACET_HEADER_CLASS, GRID_CELL_ROLE, true,
		            dynaForm.isOpenExtended());

		// encode extended grid
		encodeBody(fc, dynaForm, dynaFormModel.getExtendedRows(), true, dynaForm.isOpenExtended());

		encodeFacet(fc, dynaForm, FACET_FOOTER_EXTENDED, totalColspan, FACET_FOOTER_CLASS, GRID_CELL_ROLE, true,
		            dynaForm.isOpenExtended());

		if ("bottom".equals(bbPosition) || "both".equals(bbPosition)) {
			encodeFacet(fc, dynaForm, FACET_BUTTON_BAR, totalColspan, FACET_BUTTON_BAR_BOTTOM_CLASS, BUTTON_BAR_ROLE, false,
			            true);
		}

		writer.endElement("table");
	}

	protected void encodeScript(FacesContext fc, DynaForm dynaForm) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = dynaForm.getClientId(fc);

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('DynaForm','" + dynaForm.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",autoSubmit:" + dynaForm.isAutoSubmit());
		writer.write("});});");
		endScript(writer);
	}

	protected void encodeFacet(FacesContext fc, DynaForm dynaForm, String name, int totalColspan, String styleClass, String role,
	                           boolean extended, boolean visible) throws IOException {
		final UIComponent facet = dynaForm.getFacet(name);
		if (facet != null && facet.isRendered()) {
			ResponseWriter writer = fc.getResponseWriter();
			writer.startElement("tr", null);
			if (extended) {
				writer.writeAttribute("class", EXTENDED_ROW_CLASS, null);
			}

			if (!visible) {
				writer.writeAttribute("style", "display:none;", null);
			}

			writer.writeAttribute("role", "row", null);
			writer.startElement("td", null);
			if (totalColspan > 1) {
				writer.writeAttribute("colspan", totalColspan, null);
			}

			writer.writeAttribute("class", styleClass, null);
			writer.writeAttribute("role", role, null);

			facet.encodeAll(fc);

			writer.endElement("td");
			writer.endElement("tr");
		}
	}

	protected void encodeBody(FacesContext fc, DynaForm dynaForm, List<DynaFormRow> dynaFormRows, boolean extended,
	                          boolean visible) throws IOException {
		if (dynaFormRows == null || dynaFormRows.isEmpty()) {
			return;
		}

		ResponseWriter writer = fc.getResponseWriter();

		for (DynaFormRow dynaFormRow : dynaFormRows) {
			writer.startElement("tr", null);
			if (extended) {
				writer.writeAttribute("class", EXTENDED_ROW_CLASS, null);
			}

			if (!visible) {
				writer.writeAttribute("style", "display:none;", null);
			}

			writer.writeAttribute("role", "row", null);

			for (DynaFormControl dynaFormControl : dynaFormRow.getDynaFormControls()) {
				dynaForm.setData(dynaFormControl);

				// find cell by type
				DynaFormCell dynaFormCell = dynaForm.getCell(dynaFormControl.getType());

				writer.startElement("td", null);
				if (dynaFormControl.getColspan() > 1) {
					writer.writeAttribute("colspan", dynaFormControl.getColspan(), null);
				}

				if (dynaFormControl.getRowspan() > 1) {
					writer.writeAttribute("rowspan", dynaFormControl.getRowspan(), null);
				}

				String styleClass = CELL_CLASS;
				if (dynaFormControl.isApplyLabelStyle()) {
					styleClass = styleClass + " " + LABEL_CLASS;
				}

				if (dynaFormCell.getStyleClass() != null) {
					styleClass = styleClass + " " + dynaFormCell.getStyleClass();
				}

				writer.writeAttribute("class", styleClass, null);
				writer.writeAttribute("role", GRID_CELL_ROLE, null);

				dynaFormCell.encodeAll(fc);

				writer.endElement("td");
			}

			writer.endElement("tr");
		}
	}

	protected int getTotalColspan(DynaFormModel dynaFormModel) {
		// the main aim of this method is layout check
		int totalColspan = -1;
		for (DynaFormRow dynaFormRow : dynaFormModel.getRegularRows()) {
			if (totalColspan != -1 && totalColspan != dynaFormRow.getTotalColspan()) {
				LOGGER.warning(
				    "Layout of dynamic form is bad formed, it's composed of rows with different total colspans (regular part).");
			}

			totalColspan = dynaFormRow.getTotalColspan();
		}

		if (dynaFormModel.getExtendedRows() != null) {
			for (DynaFormRow dynaFormRow : dynaFormModel.getExtendedRows()) {
				if (totalColspan != -1 && totalColspan != dynaFormRow.getTotalColspan()) {
					LOGGER.warning(
					    "Layout of dynamic form is bad formed, it's composed of rows with different total colspans (extended part).");
				}

				totalColspan = dynaFormRow.getTotalColspan();
			}
		}

		if (totalColspan < 1) {
			totalColspan = 1;
		}

		return totalColspan;
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
