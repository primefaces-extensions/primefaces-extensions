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

import java.io.IOException;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.extensions.util.StringUtils;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for the {@link MasterDetail} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
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

			masterDetail.updateModel(fc, mdl);
		} else {
			// component has been navigated from outside, e.g. GET request or POST update from another component
			mdl = masterDetail.getDetailLevelByLevel(masterDetail.getLevel());
		}

		// render MasterDetailLevel
		encodeMarkup(fc, masterDetail, mdl);

		// reset calculated values
		masterDetail.resetCalculatedValues();
	}

	protected void encodeMarkup(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl)
	    throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = masterDetail.getClientId(fc);
		String styleClass =
		    masterDetail.getStyleClass() == null ? "ui-master-detail" : "ui-master-detail " + masterDetail.getStyleClass();

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
			contextValue = getContextValueFromFlow((FlowLevel[]) masterDetail.getFlow(), clientId, mdl);
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

	protected MenuModel buildBreadcrumbModel(final FacesContext fc, final MasterDetail masterDetail,
	                                         final MasterDetailLevel mdlToRender) {
		// create model from scratch
		MenuModel model = new DefaultMenuModel();
		FlowLevel[] flowLevels = (FlowLevel[]) masterDetail.getFlow();
		String clientId = masterDetail.getClientId(fc);

		for (UIComponent child : masterDetail.getChildren()) {
			if (child instanceof MasterDetailLevel) {
				MasterDetailLevel mdl = (MasterDetailLevel) child;

				// create a new menu item and add to the model
				MenuItem menuItem =
				    createMenuItem(fc, masterDetail, mdl, getContextValueFromFlow(flowLevels, clientId, mdl),
				                   mdlToRender.getLevel());
				model.addMenuItem(menuItem);

				if (mdl.getLevel() == mdlToRender.getLevel()) {
					break;
				}
			}
		}

		return model;
	}

	protected Object getContextValueFromFlow(final FlowLevel[] flowLevels, final String mdClientId, final MasterDetailLevel mdl) {
		// try to get context value from internal storage
		Object contextValue = mdl.getAttributes().get(mdClientId + MasterDetail.CURRENT_CONTEXT_VALUE);
		if (contextValue != null) {
			return contextValue;
		}

		// try to get context value from external "flow" state
		if (flowLevels != null && flowLevels.length > 0) {
			final int level = mdl.getLevel();
			for (FlowLevel fl : flowLevels) {
				if (fl.getLevel() == level) {
					return fl.getContextValue();
				}
			}
		}

		return null;
	}

	protected MenuItem createMenuItem(final FacesContext fc, final MasterDetail masterDetail, final MasterDetailLevel mdl,
	                                  final Object contextValue, final int currentLevel) {
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

		final String menuItemId = menuItem.getId();

		UIParameter uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_sdr");
		uiParameter.setName(clientId + MasterDetail.SELECT_DETAIL_REQUEST);
		uiParameter.setValue(true);
		menuItem.getChildren().add(uiParameter);

		uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_cl");
		uiParameter.setName(clientId + MasterDetail.CURRENT_LEVEL);
		uiParameter.setValue(currentLevel);
		menuItem.getChildren().add(uiParameter);

		uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_sl");
		uiParameter.setName(clientId + MasterDetail.SELECTED_LEVEL);
		uiParameter.setValue(mdl.getLevel());
		menuItem.getChildren().add(uiParameter);

		uiParameter = new UIParameter();
		uiParameter.setId(menuItemId + "_sp");
		uiParameter.setName(clientId + MasterDetail.SKIP_PROCESSING_REQUEST);
		uiParameter.setValue(true);
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
