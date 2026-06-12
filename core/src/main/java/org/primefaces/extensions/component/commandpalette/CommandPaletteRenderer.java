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
package org.primefaces.extensions.component.commandpalette;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

@FacesRenderer(rendererType = CommandPalette.DEFAULT_RENDERER, componentFamily = CommandPalette.COMPONENT_FAMILY)
public class CommandPaletteRenderer extends CoreRenderer<CommandPalette> {

    @Override
    public void decode(final FacesContext context, final CommandPalette component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final CommandPalette component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    private void encodeMarkup(final FacesContext context, final CommandPalette component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = component.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(CommandPalette.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("div", component);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (component.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, component.getStyle(), Attrs.STYLE);
        }
        writer.writeAttribute("data-trigger-event", component.getTriggerEvent(), null);

        if (component.isShowHeader() && component.getLabel() != null) {
            writer.startElement("div", null);
            writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-header", null);
            writer.startElement("label", null);
            writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-title", null);
            writer.writeText(component.getLabel(), null);
            writer.endElement("label");
            writer.endElement("div");
        }

        if (component.isShowFilter()) {
            writer.startElement("div", null);
            writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-filter", null);
            writer.startElement("input", null);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-filter-input", null);
            final String placeholder = component.getFilterPlaceholder();
            if (placeholder != null) {
                writer.writeAttribute("placeholder", placeholder, null);
            }
            writer.endElement("input");
            writer.endElement("div");
        }

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-groups", null);

        final Map<String, List<CommandPaletteItem>> groups = buildGroupMap(component);
        for (final Map.Entry<String, List<CommandPaletteItem>> entry : groups.entrySet()) {
            encodeGroup(context, component, entry.getKey(), entry.getValue());
        }

        writer.endElement("div");
        writer.endElement("div");
    }

    private Map<String, List<CommandPaletteItem>> buildGroupMap(final CommandPalette component) {
        final Map<String, List<CommandPaletteItem>> groups = new LinkedHashMap<>();
        for (final UIComponent child : component.getChildren()) {
            if (child instanceof CommandPaletteItem) {
                final CommandPaletteItem item = (CommandPaletteItem) child;
                final String group = item.getGroup();
                groups.computeIfAbsent(group, k -> new ArrayList<>()).add(item);
            }
        }
        return groups;
    }

    private void encodeGroup(final FacesContext context, final CommandPalette component, final String groupName,
                final List<CommandPaletteItem> items) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-group", null);

        final String groupTitle = items.isEmpty() ? null : items.get(0).getGroupTitle();
        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-group-header", null);
        if (groupTitle != null) {
            writer.writeAttribute(Attrs.TITLE, groupTitle, null);
        }
        writer.startElement("span", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-group-toggle", null);
        writer.writeText("\u25B6", null);
        writer.endElement("span");
        writer.startElement("span", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-group-label", null);
        writer.writeText(groupName, null);
        writer.endElement("span");
        writer.endElement("div");

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-group-items", null);
        for (final CommandPaletteItem item : items) {
            encodeItem(context, component, item);
        }
        writer.endElement("div");

        writer.endElement("div");
    }

    private void encodeItem(final FacesContext context, final CommandPalette component, final CommandPaletteItem item)
                throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute(Attrs.CLASS, CommandPalette.STYLE_CLASS + "-item", null);
        writer.writeAttribute("data-group", item.getGroup(), null);
        writer.writeAttribute("data-value", item.getValue(), null);
        writer.writeAttribute("data-label", item.getLabel(), null);
        if (item.getItemTitle() != null) {
            writer.writeAttribute(Attrs.TITLE, item.getItemTitle(), null);
        }
        writer.writeText(item.getLabel(), null);
        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final CommandPalette component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.init("ExtCommandPalette", component)
                    .attr("for", component.getFor())
                    .attr("triggerEvent", component.getTriggerEvent(), "contextmenu")
                    .attr("label", component.getLabel())
                    .attr("filterPlaceholder", component.getFilterPlaceholder(), "Search...")
                    .attr("showHeader", component.isShowHeader(), true)
                    .attr("showFilter", component.isShowFilter(), true)
                    .attr("width", component.getWidth(), "280")
                    .attr("height", component.getHeight(), "400");

        if (!LangUtils.isBlank(component.getOnSelect())) {
            wb.callback("onSelect", "function(option)", component.getOnSelect());
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }
}
