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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;

import org.primefaces.component.api.AjaxSource;

/**
 * {@link ComponentHandler} for the <code>MasterDetailLevel</code>.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class MasterDetailLevelTagHandler extends ComponentHandler {

	private final TagAttribute level;

	public MasterDetailLevelTagHandler(final ComponentConfig config) {
		super(config);
		this.level = getRequiredAttribute("level");
	}

	@Override
	public void onComponentPopulated(final FaceletContext ctx, final UIComponent c, final UIComponent parent) {
		if (!isNew(parent)) {
			return;
		}

		for (UIComponent child : c.getChildren()) {
			if (!(child instanceof UICommand)) {
				continue;
			}

			UICommand uiCommand = (UICommand) child;

			if (!isAjaxifiedCommand(uiCommand)) {
				// only ajaxified command components should be treated
				continue;
			}

			if (isPreRenderCommandListenerRegistred(uiCommand)) {
				// navigation takes place explicitly by SelectDetailLevelTagHandler ==> nothing to do
				continue;
			}

			// add step = 0
			ValueExpression selectedStepVE =
			    ctx.getFacesContext().getApplication().getExpressionFactory().createValueExpression(0, int.class);
			uiCommand.getAttributes().put(MasterDetail.SELECTED_STEP_VALUE_EXPRESSION, selectedStepVE);

			if (parent instanceof AjaxSource) {
				AjaxSource ajaxSource = (AjaxSource) uiCommand;
				if (ajaxSource.getProcess() == null) {
					// set process to @none because PrimeFaces set it to @all
					try {
						Method setProcessMethod = ajaxSource.getClass().getMethod("setProcess", new Class[] {String.class});
						setProcessMethod.invoke(ajaxSource, "@none");
					} catch (Exception e) {
						// ignore
					}
				} else if ("@none".equals(ajaxSource.getProcess().trim())) {
					// mark navigation for skipping all JSF phases except rendering
					uiCommand.getAttributes().put(MasterDetail.SKIP_PROCESSING, true);
				}
			}

			// register PreRenderCommandListener
			uiCommand.subscribeToEvent(PreRenderComponentEvent.class, new PreRenderCommandListener());
		}
	}

	boolean isAjaxifiedCommand(final UICommand uiCommand) {
		// check for ajax attribute
		try {
			Method isAjaxMethod = uiCommand.getClass().getMethod("isAjax");
			boolean isAjax = (Boolean) isAjaxMethod.invoke(uiCommand);
			if (isAjax) {
				return true;
			}
		} catch (Exception e) {
			// ignore, not all command components have ajax attribute
		}

		// check for attached f:ajax / p:ajax
		Collection<List<ClientBehavior>> behaviors = uiCommand.getClientBehaviors().values();
		if (behaviors == null || behaviors.isEmpty()) {
			return false;
		}

		for (List<ClientBehavior> listBehaviors : behaviors) {
			for (ClientBehavior clientBehavior : listBehaviors) {
				if (clientBehavior instanceof javax.faces.component.behavior.AjaxBehavior
				    || clientBehavior instanceof org.primefaces.component.behavior.ajax.AjaxBehavior) {
					return true;
				}
			}
		}

		return false;
	}

	boolean isPreRenderCommandListenerRegistred(final UICommand uiCommand) {
		List<SystemEventListener> systemEventListeners = uiCommand.getListenersForEventClass(PreRenderComponentEvent.class);
		if (systemEventListeners != null && !systemEventListeners.isEmpty()) {
			for (SystemEventListener systemEventListener : systemEventListeners) {
				if (systemEventListener instanceof PreRenderCommandListener) {
					return true;
				}
			}
		}

		return false;
	}
}
