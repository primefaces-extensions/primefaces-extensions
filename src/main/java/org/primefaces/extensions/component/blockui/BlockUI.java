/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.blockui;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;

import org.primefaces.component.api.Widget;
import org.primefaces.util.ComponentUtils;

/**
 * <code>BlockUI</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "components.css"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "blockui/blockui.js")
})
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
    protected enum PropertyKeys {

        widgetVar, css, cssOverlay, source, target, content, event, autoShow, timeout, centerX, centerY, fadeIn, fadeOut, showOverlay, focusInput;

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

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }

}
