/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.layout;

import java.util.Map;
import java.util.logging.Logger;

import jakarta.el.ValueExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;

import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.OpenEvent;
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.util.FastStringWriter;
import org.primefaces.util.LangUtils;

/**
 * <code>Layout</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@jakarta.faces.component.FacesComponent(value = Layout.COMPONENT_TYPE, namespace = Layout.COMPONENT_FAMILY)
@org.primefaces.cdk.api.FacesComponentInfo(description = "Layout component for border layout with resizable panes.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "layout/layout.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "layout/layout.js")
public class Layout extends LayoutBaseImpl {

    public static final String POSITION_SEPARATOR = "_";
    public static final String STYLE_CLASS = "ui-layout-";
    public static final String STYLE_CLASS_PANE = "ui-layout-unit ui-panel ui-widget ui-widget-content ui-corner-all";
    public static final String STYLE_CLASS_PANE_WITH_SUBPANES = "ui-corner-all pe-layout-pane-withsubpanes";
    public static final String STYLE_CLASS_PANE_HEADER = "ui-layout-unit-header ui-panel-titlebar ui-widget-header ui-corner-top pe-layout-pane-header ";
    public static final String STYLE_CLASS_PANE_CONTENT = "ui-layout-unit-content pe-layout-pane-content ui-panel-content";
    public static final String STYLE_CLASS_LAYOUT_CONTENT = "ui-layout-content ui-panel-content";

    public static final String PANE_POSITION_CENTER = "center";
    public static final String PANE_POSITION_NORTH = "north";
    public static final String PANE_POSITION_SOUTH = "south";
    public static final String PANE_POSITION_WEST = "west";
    public static final String PANE_POSITION_EAST = "east";

    private static final Logger LOG = Logger.getLogger(Layout.class.getName());

    private ResponseWriter originalWriter;
    private FastStringWriter fsw;
    private boolean buildOptions;

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isAjaxRequestSource(fc)) {
            decode(fc);
        }
        else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void processValidators(final FacesContext fc) {
        if (!isAjaxRequestSource(fc)) {
            super.processValidators(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isAjaxRequestSource(fc)) {
            super.processUpdates(fc);
        }

        final String state = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + "_state");
        if (LangUtils.isNotBlank(state)) {
            final ValueExpression stateVE = getValueExpression(PropertyKeys.state.name());
            if (stateVE != null) {
                stateVE.setValue(fc.getELContext(), state);
                getStateHelper().remove(PropertyKeys.state);
            }
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String clientId = getClientId(context);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            final LayoutPane pane = getLayoutPane(this, params.get(clientId + "_pane"));
            if (pane == null) {
                LOG.warning("LayoutPane by request parameter '" + params.get(clientId + "_pane") + "' was not found");
                return;
            }

            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.open)) {
                final OpenEvent openEvent = new OpenEvent(pane, behaviorEvent.getBehavior());
                openEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(openEvent);
                return;
            }
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.close)) {
                final CloseEvent closeEvent = new CloseEvent(pane, behaviorEvent.getBehavior());
                closeEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(closeEvent);
                return;
            }
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.resize)) {
                final double width = Double.parseDouble(params.get(clientId + "_width"));
                final double height = Double.parseDouble(params.get(clientId + "_height"));
                final ResizeEvent resizeEvent = new ResizeEvent(pane, behaviorEvent.getBehavior(), width, height);
                resizeEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(resizeEvent);
                return;
            }
        }
        super.queueEvent(event);
    }

    public static LayoutPane getLayoutPane(final UIComponent component, final String combinedPosition) {
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

    public ResponseWriter getOriginalWriter() {
        return originalWriter;
    }

    public void setOriginalWriter(final ResponseWriter originalWriter) {
        this.originalWriter = originalWriter;
    }

    public FastStringWriter getFastStringWriter() {
        return fsw;
    }

    public void setFastStringWriter(final FastStringWriter fsw) {
        this.fsw = fsw;
    }

    public boolean isBuildOptions() {
        return buildOptions;
    }

    public void setBuildOptions(final boolean buildOptions) {
        this.buildOptions = buildOptions;
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

}
