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

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 */
public class ExporterTagHandler extends TagHandler {

    private final TagAttribute target;
    private final TagAttribute type;
    private final TagAttribute fileName;
    private final TagAttribute tableTitle;
    private final TagAttribute pageOnly;
    private final TagAttribute selectionOnly;
    private final TagAttribute preProcessor;
    private final TagAttribute postProcessor;
    private final TagAttribute encoding;
    private final TagAttribute isSubTable;
    private final TagAttribute facetBackground;
    private final TagAttribute facetFontSize;
    private final TagAttribute facetFontColor;
    private final TagAttribute facetFontStyle;
    private final TagAttribute cellFontSize;
    private final TagAttribute cellFontColor;
    private final TagAttribute cellFontStyle;

    public ExporterTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        this.target = getRequiredAttribute("target");
        this.type = getRequiredAttribute("type");
        this.fileName = getAttribute("fileName");
        this.tableTitle = getAttribute("tableTitle");
        this.pageOnly = getAttribute("pageOnly");
        this.selectionOnly = getAttribute("selectionOnly");
        this.encoding = getAttribute("encoding");
        this.preProcessor = getAttribute("preProcessor");
        this.postProcessor = getAttribute("postProcessor");
        this.isSubTable = getAttribute("isSubTable");
        this.facetBackground = getAttribute("facetBackground");
        this.facetFontSize = getAttribute("facetFontSize");
        this.facetFontColor = getAttribute("facetFontColor");
        this.facetFontStyle = getAttribute("facetFontStyle");
        this.cellFontSize = getAttribute("cellFontSize");
        this.cellFontColor = getAttribute("cellFontColor");
        this.cellFontStyle = getAttribute("cellFontStyle");


    }

    public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        if (ComponentHandler.isNew(parent)) {
            ValueExpression targetVE = target.getValueExpression(faceletContext, Object.class);
            ValueExpression typeVE = type.getValueExpression(faceletContext, Object.class);
            ValueExpression fileNameVE = null;
            ValueExpression tableTitleVE = null;
            ValueExpression pageOnlyVE = null;
            ValueExpression selectionOnlyVE = null;
            ValueExpression excludeColumnsVE = null;
            ValueExpression encodingVE = null;
            MethodExpression preProcessorME = null;
            MethodExpression postProcessorME = null;
            ValueExpression isSubTableVE = null;
            ValueExpression facetBackgroundVE = null;
            ValueExpression facetFontSizeVE = null;
            ValueExpression facetFontColorVE = null;
            ValueExpression facetFontStyleVE = null;
            ValueExpression cellFontSizeVE = null;
            ValueExpression cellFontColorVE = null;
            ValueExpression cellFontStyleVE = null;

            if (fileName != null) {
                fileNameVE = fileName.getValueExpression(faceletContext, Object.class);
            }
            if (tableTitle != null) {
                tableTitleVE = tableTitle.getValueExpression(faceletContext, Object.class);
            }
            if (encoding != null) {
                encodingVE = encoding.getValueExpression(faceletContext, Object.class);
            }
            if (pageOnly != null) {
                pageOnlyVE = pageOnly.getValueExpression(faceletContext, Object.class);
            }
            if (selectionOnly != null) {
                selectionOnlyVE = selectionOnly.getValueExpression(faceletContext, Object.class);
            }
            if (preProcessor != null) {
                preProcessorME = preProcessor.getMethodExpression(faceletContext, null, new Class[]{Object.class});
            }
            if (postProcessor != null) {
                postProcessorME = postProcessor.getMethodExpression(faceletContext, null, new Class[]{Object.class});
            }
            if (isSubTable != null) {
                isSubTableVE = isSubTable.getValueExpression(faceletContext, Object.class);
            }
            if (facetBackground != null) {
                facetBackgroundVE = facetBackground.getValueExpression(faceletContext, Object.class);
            }
            if (facetFontSize != null) {
                facetFontSizeVE = facetFontSize.getValueExpression(faceletContext, Object.class);
            }
            if (facetFontColor != null) {
                facetFontColorVE = facetFontColor.getValueExpression(faceletContext, Object.class);
            }
            if (facetFontStyle != null) {
                facetFontStyleVE = facetFontStyle.getValueExpression(faceletContext, Object.class);
            }
            if (cellFontSize != null) {
                cellFontSizeVE = cellFontSize.getValueExpression(faceletContext, Object.class);
            }
            if (cellFontColor != null) {
                cellFontColorVE = cellFontColor.getValueExpression(faceletContext, Object.class);
            }
            if (cellFontStyle != null) {
                cellFontStyleVE = cellFontStyle.getValueExpression(faceletContext, Object.class);
            }


            ActionSource actionSource = (ActionSource) parent;
            actionSource.addActionListener(new DataExporter(targetVE, typeVE, fileNameVE, tableTitleVE, pageOnlyVE, selectionOnlyVE, encodingVE, preProcessorME, postProcessorME, isSubTableVE, facetBackgroundVE, facetFontSizeVE, facetFontColorVE, facetFontStyleVE, cellFontSizeVE, cellFontColorVE, cellFontStyleVE));

        }
    }

}

