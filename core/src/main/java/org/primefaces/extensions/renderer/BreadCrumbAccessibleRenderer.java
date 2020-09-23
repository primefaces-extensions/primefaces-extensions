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
package org.primefaces.extensions.renderer;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.breadcrumb.BreadCrumbRenderer;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * Accessible bread crumb renderer.
 * <p>
 * https://www.w3.org/TR/wai-aria-practices/examples/breadcrumb/index.html
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 9.0
 */
public class BreadCrumbAccessibleRenderer extends BreadCrumbRenderer {

    @Override
    protected void encodeMarkup(FacesContext context, AbstractMenu menu) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        BreadCrumb breadCrumb = (BreadCrumb) menu;
        String clientId = breadCrumb.getClientId(context);
        String styleClass = breadCrumb.getStyleClass();
        styleClass = styleClass == null ? BreadCrumb.CONTAINER_CLASS : BreadCrumb.CONTAINER_CLASS + " " + styleClass;
        int elementCount = menu.getElementsCount();
        List<MenuElement> menuElements = menu.getElements();
        boolean isIconHome = breadCrumb.getHomeDisplay().equals("icon");

        // home icon for first item
        if (isIconHome && elementCount > 0) {
            ((MenuItem) menuElements.get(0)).setStyleClass("ui-icon ui-icon-home");
        }

        writer.startElement("nav", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute(HTML.ARIA_LABEL, "Breadcrumb", null);
        if (breadCrumb.getStyle() != null) {
            writer.writeAttribute("style", breadCrumb.getStyle(), null);
        }

        if (elementCount > 0) {
            writer.startElement("ol", null);

            for (int i = 0; i < elementCount; i++) {
                MenuElement element = menuElements.get(i);

                if (element.isRendered() && element instanceof MenuItem) {
                    MenuItem item = (MenuItem) element;

                    writer.startElement("li", null);

                    boolean last = i + 1 == elementCount;
                    if (item.isDisabled() || (breadCrumb.isLastItemDisabled() && last)) {
                        encodeDisabledMenuItem(context, item);
                    }
                    else {
                        encodeMenuItem(context, menu, item, menu.getTabindex(), last);
                    }

                    writer.endElement("li");
                }
            }

            UIComponent optionsFacet = menu.getFacet("options");
            if (ComponentUtils.shouldRenderFacet(optionsFacet)) {
                writer.startElement("li", null);
                writer.writeAttribute("class", BreadCrumb.OPTIONS_CLASS, null);
                optionsFacet.encodeAll(context);
                writer.endElement("li");
            }

            writer.endElement("ol");
        }

        writer.endElement("nav");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    protected void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException {
        // Do nothing
    }

    protected void encodeMenuItem(FacesContext context, AbstractMenu menu, MenuItem menuitem, String tabindex, boolean last)
                throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String title = menuitem.getTitle();
        String style = menuitem.getStyle();
        boolean disabled = menuitem.isDisabled();
        String rel = menuitem.getRel();

        writer.startElement("a", null);
        writer.writeAttribute("tabindex", tabindex, null);
        if (last) {
            writer.writeAttribute("aria-current", "page", null);
        }
        if (shouldRenderId(menuitem)) {
            writer.writeAttribute("id", menuitem.getClientId(), null);
        }
        if (title != null) {
            writer.writeAttribute("title", title, null);
        }

        String styleClass = getLinkStyleClass(menuitem);
        if (disabled) {
            styleClass = styleClass + " ui-state-disabled";
        }

        writer.writeAttribute("class", styleClass, null);

        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        if (rel != null) {
            writer.writeAttribute("rel", rel, null);
        }

        encodeOnClick(context, menu, menuitem);

        encodeMenuItemContent(context, menu, menuitem);

        writer.endElement("a");
    }

    private void encodeDisabledMenuItem(FacesContext context, MenuItem menuItem) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        String style = menuItem.getStyle();
        String styleClass = menuItem.getStyleClass();
        styleClass = styleClass == null ? BreadCrumb.MENUITEM_LINK_CLASS : BreadCrumb.MENUITEM_LINK_CLASS + " " + styleClass;
        styleClass += " ui-state-disabled";

        writer.startElement("span", null); // outer span
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        String icon = menuItem.getIcon();
        Object value = menuItem.getValue();

        if (icon != null) {
            writer.startElement("span", null);
            writer.writeAttribute("class", BreadCrumb.MENUITEM_ICON_CLASS + " " + icon, null);
            writer.writeAttribute(HTML.ARIA_HIDDEN, "true", null);
            writer.endElement("span");
        }

        writer.startElement("span", null);
        writer.writeAttribute("class", BreadCrumb.MENUITEM_TEXT_CLASS, null);

        if (value != null) {
            if (menuItem.isEscape()) {
                writer.writeText(value, "value");
            }
            else {
                writer.write(value.toString());
            }
        }
        writer.endElement("span"); // text span
        writer.endElement("span"); // outer span
    }

}
