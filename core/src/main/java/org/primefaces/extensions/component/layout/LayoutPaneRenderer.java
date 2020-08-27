/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.component.layout;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link LayoutPane} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @version $Revision$
 * @since 0.6.0
 */
public class LayoutPaneRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final LayoutPane layoutPane = (LayoutPane) component;

        final String position = layoutPane.getPosition();
        final String combinedPosition;
        UIComponent parent = layoutPane.getParent();

        final StringBuilder combinedPositionBuilder = position == null ? null : new StringBuilder(position);
        while (parent instanceof LayoutPane) {
            assert combinedPositionBuilder != null;
            combinedPositionBuilder.insert(0, ((LayoutPane) parent).getPosition() + Layout.POSITION_SEPARATOR);
            parent = parent.getParent();
        }
        assert combinedPositionBuilder != null;
        combinedPosition = combinedPositionBuilder.toString();

        // save combined position
        layoutPane.setCombinedPosition(combinedPosition);

        boolean hasSubPanes = false;
        for (final UIComponent subChild : layoutPane.getChildren()) {
            // check first level
            if (hasSubPanes) {
                break;
            }

            if (subChild instanceof LayoutPane) {
                if (!subChild.isRendered()) {
                    continue;
                }

                hasSubPanes = true;
            }
            else {
                for (final UIComponent subSubChild : subChild.getChildren()) {
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

        final UIComponent header = layoutPane.getFacet("header");

        writer.startElement("div", null);
        writer.writeAttribute("id", layoutPane.getClientId(fc), "id");
        if (hasSubPanes) {
            writer.writeAttribute(Attrs.CLASS, Layout.STYLE_CLASS + position + " " + Layout.STYLE_CLASS_PANE_WITH_SUBPANES, null);
        }
        else {
            if (header != null) {
                writer.writeAttribute(Attrs.CLASS, Layout.STYLE_CLASS + position + " " + Layout.STYLE_CLASS_PANE, null);
            }
            else {
                if (layoutPane.getStyleClassContent() != null) {
                    writer.writeAttribute(Attrs.CLASS,
                                Layout.STYLE_CLASS + position + " " + Layout.STYLE_CLASS_PANE + " "
                                            + Layout.STYLE_CLASS_PANE_CONTENT + " "
                                            + layoutPane.getStyleClassContent(),
                                null);
                }
                else {
                    writer.writeAttribute(Attrs.CLASS,
                                Layout.STYLE_CLASS + position + " " + Layout.STYLE_CLASS_PANE + " "
                                            + Layout.STYLE_CLASS_PANE_CONTENT,
                                null);
                }

                if (layoutPane.getStyleContent() != null) {
                    writer.writeAttribute(Attrs.STYLE, layoutPane.getStyleContent(), null);
                }
            }
        }

        writer.writeAttribute("data-combinedposition", combinedPosition, null);

        // encode header
        if (header != null) {
            writer.startElement("div", null);
            if (layoutPane.getStyleClassHeader() != null) {
                writer.writeAttribute(Attrs.CLASS, Layout.STYLE_CLASS_PANE_HEADER + " " + layoutPane.getStyleClassHeader(), null);
            }
            else {
                writer.writeAttribute(Attrs.CLASS, Layout.STYLE_CLASS_PANE_HEADER, null);
            }

            if (layoutPane.getStyleHeader() != null) {
                writer.writeAttribute(Attrs.STYLE, layoutPane.getStyleHeader(), null);
            }

            header.encodeAll(fc);

            writer.endElement("div");
        }

        // encode content
        if (header != null) {
            writer.startElement("div", null);
            if (layoutPane.getStyleClassContent() != null) {
                writer.writeAttribute(Attrs.CLASS,
                            Layout.STYLE_CLASS_LAYOUT_CONTENT + " " + Layout.STYLE_CLASS_PANE_CONTENT + " "
                                        + layoutPane.getStyleClassContent(),
                            null);
            }
            else {
                writer.writeAttribute(Attrs.CLASS, Layout.STYLE_CLASS_LAYOUT_CONTENT + " " + Layout.STYLE_CLASS_PANE_CONTENT, null);
            }

            if (layoutPane.getStyleContent() != null) {
                writer.writeAttribute(Attrs.STYLE, "border:none; " + layoutPane.getStyleContent(), null);
            }
            else {
                writer.writeAttribute(Attrs.STYLE, "border:none", null);
            }

            renderChildren(fc, layoutPane);

            writer.endElement("div");
        }
        else {
            renderChildren(fc, layoutPane);
        }

        writer.endElement("div");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(final FacesContext fc, final UIComponent component) {
        // nothing to do
    }
}
