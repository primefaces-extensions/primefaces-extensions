/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
