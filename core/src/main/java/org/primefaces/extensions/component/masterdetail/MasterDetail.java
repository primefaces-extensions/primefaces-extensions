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
package org.primefaces.extensions.component.masterdetail;

import java.util.Map;

import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.PartialViewContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.PostRestoreStateEvent;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * <code>MasterDetail</code> component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.2
 */
@FacesComponent(value = MasterDetail.COMPONENT_TYPE, namespace = MasterDetail.COMPONENT_FAMILY)
@FacesComponentInfo(description = "Master-detail navigation with levels and optional breadcrumb.")
@ResourceDependency(library = org.primefaces.extensions.util.Constants.LIBRARY, name = "primefaces-extensions.css")
public class MasterDetail extends MasterDetailBaseImpl {

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

    private MasterDetailLevel detailLevelToProcess;
    private MasterDetailLevel detailLevelToGo;
    private int levelPositionToProcess = -1;
    private int levelCount = -1;

    @Override
    public void processEvent(final ComponentSystemEvent event) {
        super.processEvent(event);

        final FacesContext fc = FacesContext.getCurrentInstance();
        if (!(event instanceof PostRestoreStateEvent) || !isSelectDetailRequest(fc)) {
            return;
        }

        final PartialViewContext pvc = fc.getPartialViewContext();
        if (pvc.getRenderIds().isEmpty()) {
            pvc.getRenderIds().add(getClientId(fc));
        }

        final MasterDetailLevel mdl = getDetailLevelToProcess(fc);
        final Object contextValue = getContextValueFromFlow(fc, mdl, true);
        final String contextVar = mdl.getContextVar();
        if (LangUtils.isNotBlank(contextVar) && contextValue != null) {
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

        final String strSelectedLevel = fc.getExternalContext().getRequestParameterMap()
                    .get(getClientId(fc) + SELECTED_LEVEL);
        final String strSelectedStep = fc.getExternalContext().getRequestParameterMap()
                    .get(getClientId(fc) + SELECTED_STEP);

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
            step = Integer.parseInt(strSelectedStep);
        }
        else {
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
                    && fc.getExternalContext().getRequestParameterMap()
                                .containsKey(getClientId(fc) + SELECT_DETAIL_REQUEST);
    }

    public String getPreserveInputs(final FacesContext fc) {
        return fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + PRESERVE_INPUTS);
    }

    public String getResetInputs(final FacesContext fc) {
        return fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + RESET_INPUTS);
    }

    public void updateModel(final FacesContext fc, final MasterDetailLevel mdlToGo) {
        final int levelToGo = mdlToGo.getLevel();
        final ValueExpression levelVE = getValueExpression(PropertyKeys.level.name());
        if (levelVE != null) {
            levelVE.setValue(fc.getELContext(), levelToGo);
            getStateHelper().remove(PropertyKeys.level);
        }

        final String source = fc.getExternalContext().getRequestParameterMap()
                    .get(Constants.RequestParams.PARTIAL_SOURCE_PARAM);
        final MasterDetailLevel mdl = getDetailLevelToProcess(fc);

        Object contextValue = null;
        final Map<String, Object> contextValues = (Map<String, Object>) mdl.getAttributes().get(CONTEXT_VALUES);
        if (contextValues != null) {
            contextValue = contextValues.get(RESOLVED_CONTEXT_VALUE + source);
        }

        if (contextValue != null) {
            mdlToGo.getAttributes().put(getClientId(fc) + CURRENT_CONTEXT_VALUE, contextValue);

            final ValueExpression contextValueVE = getValueExpression(PropertyKeys.contextValue.name());
            if (contextValueVE != null) {
                contextValueVE.setValue(fc.getELContext(), contextValue);
                getStateHelper().remove(PropertyKeys.contextValue);
            }
        }
    }

    public Object getContextValueFromFlow(final FacesContext fc, final MasterDetailLevel mdl,
                final boolean includeModel) {
        final Object contextValue = mdl.getAttributes().get(getClientId(fc) + MasterDetail.CURRENT_CONTEXT_VALUE);
        if (contextValue != null) {
            return contextValue;
        }

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

                    final DefaultMenuItem menuItem = new DefaultMenuItem();
                    menuItem.setId(menuItemIdPrefix + level);
                    menuItem.setAjax(true);
                    menuItem.setImmediate(true);
                    menuItem.setProcess("@none");
                    menuItem.setUpdate("@parent");

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
        final String strCurrentLevel = fc.getExternalContext().getRequestParameterMap()
                    .get(getClientId(fc) + CURRENT_LEVEL);
        if (strCurrentLevel == null) {
            throw new FacesException("Current level is missing in request.");
        }

        final int currentLevel = Integer.parseInt(strCurrentLevel);
        int count = 0;

        for (final UIComponent child : getChildren()) {
            if (child instanceof MasterDetailLevel) {
                final MasterDetailLevel mdl = (MasterDetailLevel) child;
                count++;

                if (detailLevelToProcess == null && mdl.getLevel() != null && mdl.getLevel() == currentLevel) {
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

    @Override
    public Object saveState(final FacesContext context) {
        detailLevelToGo = null;
        detailLevelToProcess = null;
        levelCount = -1;
        levelPositionToProcess = -1;

        return super.saveState(context);
    }
}
