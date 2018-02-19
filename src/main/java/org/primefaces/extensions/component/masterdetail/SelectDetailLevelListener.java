/**
 * Copyright 2011-2018 PrimeFaces Extensions
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

import java.util.HashMap;
import java.util.Map;

import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

/**
 * Class for all added at runtime listeners specified by SelectDetailLevel.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class SelectDetailLevelListener implements AjaxBehaviorListener, ActionListener, StateHolder {

    private MethodExpression listener;

    /**
     * This constructor is required for serialization. Please do not remove.
     */
    public SelectDetailLevelListener() {
    }

    public SelectDetailLevelListener(MethodExpression listener) {
        this.listener = listener;
    }

    public void processAction(ActionEvent actionEvent) {
        process(actionEvent.getComponent());
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
        process(event.getComponent());
    }

    public void process(UIComponent source) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final String clientId = source.getClientId(fc);

        // find master detail level component
        MasterDetailLevel masterDetailLevel = findMasterDetailLevel(source);
        if (masterDetailLevel == null) {
            throw new FacesException(
                        "MasterDetailLevel was not found. SelectDetailLevel can be only used inside of MasterDetailLevel.");
        }

        // get resolved context value
        Map<String, Object> contextValues = (Map<String, Object>) masterDetailLevel.getAttributes().get(MasterDetail.CONTEXT_VALUES);
        if (contextValues == null) {
            contextValues = new HashMap<String, Object>();
        }

        // get current context value
        Object contextValue = contextValues.get(MasterDetail.RESOLVED_CONTEXT_VALUE + clientId);

        // invoke listener and get new context value
        Object newContextValue = listener.invoke(fc.getELContext(), new Object[] { contextValue });

        // make new context value available in MasterDetail component
        if (newContextValue != null) {
            contextValues.put(MasterDetail.RESOLVED_CONTEXT_VALUE + clientId, newContextValue);
        }
        else {
            contextValues.remove(MasterDetail.RESOLVED_CONTEXT_VALUE + clientId);
        }
    }

    public boolean isTransient() {
        return false;
    }

    public void restoreState(FacesContext facesContext, Object state) {
        Object[] values = (Object[]) state;
        listener = (MethodExpression) values[0];
    }

    public Object saveState(final FacesContext facesContext) {
        Object[] values = new Object[1];
        values[0] = listener;

        return values;
    }

    public void setTransient(boolean value) {
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
}
