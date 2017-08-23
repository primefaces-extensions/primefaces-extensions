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
package org.primefaces.extensions.component.exporter;

import javax.el.MethodExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
public abstract class Exporter {

    protected String skipComponents;
    protected enum ColumnType {
        HEADER("header"),
        FOOTER("footer");

        private final String facet;

        ColumnType(String facet) {
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

    ;

    public abstract void export(ActionEvent event, String tableId, FacesContext facesContext,
                                String outputFileName, String tableTitleValue, boolean pageOnly, boolean selectionOnly,
                                String encodingType, MethodExpression preProcessor,
                                MethodExpression postProcessor, boolean subTable) throws IOException;

    public abstract void customFormat(String facetBackground, String facetFontSize, String facetFontColor, String facetFontStyle, String fontName, String cellFontSize, String cellFontColor, String cellFontStyle, String datasetPadding, String orientation) throws IOException;

    protected String exportValue(FacesContext context, UIComponent component) {

        if (component instanceof CellEditor) {
            return exportValue(context, component.getFacet("output"));
        }
        if (component instanceof RowEditor) {
            return (String) "RowEditor";
            // return (String) component.getAttributes().get("alt");
        }

        if (component instanceof HtmlGraphicImage) {
            return (String) component.getAttributes().get("alt");
        }

        if (component instanceof HtmlCommandLink) {
            HtmlCommandLink link = (HtmlCommandLink) component;
            Object value = link.getValue();

            if (value != null) {
                return String.valueOf(value);
            } else {
                //export first value holder
                for (UIComponent child : link.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        }
        if (component instanceof HtmlOutputLink) {
            HtmlOutputLink link = (HtmlOutputLink) component;

            List<UIComponent> children = link.getChildren();
            if (children != null) {
               return exportValue(context, children.get(0)); 
            }
        }
        if (component instanceof HtmlCommandButton) {
            HtmlCommandButton button = (HtmlCommandButton) component;
            Object value = button.getValue();

            if (value != null) {
                return String.valueOf(value);
            } else {
                //export first value holder
                for (UIComponent child : button.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        }
        if (component instanceof HtmlSelectOneMenu) {
            HtmlSelectOneMenu oneMenu = (HtmlSelectOneMenu) component;
            Object value = oneMenu.getSubmittedValue();

            if (value != null) {
                return String.valueOf(value);
            } else {
                //export first value holder
                for (UIComponent child : oneMenu.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        }
        if (skipComponents.contains(component.getClass().getName())) {
            return "";
          }

          else if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object value = valueHolder.getValue();
            if (value == null)
                return "";

            //first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            }
            //Try to guess
            else {
                        Converter converterForType = ComponentUtils.getConverter(context,component);
                        if (converterForType != null)
                            return converterForType.getAsString(context, component, value);
            }

            //No converter found just return the value as string
            return value.toString();
        } else {
            //This would get the plain texts on UIInstructions when using Facelets
            String value = component.toString();
            return value.trim();
        }
    }

    protected String exportFacetValue(FacesContext context, UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            ValueHolder valueHolder = (ValueHolder) component;
            Object value = valueHolder.getValue();
            if (value == null) {
                return "";
            }

            //first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            }
            return value.toString();
        } else {
            //This would get the plain texts on UIInstructions when using Facelets
            String value = component.toString();

            return value.trim();
        }

    }

    protected List<UIColumn> getColumnsToExport(UIData table) {
        List<UIColumn> columns = new ArrayList<UIColumn>();

        for (UIComponent child : table.getChildren()) {
            if (child instanceof UIColumn) {
                UIColumn column = (UIColumn) child;

                columns.add(column);
            }
        }

        return columns;
    }

    protected String addColumnValues(DataList dataList, StringBuilder input) {
        for (UIComponent component : dataList.getChildren()) {
            if (component instanceof Column) {
                UIColumn column = (UIColumn) component;
                for (UIComponent childComponent : column.getChildren()) {
                    if (component.isRendered()) {
                        String value = exportValue(FacesContext.getCurrentInstance(), childComponent);

                        if (value != null) {
                            input.append(value + "\n \n");
                        }
                    }
                }
                return input.toString();
            } else {
                if (component.isRendered()) {
                    String value = exportValue(FacesContext.getCurrentInstance(), component);

                    if (value != null) {
                        input.append(value + "\n \n");
                    }
                }
                return input.toString();
            }
        }
        return null;
    }

    protected int getColumnsCount(DataTable table) {
        int count = 0;

         for(UIColumn col : table.getColumns()) {
                    if(col instanceof DynamicColumn) {
                        ((DynamicColumn) col).applyStatelessModel();
                    }

                    if(!col.isRendered()||!col.isExportable()) {
                        continue;
                    }

                    count++;
                }

        return count;
    }

    protected int getColumnsCount(SubTable table) {
        int count = 0;

         for(UIColumn col : table.getColumns()) {
                    if(col instanceof DynamicColumn) {
                        ((DynamicColumn) col).applyStatelessModel();
                    }

                    if(!col.isRendered()||!col.isExportable()) {
                        continue;
                    }

                    count++;
                }

        return count;
    }

    public boolean hasHeaderColumn(DataTable table) {
        for(UIColumn col : table.getColumns()) {
           if(col instanceof DynamicColumn) {
                 ((DynamicColumn) col).applyStatelessModel();
           }
           if (col.isRendered() 
                    && (col instanceof UIColumn || col instanceof DynamicColumn) 
                    && (col.getFacet("header") != null || col.getHeaderText() != null)) { 
                    return true;
            }

        }

        return false;
    }

    public boolean hasHeaderColumn(SubTable table) {
        for (UIComponent child : table.getChildren()) {
            if (child.isRendered() && child instanceof UIColumn) {
                UIColumn column = (UIColumn) child;

                if (column.getFacet("header") != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    public boolean hasFooterColumn(SubTable table) {
        for (UIComponent child : table.getChildren()) {
            if (child.isRendered() && child instanceof UIColumn) {
                UIColumn column = (UIColumn) child;

                if (column.getFacet("footer") != null || column.getHeaderText() != null) {
                    return true;
                }
            }

        }

        return false;
    }

    public void setSkipComponents(String skipComponentsValue) {
                skipComponents= skipComponentsValue;
          }

}
