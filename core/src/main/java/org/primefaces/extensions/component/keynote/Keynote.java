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
package org.primefaces.extensions.component.keynote;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.primefaces.component.api.Widget;

@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "keynote/keynote.js")
@ResourceDependency(library = "primefaces-extensions", name = "keynote/keynote.css")
public class Keynote extends UIComponentBase implements ClientBehaviorHolder, Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Keynote";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.KeynoteRenderer";

    public static final String CONTAINER_CLASS = "ui-keynote reveal";
    public static final String SLIDES_CLASS = "slides";

    protected enum PropertyKeys {
        //@formatter:off
        widgetVar,
        width,
        height,
        margin,
        minScale,
        maxScale,
        autoSlide,
        center,
        controls,
        disableLayout,
        embedded,
        loop,
        navigationMode,
        progress,
        showNotes,
        touch,
        transition,
        transitionSpeed,
        backgroundTransition,
        theme,
        library,
        style,
        styleClass
        //@formatter:on
    }

    public Keynote() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public int getWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.width, 960);
    }

    public void setWidth(final int width) {
        getStateHelper().put(PropertyKeys.width, width);
    }

    public int getHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.height, 700);
    }

    public void setHeight(final int height) {
        getStateHelper().put(PropertyKeys.height, height);
    }

    public Double getMargin() {
        return (Double) getStateHelper().eval(PropertyKeys.margin, 0.04);
    }

    public void setMargin(final Double margin) {
        getStateHelper().put(PropertyKeys.margin, margin);
    }

    public Double getMinScale() {
        return (Double) getStateHelper().eval(PropertyKeys.minScale, 0.2);
    }

    public void setMinScale(final Double minScale) {
        getStateHelper().put(PropertyKeys.minScale, minScale);
    }

    public Double getMaxScale() {
        return (Double) getStateHelper().eval(PropertyKeys.maxScale, 2.0);
    }

    public void setMaxScale(final Double maxScale) {
        getStateHelper().put(PropertyKeys.maxScale, maxScale);
    }

    public int getAutoSlide() {
        return (Integer) getStateHelper().eval(PropertyKeys.autoSlide, 0);
    }

    public void setAutoSlide(final int autoSlide) {
        getStateHelper().put(PropertyKeys.autoSlide, autoSlide);
    }

    public Boolean isCenter() {
        return (Boolean) getStateHelper().eval(PropertyKeys.center, true);
    }

    public void setCenter(final Boolean center) {
        getStateHelper().put(PropertyKeys.center, center);
    }

    public Boolean isControls() {
        return (Boolean) getStateHelper().eval(PropertyKeys.controls, true);
    }

    public void setControls(final Boolean controls) {
        getStateHelper().put(PropertyKeys.controls, controls);
    }

    public Boolean isDisableLayout() {
        return (Boolean) getStateHelper().eval(PropertyKeys.disableLayout, false);
    }

    public void setDisableLayout(final Boolean disableLayout) {
        getStateHelper().put(PropertyKeys.disableLayout, disableLayout);
    }

    public Boolean isEmbedded() {
        return (Boolean) getStateHelper().eval(PropertyKeys.embedded, false);
    }

    public void setEmbedded(final Boolean embedded) {
        getStateHelper().put(PropertyKeys.embedded, embedded);
    }

    public Boolean isLoop() {
        return (Boolean) getStateHelper().eval(PropertyKeys.loop, false);
    }

    public void setLoop(final Boolean loop) {
        getStateHelper().put(PropertyKeys.loop, loop);
    }

    public String getNavigationMode() {
        return (String) getStateHelper().eval(PropertyKeys.navigationMode, "default");
    }

    public void setNavigationMode(final String navigationMode) {
        getStateHelper().put(PropertyKeys.navigationMode, navigationMode);
    }

    public Boolean isProgress() {
        return (Boolean) getStateHelper().eval(PropertyKeys.progress, true);
    }

    public void setProgress(final Boolean progress) {
        getStateHelper().put(PropertyKeys.progress, progress);
    }

    public Boolean isShowNotes() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showNotes, false);
    }

    public void setShowNotes(final Boolean showNotes) {
        getStateHelper().put(PropertyKeys.showNotes, showNotes);
    }

    public Boolean isTouch() {
        return (Boolean) getStateHelper().eval(PropertyKeys.touch, true);
    }

    public void setTouch(final Boolean touch) {
        getStateHelper().put(PropertyKeys.touch, touch);
    }

    public String getTransition() {
        return (String) getStateHelper().eval(PropertyKeys.transition, "slide");
    }

    public void setTransition(final String transition) {
        getStateHelper().put(PropertyKeys.transition, transition);
    }

    public String getTransitionSpeed() {
        return (String) getStateHelper().eval(PropertyKeys.transitionSpeed, "default");
    }

    public void setTransitionSpeed(final String transitionSpeed) {
        getStateHelper().put(PropertyKeys.transitionSpeed, transitionSpeed);
    }

    public String getBackgroundTransition() {
        return (String) getStateHelper().eval(PropertyKeys.backgroundTransition, "fade");
    }

    public void setBackgroundTransition(final String backgroundTransition) {
        getStateHelper().put(PropertyKeys.backgroundTransition, backgroundTransition);
    }

    public String getTheme() {
        return (String) getStateHelper().eval(PropertyKeys.theme, null);
    }

    public void setTheme(final String theme) {
        getStateHelper().put(PropertyKeys.theme, theme);
    }

    public String getLibrary() {
        return (String) getStateHelper().eval(PropertyKeys.library, null);
    }

    public void setLibrary(final String _library) {
        getStateHelper().put(PropertyKeys.library, _library);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

}
