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
package org.primefaces.extensions.component.kanban;

import java.io.IOException;
import java.util.List;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.model.kanban.KanbanColumn;
import org.primefaces.extensions.model.kanban.KanbanItem;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.EscapeUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the <code>Kanban</code> component.
 *
 * @author jxmai
 * @since 16.0.0
 */
@FacesRenderer(componentFamily = KanbanBase.COMPONENT_FAMILY, rendererType = KanbanBase.DEFAULT_RENDERER)
public class KanbanRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Kanban kanban = (Kanban) component;
        encodeMarkup(context, kanban);
        encodeScript(context, kanban);
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    private void encodeMarkup(final FacesContext context, final Kanban kanban) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = kanban.getClientId();
        final String widgetVar = kanban.resolveWidgetVar();
        final String styleClass = getStyleClassBuilder(context)
                    .add(Kanban.STYLE_CLASS)
                    .add(kanban.getStyleClass())
                    .build();

        writer.startElement("div", kanban);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (kanban.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, kanban.getStyle(), Attrs.STYLE);
        }
        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final Kanban kanban)
                throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final Object value = kanban.getValue();
        if (value == null) {
            throw new FacesException("The value attribute is required and must be a List<KanbanColumn>");
        }
        if (!(value instanceof List)) {
            throw new FacesException("The value attribute must be a List<KanbanColumn>, but was: "
                        + value.getClass().getName());
        }

        @SuppressWarnings("unchecked")
        final List<KanbanColumn> columns = (List<KanbanColumn>) value;
        final String data = toJSON(columns).toString();

        wb.init("ExtKanban", kanban);
        wb.attr("draggable", kanban.isDraggable());
        wb.attr("addItemButton", kanban.isAddItemButton());
        wb.attr("gutter", kanban.getGutter());
        wb.attr("widthBoard", kanban.getWidthBoard());
        wb.attr("responsivePercentage", kanban.isResponsivePercentage());
        wb.nativeAttr("extender", kanban.getExtender());
        wb.nativeAttr("boards", data);

        encodeClientBehaviors(context, kanban);
        wb.finish();
    }

    private static JSONArray toJSON(final List<KanbanColumn> columns) {
        final JSONArray boards = new JSONArray();

        for (final KanbanColumn column : columns) {
            final JSONObject board = new JSONObject();
            board.put("id", column.getId());
            board.put("title", column.getTitle());

            if (column.getCssClass() != null && !column.getCssClass().isEmpty()) {
                board.put("class", column.getCssClass().replaceAll("\\s+", ","));
            }

            final JSONArray items = new JSONArray();
            for (final KanbanItem item : column.getItems()) {
                final JSONObject itemJson = new JSONObject();
                itemJson.put("id", item.getId());

                final StringBuilder titleHtml = new StringBuilder();
                titleHtml.append("<div class=\"kanban-item-title\">")
                            .append(EscapeUtils.forHtml(item.getTitle()))
                            .append("</div>");
                if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                    titleHtml.append("<div class=\"kanban-item-description\">")
                                .append(EscapeUtils.forHtml(item.getDescription()))
                                .append("</div>");
                }
                itemJson.put("title", titleHtml.toString());

                if (item.getCssClass() != null && !item.getCssClass().isEmpty()) {
                    final JSONArray classes = new JSONArray();
                    for (final String cls : item.getCssClass().split(" ")) {
                        if (!cls.isEmpty()) {
                            classes.put(cls);
                        }
                    }
                    itemJson.put("class", classes);
                }

                items.put(itemJson);
            }
            board.put("item", items);
            boards.put(board);
        }

        return boards;
    }

}
