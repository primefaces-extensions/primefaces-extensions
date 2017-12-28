/**
 * Copyright 2011-2017 PrimeFaces Extensions
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
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for {@link FluidGrid} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.5
 */
public class FluidGridRenderer extends CoreRenderer {

    private static final String GRID_CLASS = "pe-fluidgrid";
    private static final String GRID_ITEM_CLASS = "pe-fluidgrid-item";

    private static final String LIST_ROLE = "list";
    private static final String LIST_ITEM_ROLE = "listitem";

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final FluidGrid fluidGrid = (FluidGrid) component;

        encodeMarkup(fc, fluidGrid);
        encodeScript(fc, fluidGrid);
    }

    protected void encodeMarkup(final FacesContext fc, final FluidGrid fluidGrid) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = fluidGrid.getClientId(fc);
        String styleClass = fluidGrid.getStyleClass();
        styleClass = styleClass == null ? GRID_CLASS : GRID_CLASS + " " + styleClass;

        writer.startElement("div", fluidGrid);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (fluidGrid.getStyle() != null) {
            writer.writeAttribute("style", fluidGrid.getStyle(), "style");
        }

        writer.writeAttribute("role", LIST_ROLE, null);

        if (fluidGrid.getVar() != null) {
            // dynamic items
            final Object value = fluidGrid.getValue();
            if (value != null) {
                if (!(value instanceof Collection<?>)) {
                    throw new FacesException("Value in FluidGrid must be of type Collection / List");
                }

                for (final UIComponent kid : fluidGrid.getChildren()) {
                    if (kid.isRendered() && !(kid instanceof UIFluidGridItem)) {
                        // first render children like stamped elements, etc.
                        renderChild(fc, kid);
                    }
                }

                @SuppressWarnings("unchecked")
                final Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
                for (final FluidGridItem fluidGridItem : col) {
                    // find ui item by type
                    final UIFluidGridItem uiItem = fluidGrid.getItem(fluidGridItem.getType());

                    if (uiItem.isRendered()) {
                        // set data in request scope
                        fluidGrid.setData(fluidGridItem);

                        // render item
                        renderItem(fc, writer, fluidGrid, uiItem);
                    }
                }
            }
        }
        else {
            // static items
            for (final UIComponent kid : fluidGrid.getChildren()) {
                if (kid.isRendered()) {
                    if (kid instanceof UIFluidGridItem) {
                        // render item
                        renderItem(fc, writer, fluidGrid, (UIFluidGridItem) kid);
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

    protected void encodeScript(final FacesContext fc, final FluidGrid fluidGrid) throws IOException {
        final String clientId = fluidGrid.getClientId(fc);

        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.initWithDomReady("ExtFluidGrid", fluidGrid.resolveWidgetVar(), clientId);
        wb.append(",opts:{");
        wb.append("isFitWidth:" + fluidGrid.isFitWidth());
        wb.append(",isOriginLeft:" + fluidGrid.isOriginLeft());
        wb.append(",isOriginTop:" + fluidGrid.isOriginTop());
        wb.append(",isResizeBound:" + fluidGrid.isResizeBound());
        wb.append(",hasImages:" + fluidGrid.isHasImages());

        if (fluidGrid.gethGutter() != 0) {
            wb.append(",gutter:" + fluidGrid.gethGutter());
        }

        final String stamp = SearchExpressionFacade.resolveClientIds(fc, fluidGrid, fluidGrid.getStamp());
        if (stamp != null) {
            wb.append(",stamp:'" + stamp + "'");
        }

        if (fluidGrid.getTransitionDuration() != null) {
            wb.append(",transitionDuration:'" + fluidGrid.getTransitionDuration() + "'");
        }
        else {
            wb.append(",transitionDuration:0");
        }

        wb.append("}");

        encodeClientBehaviors(fc, fluidGrid);
        wb.finish();
    }

    protected void renderItem(final FacesContext fc, final ResponseWriter writer, final FluidGrid fluidGrid,
                final UIFluidGridItem uiItem)
                throws IOException {
        writer.startElement("div", null);

        if (uiItem.getStyleClass() != null) {
            writer.writeAttribute("class", GRID_ITEM_CLASS + " " + uiItem.getStyleClass(), null);
        }
        else {
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
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
