/*
 * Copyright (c) 2011-2025 PrimeFaces Extensions
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

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.component.visit.VisitResult;

/**
 * Callback calling while MasterDetailLevel's subtree is visiting. The aim of this callback is gathering of all components implementing
 * {@link jakarta.faces.component.EditableValueHolder} below the subtree.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5.1
 */
public class MasterDetailLevelVisitCallback implements VisitCallback {

    private final List<EditableValueHolder> editableValueHolders = new ArrayList<>();

    @Override
    public VisitResult visit(VisitContext context, UIComponent target) {
        if (target instanceof MasterDetailLevel) {
            return VisitResult.ACCEPT;
        }

        if (!target.isRendered()) {
            return VisitResult.REJECT;
        }

        if (target instanceof EditableValueHolder) {
            editableValueHolders.add((EditableValueHolder) target);
        }

        return VisitResult.ACCEPT;
    }

    public List<EditableValueHolder> getEditableValueHolders() {
        return editableValueHolders;
    }
}
