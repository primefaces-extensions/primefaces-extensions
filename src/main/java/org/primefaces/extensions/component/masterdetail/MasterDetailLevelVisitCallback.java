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

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

/**
 * Callback calling while MasterDetailLevel's subtree is visiting. The aim of this callback is gathering of all components implementing
 * {@link javax.faces.component.EditableValueHolder} below the subtree.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5.1
 */
public class MasterDetailLevelVisitCallback implements VisitCallback {

    private List<EditableValueHolder> editableValueHolders = new ArrayList<EditableValueHolder>();

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
