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
package org.primefaces.extensions.component.keynote;

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
import org.primefaces.extensions.event.KeynoteEvent;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.keynote.KeynoteItem;

@FacesComponent(value = Keynote.COMPONENT_TYPE, namespace = Keynote.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Keynote is a presentation component based on Reveal.js.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "keynote/keynote.js")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "keynote/keynote.css")
public class Keynote extends KeynoteBaseImpl {

    private Map<String, UIKeynoteItem> items;

    @Override
    public void queueEvent(final FacesEvent event) {
        if (isAjaxBehaviorEventSource(event)) {
            final FacesContext context = event.getFacesContext();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.slideTransitionEnd)) {
                final boolean slideTransitionEnd = Boolean.parseBoolean(
                            params.get(getClientId(context) + "_slideTransitionEnd"));
                final boolean lastSlide = Boolean.parseBoolean(params.get(getClientId(context) + "_lastSlide"));
                final KeynoteEvent keynoteEvent = new KeynoteEvent(this, behaviorEvent.getBehavior(),
                            slideTransitionEnd, lastSlide);
                keynoteEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(keynoteEvent);
                return;
            }
            else if (isAjaxBehaviorEvent(event, ClientBehaviorEventKeys.slideChanged)) {
                final boolean slideChanged = Boolean.parseBoolean(params.get(getClientId(context) + "_slideChanged"));
                final boolean lastSlide = Boolean.parseBoolean(params.get(getClientId(context) + "_lastSlide"));
                final KeynoteEvent keynoteEvent = new KeynoteEvent(this, behaviorEvent.getBehavior(), slideChanged,
                            lastSlide);
                keynoteEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(keynoteEvent);
                return;
            }
            else {
                super.queueEvent(event);
                return;
            }
        }
        super.queueEvent(event);
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
            processKeynoteStaticItems(context, phaseId);
        }
    }

    @Override
    protected boolean visitChildren(final VisitContext context, final VisitCallback callback) {
        if (getVar() != null) {
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
            String key = clientId.substring(getClientId().length() + 1);
            key = key.substring(0, key.indexOf(UINamingContainer.getSeparatorChar(context)));

            final Collection<KeynoteItem> keynoteItems = (Collection<KeynoteItem>) value;
            for (final KeynoteItem keynoteItem : keynoteItems) {

                if (keynoteItem.getKey().equals(key)) {

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
                        setData(keynoteItem);

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
        items = null;

        return super.saveState(context);
    }
}
