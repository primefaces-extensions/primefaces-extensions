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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostRestoreStateEvent;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.util.Constants;

/**
 * <code>MasterDetail</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
@ListenerFor(systemEventClass = PostRestoreStateEvent.class)
@ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css")
public class MasterDetail extends UIComponentBase {

	public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.MasterDetail";
	public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
	private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.MasterDetailRenderer";
	private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

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

	private MasterDetailLevel detailLevelToProcess;
	private MasterDetailLevel detailLevelToGo;
	private int levelPositionToProcess = -1;
	private int levelCount = -1;

	/**
	 * Properties that are tracked by state saving.
	 *
	 * @author  Oleg Varaksin / last modified by $Author$
	 * @version $Revision$
	 */
	protected enum PropertyKeys {

		level,
		contextValue,
		selectLevelListener,
		showBreadcrumb,
		showAllBreadcrumbItems,
		breadcrumbAboveHeader,
		style,
		styleClass;

		private String toString;

		PropertyKeys(String toString) {
			this.toString = toString;
		}

		PropertyKeys() {
		}

		@Override
		public String toString() {
			return ((this.toString != null) ? this.toString : super.toString());
		}
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

	public void setLevel(int level) {
		setAttribute(PropertyKeys.level, level);
	}

	public Object getContextValue() {
		return getStateHelper().eval(PropertyKeys.contextValue, null);
	}

	public void setContextValue(Object contextValue) {
		setAttribute(PropertyKeys.contextValue, contextValue);
	}

	public MethodExpression getSelectLevelListener() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.selectLevelListener, null);
	}

	public void setSelectLevelListener(MethodExpression selectLevelListener) {
		setAttribute(PropertyKeys.selectLevelListener, selectLevelListener);
	}

	public boolean isShowBreadcrumb() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showBreadcrumb, true);
	}

	public void setShowBreadcrumb(boolean showBreadcrumb) {
		setAttribute(PropertyKeys.showBreadcrumb, showBreadcrumb);
	}

	public boolean isShowAllBreadcrumbItems() {
		return (Boolean) getStateHelper().eval(PropertyKeys.showAllBreadcrumbItems, false);
	}

	public void setShowAllBreadcrumbItems(boolean showAllBreadcrumbItems) {
		setAttribute(PropertyKeys.showAllBreadcrumbItems, showAllBreadcrumbItems);
	}

	public boolean isBreadcrumbAboveHeader() {
		return (Boolean) getStateHelper().eval(PropertyKeys.breadcrumbAboveHeader, true);
	}

	public void setBreadcrumbAboveHeader(boolean breadcrumbAboveHeader) {
		setAttribute(PropertyKeys.breadcrumbAboveHeader, breadcrumbAboveHeader);
	}

	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(String style) {
		setAttribute(PropertyKeys.style, style);
	}

	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public void setStyleClass(String styleClass) {
		setAttribute(PropertyKeys.styleClass, styleClass);
	}

	public void setAttribute(PropertyKeys property, Object value) {
		getStateHelper().put(property, value);

		@SuppressWarnings("unchecked")
		List<String> setAttributes =
		    (List<String>) this.getAttributes().get("javax.faces.component.UIComponentBase.attributesThatAreSet");
		if (setAttributes == null) {
			final String cname = this.getClass().getName();
			if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
				setAttributes = new ArrayList<String>(6);
				this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
			}
		}

		if (setAttributes != null && value == null) {
			final String attributeName = property.toString();
			final ValueExpression ve = getValueExpression(attributeName);
			if (ve == null) {
				setAttributes.remove(attributeName);
			} else if (!setAttributes.contains(attributeName)) {
				setAttributes.add(attributeName);
			}
		}
	}

	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
		super.processEvent(event);

		FacesContext fc = FacesContext.getCurrentInstance();
		if (!(event instanceof PostRestoreStateEvent) || !isSelectDetailRequest(fc)) {
			return;
		}

		String clienId = this.getClientId(fc);
		PartialViewContext pvc = fc.getPartialViewContext();

		/* automatic processing is not possible to unify in Mojarra and MyFaces.
		Collection<String> executeIds = pvc.getExecuteIds();
		int size = executeIds.size();
		if (!isSkipProcessing(fc) && size == 0) {
		    pvc.getExecuteIds().add(clienId);
		}*/

		// update the MasterDetail component automatically
		if (pvc.getRenderIds().isEmpty()) {
			pvc.getRenderIds().add(clienId);
		}

		MasterDetailLevel mdl = getDetailLevelToProcess(fc);
		Object contextValue = getContextValueFromFlow(fc, mdl, true);
		String contextVar = mdl.getContextVar();
		if (StringUtils.isNotBlank(contextVar) && contextValue != null) {
			Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
			requestMap.put(contextVar, contextValue);
		}
	}

	@Override
	public void processDecodes(FacesContext fc) {
		if (!isSelectDetailRequest(fc)) {
			super.processDecodes(fc);
		} else {
			getDetailLevelToProcess(fc).processDecodes(fc);
		}
	}

	@Override
	public void processValidators(FacesContext fc) {
		if (!isSelectDetailRequest(fc)) {
			super.processValidators(fc);
		} else {
			getDetailLevelToProcess(fc).processValidators(fc);
		}
	}

	@Override
	public void processUpdates(FacesContext fc) {
		if (!isSelectDetailRequest(fc)) {
			super.processUpdates(fc);
		} else {
			getDetailLevelToProcess(fc).processUpdates(fc);
		}
	}

	public MasterDetailLevel getDetailLevelToProcess(FacesContext fc) {
		if (detailLevelToProcess == null) {
			initDataForLevels(fc);
		}

		return detailLevelToProcess;
	}

	public MasterDetailLevel getDetailLevelToGo(FacesContext fc) {
		if (detailLevelToGo != null) {
			return detailLevelToGo;
		}

		final String strSelectedLevel = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + SELECTED_LEVEL);
		final String strSelectedStep = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + SELECTED_STEP);

		// selected level != null
		if (strSelectedLevel != null) {
			int selectedLevel = Integer.valueOf(strSelectedLevel);
			detailLevelToGo = getDetailLevelByLevel(selectedLevel);
			if (detailLevelToGo != null) {
				return detailLevelToGo;
			}

			throw new FacesException("MasterDetailLevel for selected level = " + selectedLevel + " not found.");
		}

		int step;
		if (strSelectedStep != null) {
			// selected step != null
			step = Integer.valueOf(strSelectedStep);
		} else {
			// selected level and selected step are null ==> go to the next level
			step = 1;
		}

		detailLevelToGo = getDetailLevelByStep(step);

		return detailLevelToGo;
	}

	public MasterDetailLevel getDetailLevelByLevel(int level) {
		for (UIComponent child : getChildren()) {
			if (child instanceof MasterDetailLevel) {
				MasterDetailLevel mdl = (MasterDetailLevel) child;
				if (mdl.getLevel() == level) {
					return mdl;
				}
			}
		}

		return null;
	}

	public boolean isSelectDetailRequest(FacesContext fc) {
		return fc.getPartialViewContext().isAjaxRequest()
		       && fc.getExternalContext().getRequestParameterMap().containsKey(getClientId(fc) + SELECT_DETAIL_REQUEST);
	}

	public String getPreserveInputs(FacesContext fc) {
		return fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + PRESERVE_INPUTS);
	}

	public String getResetInputs(FacesContext fc) {
		return fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + RESET_INPUTS);
	}

	public void updateModel(FacesContext fc, MasterDetailLevel mdlToGo) {
		final int levelToGo = mdlToGo.getLevel();
		ValueExpression levelVE = this.getValueExpression(PropertyKeys.level.toString());
		if (levelVE != null) {
			// update "level"
			levelVE.setValue(fc.getELContext(), levelToGo);
			getStateHelper().remove(PropertyKeys.level);
		}

		// get component caused this ajax request
		final String source = fc.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM);
		MasterDetailLevel mdl = getDetailLevelToProcess(fc);

		// get resolved context value
		Object contextValue = null;
		@SuppressWarnings("unchecked")
		Map<String, Object> contextValues = (Map<String, Object>) mdl.getAttributes().get(CONTEXT_VALUES);
		if (contextValues != null) {
			contextValue = contextValues.get(RESOLVED_CONTEXT_VALUE + source);
		}

		if (contextValue != null) {
			// update current context value for corresponding MasterDetailLevel
			mdlToGo.getAttributes().put(getClientId(fc) + CURRENT_CONTEXT_VALUE, contextValue);

			ValueExpression contextValueVE = this.getValueExpression(PropertyKeys.contextValue.toString());
			if (contextValueVE != null) {
				// update "contextValue"
				contextValueVE.setValue(fc.getELContext(), contextValue);
				getStateHelper().remove(PropertyKeys.contextValue);
			}
		}
	}

	public Object getContextValueFromFlow(FacesContext fc, MasterDetailLevel mdl, boolean includeModel) {
		// try to get context value from internal storage
		Object contextValue = mdl.getAttributes().get(this.getClientId(fc) + MasterDetail.CURRENT_CONTEXT_VALUE);
		if (contextValue != null) {
			return contextValue;
		}

		// try to get context value from external storage (e.g. managed bean)
		if (includeModel) {
			return this.getContextValue();
		}

		return null;
	}

	public BreadCrumb getBreadcrumb() {
		BreadCrumb breadCrumb = null;
		for (UIComponent child : this.getChildren()) {
			if (child instanceof BreadCrumb) {
				breadCrumb = (BreadCrumb) child;

				break;
			}
		}

		if (breadCrumb != null && breadCrumb.getChildCount() < 1) {
			String clientId = getClientId();
			String menuItemIdPrefix = getId() + "_bcItem_";

			for (UIComponent child : getChildren()) {
				if (child instanceof MasterDetailLevel) {
					int level = ((MasterDetailLevel) child).getLevel();

					// create menu item to detail level
					MenuItem menuItem = new MenuItem();
					menuItem.setId(menuItemIdPrefix + level);
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
					uiParameter.setValue(-1); // set dummy value and update it in renderer
					menuItem.getChildren().add(uiParameter);

					uiParameter = new UIParameter();
					uiParameter.setId(menuItemId + "_sl");
					uiParameter.setName(clientId + MasterDetail.SELECTED_LEVEL);
					uiParameter.setValue(level);
					menuItem.getChildren().add(uiParameter);

					breadCrumb.getChildren().add(menuItem);
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

	private void initDataForLevels(FacesContext fc) {
		final String strCurrentLevel = fc.getExternalContext().getRequestParameterMap().get(getClientId(fc) + CURRENT_LEVEL);
		if (strCurrentLevel == null) {
			throw new FacesException("Current level is missing in request.");
		}

		int currentLevel = Integer.valueOf(strCurrentLevel);
		int count = 0;

		for (UIComponent child : getChildren()) {
			if (child instanceof MasterDetailLevel) {
				MasterDetailLevel mdl = (MasterDetailLevel) child;
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

	private MasterDetailLevel getDetailLevelByStep(int step) {
		int levelPositionToGo = getLevelPositionToProcess() + step;
		if (levelPositionToGo < 1) {
			levelPositionToGo = 1;
		} else if (levelPositionToGo > getLevelCount()) {
			levelPositionToGo = getLevelCount();
		}

		int pos = 0;
		for (UIComponent child : getChildren()) {
			if (child instanceof MasterDetailLevel) {
				MasterDetailLevel mdl = (MasterDetailLevel) child;
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
