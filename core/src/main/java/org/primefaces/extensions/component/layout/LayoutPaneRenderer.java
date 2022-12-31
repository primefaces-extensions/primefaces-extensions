/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
            String headerStyleClass = getStyleClassBuilder(fc)
                        .add(Layout.STYLE_CLASS_PANE_HEADER)
                        .add(layoutPane.getStyleClassHeader())
                        .build();
            writer.startElement("div", null);
            writer.writeAttribute(Attrs.CLASS, headerStyleClass, null);

            if (layoutPane.getStyleHeader() != null) {
                writer.writeAttribute(Attrs.STYLE, layoutPane.getStyleHeader(), null);
            }

            header.encodeAll(fc);

            writer.endElement("div");
        }

        String contentStyleClass = getStyleClassBuilder(fc)
                    .add(Layout.STYLE_CLASS_LAYOUT_CONTENT)
                    .add(Layout.STYLE_CLASS_PANE_CONTENT)
                    .add(layoutPane.getStyleClassContent())
                    .build();

        // encode content
        if (header != null) {
            writer.startElement("div", null);
            writer.writeAttribute(Attrs.CLASS, contentStyleClass, null);

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
