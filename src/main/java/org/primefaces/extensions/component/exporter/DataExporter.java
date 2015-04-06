/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import java.io.IOException;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
public class DataExporter implements ActionListener, StateHolder {

    private ValueExpression target;

    private ValueExpression type;

    private ValueExpression fileName;

    private ValueExpression tableTitle;

    private ValueExpression encoding;

    private ValueExpression pageOnly;

    private ValueExpression selectionOnly;

    private MethodExpression preProcessor;

    private MethodExpression postProcessor;

    private ValueExpression subTable;

    private ValueExpression facetBackground;

    private ValueExpression facetFontSize;

    private ValueExpression facetFontColor;

    private ValueExpression facetFontStyle;

    private ValueExpression fontName;

    private ValueExpression cellFontSize;

    private ValueExpression cellFontColor;

    private ValueExpression cellFontStyle;

    private ValueExpression datasetPadding;

    private ValueExpression orientation;

    private ValueExpression skipComponents;

    public DataExporter() {
    }

    public DataExporter(ValueExpression target, ValueExpression type, ValueExpression fileName, ValueExpression tableTitle, ValueExpression pageOnly, ValueExpression selectionOnly, ValueExpression encoding, MethodExpression preProcessor, MethodExpression postProcessor, ValueExpression subTable, ValueExpression facetBackground, ValueExpression facetFontSize, ValueExpression facetFontColor, ValueExpression facetFontStyle, ValueExpression fontName, ValueExpression cellFontSize, ValueExpression cellFontColor, ValueExpression cellFontStyle, ValueExpression datasetPadding, ValueExpression orientation, ValueExpression skipComponents) {
        this.target = target;
        this.type = type;
        this.fileName = fileName;
        this.tableTitle = tableTitle;
        this.pageOnly = pageOnly;
        this.selectionOnly = selectionOnly;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
        this.encoding = encoding;
        this.subTable = subTable;
        this.facetBackground = facetBackground;
        this.facetFontSize = facetFontSize;
        this.facetFontColor = facetFontColor;
        this.facetFontStyle = facetFontStyle;
        this.fontName = fontName;
        this.cellFontSize = cellFontSize;
        this.cellFontColor = cellFontColor;
        this.cellFontStyle = cellFontStyle;
        this.datasetPadding = datasetPadding;
        this.orientation = orientation;
        this.skipComponents = skipComponents;
    }

    public void processAction(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();

        String tableId = (String) target.getValue(elContext);
        String exportAs = (String) type.getValue(elContext);
        String outputFileName = (String) fileName.getValue(elContext);

        String tableTitleValue = "";
        if (tableTitle != null) {
            tableTitleValue = (String) tableTitle.getValue(elContext);
        }

        String encodingType = "UTF-8";
        if (encoding != null) {
            encodingType = (String) encoding.getValue(elContext);
        }

        boolean isPageOnly = false;
        if (pageOnly != null) {
            isPageOnly = pageOnly.isLiteralText() ? Boolean.valueOf(pageOnly.getValue(context.getELContext()).toString()) : (Boolean) pageOnly.getValue(context.getELContext());
        }

        boolean isSelectionOnly = false;
        if (selectionOnly != null) {
            isSelectionOnly = selectionOnly.isLiteralText() ? Boolean.valueOf(selectionOnly.getValue(context.getELContext()).toString()) : (Boolean) selectionOnly.getValue(context.getELContext());
        }

        boolean subtable = false;
        if (subTable != null) {
            subtable = subTable.isLiteralText() ? Boolean.valueOf(subTable.getValue(context.getELContext()).toString()) : (Boolean) subTable.getValue(context.getELContext());
        }
        String facetBackgroundValue = null;
        if (facetBackground != null)
            facetBackgroundValue = (String) facetBackground.getValue(elContext);
        String facetFontSizeValue = "10";
        if (facetFontSize != null)
            facetFontSizeValue = (String) facetFontSize.getValue(elContext);
        String facetFontColorValue = null;
        if (facetFontColor != null)
            facetFontColorValue = (String) facetFontColor.getValue(elContext);
        String facetFontStyleValue = "BOLD";
        if (facetFontStyle != null)
            facetFontStyleValue = (String) facetFontStyle.getValue(elContext);
        String fontNameValue = null;
        if (fontName != null)
            fontNameValue = (String) fontName.getValue(elContext);
        String cellFontSizeValue = "8";
        if (cellFontSize != null)
            cellFontSizeValue = (String) cellFontSize.getValue(elContext);
        String cellFontColorValue = null;
        if (cellFontColor != null)
            cellFontColorValue = (String) cellFontColor.getValue(elContext);
        String cellFontStyleValue = "NORMAL";
        if (cellFontStyle != null)
            cellFontStyleValue = (String) cellFontStyle.getValue(elContext);
        String datasetPaddingValue = "5";
        if (datasetPadding != null)
            datasetPaddingValue = (String) datasetPadding.getValue(elContext);
        String orientationValue = "Portrait";
        if (orientation != null)
            orientationValue = (String) orientation.getValue(elContext);
        String skipComponentsValue="";
        if (skipComponents != null)
            skipComponentsValue = (String) skipComponents.getValue(elContext);

        try {
            ExporterFactory factory = ExporterFactoryProvider.getExporterFactory(context);
            Exporter exporter = factory.getExporterForType(exportAs);
            exporter.setSkipComponents(skipComponentsValue);
            exporter.customFormat(facetBackgroundValue, facetFontSizeValue, facetFontColorValue, facetFontStyleValue, fontNameValue, cellFontSizeValue, cellFontColorValue, cellFontStyleValue, datasetPaddingValue,orientationValue);
            exporter.export(event, tableId, context, outputFileName, tableTitleValue, isPageOnly, isSelectionOnly, encodingType, preProcessor, postProcessor, subtable);
            context.responseComplete();
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean value) {
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;

        target = (ValueExpression) values[0];
        type = (ValueExpression) values[1];
        fileName = (ValueExpression) values[2];
        tableTitle = (ValueExpression) values[3];
        pageOnly = (ValueExpression) values[4];
        selectionOnly = (ValueExpression) values[5];
        preProcessor = (MethodExpression) values[6];
        postProcessor = (MethodExpression) values[7];
        encoding = (ValueExpression) values[8];
        subTable = (ValueExpression) values[9];
        facetBackground = (ValueExpression) values[10];
        facetFontSize = (ValueExpression) values[11];
        facetFontColor = (ValueExpression) values[12];
        facetFontStyle = (ValueExpression) values[13];
        fontName = (ValueExpression) values[14];
        cellFontSize = (ValueExpression) values[15];
        cellFontColor = (ValueExpression) values[16];
        cellFontStyle = (ValueExpression) values[17];
        datasetPadding = (ValueExpression) values[18];
        orientation = (ValueExpression) values[19];
        skipComponents = (ValueExpression) values[20];
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[21];

        values[0] = target;
        values[1] = type;
        values[2] = fileName;
        values[3] = tableTitle;
        values[4] = pageOnly;
        values[5] = selectionOnly;
        values[6] = preProcessor;
        values[7] = postProcessor;
        values[8] = encoding;
        values[9] = subTable;
        values[10] = facetBackground;
        values[11] = facetFontSize;
        values[12] = facetFontColor;
        values[13] = facetFontStyle;
        values[14] = fontName;
        values[15] = cellFontSize;
        values[16] = cellFontColor;
        values[17] = cellFontStyle;
        values[18] = datasetPadding;
        values[19] = orientation;
        values[20] = skipComponents;

        return ((Object[]) values);
    }
}
