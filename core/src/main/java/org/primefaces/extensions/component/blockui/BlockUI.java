/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.component.blockui;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;

/**
 * <code>BlockUI</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.css")
@ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.js")
public class BlockUI extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.BlockUI";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.BlockUIRenderer";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    // @formatter:off
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        widgetVar,
        css,
        cssOverlay,
        source,
        target,
        content,
        event,
        autoShow,
        timeout,
        centerX,
        centerY,
        fadeIn,
        fadeOut,
        showOverlay,
        focusInput

    }
    // @formatter:on

    public BlockUI() {
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

    public void setCss(final String css) {
        getStateHelper().put(PropertyKeys.css, css);
    }

    public String getCss() {
        return (String) getStateHelper().eval(PropertyKeys.css, null);
    }

    public void setCssOverlay(final String cssOverlay) {
        getStateHelper().put(PropertyKeys.cssOverlay, cssOverlay);
    }

    public String getCssOverlay() {
        return (String) getStateHelper().eval(PropertyKeys.cssOverlay, null);
    }

    public String getSource() {
        return (String) getStateHelper().eval(PropertyKeys.source, null);
    }

    public void setSource(final String source) {
        getStateHelper().put(PropertyKeys.source, source);
    }

    public String getTarget() {
        return (String) getStateHelper().eval(PropertyKeys.target, null);
    }

    public void setTarget(final String target) {
        getStateHelper().put(PropertyKeys.target, target);
    }

    public String getContent() {
        return (String) getStateHelper().eval(PropertyKeys.content, null);
    }

    public void setContent(final String content) {
        getStateHelper().put(PropertyKeys.content, content);
    }

    public String getEvent() {
        return (String) getStateHelper().eval(PropertyKeys.event, null);
    }

    public void setEvent(final String event) {
        getStateHelper().put(PropertyKeys.event, event);
    }

    public boolean isAutoShow() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoShow, false);
    }

    public void setAutoShow(final boolean autoShow) {
        getStateHelper().put(PropertyKeys.autoShow, autoShow);
    }

    public int getTimeout() {
        return (Integer) getStateHelper().eval(PropertyKeys.timeout, 0);
    }

    public void setTimeout(final int timeout) {
        getStateHelper().put(PropertyKeys.timeout, timeout);
    }

    public boolean isCenterX() {
        return (Boolean) getStateHelper().eval(PropertyKeys.centerX, true);
    }

    public void setCenterX(final boolean centerX) {
        getStateHelper().put(PropertyKeys.centerX, centerX);
    }

    public boolean isCenterY() {
        return (Boolean) getStateHelper().eval(PropertyKeys.centerY, true);
    }

    public void setCenterY(final boolean centerY) {
        getStateHelper().put(PropertyKeys.centerY, centerY);
    }

    public int getFadeIn() {
        return (Integer) getStateHelper().eval(PropertyKeys.fadeIn, 200);
    }

    public void setFadeIn(final int fadeIn) {
        getStateHelper().put(PropertyKeys.fadeIn, fadeIn);
    }

    public int getFadeOut() {
        return (Integer) getStateHelper().eval(PropertyKeys.fadeOut, 400);
    }

    public void setFadeOut(final int fadeOut) {
        getStateHelper().put(PropertyKeys.fadeOut, fadeOut);
    }

    public void setShowOverlay(final boolean showOverlay) {
        getStateHelper().put(PropertyKeys.showOverlay, showOverlay);
    }

    public boolean isShowOverlay() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showOverlay, true);
    }

    public void setFocusInput(final boolean focusInput) {
        getStateHelper().put(PropertyKeys.focusInput, focusInput);
    }

    public boolean isFocusInput() {
        return (Boolean) getStateHelper().eval(PropertyKeys.focusInput, true);
    }

}
