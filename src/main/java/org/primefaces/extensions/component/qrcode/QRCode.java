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
package org.primefaces.extensions.component.qrcode;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * <code>QRCode</code> component.
 *
 * @author Mauricio Fenoglio / last modified by Melloware
 * @since 1.2.0
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "qrcode/qrcode.js")
})
public class QRCode extends UIOutput implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.QRCode";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.QRCodeRenderer";

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
        fontColor;
       //@formatter:on

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    public QRCode() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
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

}
