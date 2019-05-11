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
package org.primefaces.extensions.component.layout;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>Layout</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.css"),
            @ResourceDependency(library = "primefaces-extensions", name = "layout/layout.js")
})
public class Layout extends UIComponentBase implements Widget, ClientBehaviorHolder {

    public static final String POSITION_SEPARATOR = "_";
    public static final String STYLE_CLASS_PANE = "ui-layout-unit ui-widget-content ui-corner-all";
    public static final String STYLE_CLASS_PANE_WITH_SUBPANES = "ui-corner-all pe-layout-pane-withsubpanes";
    public static final String STYLE_CLASS_PANE_HEADER = "ui-layout-unit-header ui-widget-header ui-corner-top pe-layout-pane-header";
    public static final String STYLE_CLASS_PANE_CONTENT = "ui-layout-unit-content pe-layout-pane-content";
    public static final String STYLE_CLASS_LAYOUT_CONTENT = "ui-layout-content";

    public static final String PANE_POSITION_CENTER = "center";
    public static final String PANE_POSITION_NORTH = "north";
    public static final String PANE_POSITION_SOUTH = "south";
    public static final String PANE_POSITION_WEST = "west";
    public static final String PANE_POSITION_EAST = "east";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Layout";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.LayoutRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList(OpenEvent.NAME, CloseEvent.NAME, ResizeEvent.NAME));
    private static final Logger LOG = Logger.getLogger(Layout.class.getName());

    private boolean buildOptions;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by Melloware
     */
    public enum PropertyKeys {

      // @formatter:off
      widgetVar,
      fullPage,
      options,
      style,
      styleClass,
      state,
      stateCookie,
      togglerTip_open("Open"),
      togglerTip_closed("Close"),
      resizerTip("Resize"),
      sliderTip("Slide"),
      maskPanesEarly;
      // @formatter:on

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

    public Layout() {
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

    public boolean isFullPage() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fullPage, true);
    }

    public void setFullPage(final boolean fullPage) {
        getStateHelper().put(PropertyKeys.fullPage, fullPage);
    }

    public Object getOptions() {
        return getStateHelper().eval(PropertyKeys.options, null);
    }

    public void setOptions(final Object options) {
        getStateHelper().put(PropertyKeys.options, options);
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

    public String getState() {
        return (String) getStateHelper().eval(PropertyKeys.state, null);
    }

    public void setState(final String state) {
        getStateHelper().put(PropertyKeys.state, state);
    }

    public boolean isStateCookie() {
        return (Boolean) getStateHelper().eval(PropertyKeys.stateCookie, false);
    }

    public void setStateCookie(final boolean stateCookie) {
        getStateHelper().put(PropertyKeys.stateCookie, stateCookie);
    }

    public String getTogglerTipOpen() {
        return (String) getStateHelper().eval(PropertyKeys.togglerTip_open, null);
    }

    public void setTogglerTipOpen(final String togglerTipOpen) {
        getStateHelper().put(PropertyKeys.togglerTip_open, togglerTipOpen);
    }

    public String getTogglerTipClosed() {
        return (String) getStateHelper().eval(PropertyKeys.togglerTip_closed, null);
    }

    public void setTogglerTipClosed(final String togglerTipClosed) {
        getStateHelper().put(PropertyKeys.togglerTip_closed, togglerTipClosed);
    }

    public String getResizerTip() {
        return (String) getStateHelper().eval(PropertyKeys.resizerTip, null);
    }

    public void setResizerTip(final String resizerTip) {
        getStateHelper().put(PropertyKeys.resizerTip, resizerTip);
    }

    public String getSliderTip() {
        return (String) getStateHelper().eval(PropertyKeys.sliderTip, null);
    }

    public void setSliderTip(final String sliderTip) {
        getStateHelper().put(PropertyKeys.sliderTip, sliderTip);
    }

    public boolean isMaskPanesEarly() {
        return (Boolean) getStateHelper().eval(PropertyKeys.maskPanesEarly, false);
    }

    public void setMaskPanesEarly(final boolean maskPanesEarly) {
        getStateHelper().put(PropertyKeys.maskPanesEarly, maskPanesEarly);
    }

    public boolean isBuildOptions() {
        return buildOptions;
    }

    public void setBuildOptions(final boolean buildOptions) {
        this.buildOptions = buildOptions;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }

        final String state = fc.getExternalContext().getRequestParameterMap().get(this.getClientId(fc) + "_state");
        if (!LangUtils.isValueBlank(state)) {
            final ValueExpression stateVE = getValueExpression(PropertyKeys.state.toString());
            if (stateVE != null) {
                // save "state"
                stateVE.setValue(fc.getELContext(), state);
                getStateHelper().remove(PropertyKeys.state);
            }
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (isSelfRequest(context)) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(context);

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final LayoutPane pane = getLayoutPane(this, params.get(clientId + "_pane"));
            if (pane == null) {
                LOG.warning("LayoutPane by request parameter '" + params.get(clientId + "_pane") + "' was not found");

                return;
            }

            if (OpenEvent.NAME.equals(eventName)) {
                final OpenEvent openEvent = new OpenEvent(pane, behaviorEvent.getBehavior());
                openEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(openEvent);

                return;
            }
            else if (CloseEvent.NAME.equals(eventName)) {
                final CloseEvent closeEvent = new CloseEvent(pane, behaviorEvent.getBehavior());
                closeEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(closeEvent);

                return;
            }
            else if (ResizeEvent.NAME.equals(eventName)) {
                final double width = Double.valueOf(params.get(clientId + "_width"));
                final double height = Double.valueOf(params.get(clientId + "_height"));

                final ResizeEvent resizeEvent = new ResizeEvent(pane, behaviorEvent.getBehavior(), width, height);
                resizeEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(resizeEvent);

                return;
            }
        }

        super.queueEvent(event);
    }

    public LayoutPane getLayoutPane(final UIComponent component, final String combinedPosition) {
        for (final UIComponent child : component.getChildren()) {
            if (child instanceof LayoutPane) {
                if (((LayoutPane) child).getCombinedPosition().equals(combinedPosition)) {
                    return (LayoutPane) child;
                }
                else {
                    final LayoutPane pane = getLayoutPane(child, combinedPosition);
                    if (pane != null) {
                        return pane;
                    }
                }
            }
        }

        return null;
    }

    public void removeOptions() {
        getStateHelper().remove(PropertyKeys.options);
    }

    public boolean isNested() {
        return getParent() instanceof LayoutPane;
    }

    public boolean isElementLayout() {
        return !isNested() && !isFullPage();
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context)
                    .equals(context.getExternalContext().getRequestParameterMap().get(
                                Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
    }
}
