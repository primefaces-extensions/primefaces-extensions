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
package org.primefaces.extensions.component.switchcase;

import java.io.IOException;
import java.util.Objects;

import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.event.PhaseId;

import org.primefaces.cdk.api.FacesComponentInfo;

/**
 * Component class for the <code>Switch</code> component.
 *
 * @author Michael Gmeiner / last modified by Melloware
 * @since 0.6
 */
@jakarta.faces.component.FacesComponent(value = Switch.COMPONENT_TYPE, namespace = Switch.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Switch evaluates a value and renders the matching case or defaultCase child.")
public class Switch extends SwitchBaseImpl {

    private void evaluate() {
        UIComponent caseToRender = null;
        UIComponent defaultCase = null;

        for (final UIComponent child : getChildren()) {
            child.setRendered(false);

            if (child instanceof Case) {
                final Case caseComponent = (Case) child;
                final Object evaluate = getValue();
                final Object caseValue = caseComponent.getValue();

                if (Objects.equals(evaluate, caseValue)) {
                    caseToRender = caseComponent;
                }
            }
            else if (child instanceof DefaultCase) {
                defaultCase = child;
            }
            else {
                throw new FacesException("Switch only accepts case or defaultCase as children.");
            }
        }

        if (caseToRender == null) {
            caseToRender = defaultCase;
        }

        if (caseToRender != null) {
            caseToRender.setRendered(true);
        }
    }

    @Override
    public void processDecodes(final FacesContext context) {
        evaluate();
        super.processDecodes(context);
    }

    @Override
    public void processValidators(final FacesContext context) {
        evaluate();
        super.processValidators(context);
    }

    @Override
    public void processUpdates(final FacesContext context) {
        evaluate();
        super.processUpdates(context);
    }

    @Override
    public void broadcast(final FacesEvent event) {
        evaluate();
        super.broadcast(event);
    }

    @Override
    public boolean visitTree(final VisitContext context, final VisitCallback callback) {
        if (context.getFacesContext().getCurrentPhaseId() != PhaseId.RESTORE_VIEW) {
            evaluate();
        }
        return super.visitTree(context, callback);
    }

    @Override
    public void encodeBegin(final FacesContext context) throws IOException {
        evaluate();
        super.encodeBegin(context);
    }
}
