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
package org.primefaces.extensions.component.dynaform;

import java.util.HashMap;
import java.util.List;
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
import jakarta.faces.event.PhaseId;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.util.Constants;

/**
 * <code>DynaForm</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
@FacesComponent(value = DynaForm.COMPONENT_TYPE, namespace = DynaForm.COMPONENT_FAMILY)
@FacesComponentInfo(description = "DynaForm is a dynamic form component that renders based on a DynaFormModel.")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "dynaform/dynaform.css")
@ResourceDependency(library = Constants.LIBRARY, name = "dynaform/dynaform.js")
public class DynaForm extends DynaFormBaseImpl {

    private Map<String, UIDynaFormControl> cells;

    public UIDynaFormControl getControlCell(final String type) {
        final UIDynaFormControl cell = getControlCells().get(type);

        if (cell == null) {
            throw new FacesException("UIDynaFormControl to type " + type + " was not found");
        }
        else {
            return cell;
        }
    }

    protected Map<String, UIDynaFormControl> getControlCells() {
        if (cells == null) {
            cells = new HashMap<>();
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIDynaFormControl) {
                    final UIDynaFormControl dynaFormCell = (UIDynaFormControl) child;
                    cells.put(dynaFormCell.getType(), dynaFormCell);
                }
            }
        }

        return cells;
    }

    protected static void checkModelInstance(Object value) {
        if (!(value instanceof DynaFormModel)) {
            throw new FacesException("Value in DynaForm must be of type DynaFormModel");
        }
    }

    @Override
    protected KeyData findData(final String key) {
        final Object value = getValue();
        if (value == null) {
            return null;
        }

        checkModelInstance(value);

        final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
        for (final DynaFormControl dynaFormControl : dynaFormControls) {
            if (key.equals(dynaFormControl.getKey())) {
                return dynaFormControl;
            }
        }

        return null;
    }

    @Override
    protected void processChildren(final FacesContext context, final PhaseId phaseId) {
        final Object value = getValue();
        if (value != null) {
            checkModelInstance(value);

            final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
            for (final DynaFormControl dynaFormControl : dynaFormControls) {
                processDynaFormCells(context, phaseId, dynaFormControl);
            }
        }

        resetData();
    }

    @Override
    protected boolean visitChildren(final VisitContext context, final VisitCallback callback) {
        final Object value = getValue();
        if (value == null) {
            return false;
        }

        checkModelInstance(value);

        final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
        for (final DynaFormControl dynaFormControl : dynaFormControls) {
            if (visitDynaFormCells(context, callback, dynaFormControl)) {
                return true;
            }
        }

        resetData();

        return false;
    }

    @Override
    protected boolean invokeOnChildren(final FacesContext context, final String clientId, final ContextCallback callback) {
        final Object value = getValue();
        if (value == null) {
            return false;
        }

        checkModelInstance(value);

        if (getChildCount() > 0) {
            // extract the dynaFormControl key from the clientId
            // it's similar to rowKey in UIData
            String key = clientId.substring(getClientId().length() + 1);
            key = key.substring(0, key.indexOf(UINamingContainer.getSeparatorChar(context)));

            final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
            for (final DynaFormControl dynaFormControl : dynaFormControls) {

                // determine associated DynaFormControl
                if (dynaFormControl.getKey().equals(key)) {

                    // get UI control for DynaFormControl
                    final UIDynaFormControl uiDynaFormControl = getControlCell(dynaFormControl.getType());

                    try {
                        // push the associated data before visiting the child components
                        setData(dynaFormControl);

                        // visit childs
                        if (uiDynaFormControl.invokeOnComponent(context, clientId, callback)) {
                            return true;
                        }
                    }
                    finally {
                        resetData();
                    }

                    break;
                }
            }
        }

        return false;
    }

    private void processDynaFormCells(final FacesContext context, final PhaseId phaseId, final DynaFormControl dynaFormControl) {
        for (int i = 0; i < getChildCount(); i++) {
            final UIComponent kid = getChildren().get(i);
            if (!(kid instanceof UIDynaFormControl) || !kid.isRendered()
                        || !((UIDynaFormControl) kid).getType().equals(dynaFormControl.getType())) {
                continue;
            }

            setData(dynaFormControl);
            if (getData() == null) {
                return;
            }

            for (int j = 0; j < kid.getChildCount(); j++) {
                final UIComponent grandkid = kid.getChildren().get(j);
                processGrandkid(context, phaseId, grandkid);
            }
        }
    }

    private static void processGrandkid(final FacesContext context, final PhaseId phaseId, final UIComponent grandkid) {
        if (!grandkid.isRendered()) {
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

    private boolean visitDynaFormCells(final VisitContext context, final VisitCallback callback, final DynaFormControl dynaFormControl) {
        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIDynaFormControl
                            && ((UIDynaFormControl) child).getType().equals(dynaFormControl.getType())) {
                    setData(dynaFormControl);
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

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        cells = null;

        return super.saveState(context);
    }
}
