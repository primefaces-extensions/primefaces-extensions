/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;

import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.roweditor.RowEditor;
import javax.faces.component.html.HtmlSelectOneMenu;

//import com.sun.faces.facelets.compiler.UIInstructions;
//end
/**
 * <code>Exporter</code> component.
 *
 * @author  Sudheer Jonna / last modified by $Author$
 * @since   0.7.0
 */
public abstract class Exporter {
	    
	protected enum ColumnType{
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
	};
    public abstract void export(ActionEvent event,String tableId,FacesContext facesContext,
			String outputFileName,String tableTitleValue, boolean pageOnly, boolean selectionOnly,
			String encodingType, MethodExpression preProcessor,
			MethodExpression postProcessor,boolean isSubTable) throws IOException;

	public abstract void customFormat(String facetBackground,String facetFontSize,String facetFontColor,String facetFontStyle,String cellFontSize,String cellFontColor,String cellFontStyle) throws IOException;
	protected List<UIColumn> getColumnsToExport(UIData table) {
        List<UIColumn> columns = new ArrayList<UIColumn>();
        int columnIndex = -1;

        for(UIComponent child : table.getChildren()) {
            if(child instanceof UIColumn) {
                UIColumn column = (UIColumn) child;
                columnIndex++;

                columns.add(column);
            }
        }

        return columns;
    }

    protected boolean hasColumnFooter(List<UIColumn> columns) {
        for(UIColumn column : columns) {
            if(column.getFooter() != null)
                return true;
        }

        return false;
    }

    protected String exportValue(FacesContext context, UIComponent component) {
    	if(component instanceof CellEditor) {
    		   return exportValue(context, component.getFacet("output"));
    		       }
   	    if (component instanceof RowEditor) {
   		   return (String) "RowEditor";
           // return (String) component.getAttributes().get("alt");
            
   	      }

    	 if (component instanceof HtmlGraphicImage) {
             return (String) component.getAttributes().get("alt");
             
    	 }

        if(component instanceof HtmlCommandLink) {
            HtmlCommandLink link = (HtmlCommandLink) component;
            Object value = link.getValue();

            if(value != null) {
                return String.valueOf(value);
            } 
            else {
                //export first value holder
                for(UIComponent child : link.getChildren()) {
                    if(child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        }
        if(component instanceof HtmlCommandButton) {
        	HtmlCommandButton button = (HtmlCommandButton) component;
            Object value = button.getValue();

            if(value != null) {
                return String.valueOf(value);
            } 
            else {
                //export first value holder
                for(UIComponent child : button.getChildren()) {
                    if(child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        }
        if(component instanceof HtmlSelectOneMenu) {
            HtmlSelectOneMenu oneMenu = (HtmlSelectOneMenu) component;
            Object value = oneMenu.getSubmittedValue();

            if(value != null) {
                return String.valueOf(value);
            }
            else {
                //export first value holder
                for(UIComponent child : oneMenu.getChildren()) {
                    if(child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        }
        else if(component instanceof ValueHolder) {
 
			if(component instanceof EditableValueHolder) {
				Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
				if (submittedValue != null) {
					return submittedValue.toString();
				}
			}

			ValueHolder valueHolder = (ValueHolder) component;
			Object value = valueHolder.getValue();
			if(value == null)
				return "";

			//first ask the converter
			if(valueHolder.getConverter() != null) {
				return valueHolder.getConverter().getAsString(context, component, value);
			}
			//Try to guess
			else {
				ValueExpression expr = component.getValueExpression("value");
				if(expr != null) {
					Class<?> valueType = expr.getType(context.getELContext());
					if(valueType != null) {
						Converter converterForType = context.getApplication().createConverter(valueType);

						if(converterForType != null)
							return converterForType.getAsString(context, component, value);
					}
				}
			}

			//No converter found just return the value as string
			return value.toString();
		} 

        else {
			//This would get the plain texts on UIInstructions when using Facelets
			String value = component.toString();
			if(value != null)
				return value.trim();
			else
				return "";
		}
    }
}
