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

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.*;

/**
 * <code>Exporter</code> component.
 *
 * @author Sudheer Jonna / last modified by $Author$
 * @since 0.7.0
 * @deprecated use core Primefaces DataExporter
 */
@Deprecated
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
    public void apply(final FaceletContext faceletContext, final UIComponent parent) {
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
