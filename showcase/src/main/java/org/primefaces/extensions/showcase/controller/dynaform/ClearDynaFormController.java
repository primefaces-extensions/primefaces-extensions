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
package org.primefaces.extensions.showcase.controller.dynaform;

import java.io.*;
import java.util.*;

import javax.annotation.*;
import javax.faces.component.visit.*;
import javax.faces.context.*;
import javax.faces.view.*;
import javax.inject.*;

import org.primefaces.extensions.component.dynaform.*;
import org.primefaces.extensions.model.dynaform.*;
import org.primefaces.extensions.showcase.model.dynaform.*;
import org.primefaces.extensions.util.visitcallback.*;

/**
 * ClearDynaFormController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class ClearDynaFormController implements Serializable {

    protected static final Set<VisitHint> VISIT_HINTS = EnumSet.of(VisitHint.SKIP_UNRENDERED);
    private static final long serialVersionUID = 20130504L;

    private DynaFormModel model;
    private List<Condition> conditions;

    @PostConstruct
    protected void initialize() {
        model = new DynaFormModel();
        conditions = new ArrayList<>();

        // 1. condition and row
        Condition condition = new Condition("model", 2, "eq", "mercedes", 0);
        conditions.add(condition);

        DynaFormRow row = model.createRegularRow();
        row.addControl(condition, "column");
        row.addControl(condition, "offset");
        row.addControl(condition, "operator");
        row.addControl(condition, "value");
        row.addControl(condition, "clear");

        // 2. condition and row
        condition = new Condition("manufacturer", 1, "not", "chrysler group", 1);
        conditions.add(condition);

        row = model.createRegularRow();
        row.addControl(condition, "column");
        row.addControl(condition, "offset");
        row.addControl(condition, "operator");
        row.addControl(condition, "value");
        row.addControl(condition, "clear");

        // 3. condition and row
        condition = new Condition("year", 0, "lt", "2010", 2);
        conditions.add(condition);

        row = model.createRegularRow();
        row.addControl(condition, "column");
        row.addControl(condition, "offset");
        row.addControl(condition, "operator");
        row.addControl(condition, "value");
        row.addControl(condition, "clear");
    }

    public static void clearInputs(final int index) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final DynaForm dynaForm = (DynaForm) fc.getViewRoot().findComponent(":mainForm:dynaForm");

        // Ids of components to be visited
        final String[] ids = new String[] {"tableColumn", "inputValue", "inputOffset", "valueOperator"};

        final VisitTaskExecutor visitTaskExecutor = new ClearInputsExecutor(fc.getELContext(), ids, index);

        // clear inputs in the visit callback
        final ExecutableVisitCallback visitCallback = new ExecutableVisitCallback(visitTaskExecutor);
        dynaForm.visitTree(VisitContext.createVisitContext(fc, null, VISIT_HINTS), visitCallback);
    }

    public void removeCondition(final Condition condition) {
        model.removeRegularRow(condition.getIndex());
        conditions.remove(condition);

        // re-index conditions
        int idx = 0;
        for (final Condition cond : conditions) {
            cond.setIndex(idx);
            idx++;
        }
    }

    public String getConditions() {
        final StringBuilder sb = new StringBuilder();
        for (final Condition condition : conditions) {
            sb.append(condition.toString());
            sb.append("<br/>");
        }

        return sb.toString();
    }

    public DynaFormModel getModel() {
        return model;
    }
}
