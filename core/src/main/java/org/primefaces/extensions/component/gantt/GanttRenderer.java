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
package org.primefaces.extensions.component.gantt;

import java.io.IOException;
import java.util.List;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>gantt</code> component.
 */
@FacesRenderer(rendererType = Gantt.DEFAULT_RENDERER, componentFamily = Gantt.COMPONENT_FAMILY)
public class GanttRenderer extends CoreRenderer<Gantt> {

    @Override
    public void encodeEnd(final FacesContext context, final Gantt component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    @Override
    public void decode(final FacesContext context, final Gantt component) {
        decodeBehaviors(context, component);
    }

    private void encodeMarkup(final FacesContext context, final Gantt component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId();
        final String widgetVar = component.resolveWidgetVar();
        final String styleClass = getStyleClassBuilder(context)
                    .add(Gantt.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }
        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final Gantt component)
                throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final List<GanttTask> tasks;
        if (component.getTasks() == null) {
            tasks = new java.util.ArrayList<>();
        }
        else {
            tasks = component.getTasks();
        }

        final JSONArray tasksJson = toJSON(tasks);

        wb.init("ExtGantt", component);
        wb.attr("tasks", tasksJson.toString());
        wb.attr("viewMode", component.getViewMode());
        wb.attr("todayButton", component.getTodayButton());
        wb.attr("viewModeSelect", component.getViewModeSelect());
        wb.attr("readonly", component.getReadonly());
        wb.attr("showProgress", component.getShowProgress());
        wb.attr("showExpectedProgress", component.getShowExpectedProgress());
        wb.attr("columnWidth", component.getColumnWidth());
        wb.attr("barHeight", component.getBarHeight());
        wb.attr("dateFormat", component.getDateFormat());
        wb.attr("lang", component.getLang());
        wb.attr("weekendColor", component.getWeekendColor());
        wb.attr("snapAt", component.getSnapAt());
        wb.attr("infinitePadding", component.getInfinitePadding());
        wb.nativeAttr("extender", component.getExtender());

        encodeClientBehaviors(context, component);
        wb.finish();
    }

    public static JSONArray toJSON(final List<GanttTask> tasks) {
        final JSONArray array = new JSONArray();

        for (final GanttTask task : tasks) {
            final JSONObject json = new JSONObject();
            json.put("id", task.getId());
            json.put("name", task.getName());
            json.put("start", task.getStart());
            json.put("end", task.getEnd());
            if (task.getProgress() != null) {
                json.put("progress", task.getProgress());
            }
            if (task.getDependencies() != null && !task.getDependencies().isEmpty()) {
                json.put("dependencies", task.getDependencies());
            }
            if (task.getColor() != null && !task.getColor().isEmpty()) {
                json.put("color", task.getColor());
            }
            if (task.getColorProgress() != null && !task.getColorProgress().isEmpty()) {
                json.put("color_progress", task.getColorProgress());
            }
            if (task.getCustomClass() != null && !task.getCustomClass().isEmpty()) {
                json.put("custom_class", task.getCustomClass());
            }
            if (task.getThumbnail() != null && !task.getThumbnail().isEmpty()) {
                json.put("thumbnail", task.getThumbnail());
            }
            if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                json.put("description", task.getDescription());
            }
            if (task.getBarLabel() != null && !task.getBarLabel().isEmpty()) {
                json.put("bar_label", task.getBarLabel());
            }
            if (task.isInvalid()) {
                json.put("invalid", true);
            }
            array.put(json);
        }

        return array;
    }
}
