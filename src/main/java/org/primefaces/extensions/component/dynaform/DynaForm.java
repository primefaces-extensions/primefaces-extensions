/**
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.component.dynaform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractDynamicData;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormModel;

/**
 * <code>DynaForm</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css")
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js")
public class DynaForm extends AbstractDynamicData implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DynaForm";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DynaFormRenderer";

    private Map<String, UIDynaFormControl> cells;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar, autoSubmit, openExtended, buttonBarPosition, // top, bottom, both
        style, styleClass, columnClasses;

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    public DynaForm() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(final String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public boolean isAutoSubmit() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoSubmit, false);
    }

    public void setAutoSubmit(final boolean autoSubmit) {
        getStateHelper().put(PropertyKeys.autoSubmit, autoSubmit);
    }

    public boolean isOpenExtended() {
        return (Boolean) getStateHelper().eval(PropertyKeys.openExtended, false);
    }

    public void setOpenExtended(final boolean openExtended) {
        getStateHelper().put(PropertyKeys.openExtended, openExtended);
    }

    public String getButtonBarPosition() {
        return (String) getStateHelper().eval(PropertyKeys.buttonBarPosition, "bottom");
    }

    public void setButtonBarPosition(final String buttonBarPosition) {
        getStateHelper().put(PropertyKeys.buttonBarPosition, buttonBarPosition);
    }

    public void setStyle(final String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setColumnClasses(java.lang.String columnClasses) {
        getStateHelper().put(PropertyKeys.columnClasses, columnClasses);
    }

    public java.lang.String getColumnClasses() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.columnClasses, null);
    }

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

    protected void checkModelInstance(Object value) {
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
        for (final UIComponent kid : getChildren()) {
            if (!(kid instanceof UIDynaFormControl) || !kid.isRendered()
                        || !((UIDynaFormControl) kid).getType().equals(dynaFormControl.getType())) {
                continue;
            }

            setData(dynaFormControl);
            if (getData() == null) {
                return;
            }

            for (final UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
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
}
