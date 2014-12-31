/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.fluidgrid;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractDynamicData;
import org.primefaces.extensions.event.LayoutCompleteEvent;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
import org.primefaces.util.Constants;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.util.ComponentUtils;

/**
 * <code>FluidGrid</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 1.1.0
 */
@ResourceDependencies({@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), 
        @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), 
        @ResourceDependency(library = "primefaces", name = "primefaces.js"), 
        @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"), 
        @ResourceDependency(library = "primefaces-extensions", name = "fluidgrid/fluidgrid.css"), 
        @ResourceDependency(library = "primefaces-extensions", name = "fluidgrid/fluidgrid.js")})
public class FluidGrid extends AbstractDynamicData implements Widget, ClientBehaviorHolder
{

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.FluidGrid";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.FluidGridRenderer";

    private Map<String, UIFluidGridItem> items;

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("layoutComplete"));

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys
    {

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
        hasImages;

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return ((this.toString != null) ? this.toString : super.toString());
        }
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

    public void setWidgetVar(String widgetVar) {
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

    public int gethGutter() {
        return (Integer) getStateHelper().eval(PropertyKeys.hGutter, 0);
    }

    public void sethGutter(int hGutter) {
        getStateHelper().put(PropertyKeys.hGutter, hGutter);
    }

    public int getvGutter() {
        return (Integer) getStateHelper().eval(PropertyKeys.vGutter, 0);
    }

    public void setvGutter(int vGutter) {
        getStateHelper().put(PropertyKeys.vGutter, vGutter);
    }

    public boolean isFitWidth() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fitWidth, false);
    }

    public void setFitWidth(boolean fitWidth) {
        getStateHelper().put(PropertyKeys.fitWidth, fitWidth);
    }

    public boolean isOriginLeft() {
        return (Boolean) getStateHelper().eval(PropertyKeys.originLeft, true);
    }

    public void setOriginLeft(boolean originLeft) {
        getStateHelper().put(PropertyKeys.originLeft, originLeft);
    }

    public boolean isOriginTop() {
        return (Boolean) getStateHelper().eval(PropertyKeys.originTop, true);
    }

    public void setOriginTop(boolean originTop) {
        getStateHelper().put(PropertyKeys.originTop, originTop);
    }

    public boolean isResizeBound() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizeBound, true);
    }

    public void setResizeBound(boolean resizeBound) {
        getStateHelper().put(PropertyKeys.resizeBound, resizeBound);
    }

    public String getStamp() {
        return (String) getStateHelper().eval(PropertyKeys.stamp, null);
    }

    public void setStamp(String stamp) {
        getStateHelper().put(PropertyKeys.stamp, stamp);
    }

    public String getTransitionDuration() {
        return (String) getStateHelper().eval(PropertyKeys.transitionDuration, "0.4s");
    }

    public void setTransitionDuration(String transitionDuration) {
        getStateHelper().put(PropertyKeys.transitionDuration, transitionDuration);
    }

    public boolean isHasImages() {
        return (Boolean) getStateHelper().eval(PropertyKeys.hasImages, false);
    }

    public void setHasImages(boolean hasImages) {
        getStateHelper().put(PropertyKeys.hasImages, hasImages);
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if ("layoutComplete".equals(eventName)) {
            if (event instanceof AjaxBehaviorEvent && isSelfRequest(context)) {
                LayoutCompleteEvent layoutCompleteEvent = new LayoutCompleteEvent(this, ((AjaxBehaviorEvent) event).getBehavior
                        ());
                layoutCompleteEvent.setPhaseId(event.getPhaseId());

                super.queueEvent(layoutCompleteEvent);
            }
        } else {
            super.queueEvent(event);
        }
    }

    private boolean isSelfRequest(FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants
                .RequestParams.PARTIAL_SOURCE_PARAM));
    }

	public String resolveWidgetVar() {
        return ComponentUtils.resolveWidgetVar(getFacesContext(), this);
	}

    public UIFluidGridItem getItem(String type) {
        UIFluidGridItem item = getItems().get(type);

        if (item == null) {
            throw new FacesException("UIFluidGridItem to type " + type + " was not found");
        } else {
            return item;
        }
    }

    protected Map<String, UIFluidGridItem> getItems() {
        if (items == null) {
            items = new HashMap<String, UIFluidGridItem>();
            for (UIComponent child : getChildren()) {
                if (child instanceof UIFluidGridItem) {
                    UIFluidGridItem fluidGridItem = (UIFluidGridItem) child;
                    items.put(fluidGridItem.getType(), fluidGridItem);
                }
            }
        }

        return items;
    }

    @Override
    protected KeyData findData(String key) {
        Object value = getValue();
        if (value == null) {
            return null;
        }

        if (!(value instanceof Collection<?>)) {
            throw new FacesException("Value in FluidGrid must be of type Collection / List");
        }

        @SuppressWarnings("unchecked")
        Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
        for (FluidGridItem fluidGridItem : col) {
            if (key.equals(fluidGridItem.getKey())) {
                return fluidGridItem;
            }
        }

        return null;
    }

    @Override
    protected void processChildren(FacesContext context, PhaseId phaseId) {
        if (context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context) + "_layoutComplete")) {
            // don't decode, validate, update children if the processing was triggered by the "layoutComplete" event
            return;
        }

        if (getVar() != null) {
            // dynamic items
            Object value = getValue();
            if (value != null) {
                if (!(value instanceof Collection<?>)) {
                    throw new FacesException("Value in FluidGrid must be of type Collection / List");
                }

                @SuppressWarnings("unchecked")
                Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
                for (FluidGridItem fluidGridItem : col) {
                    processFluidGridDynamicItems(context, phaseId, fluidGridItem);
                }
            }

            resetData();
        } else {
            // static items
            processFluidGridStaticItems(context, phaseId);
        }
    }

    @Override
    protected boolean visitChildren(VisitContext context, VisitCallback callback) {
        if (getVar() != null) {
            // dynamic items
            Object value = getValue();
            if (value == null) {
                return false;
            }

            if (!(value instanceof Collection<?>)) {
                throw new FacesException("Value in FluidGrid must be of type Collection / List");
            }

            @SuppressWarnings("unchecked")
            Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
            for (FluidGridItem fluidGridItem : col) {
                if (visitFluidGridDynamicItems(context, callback, fluidGridItem)) {
                    return true;
                }
            }

            resetData();
        } else {
            // static items
            if (visitFluidGridStaticItems(context, callback)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean invokeOnChildren(FacesContext context, String clientId, ContextCallback callback) {
        if (getVar() != null) {
            // dynamic items
            Object value = getValue();
            if (value == null) {
                return false;
            }

            if (!(value instanceof Collection<?>)) {
                throw new FacesException("Value in FluidGrid must be of type Collection / List");
            }

            @SuppressWarnings("unchecked")
            Collection<FluidGridItem> col = (Collection<FluidGridItem>) value;
            for (FluidGridItem fluidGridItem : col) {
                setData(fluidGridItem);

                if (super.invokeOnComponent(context, clientId, callback)) {
                    return true;
                }
            }

            resetData();
        } else {
            // static items
            if (getChildCount() > 0) {
                for (UIComponent child : getChildren()) {
                    if (child.invokeOnComponent(context, clientId, callback)) {
                        return true;
                    }    
                }
            }
        }

        return false;
    }

    private void processFluidGridDynamicItems(FacesContext context, PhaseId phaseId, FluidGridItem fluidGridItem) {
        for (UIComponent kid : getChildren()) {
            if (!(kid instanceof UIFluidGridItem) || !kid.isRendered() || !((UIFluidGridItem) kid).getType().equals
                    (fluidGridItem.getType())) {
                continue;
            }

            for (UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
                }

                setData(fluidGridItem);
                if (getData() == null) {
                    return;
                }

                if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                    grandkid.processDecodes(context);
                } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                    grandkid.processValidators(context);
                } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                    grandkid.processUpdates(context);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void processFluidGridStaticItems(FacesContext context, PhaseId phaseId) {
        for (UIComponent kid : getChildren()) {
            if (!(kid instanceof UIFluidGridItem) || !kid.isRendered()) {
                continue;
            }

            for (UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
                }

                if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                    grandkid.processDecodes(context);
                } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                    grandkid.processValidators(context);
                } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                    grandkid.processUpdates(context);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private boolean visitFluidGridDynamicItems(VisitContext context, VisitCallback callback, FluidGridItem fluidGridItem) {
        if (getChildCount() > 0) {
            for (UIComponent child : getChildren()) {
                if (child instanceof UIFluidGridItem && ((UIFluidGridItem) child).getType().equals(fluidGridItem.getType())) {
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

    private boolean visitFluidGridStaticItems(VisitContext context, VisitCallback callback) {
        if (getChildCount() > 0) {
            for (UIComponent child : getChildren()) {
                if (child instanceof UIFluidGridItem && child.visitTree(context, callback)) {
                    return true;
                }
            }
        }

        return false;
    }
}
