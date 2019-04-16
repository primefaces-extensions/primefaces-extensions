/**
 * Copyright 2011-2019 PrimeFaces Extensions
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

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.row.Row;
import org.primefaces.extensions.model.dynaform.AbstractDynaFormElement;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormModelElement;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.renderkit.CoreRenderer;
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

    private static final Logger LOGGER = Logger.getLogger(DynaFormRenderer.class.getName());

    private static final String FACET_HEADER_REGULAR = "headerRegular";
    private static final String FACET_FOOTER_REGULAR = "footerRegular";
    private static final String FACET_HEADER_EXTENDED = "headerExtended";
    private static final String FACET_FOOTER_EXTENDED = "footerExtended";
    private static final String FACET_BUTTON_BAR = "buttonBar";
    private static final String FACET_STATIC_TOP = "staticTop";
    private static final String FACET_STATIC_BOTTOM = "staticBottom";

    private static final String GRID_CLASS = "pe-dynaform-grid";
    private static final String NESTED_GRID_CLASS = "pe-dynaform-nested-grid";
    private static final String CELL_CLASS = "pe-dynaform-cell";
    private static final String CELL_FIRST_CLASS = "pe-dynaform-cell-first";
    private static final String CELL_LAST_CLASS = "pe-dynaform-cell-last";
    private static final String LABEL_CLASS = "pe-dynaform-label";
    private static final String LABEL_INVALID_CLASS = "ui-state-error ui-corner-all";
    private static final String LABEL_INDICATOR_CLASS = "pe-dynaform-label-rfi";
    private static final String LABEL_CONTROL_TYPE_CLASS_FORMAT = "pe-dynaform-%s-label";

    private static final String FACET_BUTTON_BAR_TOP_CLASS = "pe-dynaform-buttonbar-top";
    private static final String FACET_BUTTON_BAR_BOTTOM_CLASS = "pe-dynaform-buttonbar-bottom";
    private static final String FACET_HEADER_CLASS = "pe-dynaform-headerfacet";
    private static final String FACET_FOOTER_CLASS = "pe-dynaform-footerfacet";
    private static final String FACET_STATIC_TOP_CLASS = "pe-dynaform-static-top";
    private static final String FACET_STATIC_BOTTOM_CLASS = "pe-dynaform-static-bottom";
    private static final String EXTENDED_ROW_CLASS = "pe-dynaform-extendedrow";

    private static final String BUTTON_BAR_ROLE = "toolbar";
    private static final String GRID_CELL_ROLE = "gridcell";

    private static final String[] EMPTY_COLUMN_CLASSES = new String[] {Constants.EMPTY_STRING, Constants.EMPTY_STRING};

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
        writer.writeAttribute("class", styleClass, "styleClass");
        if (dynaForm.getStyle() != null) {
            writer.writeAttribute("style", dynaForm.getStyle(), "style");
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
        final String clientId = dynaForm.getClientId(fc);
        wb.init("ExtDynaForm", dynaForm.resolveWidgetVar(), clientId);
        wb.attr("uuid", dynaFormModel.getUuid());
        wb.attr("autoSubmit", dynaForm.isAutoSubmit());
        wb.attr("isPostback", fc.isPostback());
        wb.finish();
    }

    protected void encodeFacet(final FacesContext fc, final DynaForm dynaForm, final String name, final int totalColspan, final String styleClass,
                final String role,
                final boolean extended, final boolean visible) throws IOException {
        final UIComponent facet = dynaForm.getFacet(name);
        if (facet != null && facet.isRendered()) {
            final ResponseWriter writer = fc.getResponseWriter();
            writer.startElement("tr", null);
            if (extended) {
                writer.writeAttribute("class", EXTENDED_ROW_CLASS, null);
            }

            if (!visible) {
                writer.writeAttribute("style", "display:none;", null);
            }

            writer.writeAttribute("role", "row", null);
            writer.startElement("td", null);
            if (totalColspan > 1) {
                writer.writeAttribute("colspan", totalColspan, null);
            }

            writer.writeAttribute("class", styleClass, null);
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
                writer.writeAttribute("class", EXTENDED_ROW_CLASS, null);
            }

            if (!visible) {
                writer.writeAttribute("style", "display:none;", null);
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
                    // render label
                    final DynaFormLabel label = (DynaFormLabel) element;

                    writer.writeAttribute("class", (styleClass
                                + " " + LABEL_CLASS
                                + " " + StringUtils.defaultIfBlank(label.getStyleClass(), Constants.EMPTY_STRING)
                                + " " + labelCommonClass).trim(), null);
                    writer.writeAttribute("role", GRID_CELL_ROLE, null);

                    writer.startElement("label", null);
                    if (!label.isTargetValid()) {
                        writer.writeAttribute("class", LABEL_INVALID_CLASS, null);
                    }

                    writer.writeAttribute("for", label.getTargetClientId(), null);

                    if (label.getValue() != null) {
                        if (label.isEscape()) {
                            writer.writeText(label.getValue(), "value");
                        }
                        else {
                            writer.write(label.getValue());
                        }
                    }

                    if (label.isTargetRequired()) {
                        writer.startElement("span", null);
                        writer.writeAttribute("class", LABEL_INDICATOR_CLASS, null);
                        writer.write("*");
                        writer.endElement("span");
                    }

                    writer.endElement("label");
                }
                else if (element instanceof DynaFormControl) {
                    // render control
                    final DynaFormControl control = (DynaFormControl) element;
                    dynaForm.setData(control);

                    // find control's cell by type
                    final UIDynaFormControl cell = dynaForm.getControlCell(control.getType());

                    if (cell.getStyle() != null) {
                        writer.writeAttribute("style", cell.getStyle(), null);
                    }

                    if (cell.getStyleClass() != null) {
                        styleClass = styleClass + " " + cell.getStyleClass();
                    }

                    writer.writeAttribute("class", (styleClass + " " + controlCommonClass).trim(), null);
                    writer.writeAttribute("role", GRID_CELL_ROLE, null);

                    cell.encodeAll(fc);
                }
                else if (element instanceof DynaFormModelElement) {
                    final DynaFormModelElement nestedModel = (DynaFormModelElement) element;

                    // render nested model
                    writer.writeAttribute("class", styleClass, null);
                    writer.writeAttribute("role", GRID_CELL_ROLE, null);

                    encodeMarkup(fc, dynaForm, nestedModel.getModel(), true);
                }

                writer.endElement("td");
            }

            writer.endElement("tr");
        }

        dynaForm.resetData();
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
            writer.writeAttribute("class", styleClass, null);

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
                writer.writeAttribute("class", columnClass, null);
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

                    final ContextCallback callback = new ContextCallback() {

                        @Override
                        public void invokeContextCallback(final FacesContext context, final UIComponent target) {
                            label.setTargetValid(((EditableValueHolder) target).isValid());
                            label.setTargetRequired(((EditableValueHolder) target).isRequired());
                        }
                    };

                    if (CompositeUtils.isComposite(target)) {
                        CompositeUtils.invokeOnDeepestEditableValueHolder(fc, target, callback);
                    }
                    else {
                        callback.invokeContextCallback(fc, target);
                    }

                    if (label.getValue() != null) {
                        target.getAttributes().put("label", label.getValue());
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
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
