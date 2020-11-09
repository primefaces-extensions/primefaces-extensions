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
package org.primefaces.extensions.component.dynaform;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.ContextCallback;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.api.InputHolder;
import org.primefaces.component.row.Row;
import org.primefaces.extensions.model.dynaform.*;
import org.primefaces.extensions.util.Attrs;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.CompositeUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for {@link DynaForm} component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormRenderer extends CoreRenderer {

    public static final String FACET_HEADER_REGULAR = "headerRegular";
    public static final String FACET_FOOTER_REGULAR = "footerRegular";
    public static final String FACET_HEADER_EXTENDED = "headerExtended";
    public static final String FACET_FOOTER_EXTENDED = "footerExtended";
    public static final String FACET_BUTTON_BAR = "buttonBar";
    public static final String FACET_STATIC_TOP = "staticTop";
    public static final String FACET_STATIC_BOTTOM = "staticBottom";

    public static final String GRID_CLASS = "pe-dynaform-grid";
    public static final String NESTED_GRID_CLASS = "pe-dynaform-nested-grid";
    public static final String CELL_CLASS = "pe-dynaform-cell";
    public static final String CELL_FIRST_CLASS = "pe-dynaform-cell-first";
    public static final String CELL_LAST_CLASS = "pe-dynaform-cell-last";
    public static final String LABEL_CLASS = "pe-dynaform-label";
    public static final String LABEL_INVALID_CLASS = "ui-state-error ui-corner-all";
    public static final String LABEL_INDICATOR_CLASS = "pe-dynaform-label-rfi";
    public static final String LABEL_CONTROL_TYPE_CLASS_FORMAT = "pe-dynaform-%s-label";

    public static final String FACET_BUTTON_BAR_TOP_CLASS = "pe-dynaform-buttonbar-top";
    public static final String FACET_BUTTON_BAR_BOTTOM_CLASS = "pe-dynaform-buttonbar-bottom";
    public static final String FACET_HEADER_CLASS = "pe-dynaform-headerfacet";
    public static final String FACET_FOOTER_CLASS = "pe-dynaform-footerfacet";
    public static final String FACET_STATIC_TOP_CLASS = "pe-dynaform-static-top";
    public static final String FACET_STATIC_BOTTOM_CLASS = "pe-dynaform-static-bottom";
    public static final String EXTENDED_ROW_CLASS = "pe-dynaform-extendedrow";

    public static final String BUTTON_BAR_ROLE = "toolbar";
    public static final String GRID_CELL_ROLE = "gridcell";

    private static final String[] EMPTY_COLUMN_CLASSES = new String[] {Constants.EMPTY_STRING, Constants.EMPTY_STRING};

    private static final Logger LOGGER = Logger.getLogger(DynaFormRenderer.class.getName());

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final DynaForm dynaForm = (DynaForm) component;

        // get model
        final DynaFormModel dynaFormModel = (DynaFormModel) dynaForm.getValue();
        encodeMarkup(fc, dynaForm, dynaFormModel, false);
        encodeScript(fc, dynaForm, dynaFormModel);
    }

    protected void encodeMarkup(final FacesContext fc, final DynaForm dynaForm, final DynaFormModel dynaFormModel, final boolean nestedGrid)
                throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();

        writer.startElement("table", dynaForm);

        if (!nestedGrid) {
            final String clientId = dynaForm.getClientId(fc);
            writer.writeAttribute("id", clientId, "id");
        }

        String styleClass = nestedGrid ? NESTED_GRID_CLASS : GRID_CLASS;
        styleClass += dynaForm.getStyleClass() == null ? Constants.EMPTY_STRING : " " + dynaForm.getStyleClass();

        writer.writeAttribute("cellspacing", "0", "cellspacing");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        if (dynaForm.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, dynaForm.getStyle(), Attrs.STYLE);
        }

        writer.writeAttribute("role", "grid", null);

        // prepare labels with informations about corresponding control components
        preRenderLabel(fc, dynaForm, dynaFormModel);

        final int totalColspan = getTotalColspan(dynaFormModel);
        final String bbPosition = dynaForm.getButtonBarPosition();

        if (!nestedGrid) {
            if ("top".equals(bbPosition) || "both".equals(bbPosition)) {
                encodeFacet(fc, dynaForm, FACET_BUTTON_BAR, totalColspan, FACET_BUTTON_BAR_TOP_CLASS, BUTTON_BAR_ROLE, false, true);
            }

            encodeFacet(fc, dynaForm, FACET_HEADER_REGULAR, totalColspan, FACET_HEADER_CLASS, GRID_CELL_ROLE, false, true);
            encodeStatic(fc, dynaForm, FACET_STATIC_TOP, totalColspan, FACET_STATIC_TOP_CLASS);
        }

        // encode regular grid
        encodeBody(fc, dynaForm, dynaFormModel.getRegularRows(), false, true);

        if (!nestedGrid) {
            encodeFacet(fc, dynaForm, FACET_FOOTER_REGULAR, totalColspan, FACET_FOOTER_CLASS, GRID_CELL_ROLE, false, true);
            encodeFacet(fc, dynaForm, FACET_HEADER_EXTENDED, totalColspan, FACET_HEADER_CLASS, GRID_CELL_ROLE, true,
                        dynaForm.isOpenExtended());
        }

        // encode extended grid
        encodeBody(fc, dynaForm, dynaFormModel.getExtendedRows(), true, dynaForm.isOpenExtended());

        if (!nestedGrid) {
            encodeStatic(fc, dynaForm, FACET_STATIC_BOTTOM, totalColspan, FACET_STATIC_BOTTOM_CLASS);
            encodeFacet(fc, dynaForm, FACET_FOOTER_EXTENDED, totalColspan, FACET_FOOTER_CLASS, GRID_CELL_ROLE, true, dynaForm.isOpenExtended());

            if ("bottom".equals(bbPosition) || "both".equals(bbPosition)) {
                encodeFacet(fc, dynaForm, FACET_BUTTON_BAR, totalColspan, FACET_BUTTON_BAR_BOTTOM_CLASS, BUTTON_BAR_ROLE, false, true);
            }
        }

        writer.endElement("table");
    }

    protected void encodeScript(final FacesContext fc, final DynaForm dynaForm, final DynaFormModel dynaFormModel) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(fc);
        wb.init("ExtDynaForm", dynaForm);
        wb.attr("uuid", dynaFormModel.getUuid());
        wb.attr("autoSubmit", dynaForm.isAutoSubmit());
        wb.attr("isPostback", fc.isPostback());
        wb.finish();
    }

    protected void encodeFacet(final FacesContext fc, final DynaForm dynaForm, final String name, final int totalColspan, final String styleClass,
                final String role,
                final boolean extended, final boolean visible) throws IOException {
        final UIComponent facet = dynaForm.getFacet(name);
        if (ComponentUtils.shouldRenderFacet(facet)) {
            final ResponseWriter writer = fc.getResponseWriter();
            writer.startElement("tr", null);
            if (extended) {
                writer.writeAttribute(Attrs.CLASS, EXTENDED_ROW_CLASS, null);
            }

            if (!visible) {
                writer.writeAttribute(Attrs.STYLE, "display:none;", null);
            }

            writer.writeAttribute("role", "row", null);
            writer.startElement("td", null);
            if (totalColspan > 1) {
                writer.writeAttribute("colspan", totalColspan, null);
            }

            writer.writeAttribute(Attrs.CLASS, styleClass, null);
            writer.writeAttribute("role", role, null);

            facet.encodeAll(fc);

            writer.endElement("td");
            writer.endElement("tr");
        }
    }

    protected void encodeBody(final FacesContext fc, final DynaForm dynaForm, final List<DynaFormRow> dynaFormRows, final boolean extended,
                final boolean visible) throws IOException {
        if (dynaFormRows == null || dynaFormRows.isEmpty()) {
            return;
        }

        final ResponseWriter writer = fc.getResponseWriter();

        final String columnClassesValue = dynaForm.getColumnClasses();
        final String[] columnClasses = columnClassesValue == null ? EMPTY_COLUMN_CLASSES : columnClassesValue.split(",");
        final String labelCommonClass = columnClasses[0].trim();
        final String controlCommonClass = columnClasses.length > 1 ? columnClasses[1].trim() : EMPTY_COLUMN_CLASSES[1];

        for (final DynaFormRow dynaFormRow : dynaFormRows) {
            writer.startElement("tr", null);
            if (extended) {
                writer.writeAttribute(Attrs.CLASS, EXTENDED_ROW_CLASS, null);
            }

            if (!visible) {
                writer.writeAttribute(Attrs.STYLE, "display:none;", null);
            }

            writer.writeAttribute("role", "row", null);

            final List<AbstractDynaFormElement> elements = dynaFormRow.getElements();
            final int size = elements.size();

            for (int i = 0; i < size; i++) {
                final AbstractDynaFormElement element = elements.get(i);

                writer.startElement("td", null);
                if (element.getColspan() > 1) {
                    writer.writeAttribute("colspan", element.getColspan(), null);
                }

                if (element.getRowspan() > 1) {
                    writer.writeAttribute("rowspan", element.getRowspan(), null);
                }

                String styleClass = CELL_CLASS;
                if (i == 0 && element.getColspan() == 1) {
                    styleClass = styleClass + " " + CELL_FIRST_CLASS;
                }

                if (i == size - 1 && element.getColspan() == 1) {
                    styleClass = styleClass + " " + CELL_LAST_CLASS;
                }

                if (element instanceof DynaFormLabel) {
                    renderLabel(writer, labelCommonClass, (DynaFormLabel) element, styleClass);
                }
                else if (element instanceof DynaFormControl) {
                    renderControl(fc, dynaForm, writer, controlCommonClass, (DynaFormControl) element, styleClass);
                }
                else if (element instanceof DynaFormModelElement) {
                    renderNestedModel(fc, dynaForm, writer, (DynaFormModelElement) element, styleClass);
                }

                writer.endElement("td");
            }

            writer.endElement("tr");
        }

        dynaForm.resetData();
    }

    protected void renderNestedModel(
                final FacesContext fc, final DynaForm dynaForm, final ResponseWriter writer, final DynaFormModelElement element, final String styleClass)
                throws IOException {

        writer.writeAttribute(Attrs.CLASS, styleClass, null);
        writer.writeAttribute("role", GRID_CELL_ROLE, null);

        encodeMarkup(fc, dynaForm, element.getModel(), true);
    }

    protected void renderControl(
                final FacesContext fc, final DynaForm dynaForm, final ResponseWriter writer, final String controlCommonClass, final DynaFormControl element,
                String styleClass) throws IOException {
        dynaForm.setData(element);

        // find control's cell by type
        final UIDynaFormControl cell = dynaForm.getControlCell(element.getType());

        if (cell.getStyle() != null) {
            writer.writeAttribute(Attrs.STYLE, cell.getStyle(), null);
        }

        if (cell.getStyleClass() != null) {
            styleClass = styleClass + " " + cell.getStyleClass();
        }

        writer.writeAttribute(Attrs.CLASS, (styleClass + " " + controlCommonClass).trim(), null);
        writer.writeAttribute("role", GRID_CELL_ROLE, null);

        cell.encodeAll(fc);
    }

    protected void renderLabel(final ResponseWriter writer, final String labelCommonClass,
                final DynaFormLabel element, final String styleClass) throws IOException {

        writer.writeAttribute(Attrs.CLASS, (styleClass
                    + " " + LABEL_CLASS
                    + " " + ExtLangUtils.defaultString(element.getStyleClass())
                    + " " + labelCommonClass).trim(), null);
        writer.writeAttribute("role", GRID_CELL_ROLE, null);

        writer.startElement(Attrs.LABEL, null);
        if (!element.isTargetValid()) {
            writer.writeAttribute(Attrs.CLASS, LABEL_INVALID_CLASS, null);
        }

        writer.writeAttribute("for", element.getTargetClientId(), null);

        if (element.getValue() != null) {
            if (element.isEscape()) {
                writer.writeText(element.getValue(), "value");
            }
            else {
                writer.write(element.getValue());
            }
        }

        if (element.isTargetRequired()) {
            writer.startElement("span", null);
            writer.writeAttribute(Attrs.CLASS, LABEL_INDICATOR_CLASS, null);
            writer.write("*");
            writer.endElement("span");
        }

        writer.endElement(Attrs.LABEL);
    }

    protected void encodeStatic(final FacesContext fc, final DynaForm dynaForm, final String name, final int totalColspan, final String styleClass)
                throws IOException {
        final UIComponent facet = dynaForm.getFacet(name);
        if (facet == null || !facet.isRendered()) {
            return;
        }

        final List<UIComponent> components = facet instanceof Row
                    ? Collections.singletonList(facet)
                    : facet.getChildren();

        final ResponseWriter writer = fc.getResponseWriter();
        for (final UIComponent child : components) {
            if (!child.isRendered() || !(child instanceof Row)) {
                continue;
            }

            writer.startElement("tr", null);
            if (shouldWriteId(child)) {
                writer.writeAttribute("id", child.getClientId(fc), null);
            }
            writer.writeAttribute("role", "row", null);
            writer.writeAttribute(Attrs.CLASS, styleClass, null);

            int i = 0;
            for (final UIComponent column : child.getChildren()) {
                if (!column.isRendered()) {
                    continue;
                }

                String columnClass = CELL_CLASS;
                if (i % totalColspan == 0) {
                    columnClass = columnClass + " " + CELL_FIRST_CLASS;
                }

                if ((i + 1) % totalColspan == 0) {
                    columnClass = columnClass + " " + CELL_LAST_CLASS;
                }

                writer.startElement("td", null);
                writer.writeAttribute("role", GRID_CELL_ROLE, null);
                writer.writeAttribute(Attrs.CLASS, columnClass, null);
                column.encodeAll(fc);
                writer.endElement("td");
                i++;
            }

            writer.endElement("tr");
        }
    }

    protected void preRenderLabel(final FacesContext fc, final DynaForm dynaForm, final DynaFormModel model) {
        for (final DynaFormLabel label : model.getLabels()) {
            // get corresponding control if any
            final DynaFormControl control = label.getForControl();
            if (control != null) {
                // find control's cell by type
                final UIDynaFormControl cell = dynaForm.getControlCell(control.getType());

                if (cell.getFor() != null) {
                    dynaForm.setData(control);

                    final UIComponent target = cell.findComponent(cell.getFor());
                    if (target == null) {
                        LOGGER.warning("Cannot find component with identifier " + cell.getFor() + " inside UIDynaFormControl");

                        continue;
                    }

                    final String targetClientId = target instanceof InputHolder
                                ? ((InputHolder) target).getInputClientId()
                                : target.getClientId(fc);
                    label.setTargetClientId(targetClientId);

                    final ContextCallback callback = (context, target1) -> {
                        label.setTargetValid(((EditableValueHolder) target1).isValid());
                        label.setTargetRequired(((EditableValueHolder) target1).isRequired());
                    };

                    if (CompositeUtils.isComposite(target)) {
                        CompositeUtils.invokeOnDeepestEditableValueHolder(fc, target, callback);
                    }
                    else {
                        callback.invokeContextCallback(fc, target);
                    }

                    if (label.getValue() != null) {
                        target.getAttributes().put(Attrs.LABEL, label.getValue());
                    }

                    label.setStyleClass(String.format(LABEL_CONTROL_TYPE_CLASS_FORMAT, control.getType().toLowerCase()));
                }
            }
        }

        dynaForm.resetData();
    }

    protected int getTotalColspan(final DynaFormModel dynaFormModel) {
        // the main aim of this method is layout check
        int totalColspan = -1;
        for (final DynaFormRow dynaFormRow : dynaFormModel.getRegularRows()) {
            if (dynaFormRow.getTotalColspan() > totalColspan) {
                totalColspan = dynaFormRow.getTotalColspan();
            }
        }

        if (dynaFormModel.getExtendedRows() != null) {
            for (final DynaFormRow dynaFormRow : dynaFormModel.getExtendedRows()) {
                if (dynaFormRow.getTotalColspan() > totalColspan) {
                    totalColspan = dynaFormRow.getTotalColspan();
                }
            }
        }

        if (totalColspan < 1) {
            totalColspan = 1;
        }

        return totalColspan;
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) {
        // Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
