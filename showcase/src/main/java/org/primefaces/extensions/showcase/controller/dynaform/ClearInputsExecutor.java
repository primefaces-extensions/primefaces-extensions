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
package org.primefaces.extensions.showcase.controller.dynaform;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitResult;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.util.visitcallback.VisitTaskExecutor;

/**
 * Executor to clear all input values of one row in pe:dynaForm.
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class ClearInputsExecutor implements VisitTaskExecutor {

    private static final String INDEX_ATTR = "rcIndex";

    private ELContext elContext;
    private String[] ids;
    private int index;

    public ClearInputsExecutor(ELContext elContext, String[] ids, int index) {
        this.elContext = elContext;
        this.ids = ids;
        this.index = index;
    }

    /**
     * @see org.primefaces.extensions.util.visitcallback.VisitTaskExecutor
     */
    @Override
    public VisitResult execute(UIComponent component) {
        UIInput input = (UIInput) component;
        String id = input.getId();

        // reset UI
        input.resetValue();

        // reset value in bean
        if ("tableColumn".equals(id) || "inputValue".equals(id)) {
            ValueExpression ve = input.getValueExpression("value");
            if (ve != null) {
                ve.setValue(elContext, StringUtils.EMPTY);
            }
        }
        else if ("inputOffset".equals(id)) {
            ValueExpression ve = input.getValueExpression("value");
            if (ve != null) {
                ve.setValue(elContext, 0);
            }
        }
        else if ("valueOperator".equals(id)) {
            ValueExpression ve = input.getValueExpression("value");
            if (ve != null) {
                ve.setValue(elContext, "eq");
            }
        }

        // update the corresponding input during response
        PrimeFaces.current().ajax().update(input.getClientId());

        // delete handled id from the list, so that similar inputs should not be executed again
        ArrayUtils.removeElement(ids, id);

        return ids.length != 0 ? VisitResult.REJECT : VisitResult.COMPLETE;
    }

    /**
     * @see org.primefaces.extensions.util.visitcallback.VisitTaskExecutor
     */
    @Override
    public boolean shouldExecute(UIComponent component) {
        if (component instanceof EditableValueHolder && ArrayUtils.contains(ids, component.getId())) {
            Integer idx = (Integer) component.getAttributes().get(INDEX_ATTR);
            if (idx != null && index == idx) {
                return true;
            }
        }

        return false;
    }
}
