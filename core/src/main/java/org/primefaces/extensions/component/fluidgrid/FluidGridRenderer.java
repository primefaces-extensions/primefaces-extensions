/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.fluidgrid;

import java.io.IOException;
import java.util.Collection;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.expression.SearchExpressionUtils;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for {@link FluidGrid} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.5
 */
@FacesRenderer(rendererType = FluidGrid.DEFAULT_RENDERER, componentFamily = FluidGrid.COMPONENT_FAMILY)
public class FluidGridRenderer extends CoreRenderer<FluidGrid> {

    private static final String GRID_CLASS = "pe-fluidgrid";
    private static final String GRID_ITEM_CLASS = "pe-fluidgrid-item";

    private static final String LIST_ROLE = "list";
    private static final String LIST_ITEM_ROLE = "listitem";

    @Override
    public void decode(final FacesContext context, final FluidGrid component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final FluidGrid component) throws IOException {
        encodeMarkup(fc, component);
        encodeScript(fc, component);
    }

    protected void encodeMarkup(final FacesContext fc, final FluidGrid component) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = component.getClientId(fc);
        final String styleClass = getStyleClassBuilder(fc)
                    .add(GRID_CLASS)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }

        writer.writeAttribute("role", LIST_ROLE, null);

        if (component.getVar() != null) {
            // dynamic items
            final Object value = component.getValue();
            if (value != null) {
                if (!(value instanceof Collection<?>)) {
                    throw new FacesException("Value in FluidGrid must be of type Collection / List");
                }

                for (final UIComponent kid : component.getChildren()) {
                    if (kid.isRendered() && !(kid instanceof UIFluidGridItem)) {
                        // first render children like stamped elements, etc.
                        renderChild(fc, kid);
                    }
                }

                final Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
                for (final FluidGridItem fluidGridItem : col) {
                    // find ui item by type
                    final UIFluidGridItem uiItem = component.getItem(fluidGridItem.getType());

                    if (uiItem.isRendered()) {
                        // set data in request scope
                        component.setData(fluidGridItem);

                        // render item
                        renderItem(fc, writer, component, uiItem);
                    }
                }
            }
        }
        else {
            // static items
            for (final UIComponent kid : component.getChildren()) {
                if (kid.isRendered()) {
                    if (kid instanceof UIFluidGridItem) {
                        // render item
                        renderItem(fc, writer, component, (UIFluidGridItem) kid);
                    }
                    else {
                        // render a child like stamped element, etc.
                        renderChild(fc, kid);
                    }
                }
            }
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext fc, final FluidGrid component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtFluidGrid", component);
        wb.append(",opts:{");
        wb.append("isFitWidth:" + component.isFitWidth());
        wb.append(",isOriginLeft:" + component.isOriginLeft());
        wb.append(",isOriginTop:" + component.isOriginTop());
        wb.append(",isResizeBound:" + component.isResizeBound());
        wb.append(",hasImages:" + component.isHasImages());

        if (component.getHGutter() != 0) {
            wb.append(",gutter:" + component.getHGutter());
        }

        final String stamp = SearchExpressionUtils.resolveClientIdsForClientSide(fc, component, component.getStamp());
        if (stamp != null) {
            wb.append(",stamp:'" + stamp + "'");
        }

        if (component.getTransitionDuration() != null) {
            wb.append(",transitionDuration:'" + component.getTransitionDuration() + "'");
        }
        else {
            wb.append(",transitionDuration:0");
        }

        wb.append("}");

        encodeClientBehaviors(fc, component);
        wb.finish();
    }

    protected void renderItem(final FacesContext fc, final ResponseWriter writer, final FluidGrid component,
                final UIFluidGridItem uiItem)
                throws IOException {
        writer.startElement("div", null);

        if (uiItem.getStyleClass() != null) {
            writer.writeAttribute(Attrs.CLASS, GRID_ITEM_CLASS + " " + uiItem.getStyleClass(), null);
        }
        else {
            writer.writeAttribute(Attrs.CLASS, GRID_ITEM_CLASS, null);
        }

        if (component.getVGutter() != 0) {
            writer.writeAttribute(Attrs.STYLE, "margin-bottom: " + component.getVGutter() + "px", null);
        }

        writer.writeAttribute("role", LIST_ITEM_ROLE, null);

        // encode content of pe:fluidGridItem
        uiItem.encodeAll(fc);

        writer.endElement("div");
    }

    @Override
    public void encodeChildren(final FacesContext context, final FluidGrid component) {
        // Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}