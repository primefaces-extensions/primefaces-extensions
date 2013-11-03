/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.fluidgrid;

import java.io.IOException;
import java.util.Collection;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for {@link FluidGrid} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class FluidGridRenderer extends CoreRenderer {

	private static final String GRID_CLASS = "pe-fluidgrid";
	private static final String GRID_ITEM_CLASS = "pe-fluidgrid-item";

	private static final String LIST_ROLE = "list";
	private static final String LIST_ITEM_ROLE = "listitem";

	@Override
	public void decode(FacesContext context, UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		FluidGrid fluidGrid = (FluidGrid) component;

		encodeMarkup(fc, fluidGrid);
		encodeScript(fc, fluidGrid);
	}

	protected void encodeMarkup(FacesContext fc, FluidGrid fluidGrid) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = fluidGrid.getClientId(fc);
		String styleClass = fluidGrid.getStyleClass();
		styleClass = (styleClass == null) ? GRID_CLASS : GRID_CLASS + " " + styleClass;

		writer.startElement("div", fluidGrid);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		if (fluidGrid.getStyle() != null) {
			writer.writeAttribute("style", fluidGrid.getStyle(), "style");
		}

		writer.writeAttribute("role", LIST_ROLE, null);

		if (fluidGrid.getVar() != null) {
			// dynamic items
			Object value = fluidGrid.getValue();
			if (value != null) {
				if (!(value instanceof Collection<?>)) {
					throw new FacesException("Value in FluidGrid must be of type Collection / List");
				}

				for (UIComponent kid : fluidGrid.getChildren()) {
					if (kid.isRendered() && !(kid instanceof UIFluidGridItem)) {
						// first render children like stamped elements, etc.
						renderChild(fc, kid);
					}
				}

				@SuppressWarnings("unchecked")
				Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
				for (FluidGridItem fluidGridItem : col) {
					// find ui item by type
					UIFluidGridItem uiItem = fluidGrid.getItem(fluidGridItem.getType());

					if (uiItem.isRendered()) {
						// set data in request scope
						fluidGrid.setData(fluidGridItem);

						// render item
						renderItem(fc, writer, fluidGrid, uiItem);
					}
				}
			}
		} else {
			// static items
			for (UIComponent kid : fluidGrid.getChildren()) {
				if (kid.isRendered()) {
					if (kid instanceof UIFluidGridItem) {
						// render item
						renderItem(fc, writer, fluidGrid, (UIFluidGridItem) kid);
					} else {
						// render a child like stamped element, etc.
						renderChild(fc, kid);
					}
				}
			}
		}

		writer.endElement("div");
	}

	protected void encodeScript(FacesContext fc, FluidGrid fluidGrid) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = fluidGrid.getClientId(fc);

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('FluidGrid','" + fluidGrid.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",opts:{");

		writer.write("isFitWidth:" + fluidGrid.isFitWidth());
		writer.write(",isOriginLeft:" + fluidGrid.isOriginLeft());
		writer.write(",isOriginTop:" + fluidGrid.isOriginTop());
		writer.write(",isResizeBound:" + fluidGrid.isResizeBound());
		writer.write(",hasImages:" + fluidGrid.isHasImages());

		if (fluidGrid.gethGutter() != 0) {
			writer.write(",gutter:" + fluidGrid.gethGutter());
		}

		String stamp = SearchExpressionFacade.resolveComponentsForClient(fc, fluidGrid, fluidGrid.getStamp());
		if (stamp != null) {
			writer.write(",stamp:'" + stamp + "'");
		}

		if (fluidGrid.getTransitionDuration() != null) {
			writer.write(",transitionDuration:'" + fluidGrid.getTransitionDuration() + "'");
		} else {
			writer.write(",transitionDuration:0");
		}

		writer.write("}");
		encodeClientBehaviors(fc, fluidGrid);
		writer.write("},true);});");

		endScript(writer);
	}

	protected void renderItem(FacesContext fc, ResponseWriter writer, FluidGrid fluidGrid, UIFluidGridItem uiItem)
	    throws IOException {
		writer.startElement("div", null);

		if (uiItem.getStyleClass() != null) {
			writer.writeAttribute("class", GRID_ITEM_CLASS + " " + uiItem.getStyleClass(), null);
		} else {
			writer.writeAttribute("class", GRID_ITEM_CLASS, null);
		}

		if (fluidGrid.getvGutter() != 0) {
			writer.writeAttribute("style", "margin-bottom: " + fluidGrid.getvGutter() + "px", null);
		}

		writer.writeAttribute("role", LIST_ITEM_ROLE, null);

		// encode content of pe:fluidGridItem
		uiItem.encodeAll(fc);

		writer.endElement("div");
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
