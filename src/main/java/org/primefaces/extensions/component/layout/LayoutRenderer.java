/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.layout;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.DataModel;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link Layout} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class LayoutRenderer extends CoreRenderer {

	@Override
	public void decode(final FacesContext fc, final UIComponent component) {
		Layout layout = (Layout) component;
		layout.setDataModel(null);

		decodeBehaviors(fc, layout);
	}

	@Override
	public void encodeBegin(final FacesContext fc, final UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		Layout layout = (Layout) component;
		String clientId = layout.getClientId(fc);

		Map<String, UIComponent> layoutPanes = layout.getLayoutPanes();
		if (layoutPanes.isEmpty() || layoutPanes.get(Layout.POSITION_CENTER) == null) {
			throw new FacesException("Full page layout must have at least one rendered layout pane with 'center' position");
		}

		encodeScript(fc, layout, layoutPanes);

		if (!layout.isFullPage()) {
			writer.startElement("div", layout);
			writer.writeAttribute("id", clientId, "id");

			if (layout.getStyle() != null) {
				writer.writeAttribute("style", layout.getStyle(), "style");
			}

			if (layout.getStyleClass() != null) {
				writer.writeAttribute("class", layout.getStyleClass(), "styleClass");
			}
		}

		encodeMarkup(fc, layout, layoutPanes);
	}

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		Layout layout = (Layout) component;

		if (!layout.isFullPage()) {
			writer.endElement("div");
		}
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeChildren(final FacesContext fc, final UIComponent component) throws IOException {
		// nothing to do
	}

	protected void encodeScript(final FacesContext fc, final Layout layout, final Map<String, UIComponent> layoutPanes)
	    throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = layout.getClientId();

		startScript(writer, clientId);
		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('Layout', '" + layout.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",indexTab:");

		DataModel<MenuItem> dataModel = layout.getDataModel();
		if (dataModel == null || dataModel.getRowCount() < 1) {
			writer.write("-1");
		} else {
			String viewId = fc.getViewRoot().getViewId();
			viewId = viewId.substring(0, viewId.lastIndexOf('.'));
			if (viewId.startsWith("/")) {
				viewId = viewId.substring(1);
			}

			int indexTab = 0;
			for (MenuItem item : dataModel) {
				String url = item.getUrl();
				url = url.substring(0, url.lastIndexOf('.'));
				if (url.startsWith("/")) {
					url = url.substring(1);
				}

				if (viewId.equals(url)) {
					break;
				}

				++indexTab;
			}

			writer.write(String.valueOf(indexTab));
		}

		writer.write(",northOptions:{north__spacing:0");
		writeLayoutPaneOption(fc, writer, layoutPanes, Layout.POSITION_NORTH);
		if (layoutPanes.get(Layout.POSITION_NORTH) == null) {
			writer.write(",north__size:0");
			writer.write(",north__paneposition:'north'");
		}

		writer.write("}");

		writer.write(",tabLayoutOpt:{resizeWithWindow:false");
		writeLayoutPaneOption(fc, writer, layoutPanes, Layout.POSITION_SOUTH);
		writeLayoutPaneOption(fc, writer, layoutPanes, Layout.POSITION_CENTER);
		writeLayoutPaneOption(fc, writer, layoutPanes, Layout.POSITION_WEST);
		writeLayoutPaneOption(fc, writer, layoutPanes, Layout.POSITION_EAST);
		writer.write("}");

		writer.write(",centerLayoutOpt:");

		boolean hasCenterLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(Layout.POSITION_CENTER));
		if (hasCenterLayoutOptions) {
			writer.write("{resizeWhileDragging:false");
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_CENTER + Layout.POSITION_SEPARATOR + Layout.POSITION_NORTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_CENTER + Layout.POSITION_SEPARATOR + Layout.POSITION_SOUTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_CENTER + Layout.POSITION_SEPARATOR + Layout.POSITION_CENTER);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_CENTER + Layout.POSITION_SEPARATOR + Layout.POSITION_WEST);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_CENTER + Layout.POSITION_SEPARATOR + Layout.POSITION_EAST);
			writer.write("}");
		} else {
			writer.write("null");
		}

		writer.write(",westLayoutOpt:");

		boolean hasWestLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(Layout.POSITION_WEST));
		if (hasWestLayoutOptions) {
			writer.write("{resizeWhileDragging:true");
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_WEST + Layout.POSITION_SEPARATOR + Layout.POSITION_NORTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_WEST + Layout.POSITION_SEPARATOR + Layout.POSITION_SOUTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_WEST + Layout.POSITION_SEPARATOR + Layout.POSITION_CENTER);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_WEST + Layout.POSITION_SEPARATOR + Layout.POSITION_WEST);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_WEST + Layout.POSITION_SEPARATOR + Layout.POSITION_EAST);
			writer.write("}");
		} else {
			writer.write("null");
		}

		writer.write(",eastLayoutOpt:");

		boolean hasEastLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(Layout.POSITION_EAST));
		if (hasEastLayoutOptions) {
			writer.write("{resizeWhileDragging:true");
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_EAST + Layout.POSITION_SEPARATOR + Layout.POSITION_NORTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_EAST + Layout.POSITION_SEPARATOR + Layout.POSITION_SOUTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_EAST + Layout.POSITION_SEPARATOR + Layout.POSITION_CENTER);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_EAST + Layout.POSITION_SEPARATOR + Layout.POSITION_WEST);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_EAST + Layout.POSITION_SEPARATOR + Layout.POSITION_EAST);
			writer.write("}");
		} else {
			writer.write("null");
		}

		writer.write(",southLayoutOpt:");

		boolean hasSouthLayoutOptions = hasNestedLayoutOptions((LayoutPane) layoutPanes.get(Layout.POSITION_SOUTH));
		if (hasSouthLayoutOptions) {
			writer.write("{resizeWhileDragging:true");
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_SOUTH + Layout.POSITION_SEPARATOR + Layout.POSITION_NORTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_SOUTH + Layout.POSITION_SEPARATOR + Layout.POSITION_SOUTH);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_SOUTH + Layout.POSITION_SEPARATOR + Layout.POSITION_CENTER);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_SOUTH + Layout.POSITION_SEPARATOR + Layout.POSITION_WEST);
			writeLayoutPaneOption(fc, writer, layoutPanes,
			                      Layout.POSITION_SOUTH + Layout.POSITION_SEPARATOR + Layout.POSITION_EAST);
			writer.write("}");
		} else {
			writer.write("null");
		}

		if (layout.isFullPage()) {
			writer.write(",forTarget:'body'");
		} else {
			writer.write(",forTarget:'" + ComponentUtils.escapeJQueryId(clientId) + "'");
		}

		writer.write(",serverState:");

		ValueExpression stateVE = layout.getValueExpression(Layout.PropertyKeys.state.toString());
		if (stateVE != null) {
			writer.write("true");
			if (StringUtils.isNotBlank(layout.getState())) {
				writer.write(",state:'" + layout.getState() + "'");
			} else {
				writer.write(",state:'{}'");
			}
		} else {
			writer.write("false");
		}

		writer.write(",clientState:");
		if (layout.isStateCookie()) {
			writer.write("true");
		} else {
			writer.write("false");
		}

		writer.write(",togglerTipClose:");
		if (layout.getTogglerTipClose() != null) {
			writer.write("\"" + escapeText(layout.getTogglerTipClose()) + "\"");
		} else {
			writer.write("'Close'");
		}

		writer.write(",togglerTipOpen:");
		if (layout.getTogglerTipOpen() != null) {
			writer.write("\"" + escapeText(layout.getTogglerTipOpen()) + "\"");
		} else {
			writer.write("'Open'");
		}

		writer.write(",resizerTip:");
		if (layout.getResizerTip() != null) {
			writer.write("\"" + escapeText(layout.getResizerTip()) + "\"");
		} else {
			writer.write("'Resize'");
		}

		encodeClientBehaviors(fc, layout);

		writer.write("},true);});");
		endScript(writer);
	}

	protected void encodeMarkup(final FacesContext fc, final Layout layout, final Map<String, UIComponent> layoutPanes)
	    throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = layout.getClientId();

		LayoutPane layoutPane = (LayoutPane) layoutPanes.get(Layout.POSITION_NORTH);
		if (layoutPane != null && layoutPane.isRendered()) {
			writer.startElement("div", null);
			writer.writeAttribute("id", clientId + "-layout-outer-north", null);
			writer.writeAttribute("class", "layout-outer-north", null);
			writer.startElement("div", null);
			writer.writeAttribute("class", "layout-inner-north " + Layout.STYLE_CLASS_PANE, null);
			writer.startElement("div", null);
			writer.writeAttribute("class", "ui-layout-pane-content", null);

			renderChildren(fc, layoutPane);

			writer.endElement("div");
			writer.endElement("div");
			writer.endElement("div");
		}

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "-layout-outer-center", null);
		writer.writeAttribute("class", "layout-outer-center", null);

		DataModel<MenuItem> dataModel = layout.getDataModel();
		if (dataModel != null && dataModel.getRowCount() > 0) {
			// render tabs
			writer.startElement("ul", null);
			writer.writeAttribute("id", clientId + "-layout-tabbuttons", null);
			writer.writeAttribute("class", "layout-tabbuttons", null);
			writer.writeAttribute("style", "display: none;", null);

			Iterator<MenuItem> iter = dataModel.iterator();
			while (iter.hasNext()) {
				MenuItem mi = iter.next();
				writer.startElement("li", null);
				if (!iter.hasNext()) {
					writer.writeAttribute("class", "ui-tab last-tab", null);
				} else {
					writer.writeAttribute("class", "ui-tab", null);
				}

				writer.startElement("a", null);

				// build complete URL
				ExternalContext ec = fc.getExternalContext();
				StringBuilder url = new StringBuilder();
				String scheme = ec.getRequestScheme();
				int port = ec.getRequestServerPort();

				url.append(scheme); // http, https
				url.append("://");
				url.append(ec.getRequestServerName());
				if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
					url.append(':');
					url.append(port);
				}

				String actionURL = fc.getApplication().getViewHandler().getActionURL(fc, mi.getUrl());
				url.append(ec.encodeActionURL(actionURL));

				// write URL
				writer.writeURIAttribute("href", "#" + url.toString(), null);

				// render inner table = tab icon + label
				writer.startElement("table", null);
				writer.startElement("tr", null);
				writer.startElement("td", null);
				writer.writeAttribute("class", "ui-icon " + mi.getIcon(), null);
				writer.endElement("td");
				writer.startElement("td", null);
				writer.write((String) mi.getValue());
				writer.endElement("td");
				writer.endElement("tr");
				writer.endElement("table");

				writer.endElement("a");
				writer.endElement("li");
			}

			writer.endElement("ul");
		}

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "-layout-tabpanels", null);
		writer.writeAttribute("class", "layout-tabpanels", null);

		writer.startElement("div", null);
		writer.writeAttribute("class", "ui-layout-tab", null);

		UIForm form = (UIForm) layoutPanes.get("form");
		if (form != null) {
			form.encodeBegin(fc);
		}

		// render current tab panel pane by pane
		encodePane(fc, writer, layoutPanes, Layout.POSITION_SOUTH);
		encodePane(fc, writer, layoutPanes, Layout.POSITION_CENTER);
		encodePane(fc, writer, layoutPanes, Layout.POSITION_WEST);
		encodePane(fc, writer, layoutPanes, Layout.POSITION_EAST);

		if (form != null) {
			form.encodeEnd(fc);
		}

		writer.endElement("div");
		writer.endElement("div");

		ValueExpression stateVE = layout.getValueExpression(Layout.PropertyKeys.state.toString());
		if (stateVE != null) {
			// render hidden field for server-side state saving
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", clientId + "_state", null);
			writer.writeAttribute("name", clientId + "_state", null);
			writer.writeAttribute("autocomplete", "off", null);
			if (StringUtils.isNotBlank(layout.getState())) {
				writer.writeAttribute("value", "'" + layout.getState() + "'", null);
			} else {
				writer.writeAttribute("value", "'{}'", null);
			}

			writer.endElement("input");
		}

		writer.endElement("div");
	}

	protected void encodePane(final FacesContext fc, final ResponseWriter writer, final Map<String, UIComponent> layoutPanes,
	                          final String position) throws IOException {
		LayoutPane layoutPane = (LayoutPane) layoutPanes.get(position);
		if (layoutPane == null) {
			return;
		}

		writer.startElement("div", null);

		// render class attribute
		if (layoutPane.isExistNestedPanes() || layoutPane.isStatusbar()) {
			writer.writeAttribute("class", "ui-layout-" + layoutPane.getPosition(), null);
		} else {
			writer.writeAttribute("class", "ui-layout-" + layoutPane.getPosition() + " " + Layout.STYLE_CLASS_PANE, null);
		}

		// render stuff inside pane(s)
		if (layoutPane.isExistNestedPanes()) {
			encodePane(fc, writer, layoutPanes, position + Layout.POSITION_SEPARATOR + Layout.POSITION_NORTH);
			encodePane(fc, writer, layoutPanes, position + Layout.POSITION_SEPARATOR + Layout.POSITION_CENTER);
			encodePane(fc, writer, layoutPanes, position + Layout.POSITION_SEPARATOR + Layout.POSITION_SOUTH);
			encodePane(fc, writer, layoutPanes, position + Layout.POSITION_SEPARATOR + Layout.POSITION_EAST);
			encodePane(fc, writer, layoutPanes, position + Layout.POSITION_SEPARATOR + Layout.POSITION_WEST);
		} else {
			encodePaneHeader(fc, writer, layoutPane);
			encodePaneContent(fc, writer, layoutPane);
		}

		writer.endElement("div");
	}

	protected void encodePaneHeader(final FacesContext fc, final ResponseWriter writer, final LayoutPane layoutPane)
	    throws IOException {
		UIComponent header = layoutPane.getFacet("header");
		if (header != null) {
			writer.startElement("div", null);
			if (layoutPane.getStyleClassHeader() != null) {
				writer.writeAttribute("class", Layout.STYLE_CLASS_PANE_HEADER + " " + layoutPane.getStyleClassHeader(), null);
			} else {
				writer.writeAttribute("class", Layout.STYLE_CLASS_PANE_HEADER, null);
			}

			if (layoutPane.getStyleHeader() != null) {
				writer.writeAttribute("style", layoutPane.getStyleHeader(), null);
			}

			header.encodeAll(fc);
			writer.endElement("div");
		}
	}

	protected void encodePaneContent(final FacesContext fc, final ResponseWriter writer, final LayoutPane layoutPane)
	    throws IOException {
		writer.startElement("div", null);

		String styleClass = Layout.STYLE_CLASS_PANE_CONTENT;
		if (layoutPane.isStatusbar()) {
			styleClass = styleClass + " ui-state-default statusbar";
		}

		if (layoutPane.getStyleClassContent() != null) {
			writer.writeAttribute("class", styleClass + " " + layoutPane.getStyleClassContent(), null);
		} else {
			writer.writeAttribute("class", styleClass, null);
		}

		if (layoutPane.getStyleContent() != null) {
			writer.writeAttribute("style", layoutPane.getStyleContent(), null);
		}

		renderChildren(fc, layoutPane);
		writer.endElement("div");
	}

	private void writeLayoutPaneOption(final FacesContext fc, final ResponseWriter writer,
	                                   final Map<String, UIComponent> layoutPanes, final String position) throws IOException {
		LayoutPane pane = (LayoutPane) layoutPanes.get(position);
		if (pane == null) {
			return;
		}

		writer.write("," + pane.getPosition() + "__resizable:" + pane.isResizable());
		writer.write("," + pane.getPosition() + "__closable:" + pane.isClosable());
		writer.write("," + pane.getPosition() + "__initClosed:" + pane.isInitClosed());
		writer.write("," + pane.getPosition() + "__spacing_open:" + pane.getSpacing());
		writer.write("," + pane.getPosition() + "__spacing_close:" + pane.getSpacing());

		if (pane.getSize() != null) {
			writer.write("," + pane.getPosition() + "__size:" + pane.getSize());
		}

		if (pane.getMinSize() != null) {
			writer.write("," + pane.getPosition() + "__minSize:" + pane.getMinSize());
		}

		if (pane.getMaxSize() != null) {
			writer.write("," + pane.getPosition() + "__maxSize:" + pane.getMaxSize());
		}

		if (pane.getMinWidth() != null) {
			writer.write("," + pane.getPosition() + "__minWidth:" + pane.getMinWidth());
		}

		if (pane.getMaxWidth() != null) {
			writer.write("," + pane.getPosition() + "__maxWidth:" + pane.getMaxWidth());
		}

		if (pane.getMinHeight() != null) {
			writer.write("," + pane.getPosition() + "__minHeight:" + pane.getMinHeight());
		}

		if (pane.getMaxHeight() != null) {
			writer.write("," + pane.getPosition() + "__maxHeight:" + pane.getMaxHeight());
		}

		writer.write("," + pane.getPosition() + "__paneposition:'" + position + "'");
	}

	private boolean hasNestedLayoutOptions(final LayoutPane layoutPane) {
		return (layoutPane != null && layoutPane.isExistNestedPanes());
	}
}
