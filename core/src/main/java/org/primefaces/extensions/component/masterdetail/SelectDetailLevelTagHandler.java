/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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
package org.primefaces.extensions.component.masterdetail;

import java.util.*;
import java.util.Map.Entry;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.view.facelets.*;

import org.primefaces.component.api.AjaxSource;
import org.primefaces.extensions.util.ExtLangUtils;

/**
 * {@link TagHandler} for the <code>SelectDetailLevel</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class SelectDetailLevelTagHandler extends TagHandler {

    private final TagAttribute contextValue;
    private final TagAttribute listener;
    private final TagAttribute level;
    private final TagAttribute step;
    private final TagAttribute preserveInputs;
    private final TagAttribute resetInputs;
    private final TagAttribute event;

    public SelectDetailLevelTagHandler(final TagConfig config) {
        super(config);
        contextValue = getAttribute("contextValue");
        listener = getAttribute("listener");
        level = getAttribute("level");
        step = getAttribute("step");
        preserveInputs = getAttribute("preserveInputs");
        resetInputs = getAttribute("resetInputs");
        event = super.getAttribute("event");
    }

    @Override
    public void apply(final FaceletContext ctx, final UIComponent parent) {
        if (!isAjaxifiedComponent(parent)) {
            throw new FacesException("SelectDetailLevel must be only placed inside an ajaxified component.");
        }

        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        // get value expression for contextValue attribute of this tag handler
        final ValueExpression contextValueVE;
        if (contextValue != null) {
            contextValueVE = contextValue.getValueExpression(ctx, Object.class);
            if (contextValueVE != null) {
                // store valid value expression in the attributes map
                parent.getAttributes().put(MasterDetail.CONTEXT_VALUE_VALUE_EXPRESSION, contextValueVE);
            }
        }

        // get value expression for level attribute of this tag handler
        final ValueExpression selectedLevelVE;
        if (level != null) {
            selectedLevelVE = level.getValueExpression(ctx, int.class);
            if (selectedLevelVE != null) {
                // store valid value expression in the attributes map
                parent.getAttributes().put(MasterDetail.SELECTED_LEVEL_VALUE_EXPRESSION, selectedLevelVE);
            }
        }

        // get value expression for step attribute of this tag handler
        final ValueExpression selectedStepVE;
        if (step != null) {
            selectedStepVE = step.getValueExpression(ctx, int.class);
            if (selectedStepVE != null) {
                // store valid value expression in the attributes map
                parent.getAttributes().put(MasterDetail.SELECTED_STEP_VALUE_EXPRESSION, selectedStepVE);
            }
        }

        // get value expression for preserveInputs attribute of this tag handler
        final ValueExpression preserveInputsVE;
        if (preserveInputs != null) {
            preserveInputsVE = preserveInputs.getValueExpression(ctx, String.class);
            if (preserveInputsVE != null) {
                // store valid value expression in the attributes map
                parent.getAttributes().put(MasterDetail.PRESERVE_INPUTS_VALUE_EXPRESSION, preserveInputsVE);
            }
        }

        // get value expression for resetInputs attribute of this tag handler
        final ValueExpression resetInputsVE;
        if (resetInputs != null) {
            resetInputsVE = resetInputs.getValueExpression(ctx, String.class);
            if (resetInputsVE != null) {
                // store valid value expression in the attributes map
                parent.getAttributes().put(MasterDetail.RESET_INPUTS_VALUE_EXPRESSION, resetInputsVE);
            }
        }

        // register a ComponentSystemEventListener
        parent.subscribeToEvent(PreRenderComponentEvent.class, new PreRenderSourceListener());

        // register an ActionListener / AjaxBehaviorListener
        if (listener != null) {
            final MethodExpression me = listener.getMethodExpression(ctx, Object.class, new Class[] {Object.class});

            if (parent instanceof ActionSource) {
                ((ActionSource) parent).addActionListener(new SelectDetailLevelListener(me));
            }
            else if (parent instanceof ClientBehaviorHolder) {
                // find attached f:ajax / p:ajax corresponding to supported events
                final Collection<List<ClientBehavior>> clientBehaviors = getClientBehaviors(ctx, event, (ClientBehaviorHolder) parent);
                if (clientBehaviors == null || clientBehaviors.isEmpty()) {
                    return;
                }

                for (final List<ClientBehavior> listBehaviors : clientBehaviors) {
                    for (final ClientBehavior clientBehavior : listBehaviors) {
                        if (clientBehavior instanceof org.primefaces.behavior.ajax.AjaxBehavior) {
                            ((org.primefaces.behavior.ajax.AjaxBehavior) clientBehavior).addAjaxBehaviorListener(
                                        new SelectDetailLevelListener(me));
                        }
                        else if (clientBehavior instanceof javax.faces.component.behavior.AjaxBehavior) {
                            ((javax.faces.component.behavior.AjaxBehavior) clientBehavior).addAjaxBehaviorListener(
                                        new SelectDetailLevelListener(me));
                        }
                    }
                }
            }
        }
    }

    public static boolean isAjaxifiedComponent(final UIComponent component) {
        // check for ajax source
        if (component instanceof AjaxSource && ((AjaxSource) component).isAjaxified()) {
            return true;
        }

        if (component instanceof ClientBehaviorHolder) {
            // check for attached f:ajax / p:ajax
            final Collection<List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors()
                        .values();
            if (!behaviors.isEmpty()) {
                for (final List<ClientBehavior> listBehaviors : behaviors) {
                    for (final ClientBehavior clientBehavior : listBehaviors) {
                        if (clientBehavior instanceof javax.faces.component.behavior.AjaxBehavior
                                    || clientBehavior instanceof org.primefaces.behavior.ajax.AjaxBehavior) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static Collection<List<ClientBehavior>> getClientBehaviors(final FaceletContext context, final TagAttribute event,
                final ClientBehaviorHolder clientBehaviorHolder) {
        final Map<String, List<ClientBehavior>> mapBehaviors = clientBehaviorHolder.getClientBehaviors();
        if (mapBehaviors == null || mapBehaviors.isEmpty()) {
            return Collections.emptyList();
        }

        final String events = event != null ? event.getValue(context) : null;
        final String[] arrEvents = events != null ? events.split("[\\s,]+") : null;
        if (arrEvents == null || arrEvents.length < 1) {
            return mapBehaviors.values();
        }

        final Collection<List<ClientBehavior>> behaviors = new ArrayList<>();

        for (final Entry<String, List<ClientBehavior>> entry : mapBehaviors.entrySet()) {
            if (ExtLangUtils.contains(arrEvents, entry.getKey())) {
                behaviors.add(entry.getValue());
            }
        }

        return behaviors;
    }
}
