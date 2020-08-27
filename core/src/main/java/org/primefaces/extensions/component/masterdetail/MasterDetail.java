/*
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
package org.primefaces.extensions.component.masterdetail;

import java.util.Map;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PostRestoreStateEvent;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>MasterDetail</code> component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css")
public class MasterDetail extends UIComponentBase {

    public static final String CONTEXT_VALUE_VALUE_EXPRESSION = "mdContextValueVE";
    public static final String SELECTED_LEVEL_VALUE_EXPRESSION = "selectedLevelVE";
    public static final String SELECTED_STEP_VALUE_EXPRESSION = "selectedStepVE";
    public static final String PRESERVE_INPUTS_VALUE_EXPRESSION = "preserveInputsVE";
    public static final String RESET_INPUTS_VALUE_EXPRESSION = "resetInputsVE";
    public static final String CONTEXT_VALUES = "mdContextValues";
    public static final String SELECT_DETAIL_REQUEST = "_selectDetailRequest";
    public static final String CURRENT_LEVEL = "_currentLevel";
    public static final String SELECTED_LEVEL = "_selectedLevel";
    public static final String SELECTED_STEP = "_selectedStep";
    public static final String PRESERVE_INPUTS = "_preserveInputs";
    public static final String RESET_INPUTS = "_resetInputs";
    public static final String CURRENT_CONTEXT_VALUE = "_curContextValue";
    public static final String RESOLVED_CONTEXT_VALUE = "contextValue_";
    public static final String BREADCRUMB_ID_PREFIX = "_bc";

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MasterDetail";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MasterDetailRenderer";

    private MasterDetailLevel detailLevelToProcess;
    private MasterDetailLevel detailLevelToGo;
    private int levelPositionToProcess = -1;
    private int levelCount = -1;

    /**
     * Properties that are tracked by state saving.
     *
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {
        // @formatter:off
        level,
        contextValue,
        selectLevelListener,
        showBreadcrumb,
        showAllBreadcrumbItems,
        breadcrumbAboveHeader,
        style,
        styleClass
        // @formatter:on
    }

    public MasterDetail() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public int getLevel() {
        return (Integer) getStateHelper().eval(PropertyKeys.level, 1);
    }

    public void setLevel(final int level) {
        getStateHelper().put(PropertyKeys.level, level);
    }

    public Object getContextValue() {
        return getStateHelper().eval(PropertyKeys.contextValue, null);
    }

    public void setContextValue(final Object contextValue) {
        getStateHelper().put(PropertyKeys.contextValue, contextValue);
    }

    public MethodExpression getSelectLevelListener() {
        return (MethodExpression) getStateHelper().eval(PropertyKeys.selectLevelListener, null);
    }

    public void setSelectLevelListener(final MethodExpression selectLevelListener) {
        getStateHelper().put(PropertyKeys.selectLevelListener, selectLevelListener);
    }

    public boolean isShowBreadcrumb() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showBreadcrumb, true);
    }

    public void setShowBreadcrumb(final boolean showBreadcrumb) {
        getStateHelper().put(PropertyKeys.showBreadcrumb, showBreadcrumb);
    }

    public boolean isShowAllBreadcrumbItems() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showAllBreadcrumbItems, false);
    }

    public void setShowAllBreadcrumbItems(final boolean showAllBreadcrumbItems) {
        getStateHelper().put(PropertyKeys.showAllBreadcrumbItems, showAllBreadcrumbItems);
    }

    public boolean isBreadcrumbAboveHeader() {
        return (Boolean) getStateHelper().eval(PropertyKeys.breadcrumbAboveHeader, true);
    }

    public void setBreadcrumbAboveHeader(final boolean breadcrumbAboveHeader) {
        getStateHelper().put(PropertyKeys.breadcrumbAboveHeader, breadcrumbAboveHeader);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(final String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(final String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    @Override
    public void processEvent(final ComponentSystemEvent event) {
        super.processEvent(event);

        final FacesContext fc = FacesContext.getCurrentInstance();
        if (!(event instanceof PostRestoreStateEvent) || !isSelectDetailRequest(fc)) {
            return;
        }

        final PartialViewContext pvc = fc.getPartialViewContext();
        if (pvc.getRenderIds().isEmpty()) {
            // update the MasterDetail component automatically
            pvc.getRenderIds().add(getClientId(fc));
        }

        final MasterDetailLevel mdl = getDetailLevelToProcess(fc);
        final Object contextValue = getContextValueFromFlow(fc, mdl, true);
        final String contextVar = mdl.getContextVar();
        if (!LangUtils.isValueBlank(contextVar) && contextValue != null) {
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            requestMap.put(contextVar, contextValue);
        }
    }

    @Override
    public void processDecodes(final FacesContext fc) {
        if (!isSelectDetailRequest(fc)) {
            super.processDecodes(fc);
        }
        else {
            getDetailLevelToProcess(fc).processDecodes(fc);
        }
    }

    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelectDetailRequest(fc)) {
            super.processValidators(fc);
        }
        else {
            getDetailLevelToProcess(fc).processValidators(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelectDetailRequest(fc)) {
            super.processUpdates(fc);
        }
        else {
            getDetailLevelToProcess(fc).processUpdates(fc);
        }
    }

    public MasterDetailLevel getDetailLevelToProcess(final FacesContext fc) {
        if (detailLevelToProcess == null) {
            initDataForLevels(fc);
        }

        return detailLevelToProcess;
    }

    public MasterDetailLevel getDetailLevelToGo(final FacesContext fc) {
        if (detailLevelToGo != null) {
            return detailLevelToGo;
        }

        final String strSelectedLevel = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + SELECTED_LEVEL);
        final String strSelectedStep = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + SELECTED_STEP);

        // selected level != null
        if (strSelectedLevel != null) {
            final int selectedLevel = Integer.parseInt(strSelectedLevel);
            detailLevelToGo = getDetailLevelByLevel(selectedLevel);
            if (detailLevelToGo != null) {
                return detailLevelToGo;
            }

            throw new FacesException("MasterDetailLevel for selected level = " + selectedLevel + " not found.");
        }

        final int step;
        if (strSelectedStep != null) {
            // selected step != null
            step = Integer.parseInt(strSelectedStep);
        }
        else {
            // selected level and selected step are null ==> go to the next level
            step = 1;
        }

        detailLevelToGo = getDetailLevelByStep(step);

        return detailLevelToGo;
    }

    public MasterDetailLevel getDetailLevelByLevel(final int level) {
        for (final UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                final MasterDetailLevel mdl = (MasterDetailLevel) child;
                if (mdl.getLevel() == level) {
                    return mdl;
                }
            }
        }

        return null;
    }

    public boolean isSelectDetailRequest(final FacesContext fc) {
        return fc.getPartialViewContext().isAjaxRequest()
                    && fc.getExternalContext().getRequestParameterMap().containsKey(getClientId(fc) + SELECT_DETAIL_REQUEST);
    }

    public String getPreserveInputs(final FacesContext fc) {
        return fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + PRESERVE_INPUTS);
    }

    public String getResetInputs(final FacesContext fc) {
        return fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + RESET_INPUTS);
    }

    public void updateModel(final FacesContext fc, final MasterDetailLevel mdlToGo) {
        final int levelToGo = mdlToGo.getLevel();
        final ValueExpression levelVE = getValueExpression(PropertyKeys.level.toString());
        if (levelVE != null) {
            // update "level"
            levelVE.setValue(fc.getELContext(), levelToGo);
            getStateHelper().remove(PropertyKeys.level);
        }

        // get component caused this ajax request
        final String source = fc.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM);
        final MasterDetailLevel mdl = getDetailLevelToProcess(fc);

        // get resolved context value
        Object contextValue = null;
        final Map<String, Object> contextValues = (Map<String, Object>) mdl.getAttributes().get(CONTEXT_VALUES);
        if (contextValues != null) {
            contextValue = contextValues.get(RESOLVED_CONTEXT_VALUE + source);
        }

        if (contextValue != null) {
            // update current context value for corresponding MasterDetailLevel
            mdlToGo.getAttributes().put(getClientId(fc) + CURRENT_CONTEXT_VALUE, contextValue);

            final ValueExpression contextValueVE = getValueExpression(PropertyKeys.contextValue.toString());
            if (contextValueVE != null) {
                // update "contextValue"
                contextValueVE.setValue(fc.getELContext(), contextValue);
                getStateHelper().remove(PropertyKeys.contextValue);
            }
        }
    }

    public Object getContextValueFromFlow(final FacesContext fc, final MasterDetailLevel mdl, final boolean includeModel) {
        // try to get context value from internal storage
        final Object contextValue = mdl.getAttributes().get(getClientId(fc) + MasterDetail.CURRENT_CONTEXT_VALUE);
        if (contextValue != null) {
            return contextValue;
        }

        // try to get context value from external storage (e.g. managed bean)
        if (includeModel) {
            return getContextValue();
        }

        return null;
    }

    public BreadCrumb getBreadcrumb() {
        BreadCrumb breadCrumb = null;
        for (final UIComponent child : getChildren()) {
            if (child instanceof BreadCrumb) {
                breadCrumb = (BreadCrumb) child;

                break;
            }
        }

        final MenuModel model = breadCrumb != null ? breadCrumb.getModel() : null;

        if (model != null && model.getElements().isEmpty()) {
            final String clientId = getClientId();
            final String menuItemIdPrefix = getId() + "_bcItem_";

            for (final UIComponent child : getChildren()) {
                if (child instanceof MasterDetailLevel) {
                    final int level = ((MasterDetailLevel) child).getLevel();

                    // create menu item to detail level
                    final DefaultMenuItem menuItem = new DefaultMenuItem();
                    menuItem.setId(menuItemIdPrefix + level);
                    menuItem.setAjax(true);
                    menuItem.setImmediate(true);
                    menuItem.setProcess("@none");
                    menuItem.setUpdate("@parent");

                    // add UIParameter
                    menuItem.setParam(clientId + MasterDetail.SELECT_DETAIL_REQUEST, true);
                    menuItem.setParam(clientId + MasterDetail.CURRENT_LEVEL, -1);
                    menuItem.setParam(clientId + MasterDetail.SELECTED_LEVEL, level);

                    model.getElements().add(menuItem);
                }
            }
        }

        return breadCrumb;
    }

    public void resetCalculatedValues() {
        detailLevelToProcess = null;
        detailLevelToGo = null;
        levelPositionToProcess = -1;
        levelCount = -1;
    }

    private void initDataForLevels(final FacesContext fc) {
        final String strCurrentLevel = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + CURRENT_LEVEL);
        if (strCurrentLevel == null) {
            throw new FacesException("Current level is missing in request.");
        }

        final int currentLevel = Integer.parseInt(strCurrentLevel);
        int count = 0;

        for (final UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                final MasterDetailLevel mdl = (MasterDetailLevel) child;
                count++;

                if (detailLevelToProcess == null && mdl.getLevel() == currentLevel) {
                    detailLevelToProcess = mdl;
                    levelPositionToProcess = count;
                }
            }
        }

        levelCount = count;

        if (detailLevelToProcess == null) {
            throw new FacesException("Current MasterDetailLevel to process not found.");
        }
    }

    private MasterDetailLevel getDetailLevelByStep(final int step) {
        int levelPositionToGo = getLevelPositionToProcess() + step;
        if (levelPositionToGo < 1) {
            levelPositionToGo = 1;
        }
        else if (levelPositionToGo > getLevelCount()) {
            levelPositionToGo = getLevelCount();
        }

        int pos = 0;
        for (final UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                final MasterDetailLevel mdl = (MasterDetailLevel) child;
                pos++;

                if (pos == levelPositionToGo) {
                    return mdl;
                }
            }
        }

        // should not happen
        return null;
    }

    private int getLevelPositionToProcess() {
        if (levelPositionToProcess == -1) {
            initDataForLevels(FacesContext.getCurrentInstance());
        }

        return levelPositionToProcess;
    }

    private int getLevelCount() {
        if (levelCount == -1) {
            initDataForLevels(FacesContext.getCurrentInstance());
        }

        return levelCount;
    }
}
