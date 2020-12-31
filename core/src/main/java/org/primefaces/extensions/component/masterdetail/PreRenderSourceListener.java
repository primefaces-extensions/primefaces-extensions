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
package org.primefaces.extensions.component.masterdetail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;

import org.primefaces.util.ComponentTraversalUtils;

/**
 * {@link ComponentSystemEventListener} for components with attached <code>SelectDetailLevel</code>.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class PreRenderSourceListener implements ComponentSystemEventListener, Serializable {

    private static final long serialVersionUID = 20111121L;

    @Override
    public void processEvent(ComponentSystemEvent event) {
        final UIComponent source = event.getComponent();

        // find master detail component
        final MasterDetail masterDetail = findMasterDetail(source);
        if (masterDetail == null) {
            throw new FacesException("MasterDetail was not found. SelectDetailLevel can be only used inside of MasterDetail.");
        }

        // find master detail level component
        final MasterDetailLevel masterDetailLevel = findMasterDetailLevel(source);
        if (masterDetailLevel == null) {
            throw new FacesException(
                        "MasterDetailLevel was not found. SelectDetailLevel can be only used inside of MasterDetailLevel.");
        }

        final FacesContext fc = FacesContext.getCurrentInstance();
        final String masterDetailClientId = masterDetail.getClientId(fc);

        // attach parameters dynamically
        // required basic parameters
        addUIParameter(fc, source, masterDetailClientId + MasterDetail.SELECT_DETAIL_REQUEST, true);
        addUIParameter(fc, source, masterDetailClientId + MasterDetail.CURRENT_LEVEL, masterDetailLevel.getLevel());

        // selected level
        final ValueExpression selectedLevelVE = (ValueExpression) source.getAttributes().get(MasterDetail.SELECTED_LEVEL_VALUE_EXPRESSION);
        final Object selectedLevel = selectedLevelVE != null ? selectedLevelVE.getValue(fc.getELContext()) : null;
        if (selectedLevel != null) {
            addUIParameter(fc, source, masterDetailClientId + MasterDetail.SELECTED_LEVEL, selectedLevel);
        }
        else {
            removeUIParameter(source, masterDetailClientId + MasterDetail.SELECTED_LEVEL);
        }

        // selected step
        final ValueExpression selectedStepVE = (ValueExpression) source.getAttributes().get(MasterDetail.SELECTED_STEP_VALUE_EXPRESSION);
        final Object selectedStep = selectedStepVE != null ? selectedStepVE.getValue(fc.getELContext()) : null;
        if (selectedStep != null) {
            addUIParameter(fc, source, masterDetailClientId + MasterDetail.SELECTED_STEP, selectedStep);
        }
        else {
            removeUIParameter(source, masterDetailClientId + MasterDetail.SELECTED_STEP);
        }

        // preserveInputs
        final ValueExpression preserveInputsVE = (ValueExpression) source.getAttributes().get(MasterDetail.PRESERVE_INPUTS_VALUE_EXPRESSION);
        final Object preserveInputs = preserveInputsVE != null ? preserveInputsVE.getValue(fc.getELContext()) : null;
        if (preserveInputs != null) {
            addUIParameter(fc, source, masterDetailClientId + MasterDetail.PRESERVE_INPUTS, preserveInputs);
        }
        else {
            removeUIParameter(source, masterDetailClientId + MasterDetail.PRESERVE_INPUTS);
        }

        // resetInputs
        final ValueExpression resetInputsVE = (ValueExpression) source.getAttributes().get(MasterDetail.RESET_INPUTS_VALUE_EXPRESSION);
        final Object resetInputs = resetInputsVE != null ? resetInputsVE.getValue(fc.getELContext()) : null;
        if (resetInputs != null) {
            addUIParameter(fc, source, masterDetailClientId + MasterDetail.RESET_INPUTS, resetInputs);
        }
        else {
            removeUIParameter(source, masterDetailClientId + MasterDetail.RESET_INPUTS);
        }

        final ValueExpression contextValueVE = (ValueExpression) source.getAttributes().get(MasterDetail.CONTEXT_VALUE_VALUE_EXPRESSION);
        if (contextValueVE == null) {
            return;
        }

        Map<String, Object> contextValues = (Map<String, Object>) masterDetailLevel.getAttributes().get(MasterDetail.CONTEXT_VALUES);
        if (contextValues == null) {
            contextValues = new HashMap<>();
        }

        // resolve context value and make it available in MasterDetail component
        final Object contextValue = contextValueVE.getValue(fc.getELContext());
        if (contextValue != null) {
            contextValues.put(MasterDetail.RESOLVED_CONTEXT_VALUE + source.getClientId(fc), contextValue);
        }
        else {
            contextValues.remove(MasterDetail.RESOLVED_CONTEXT_VALUE + source.getClientId(fc));
        }

        masterDetailLevel.getAttributes().put(MasterDetail.CONTEXT_VALUES, contextValues);
    }

    private MasterDetail findMasterDetail(UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof MasterDetail) {
                return (MasterDetail) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    private MasterDetailLevel findMasterDetailLevel(UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof MasterDetailLevel) {
                return (MasterDetailLevel) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    private void addUIParameter(FacesContext fc, UIComponent source, String paramName, Object paramValue) {
        for (final UIComponent child : source.getChildren()) {
            if (child instanceof UIParameter && paramName.equals(((UIParameter) child).getName())) {
                // update value
                ((UIParameter) child).setValue(paramValue);

                return;
            }
        }

        final UIParameter uiParameter = new UIParameter();
        uiParameter.setId(createUniqueId(fc, source));
        uiParameter.setName(paramName);
        uiParameter.setValue(paramValue);
        uiParameter.setTransient(true);
        source.getChildren().add(uiParameter);
    }

    private void removeUIParameter(UIComponent source, String paramName) {
        final List<UIComponent> childs = source.getChildren();
        if (childs == null || childs.isEmpty()) {
            return;
        }

        for (final UIComponent child : childs) {
            if (child instanceof UIParameter && paramName.equals(((UIParameter) child).getName())) {
                childs.remove(child);

                break;
            }
        }
    }

    private String createUniqueId(FacesContext fc, UIComponent source) {
        final UniqueIdVendor parentUniqueIdVendor = ComponentTraversalUtils.closestUniqueIdVendor(source);

        if (parentUniqueIdVendor == null) {
            final UIViewRoot viewRoot = fc.getViewRoot();

            if (viewRoot != null) {
                return viewRoot.createUniqueId(fc, null);
            }
            else {
                throw new FacesException("Cannot create Id for UIParameter attached to " + source.getClass().getCanonicalName());
            }
        }

        return parentUniqueIdVendor.createUniqueId(fc, null);
    }
}
