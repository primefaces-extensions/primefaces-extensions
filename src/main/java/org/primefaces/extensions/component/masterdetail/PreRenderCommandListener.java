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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;

/**
 * {@link ComponentSystemEventListener} for an <code>UICommand</code> component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class PreRenderCommandListener implements ComponentSystemEventListener, Serializable {

	private static final long serialVersionUID = 20111121L;

	@Override
	public void processEvent(final ComponentSystemEvent event) {
		UICommand source = (UICommand) event.getComponent();

		// find master detail component
		MasterDetail masterDetail = findMasterDetail(source);
		if (masterDetail == null) {
			throw new FacesException("MasterDetail was not found. SelectDetailLevel can be only used inside of MasterDetail.");
		}

		// find master detail level component
		MasterDetailLevel masterDetailLevel = findMasterDetailLevel(source);
		if (masterDetailLevel == null) {
			throw new FacesException(
			    "MasterDetailLevel was not found. SelectDetailLevel can be only used inside of MasterDetailLevel.");
		}

		final FacesContext fc = FacesContext.getCurrentInstance();
		final String masterDetailClientId = masterDetail.getClientId(fc);

		// attach parameters dynamically
		// required basic parameters
		addUIParameter(fc, source, masterDetailClientId + MasterDetail.SELECT_DETAIL_REQUEST, true);
		addUIParameter(fc, source, masterDetailClientId + MasterDetail.CURRENT_LEVEL, masterDetailLevel.getLevel());

		// selected level
		ValueExpression selectedLevelVE =
		    (ValueExpression) source.getAttributes().get(MasterDetail.SELECTED_LEVEL_VALUE_EXPRESSION);
		Object selectedLevel = selectedLevelVE != null ? selectedLevelVE.getValue(fc.getELContext()) : null;
		if (selectedLevel != null) {
			addUIParameter(fc, source, masterDetailClientId + MasterDetail.SELECTED_LEVEL, selectedLevel);
		} else {
			removeUIParameter(source, masterDetailClientId + MasterDetail.SELECTED_LEVEL);
		}

		// selected step
		ValueExpression selectedStepVE =
		    (ValueExpression) source.getAttributes().get(MasterDetail.SELECTED_STEP_VALUE_EXPRESSION);
		Object selectedStep = selectedStepVE != null ? selectedStepVE.getValue(fc.getELContext()) : null;
		if (selectedStep != null) {
			addUIParameter(fc, source, masterDetailClientId + MasterDetail.SELECTED_STEP, selectedStep);
		} else {
			removeUIParameter(source, masterDetailClientId + MasterDetail.SELECTED_STEP);
		}

		// skip processing flag
		Boolean skipProcessing = (Boolean) source.getAttributes().get(MasterDetail.SKIP_PROCESSING);
		if ((skipProcessing != null && skipProcessing) || source.isImmediate()) {
			addUIParameter(fc, source, masterDetailClientId + MasterDetail.SKIP_PROCESSING_REQUEST, true);
		} else {
			removeUIParameter(source, masterDetailClientId + MasterDetail.SKIP_PROCESSING_REQUEST);
		}

		ValueExpression contextValueVE =
		    (ValueExpression) source.getAttributes().get(MasterDetail.CONTEXT_VALUE_VALUE_EXPRESSION);
		if (contextValueVE == null) {
			return;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> contextValues =
		    (Map<String, Object>) masterDetailLevel.getAttributes().get(MasterDetail.CONTEXT_VALUES);
		if (contextValues == null) {
			contextValues = new HashMap<String, Object>();
		}

		// resolve context value and make it available in MasterDetail component
		Object contextValue = contextValueVE.getValue(fc.getELContext());
		if (contextValue != null) {
			contextValues.put(MasterDetail.RESOLVED_CONTEXT_VALUE + source.getClientId(fc), contextValue);
		} else {
			contextValues.remove(MasterDetail.RESOLVED_CONTEXT_VALUE + source.getClientId(fc));
		}

		masterDetailLevel.getAttributes().put(MasterDetail.CONTEXT_VALUES, contextValues);
	}

	private static MasterDetail findMasterDetail(final UIComponent component) {
		UIComponent parent = component.getParent();

		while (parent != null) {
			if (parent instanceof MasterDetail) {
				return (MasterDetail) parent;
			}

			parent = parent.getParent();
		}

		return null;
	}

	private MasterDetailLevel findMasterDetailLevel(final UIComponent component) {
		UIComponent parent = component.getParent();

		while (parent != null) {
			if (parent instanceof MasterDetailLevel) {
				return (MasterDetailLevel) parent;
			}

			parent = parent.getParent();
		}

		return null;
	}

	private void addUIParameter(final FacesContext fc, final UIComponent source, final String paramName,
	                            final Object paramValue) {
		for (UIComponent child : source.getChildren()) {
			if (child instanceof UIParameter && paramName.equals(((UIParameter) child).getName())) {
				// update value
				((UIParameter) child).setValue(paramValue);

				return;
			}
		}

		UIParameter uiParameter = new UIParameter();
		uiParameter.setId(fc.getViewRoot().createUniqueId(fc, null));
		uiParameter.setName(paramName);
		uiParameter.setValue(paramValue);
		source.getChildren().add(uiParameter);
	}

	private void removeUIParameter(final UIComponent source, final String paramName) {
		List<UIComponent> childs = source.getChildren();
		if (childs == null || childs.isEmpty()) {
			return;
		}

		for (UIComponent child : childs) {
			if (child instanceof UIParameter && paramName.equals(((UIParameter) child).getName())) {
				childs.remove(child);

				break;
			}
		}
	}
}
