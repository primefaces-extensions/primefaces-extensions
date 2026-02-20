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
package org.primefaces.extensions.component.fluidgrid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.FacesException;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.ContextCallback;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UINamingContainer;
import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.PhaseId;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.event.LayoutCompleteEvent;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;

/**
 * <code>FluidGrid</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 1.1.0
 */
@FacesComponent(value = FluidGrid.COMPONENT_TYPE, namespace = FluidGrid.COMPONENT_FAMILY)
@FacesComponentInfo(description = "FluidGrid is a masonry-style layout component that arranges items in an optimal position based on vertical space.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "fluidgrid/fluidgrid.css")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "fluidgrid/fluidgrid.js")
public class FluidGrid extends FluidGridBaseImpl {

    private Map<String, UIFluidGridItem> items;

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.layoutComplete)) {
                final LayoutCompleteEvent layoutCompleteEvent = new LayoutCompleteEvent(this,
                            ((AjaxBehaviorEvent) event).getBehavior());
                layoutCompleteEvent.setPhaseId(event.getPhaseId());

                super.queueEvent(layoutCompleteEvent);
                return;
            }
            else {
                super.queueEvent(event);
                return;
            }
        }
        super.queueEvent(event);
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

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        items = null;

        return super.saveState(context);
    }
}
