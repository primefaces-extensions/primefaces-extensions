/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.fab;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link FloatingActionButton} component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 * @deprecated replaced with PrimeFaces Speed Dial
 */
@Deprecated
public class FloatingActionButtonRenderer extends BaseMenuRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    protected void encodeMarkup(final FacesContext context, final AbstractMenu menu) throws IOException {
        final FloatingActionButton fab = (FloatingActionButton) menu;
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = fab.getClientId(context);
        final String styleClass = getStyleClassBuilder(context)
                    .add(FloatingActionButton.STYLE_CLASS)
                    .add(fab.getStyleClass())
                    .build();

        writer.startElement("span", fab);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        encodeMainButton(writer, fab);
        encodeMenu(context, writer, fab);
        writer.endElement("span");
    }

    protected void encodeMainButton(final ResponseWriter writer, final FloatingActionButton fab) throws IOException {
        // Button, start
        writer.startElement("span", fab);
        String classes = "ui-fab-main ui-button";
        if (fab.getIconActive() != null) {
            classes += " ui-fab-double";
        }
        writer.writeAttribute(Attrs.CLASS, classes, "styleClass");
        if (fab.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, fab.getStyle(), Attrs.STYLE);
        }

        // Icon
        writer.startElement("span", fab);
        writer.writeAttribute(Attrs.CLASS, "ui-icon ui-icon-0 " + fab.getIcon(), "icon");
        writer.endElement("span");
        if (fab.getIconActive() != null) {
            writer.startElement("span", fab);
            writer.writeAttribute(Attrs.CLASS, "ui-icon ui-icon-1 " + fab.getIconActive(), "icon");
            writer.endElement("span");
        }

        // Button, end
        writer.endElement("span");
    }

    protected void encodeMenu(final FacesContext context, final ResponseWriter writer, final FloatingActionButton fab)
                throws IOException {
        writer.startElement("ul", fab);
        for (final MenuElement child : fab.getElements()) {
            if (child.isRendered() && child instanceof MenuItem) {
                encodeMenuItem(context, writer, fab, (MenuItem) child);
            }
        }
        writer.endElement("ul");
    }

    protected void encodeMenuItem(final FacesContext context, final ResponseWriter writer,
                final FloatingActionButton fab,
                final MenuItem menuItem) throws IOException {
        writer.startElement("li", fab);
        writer.writeAttribute("role", "menuitem", null);
        writer.writeAttribute(Attrs.CLASS, "ui-button", null);
        // Use style here allowing to set background color
        if (menuItem.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, menuItem.getStyle(), null);
            menuItem.setStyleClass(null);
        }
        if (!menuItem.isDisabled()) {
            encodeMenuItem(context, fab, menuItem, fab.getTabindex());
        }
        writer.endElement("li");
    }

    @Override
    protected void encodeScript(final FacesContext context, final AbstractMenu menu) throws IOException {
        final FloatingActionButton fab = (FloatingActionButton) menu;
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtFAB", fab);
        if (fab.isKeepOpen()) {
            wb.attr("keepOpen", fab.isKeepOpen());
        }
        encodeClientBehaviors(context, fab);
        wb.finish();
    }

}
