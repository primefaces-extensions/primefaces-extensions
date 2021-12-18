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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractDynamicData;
import org.primefaces.extensions.event.KeynoteEvent;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.keynote.KeynoteItem;
import org.primefaces.util.Constants;
import org.primefaces.util.MapBuilder;

@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
@ResourceDependency(library = "primefaces-extensions", name = "keynote/keynote.js")
@ResourceDependency(library = "primefaces-extensions", name = "keynote/keynote.css")
public class Keynote extends AbstractDynamicData implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Keynote";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.KeynoteRenderer";

    private static final String DEFAULT_EVENT = "slideChanged";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>> builder()
                .put("slideTransitionEnd", null)
                .put(DEFAULT_EVENT, null)
                .build();

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    private Map<String, UIKeynoteItem> items;

    // @formatter:off
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
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
        slideNumber,
        touch,
        transition,
        transitionSpeed,
        backgroundTransition,
        theme,
        library,
        style,
        styleClass
    }
    //@formatter:on

    public Keynote() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
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

    public String getSlideNumber() {
        return (String) getStateHelper().eval(PropertyKeys.slideNumber, "false");
    }

    public void setSlideNumber(final String slideNumber) {
        getStateHelper().put(PropertyKeys.slideNumber, slideNumber);
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
        return (String) getStateHelper().eval(PropertyKeys.theme, "none");
    }

    public void setTheme(final String theme) {
        getStateHelper().put(PropertyKeys.theme, theme);
    }

    public String getLibrary() {
        return (String) getStateHelper().eval(PropertyKeys.library, org.primefaces.extensions.util.Constants.LIBRARY);
    }

    public void setLibrary(final String library) {
        getStateHelper().put(PropertyKeys.library, library);
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

    @Override
    public void queueEvent(final FacesEvent event) {
        if (event instanceof AjaxBehaviorEvent) {
            final FacesContext context = getFacesContext();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if ("slideTransitionEnd".equals(eventName)) {
                final boolean slideTransitionEnd = Boolean.parseBoolean(
                            params.get(getClientId(context) + "_slideTransitionEnd"));
                final boolean lastSlide = Boolean.parseBoolean(params.get(getClientId(context) + "_lastSlide"));
                final KeynoteEvent keynoteEvent = new KeynoteEvent(this, behaviorEvent.getBehavior(),
                            slideTransitionEnd, lastSlide);
                keynoteEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(keynoteEvent);
            }
            else if (DEFAULT_EVENT.equals(eventName)) {
                final boolean slideChanged = Boolean.parseBoolean(params.get(getClientId(context) + "_slideChanged"));
                final boolean lastSlide = Boolean.parseBoolean(params.get(getClientId(context) + "_lastSlide"));
                final KeynoteEvent keynoteEvent = new KeynoteEvent(this, behaviorEvent.getBehavior(), slideChanged,
                            lastSlide);
                keynoteEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(keynoteEvent);
            }
            else {
                super.queueEvent(event);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    public UIKeynoteItem getItem(final String type) {
        final UIKeynoteItem item = getItems().get(type);

        if (item == null) {
            throw new FacesException("UIKeynoteItem to type " + type + " was not found");
        }
        else {
            return item;
        }
    }

    protected Map<String, UIKeynoteItem> getItems() {
        if (items == null) {
            items = new HashMap<>();
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIKeynoteItem) {
                    final UIKeynoteItem keynoteItem = (UIKeynoteItem) child;
                    items.put(keynoteItem.getType(), keynoteItem);
                }
            }
        }

        return items;
    }

    protected static void checkModelInstance(final Object value) {
        if (!(value instanceof Collection<?>)) {
            throw new FacesException("Value in Keynote must be of type Collection / List");
        }
    }

    @Override
    protected KeyData findData(final String key) {
        final Object value = getValue();
        if (value == null) {
            return null;
        }

        checkModelInstance(value);

        final Collection<KeynoteItem> col = (Collection<KeynoteItem>) value;
        for (final KeynoteItem keynoteItem : col) {
            if (key.equals(keynoteItem.getKey())) {
                return keynoteItem;
            }
        }

        return null;
    }

    @Override
    protected void processChildren(final FacesContext context, final PhaseId phaseId) {
        if (getVar() != null) {
            // dynamic items
            final Object value = getValue();
            if (value != null) {
                checkModelInstance(value);

                final Collection<KeynoteItem> col = (Collection<KeynoteItem>) value;
                for (final KeynoteItem keynoteItem : col) {
                    processKeynoteDynamicItems(context, phaseId, keynoteItem);
                }
            }

            resetData();
        }
        else {
            // static items
            processKeynoteStaticItems(context, phaseId);
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

            final Collection<KeynoteItem> col = (Collection<KeynoteItem>) value;
            for (final KeynoteItem keynoteItem : col) {
                if (visitKeynoteDynamicItems(context, callback, keynoteItem)) {
                    return true;
                }
            }

            resetData();
        }
        else {
            // static items
            return visitKeynoteStaticItems(context, callback);
        }

        return false;
    }

    @Override
    protected boolean invokeOnChildren(final FacesContext context, final String clientId,
                final ContextCallback callback) {

        final Object value = getValue();
        if (value == null) {
            return false;
        }

        checkModelInstance(value);

        if (getChildCount() > 0) {
            // extract the keynoteItem key from the clientId
            // it's similar to rowKey in UIData
            String key = clientId.substring(getClientId().length() + 1);
            key = key.substring(0, key.indexOf(UINamingContainer.getSeparatorChar(context)));

            final Collection<KeynoteItem> keynoteItems = (Collection<KeynoteItem>) value;
            for (final KeynoteItem keynoteItem : keynoteItems) {

                // determine associated KeynoteItem
                if (keynoteItem.getKey().equals(key)) {

                    // get UI control for KeynoteItem
                    UIKeynoteItem uiKeynoteItem = null;
                    if (getVar() == null) {
                        for (final UIComponent child : getChildren()) {
                            if (child instanceof UIKeynoteItem &&
                                        ((UIKeynoteItem) child).getType().equals(keynoteItem.getType())) {
                                uiKeynoteItem = (UIKeynoteItem) child;
                            }
                        }
                    }
                    else {
                        uiKeynoteItem = (UIKeynoteItem) getChildren().get(0);
                    }

                    if (uiKeynoteItem == null) {
                        continue;
                    }

                    try {
                        // push the associated data before visiting the child components
                        setData(keynoteItem);

                        // visit children
                        if (uiKeynoteItem.invokeOnComponent(context, clientId, callback)) {
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

    private void processKeynoteDynamicItems(final FacesContext context, final PhaseId phaseId,
                final KeynoteItem keynoteItem) {
        for (final UIComponent kid : getChildren()) {
            if (!(kid instanceof UIKeynoteItem) || !kid.isRendered()
                        || !((UIKeynoteItem) kid).getType().equals(keynoteItem.getType())) {
                continue;
            }

            for (final UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
                }

                setData(keynoteItem);
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

    private void processKeynoteStaticItems(final FacesContext context, final PhaseId phaseId) {
        for (final UIComponent kid : getChildren()) {
            if (!(kid instanceof UIKeynoteItem) || !kid.isRendered()) {
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

    private boolean visitKeynoteDynamicItems(final VisitContext context, final VisitCallback callback,
                final KeynoteItem keynoteItem) {
        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIKeynoteItem
                            && ((UIKeynoteItem) child).getType().equals(keynoteItem.getType())) {
                    setData(keynoteItem);
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

    private boolean visitKeynoteStaticItems(final VisitContext context, final VisitCallback callback) {
        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIKeynoteItem && child.visitTree(context, callback)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Object saveState(final FacesContext context) {
        // reset component for MyFaces view pooling
        items = null;

        return super.saveState(context);
    }

}
