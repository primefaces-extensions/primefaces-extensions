/*
 * Copyright 2011 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.masterdetail;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.extensions.util.StringUtils;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.primefaces.renderkit.CoreRenderer;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Renderer for the {@link MasterDetail} component.
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class MasterDetailRenderer extends CoreRenderer {

    private static final String FACET_HEADER = "header";
    private static final String FACET_FOOTER = "footer";

    @Override
    public void encodeEnd(final FacesContext fc, UIComponent component) throws IOException {
        MasterDetail masterDetail = (MasterDetail) component;
        MasterDetailLevel mdl;

        if (masterDetail.isSelectDetailRequest(fc)) {
            // component has been navigated via SelectDetailLevel
            if (fc.isValidationFailed()) {
                mdl = masterDetail.getDetailLevelToProcess(fc);
            } else {
                mdl = masterDetail.getDetailLevelToGo(fc);
            }
        } else {
            // component has been navigated from outside, e.g. GET request or POST update from another component
            mdl = masterDetail.getDetailLevelByLevel(masterDetail.getLevel());
        }

        // resolve all possible context values for this master detail level
        evaluateContextValues(fc, mdl);

        // render MasterDetailLevel 
        encodeMarkup(fc, masterDetail, mdl);
    }

    protected void encodeMarkup(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();
        String clientId = masterDetail.getClientId(fc);
        String styleClass = masterDetail.getStyleClass() == null ? "ui-master-detail" : "ui-master-detail " + masterDetail.getStyleClass();

        writer.startElement("div", masterDetail);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (masterDetail.getStyle() != null) {
            writer.writeAttribute("style", masterDetail.getStyle(), "style");
        }

        // render header
        encodeFacet(fc, masterDetail, FACET_HEADER);

        if (masterDetail.isShowBreadcrumb()) {
            // get breadcrumb and its current model
            BreadCrumb breadcrumb = getBreadcrumb(masterDetail);
            if (breadcrumb == null) {
                throw new FacesException("BreadCrumb component was not found below MasterDetail.");
            }

            MenuModel model = buildBreadcrumbModel(fc, masterDetail, mdl);

            // remove not up-to-date children
            int kidsCount = breadcrumb.getChildCount();
            for (int i = 0; i < kidsCount; i++) {
                breadcrumb.getChildren().remove(0);
            }

            // add new children
            for (UIComponent kid : model.getMenuItems()) {
                breadcrumb.getChildren().add(kid);
            }

            // render breadcrumb
            breadcrumb.encodeAll(fc);
        }

        // render container for MasterDetailLevel
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_detaillevel", "id");
        writer.writeAttribute("class", "ui-master-detail-level", null);

        // try to get context value if contextVar exists
        Object contextValue = null;
        String contextVar = mdl.getContextVar();
        if (StringUtils.isNotBlank(contextVar)) {
            contextValue = fc.getAttributes().get(masterDetail.getClientId(fc) + "_curContextValue");
            if (contextValue == null) {
                contextValue = getContextValueFromFlow((FlowLevel[]) masterDetail.getFlow(), mdl.getLevel());
            }
        }

        if (contextValue != null) {
            Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            requestMap.put(contextVar, contextValue);
        }

        // render MasterDetailLevel
        mdl.encodeAll(fc);

        if (contextValue != null) {
            fc.getExternalContext().getRequestMap().remove(contextVar);
        }

        writer.endElement("div");

        // render footer
        encodeFacet(fc, masterDetail, FACET_FOOTER);
        writer.endElement("div");
    }

    protected void encodeFacet(final FacesContext fc, final UIComponent component, final String name) throws IOException {
        final UIComponent facet = component.getFacet(name);
        if (facet != null) {
            facet.encodeAll(fc);
        }
    }

    protected void evaluateContextValues(final FacesContext fc, final MasterDetailLevel mdl) {
        @SuppressWarnings("unchecked") Map<String, ValueExpression> contextValueVEs = (Map<String, ValueExpression>) mdl.getAttributes().get("contextValueVEs");
        if (contextValueVEs == null || contextValueVEs.isEmpty()) {
            return;
        }

        Map<String, Object> contextValues = new HashMap<String, Object>();
        for (Map.Entry<String, ValueExpression> entry : contextValueVEs.entrySet()) {
            contextValues.put(entry.getKey(), entry.getValue().getValue(fc.getELContext()));
        }

        mdl.getAttributes().put("contextValues", contextValues);
    }

    protected MenuModel buildBreadcrumbModel(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdlToRender) {
        // create model from scratch
        MenuModel model = new DefaultMenuModel();
        FlowLevel[] flowLevels = (FlowLevel[]) masterDetail.getFlow();

        for (UIComponent child : masterDetail.getChildren()) {
            if (child instanceof MasterDetailLevel) {
                MasterDetailLevel mdl = (MasterDetailLevel) child;

                // create a new menu item and add to the model
                MenuItem menuItem = createMenuItem(fc, masterDetail, mdl, getContextValueFromFlow(flowLevels, mdl.getLevel()));
                model.addMenuItem(menuItem);

                if (mdl.getLevel() == mdlToRender.getLevel()) {
                    break;
                }
            }
        }

        return model;
    }

    protected Object getContextValueFromFlow(final FlowLevel[] flowLevels, final int level) {
        if (flowLevels == null || flowLevels.length < 1) {
            return null;
        }

        for (FlowLevel fl : flowLevels) {
            if (fl.getLevel() == level) {
                return fl.getContextValue();
            }
        }

        return null;
    }

    protected MenuItem createMenuItem(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl, final Object contextValue) {
        String clientId = masterDetail.getClientId(fc);
        MenuItem menuItem = new MenuItem();
        menuItem.setId(masterDetail.getId() + "_bcItem_" + mdl.getLevel());

        String contextVar = mdl.getContextVar();
        boolean putContext = (StringUtils.isNotBlank(contextVar) && contextValue != null);
        if (putContext) {
            Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            requestMap.put(contextVar, contextValue);
        }

        menuItem.setValue(mdl.getLevelLabel());
        menuItem.setDisabled(mdl.isLevelDisabled());

        if (putContext) {
            fc.getExternalContext().getRequestMap().remove(contextVar);
        }

        menuItem.setAjax(true);
        menuItem.setImmediate(true);
        menuItem.setProcess("@none");
        menuItem.setUpdate(null);

        UIParameter uiParameter = new UIParameter();
        uiParameter.setName(clientId + "_selectDetailRequest");
        uiParameter.setValue(true);
        menuItem.getChildren().add(uiParameter);

        uiParameter = new UIParameter();
        uiParameter.setName(clientId + "_currentLevel");
        uiParameter.setValue(masterDetail.getDetailLevelToProcess(fc).getLevel());
        menuItem.getChildren().add(uiParameter);

        uiParameter = new UIParameter();
        uiParameter.setName(clientId + "_selectedLevel");
        uiParameter.setValue(mdl.getLevel());
        menuItem.getChildren().add(uiParameter);

        return menuItem;
    }

    protected BreadCrumb getBreadcrumb(final MasterDetail masterDetail) {
        for (UIComponent child : masterDetail.getChildren()) {
            if (child instanceof BreadCrumb) {
                return (BreadCrumb) child;
            }
        }

        return null;
    }

    @Override
    public void encodeChildren(final FacesContext fc, UIComponent component) throws IOException {
        // rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
