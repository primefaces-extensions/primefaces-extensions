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

import org.primefaces.component.api.AjaxSource;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link TagHandler} for the <code>SelectDetailLevel</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class SelectDetailLevelTagHandler extends TagHandler {

	private final TagAttribute contextValue;
	private final TagAttribute level;
	private final TagAttribute step;

	public SelectDetailLevelTagHandler(final TagConfig config) {
		super(config);
		this.contextValue = getAttribute("contextValue");
		this.level = getAttribute("level");
		this.step = getAttribute("step");
	}

    @Override
	public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
		if (!(parent instanceof UICommand)) {
			throw new FacesException("SelectDetailLevel must be inside an UICommand.");
		}

		if (!ComponentHandler.isNew(parent)) {
			return;
		}

		FacesContext facesContext = ctx.getFacesContext();

		// find master detail level component
		MasterDetailLevel masterDetailLevel = findMasterDetailLevel(parent);
		if (masterDetailLevel == null) {
			throw new FacesException(
			    "MasterDetailLevel was not found. SelectDetailLevel can be only used inside of MasterDetailLevel.");
		}

		// find master detail component
		MasterDetail masterDetail = findMasterDetail(masterDetailLevel);
		if (masterDetail == null) {
			throw new FacesException("MasterDetail was not found. MasterDetailLevel can be only used inside of MasterDetail.");
		}

		// get value expression for contextValue attribute of this tag handler
		ValueExpression contextValueVE = null;
		if (contextValue != null) {
			contextValueVE = contextValue.getValueExpression(ctx, Object.class);

			// store value expression for contextValue in the masterDetailLevel component
			@SuppressWarnings("unchecked")
			Map<String, ValueExpression> contextValueVEs =
			    (Map<String, ValueExpression>) masterDetailLevel.getAttributes().get("contextValueVEs");
			if (contextValueVEs == null) {
				contextValueVEs = new HashMap<String, ValueExpression>();
			}

			contextValueVEs.put("contextValue_" + parent.getClientId(facesContext), contextValueVE);
			masterDetailLevel.getAttributes().put("contextValueVEs", contextValueVEs);
		}

		if (parent instanceof AjaxSource) {
			AjaxSource ajaxSource = (AjaxSource) parent;
			if (ajaxSource.getProcess() == null) {
				// set process to @none because PrimeFaces set it to @all
				try {
					Method setProcessMethod = ajaxSource.getClass().getMethod("setProcess", new Class[] {String.class});
					setProcessMethod.invoke(ajaxSource, "@none");
				} catch (Exception e) {
					// ignore
				}
			}
		}

		// get value expression for level attribute of this tag handler
		ValueExpression selectedLevelVE = null;
		if (level != null) {
			selectedLevelVE = level.getValueExpression(ctx, int.class);
		}

		// get value expression for step attribute of this tag handler
		ValueExpression selectedStepVE = null;
		if (step != null) {
			selectedStepVE = step.getValueExpression(ctx, int.class);
		}

		String clientId = masterDetail.getClientId(facesContext);

		// attach parameters
		UIParameter uiParameter = new UIParameter();
		uiParameter.setName(clientId + "_selectDetailRequest");
		uiParameter.setValue(true);

		uiParameter = new UIParameter();
		uiParameter.setName(clientId + "_currentLevel");
		uiParameter.setValue(masterDetailLevel.getLevel());

		if (selectedLevelVE != null) {
			uiParameter = new UIParameter();
			uiParameter.setName(clientId + "_selectedLevel");
			uiParameter.setValueExpression("value", selectedLevelVE);
		}

		if (selectedStepVE != null) {
			uiParameter = new UIParameter();
			uiParameter.setName(clientId + "_selectedStep");
			uiParameter.setValueExpression("value", selectedStepVE);
		}
	}

	public static MasterDetail findMasterDetail(final UIComponent component) {
		UIComponent parent = component.getParent();

		while (parent != null) {
			if (parent instanceof MasterDetail) {
				return (MasterDetail) parent;
			}

			parent = parent.getParent();
		}

		return null;
	}

	public static MasterDetailLevel findMasterDetailLevel(final UIComponent component) {
		UIComponent parent = component.getParent();

		while (parent != null) {
			if (parent instanceof MasterDetailLevel) {
				return (MasterDetailLevel) parent;
			}

			parent = parent.getParent();
		}

		return null;
	}
}
