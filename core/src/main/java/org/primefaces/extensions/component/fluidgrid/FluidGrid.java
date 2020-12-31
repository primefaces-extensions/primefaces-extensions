/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.fluidgrid;

import java.util.*;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractDynamicData;
import org.primefaces.extensions.event.LayoutCompleteEvent;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.util.Constants;

/**
 * <code>FluidGrid</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 1.1.0
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "fluidgrid/fluidgrid.css")
@ResourceDependency(library = "primefaces-extensions", name = "fluidgrid/fluidgrid.js")
public class FluidGrid extends AbstractDynamicData implements Widget, ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FluidGrid";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FluidGridRenderer";

    private static final Collection<String> EVENT_NAMES = Collections
                .unmodifiableCollection(Arrays.asList("layoutComplete"));

    private Map<String, UIFluidGridItem> items;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by Melloware
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        // @formatter:off
      widgetVar,
      style,
      styleClass,
      hGutter,
      vGutter,
      fitWidth,
      originLeft,
      originTop,
      resizeBound,
      stamp,
      transitionDuration,
      hasImages
      // @formatter:on
    }

    public FluidGrid() {
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

    public int gethGutter() {
        return (Integer) getStateHelper().eval(PropertyKeys.hGutter, 0);
    }

    public void sethGutter(final int hGutter) {
        getStateHelper().put(PropertyKeys.hGutter, hGutter);
    }

    public int getvGutter() {
        return (Integer) getStateHelper().eval(PropertyKeys.vGutter, 0);
    }

    public void setvGutter(final int vGutter) {
        getStateHelper().put(PropertyKeys.vGutter, vGutter);
    }

    public boolean isFitWidth() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fitWidth, false);
    }

    public void setFitWidth(final boolean fitWidth) {
        getStateHelper().put(PropertyKeys.fitWidth, fitWidth);
    }

    public boolean isOriginLeft() {
        return (Boolean) getStateHelper().eval(PropertyKeys.originLeft, true);
    }

    public void setOriginLeft(final boolean originLeft) {
        getStateHelper().put(PropertyKeys.originLeft, originLeft);
    }

    public boolean isOriginTop() {
        return (Boolean) getStateHelper().eval(PropertyKeys.originTop, true);
    }

    public void setOriginTop(final boolean originTop) {
        getStateHelper().put(PropertyKeys.originTop, originTop);
    }

    public boolean isResizeBound() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizeBound, true);
    }

    public void setResizeBound(final boolean resizeBound) {
        getStateHelper().put(PropertyKeys.resizeBound, resizeBound);
    }

    public String getStamp() {
        return (String) getStateHelper().eval(PropertyKeys.stamp, null);
    }

    public void setStamp(final String stamp) {
        getStateHelper().put(PropertyKeys.stamp, stamp);
    }

    public String getTransitionDuration() {
        return (String) getStateHelper().eval(PropertyKeys.transitionDuration, "0.4s");
    }

    public void setTransitionDuration(final String transitionDuration) {
        getStateHelper().put(PropertyKeys.transitionDuration, transitionDuration);
    }

    public boolean isHasImages() {
        return (Boolean) getStateHelper().eval(PropertyKeys.hasImages, false);
    }

    public void setHasImages(final boolean hasImages) {
        getStateHelper().put(PropertyKeys.hasImages, hasImages);
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if ("layoutComplete".equals(eventName)) {
            if (event instanceof AjaxBehaviorEvent && isSelfRequest(context)) {
                final LayoutCompleteEvent layoutCompleteEvent = new LayoutCompleteEvent(this,
                            ((AjaxBehaviorEvent) event).getBehavior());
                layoutCompleteEvent.setPhaseId(event.getPhaseId());

                super.queueEvent(layoutCompleteEvent);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    private boolean isSelfRequest(final FacesContext context) {
        return getClientId(context).equals(
                    context.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
    }

    public UIFluidGridItem getItem(final String type) {
        final UIFluidGridItem item = getItems().get(type);

        if (item == null) {
            throw new FacesException("UIFluidGridItem to type " + type + " was not found");
        }
        else {
            return item;
        }
    }

    protected Map<String, UIFluidGridItem> getItems() {
        if (items == null) {
            items = new HashMap<>();
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIFluidGridItem) {
                    final UIFluidGridItem fluidGridItem = (UIFluidGridItem) child;
                    items.put(fluidGridItem.getType(), fluidGridItem);
                }
            }
        }

        return items;
    }

    protected static void checkModelInstance(Object value) {
        if (!(value instanceof Collection<?>)) {
            throw new FacesException("Value in FluidGrid must be of type Collection / List");
        }
    }

    @Override
    protected KeyData findData(final String key) {
        final Object value = getValue();
        if (value == null) {
            return null;
        }

        checkModelInstance(value);

        final Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
        for (final FluidGridItem fluidGridItem : col) {
            if (key.equals(fluidGridItem.getKey())) {
                return fluidGridItem;
            }
        }

        return null;
    }

    @Override
    protected void processChildren(final FacesContext context, final PhaseId phaseId) {
        if (context.getExternalContext().getRequestParameterMap()
                    .containsKey(getClientId(context) + "_layoutComplete")) {
            // don't decode, validate, update children if the processing was
            // triggered by the "layoutComplete" event
            return;
        }

        if (getVar() != null) {
            // dynamic items
            final Object value = getValue();
            if (value != null) {
                checkModelInstance(value);

                final Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
                for (final FluidGridItem fluidGridItem : col) {
                    processFluidGridDynamicItems(context, phaseId, fluidGridItem);
                }
            }

            resetData();
        }
        else {
            // static items
            processFluidGridStaticItems(context, phaseId);
        }
    }

    @Override
    protected boolean visitChildren(final VisitContext context, final VisitCallback callback) {
        if (getVar() != null) {
            // dynamic items
            final Object value = getValue();
            if (value == null) {
                return false;
            }

            checkModelInstance(value);

            final Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
            for (final FluidGridItem fluidGridItem : col) {
                if (visitFluidGridDynamicItems(context, callback, fluidGridItem)) {
                    return true;
                }
            }

            resetData();
        }
        else {
            // static items
            if (visitFluidGridStaticItems(context, callback)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean invokeOnChildren(final FacesContext context, final String clientId, final ContextCallback callback) {

        final Object value = getValue();
        if (value == null) {
            return false;
        }

        checkModelInstance(value);

        if (getChildCount() > 0) {
            // extract the fluidGridItem key from the clientId
            // it's simliar to rowKey in UIData
            String key = clientId.substring(getClientId().length() + 1);
            key = key.substring(0, key.indexOf(UINamingContainer.getSeparatorChar(context)));

            final Collection<FluidGridItem> fluidGridItems = (Collection<FluidGridItem>) value;
            for (final FluidGridItem fluidGridItem : fluidGridItems) {

                // determine associated FluidGridItem
                if (fluidGridItem.getKey().equals(key)) {

                    // get UI control for FluidGridItem
                    UIFluidGridItem uiFluidGridItem = null;
                    if (getVar() == null) {
                        for (final UIComponent child : getChildren()) {
                            if (child instanceof UIFluidGridItem && ((UIFluidGridItem) child).getType().equals(fluidGridItem.getType())) {
                                uiFluidGridItem = (UIFluidGridItem) child;
                            }
                        }
                    }
                    else {
                        uiFluidGridItem = (UIFluidGridItem) getChildren().get(0);
                    }

                    if (uiFluidGridItem == null) {
                        continue;
                    }

                    try {
                        // push the associated data before visiting the child components
                        setData(fluidGridItem);

                        // visit childs
                        if (uiFluidGridItem.invokeOnComponent(context, clientId, callback)) {
                            return true;
                        }
                    }
                    finally {
                        resetData();
                    }

                }
            }
        }

        return false;
    }

    private void processFluidGridDynamicItems(final FacesContext context, final PhaseId phaseId,
                final FluidGridItem fluidGridItem) {
        for (final UIComponent kid : getChildren()) {
            if (!(kid instanceof UIFluidGridItem) || !kid.isRendered()
                        || !((UIFluidGridItem) kid).getType().equals(fluidGridItem.getType())) {
                continue;
            }

            for (final UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
                }

                setData(fluidGridItem);
                if (getData() == null) {
                    return;
                }

                if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                    grandkid.processDecodes(context);
                }
                else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                    grandkid.processValidators(context);
                }
                else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                    grandkid.processUpdates(context);
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void processFluidGridStaticItems(final FacesContext context, final PhaseId phaseId) {
        for (final UIComponent kid : getChildren()) {
            if (!(kid instanceof UIFluidGridItem) || !kid.isRendered()) {
                continue;
            }

            for (final UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
                }

                if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                    grandkid.processDecodes(context);
                }
                else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                    grandkid.processValidators(context);
                }
                else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                    grandkid.processUpdates(context);
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private boolean visitFluidGridDynamicItems(final VisitContext context, final VisitCallback callback,
                final FluidGridItem fluidGridItem) {
        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIFluidGridItem
                            && ((UIFluidGridItem) child).getType().equals(fluidGridItem.getType())) {
                    setData(fluidGridItem);
                    if (getData() == null) {
                        return false;
                    }

                    if (child.visitTree(context, callback)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean visitFluidGridStaticItems(final VisitContext context, final VisitCallback callback) {
        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIFluidGridItem && child.visitTree(context, callback)) {
                    return true;
                }
            }
        }

        return false;
    }
}
