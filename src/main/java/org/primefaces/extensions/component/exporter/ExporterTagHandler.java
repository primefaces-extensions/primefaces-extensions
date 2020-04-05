/**
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
    private final TagAttribute subTable;
    private final TagAttribute facetBackground;
    private final TagAttribute facetFontSize;
    private final TagAttribute facetFontColor;
    private final TagAttribute facetFontStyle;
    private final TagAttribute fontName;
    private final TagAttribute cellFontSize;
    private final TagAttribute cellFontColor;
    private final TagAttribute cellFontStyle;
    private final TagAttribute datasetPadding;
    private final TagAttribute orientation;
    private final TagAttribute skipComponents;

    public ExporterTagHandler(final TagConfig tagConfig) {
        super(tagConfig);
        target = getRequiredAttribute("target");
        type = getRequiredAttribute("type");
        fileName = getAttribute("fileName");
        tableTitle = getAttribute("tableTitle");
        pageOnly = getAttribute("pageOnly");
        selectionOnly = getAttribute("selectionOnly");
        encoding = getAttribute("encoding");
        preProcessor = getAttribute("preProcessor");
        postProcessor = getAttribute("postProcessor");
        subTable = getAttribute("subTable");
        facetBackground = getAttribute("facetBackground");
        facetFontSize = getAttribute("facetFontSize");
        facetFontColor = getAttribute("facetFontColor");
        facetFontStyle = getAttribute("facetFontStyle");
        fontName = getAttribute("fontName");
        cellFontSize = getAttribute("cellFontSize");
        cellFontColor = getAttribute("cellFontColor");
        cellFontStyle = getAttribute("cellFontStyle");
        datasetPadding = getAttribute("datasetPadding");
        orientation = getAttribute("orientation");
        skipComponents = getAttribute("skipComponents");
    }

    @Override
    public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        if (ComponentHandler.isNew(parent)) {
            final ValueExpression targetVE = target.getValueExpression(faceletContext, Object.class);
            final ValueExpression typeVE = type.getValueExpression(faceletContext, Object.class);
            ValueExpression fileNameVE = null;
            ValueExpression tableTitleVE = null;
            ValueExpression pageOnlyVE = null;
            ValueExpression selectionOnlyVE = null;
            ValueExpression encodingVE = null;
            MethodExpression preProcessorME = null;
            MethodExpression postProcessorME = null;
            ValueExpression subTableVE = null;
            ValueExpression facetBackgroundVE = null;
            ValueExpression facetFontSizeVE = null;
            ValueExpression facetFontColorVE = null;
            ValueExpression facetFontStyleVE = null;
            ValueExpression fontNameVE = null;
            ValueExpression cellFontSizeVE = null;
            ValueExpression cellFontColorVE = null;
            ValueExpression cellFontStyleVE = null;
            ValueExpression datasetPaddingVE = null;
            ValueExpression orientationVE = null;
            ValueExpression skipComponentsVE = null;

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
                preProcessorME = preProcessor.getMethodExpression(faceletContext, null, new Class[] {Object.class});
            }
            if (postProcessor != null) {
                postProcessorME = postProcessor.getMethodExpression(faceletContext, null, new Class[] {Object.class});
            }
            if (subTable != null) {
                subTableVE = subTable.getValueExpression(faceletContext, Object.class);
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
            if (fontName != null) {
                fontNameVE = fontName.getValueExpression(faceletContext, Object.class);
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
            if (datasetPadding != null) {
                datasetPaddingVE = datasetPadding.getValueExpression(faceletContext, Object.class);
            }
            if (orientation != null) {
                orientationVE = orientation.getValueExpression(faceletContext, Object.class);
            }
            if (skipComponents != null) {
                skipComponentsVE = skipComponents.getValueExpression(faceletContext, Object.class);
            }

            final ActionSource actionSource = (ActionSource) parent;
            actionSource.addActionListener(new DataExporter(targetVE, typeVE, fileNameVE, tableTitleVE, pageOnlyVE, selectionOnlyVE, encodingVE, preProcessorME,
                        postProcessorME, subTableVE, facetBackgroundVE, facetFontSizeVE, facetFontColorVE, facetFontStyleVE, fontNameVE, cellFontSizeVE,
                        cellFontColorVE, cellFontStyleVE, datasetPaddingVE, orientationVE, skipComponentsVE));

        }
    }

}
