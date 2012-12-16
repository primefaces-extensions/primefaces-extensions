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

package org.primefaces.extensions.component.layout;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link LayoutPane} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.6.0
 */
public class LayoutPaneRenderer extends CoreRenderer {

	@Override
	public void encodeBegin(FacesContext fc, UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		LayoutPane layoutPane = (LayoutPane) component;

		String position = layoutPane.getPosition();
		String combinedPosition = position;
		UIComponent parent = layoutPane.getParent();

		while (parent instanceof LayoutPane) {
			combinedPosition = ((LayoutPane) parent).getPosition() + Layout.POSITION_SEPARATOR + combinedPosition;
			parent = parent.getParent();
		}

		// save combined position
		layoutPane.setCombinedPosition(combinedPosition);

		boolean hasSubPanes = false;
		for (UIComponent subChild : layoutPane.getChildren()) {
			// check first level
			if (hasSubPanes) {
				break;
			}

			if (subChild instanceof LayoutPane) {
				if (!subChild.isRendered()) {
					continue;
				}

				hasSubPanes = true;
			} else {
				for (UIComponent subSubChild : subChild.getChildren()) {
					// check second level
					if (subSubChild instanceof LayoutPane) {
						if (!subSubChild.isRendered()) {
							continue;
						}

						hasSubPanes = true;

						break;
					}
				}
			}
		}

		UIComponent header = layoutPane.getFacet("header");

		writer.startElement("div", null);
		writer.writeAttribute("id", layoutPane.getClientId(fc), "id");
		if (hasSubPanes) {
			writer.writeAttribute("class", "ui-layout-" + position + " " + Layout.STYLE_CLASS_PANE_WITH_SUBPANES, null);
		} else {
			if (header != null) {
				writer.writeAttribute("class", "ui-layout-" + position + " " + Layout.STYLE_CLASS_PANE, null);
			} else {
				if (layoutPane.getStyleClassContent() != null) {
					writer.writeAttribute("class",
					                      "ui-layout-" + position + " " + Layout.STYLE_CLASS_PANE + " "
					                      + Layout.STYLE_CLASS_PANE_CONTENT + " "
					                      + layoutPane.getStyleClassContent(), null);
				} else {
					writer.writeAttribute("class",
					                      "ui-layout-" + position + " " + Layout.STYLE_CLASS_PANE + " "
					                      + Layout.STYLE_CLASS_PANE_CONTENT, null);
				}

				if (layoutPane.getStyleContent() != null) {
					writer.writeAttribute("style", layoutPane.getStyleContent(), null);
				}
			}
		}

		writer.writeAttribute("data-combinedposition", combinedPosition, null);

		// encode header
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

		// encode content
		if (header != null) {
			writer.startElement("div", null);
			if (layoutPane.getStyleClassContent() != null) {
				writer.writeAttribute("class",
				                      Layout.STYLE_CLASS_LAYOUT_CONTENT + " " + Layout.STYLE_CLASS_PANE_CONTENT + " "
				                      + layoutPane.getStyleClassContent(), null);
			} else {
				writer.writeAttribute("class", Layout.STYLE_CLASS_LAYOUT_CONTENT + " " + Layout.STYLE_CLASS_PANE_CONTENT, null);
			}

			if (layoutPane.getStyleContent() != null) {
				writer.writeAttribute("style", "border:none; " + layoutPane.getStyleContent(), null);
			} else {
				writer.writeAttribute("style", "border:none", null);
			}

			renderChildren(fc, layoutPane);

			writer.endElement("div");
		} else {
			renderChildren(fc, layoutPane);
		}
	}

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();

		writer.endElement("div");
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeChildren(final FacesContext fc, final UIComponent component) throws IOException {
		// nothing to do
	}
}
