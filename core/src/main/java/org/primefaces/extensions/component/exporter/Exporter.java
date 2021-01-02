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
package org.primefaces.extensions.component.exporter;

import java.io.IOException;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;

import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.column.Column;
import org.primefaces.component.datalist.DataList;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.roweditor.RowEditor;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 * @deprecated use core Primefaces DataExporter
 */
@Deprecated
public abstract class Exporter {

    protected String skipComponents;

    protected enum ColumnType {
        HEADER("header"), FOOTER("footer");

        private final String facet;

        ColumnType(final String facet) {
            this.facet = facet;
        }

        public String facet() {
            return facet;
        }

        @Override
        public String toString() {
            return facet;
        }
    }

    public abstract void export(ActionEvent event, String tableId, FacesContext facesContext,
                String outputFileName, String tableTitleValue, boolean pageOnly, boolean selectionOnly,
                String encodingType, MethodExpression preProcessor,
                MethodExpression postProcessor, boolean subTable) throws IOException;

    public abstract void customFormat(String facetBackground, String facetFontSize, String facetFontColor, String facetFontStyle, String fontName,
                String cellFontSize, String cellFontColor, String cellFontStyle, String datasetPadding, String orientation) throws IOException;

    protected static String exportColumnByFunction(final FacesContext context, final UIColumn column) {
        final MethodExpression exportFunction = column.getExportFunction();

        if (exportFunction != null) {
            return (String) exportFunction.invoke(context.getELContext(), new Object[] {column});
        }

        return Constants.EMPTY_STRING;
    }

    protected String exportValue(final FacesContext context, final UIComponent component) {

        if (component instanceof CellEditor) {
            return exportValue(context, component.getFacet("output"));
        }
        if (component instanceof RowEditor) {
            return "RowEditor";
        }

        if (component instanceof HtmlGraphicImage) {
            return (String) component.getAttributes().get("alt");
        }

        if (component instanceof HtmlCommandLink) {
            final HtmlCommandLink link = (HtmlCommandLink) component;
            final Object value = link.getValue();

            if (value != null) {
                return String.valueOf(value);
            }
            else {
                // export first value holder
                for (final UIComponent child : link.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return Constants.EMPTY_STRING;
            }
        }
        if (component instanceof HtmlOutputLink) {
            final HtmlOutputLink link = (HtmlOutputLink) component;

            final List<UIComponent> children = link.getChildren();
            if (children != null) {
                return exportValue(context, children.get(0));
            }
        }
        if (component instanceof HtmlCommandButton) {
            final HtmlCommandButton button = (HtmlCommandButton) component;
            final Object value = button.getValue();

            if (value != null) {
                return String.valueOf(value);
            }
            else {
                // export first value holder
                for (final UIComponent child : button.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return Constants.EMPTY_STRING;
            }
        }
        if (component instanceof HtmlSelectOneMenu) {
            final HtmlSelectOneMenu oneMenu = (HtmlSelectOneMenu) component;
            final Object value = oneMenu.getSubmittedValue();

            if (value != null) {
                return String.valueOf(value);
            }
            else {
                // export first value holder
                for (final UIComponent child : oneMenu.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return Constants.EMPTY_STRING;
            }
        }
        if (skipComponents.contains(component.getClass().getName())) {
            return Constants.EMPTY_STRING;
        }
        else if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                final Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            final ValueHolder valueHolder = (ValueHolder) component;
            final Object value = valueHolder.getValue();
            if (value == null) {
                return Constants.EMPTY_STRING;
            }

            // first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            }
            // Try to guess
            else {
                final Converter converterForType = ComponentUtils.getConverter(context, component);
                if (converterForType != null) {
                    return converterForType.getAsString(context, component, value);
                }
            }

            // No converter found just return the value as string
            return value.toString();
        }
        else {
            // This would get the plain texts on UIInstructions when using Facelets
            final String value = component.toString();
            return value.trim();
        }
    }

    protected static String exportFacetValue(final FacesContext context, final UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                final Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            final ValueHolder valueHolder = (ValueHolder) component;
            final Object value = valueHolder.getValue();
            if (value == null) {
                return Constants.EMPTY_STRING;
            }

            // first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            }
            return value.toString();
        }
        else {
            // This would get the plain texts on UIInstructions when using Facelets
            final String value = component.toString();

            return value.trim();
        }

    }

    protected String addColumnValues(final DataList dataList, final StringBuilder input) {
        for (final UIComponent component : dataList.getChildren()) {
            if (component instanceof Column) {
                final UIColumn column = (UIColumn) component;
                for (final UIComponent childComponent : column.getChildren()) {
                    if (component.isRendered()) {
                        final String value = exportValue(FacesContext.getCurrentInstance(), childComponent);

                        if (value != null) {
                            input.append(value + "\n \n");
                        }
                    }
                }
                return input.toString();
            }
            else {
                if (component.isRendered()) {
                    final String value = exportValue(FacesContext.getCurrentInstance(), component);

                    if (value != null) {
                        input.append(value).append("\n \n");
                    }
                }
                return input.toString();
            }
        }
        return null;
    }

    protected static int getColumnsCount(final DataTable table) {
        int count = 0;

        for (final UIColumn col : table.getColumns()) {
            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (!col.isRendered() || !col.isExportable()) {
                continue;
            }

            count++;
        }

        return count;
    }

    protected static int getColumnsCount(final SubTable table) {
        int count = 0;

        for (final UIColumn col : table.getColumns()) {
            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }

            if (!col.isRendered() || !col.isExportable()) {
                continue;
            }

            count++;
        }

        return count;
    }

    public static boolean hasHeaderColumn(final DataTable table) {
        for (final UIColumn col : table.getColumns()) {
            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyStatelessModel();
            }
            if (col.isRendered()
                        && (col.getFacet(ColumnType.HEADER.facet()) != null || col.getHeaderText() != null)) {
                return true;
            }

        }

        return false;
    }

    public static boolean hasHeaderColumn(final SubTable table) {
        for (final UIComponent child : table.getChildren()) {
            if (child.isRendered() && child instanceof UIColumn) {
                final UIColumn column = (UIColumn) child;

                if (column.getFacet(ColumnType.HEADER.facet()) != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    public static boolean hasFooterColumn(final SubTable table) {
        for (final UIComponent child : table.getChildren()) {
            if (child.isRendered() && child instanceof UIColumn) {
                final UIColumn column = (UIColumn) child;

                if (column.getFacet(ColumnType.FOOTER.facet()) != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    public void setSkipComponents(final String skipComponentsValue) {
        skipComponents = skipComponentsValue;
    }

}
