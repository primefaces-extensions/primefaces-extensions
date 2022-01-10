/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.qrcode;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

import org.primefaces.component.api.Widget;

/**
 * <code>QRCode</code> component.
 *
 * @author Mauricio Fenoglio / last modified by Melloware
 * @since 1.2.0
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "qrcode/qrcode.js")
public class QRCode extends UIOutput implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.QRCode";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String STYLE_CLASS = "ui-qrcode";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.QRCodeRenderer";

    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        //@formatter:off
        widgetVar,
        renderMethod,
        renderMode,
        minVersion,
        maxVersion,
        leftOffset,
        topOffset,
        size,
        fillColor,
        ecLevel,
        background,
        text,
        radius,
        quiet,
        mSize,
        mPosX,
        mPosY,
        label,
        fontName,
        fontColor,
        style,
        styleClass
       //@formatter:on
    }

    public QRCode() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getRenderMethod() {
        return (String) getStateHelper().eval(PropertyKeys.renderMethod, null);
    }

    public void setRenderMethod(final String renderMethod) {
        getStateHelper().put(PropertyKeys.renderMethod, renderMethod);
    }

    public int getMinVersion() {
        return (Integer) getStateHelper().eval(PropertyKeys.minVersion, 1);
    }

    public void setMinVersion(final int minVersion) {
        getStateHelper().put(PropertyKeys.minVersion, minVersion);
    }

    public int getMaxVersion() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxVersion, 40);
    }

    public void setMaxVersion(final int maxVersion) {
        getStateHelper().put(PropertyKeys.maxVersion, maxVersion);
    }

    public int getLeftOffset() {
        return (Integer) getStateHelper().eval(PropertyKeys.leftOffset, 0);
    }

    public void setLeftOffset(final int leftOffset) {
        getStateHelper().put(PropertyKeys.leftOffset, leftOffset);
    }

    public int getTopOffset() {
        return (Integer) getStateHelper().eval(PropertyKeys.topOffset, 0);
    }

    public void setTopOffset(final int topOffset) {
        getStateHelper().put(PropertyKeys.topOffset, topOffset);
    }

    public int getSize() {
        return (Integer) getStateHelper().eval(PropertyKeys.size, 200);
    }

    public void setSize(final int size) {
        getStateHelper().put(PropertyKeys.size, size);
    }

    public String getFillColor() {
        return (String) getStateHelper().eval(PropertyKeys.fillColor, null);
    }

    public void setFillColor(final String fillColor) {
        getStateHelper().put(PropertyKeys.fillColor, fillColor);
    }

    public String getBackground() {
        return (String) getStateHelper().eval(PropertyKeys.background, null);
    }

    public void setBackground(final String background) {
        getStateHelper().put(PropertyKeys.background, background);
    }

    public String getText() {
        return (String) getStateHelper().eval(PropertyKeys.text, null);
    }

    public void setText(final String text) {
        getStateHelper().put(PropertyKeys.text, text);
    }

    public String getLabel() {
        return (String) getStateHelper().eval(PropertyKeys.label, null);
    }

    public void setLabel(final String text) {
        getStateHelper().put(PropertyKeys.label, text);
    }

    public Double getRadius() {
        return (Double) getStateHelper().eval(PropertyKeys.radius, 0d);
    }

    public void setRadius(final Double radius) {
        getStateHelper().put(PropertyKeys.radius, radius);
    }

    public Integer getQuiet() {
        return (Integer) getStateHelper().eval(PropertyKeys.quiet, 0);
    }

    public void setQuiet(final Integer quiet) {
        getStateHelper().put(PropertyKeys.quiet, quiet);
    }

    public String getEcLevel() {
        return (String) getStateHelper().eval(PropertyKeys.ecLevel, "L");
    }

    public void setEcLevel(final String ecLevel) {
        getStateHelper().put(PropertyKeys.ecLevel, ecLevel);
    }

    public Integer getRenderMode() {
        return (Integer) getStateHelper().eval(PropertyKeys.renderMode, 0);
    }

    public void setRenderMode(final Integer renderMode) {
        getStateHelper().put(PropertyKeys.renderMode, renderMode);
    }

    public String getFontColor() {
        return (String) getStateHelper().eval(PropertyKeys.fontColor, null);
    }

    public void setFontColor(final String fontColor) {
        getStateHelper().put(PropertyKeys.fontColor, fontColor);
    }

    public String getFontName() {
        return (String) getStateHelper().eval(PropertyKeys.fontName, null);
    }

    public void setFontName(final String fontColor) {
        getStateHelper().put(PropertyKeys.fontName, fontColor);
    }

    public Double getMPosX() {
        return (Double) getStateHelper().eval(PropertyKeys.mPosX, 0.5d);
    }

    public void setMPosX(final Double mPosX) {
        getStateHelper().put(PropertyKeys.mPosX, mPosX);
    }

    public Double getMPosY() {
        return (Double) getStateHelper().eval(PropertyKeys.mPosY, 0.5d);
    }

    public void setMPosY(final Double mPosY) {
        getStateHelper().put(PropertyKeys.mPosY, mPosY);
    }

    public Double getMSize() {
        return (Double) getStateHelper().eval(PropertyKeys.mSize, 0.1d);
    }

    public void setMSize(final Double mSize) {
        getStateHelper().put(PropertyKeys.mSize, mSize);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

}
