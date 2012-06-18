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

package org.primefaces.extensions.component.resetinput;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import org.apache.commons.lang3.StringUtils;

import org.primefaces.extensions.util.TagUtils;

/**
 * {@link TagHandler} for the <code>ResetInput</code> component.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class ResetInputTagHandler extends TagHandler {

	private final TagAttribute forValue;
	private final TagAttribute event;

	public ResetInputTagHandler(final TagConfig config) {
		super(config);
		this.forValue = super.getRequiredAttribute("for");
		this.event = super.getAttribute("event");
	}

	public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
		if (!ComponentHandler.isNew(parent)) {
			return;
		}

		final String strFor = forValue.getValue(ctx);
		if (StringUtils.isBlank(strFor)) {
			return;
		}

		if (parent instanceof ActionSource) {
			((ActionSource) parent).addActionListener(new ResetInputListener(strFor));

			return;
		} else if (parent instanceof ClientBehaviorHolder) {
			// find attached f:ajax / p:ajax corresponding to supported events
			Collection<List<ClientBehavior>> clientBehaviors =
			    TagUtils.getClientBehaviors(ctx, event, (ClientBehaviorHolder) parent);
			if (clientBehaviors == null || clientBehaviors.isEmpty()) {
				return;
			}

			for (List<ClientBehavior> listBehaviors : clientBehaviors) {
				for (ClientBehavior clientBehavior : listBehaviors) {
					if (clientBehavior instanceof org.primefaces.component.behavior.ajax.AjaxBehavior) {
						((org.primefaces.component.behavior.ajax.AjaxBehavior) clientBehavior).addAjaxBehaviorListener(
						    new ResetInputListener(strFor));
					} else if (clientBehavior instanceof javax.faces.component.behavior.AjaxBehavior) {
						((javax.faces.component.behavior.AjaxBehavior) clientBehavior).addAjaxBehaviorListener(
						    new ResetInputListener(strFor));
					}
				}
			}

			return;
		}

		throw new FacesException("ResetInput must be attached to a command or ajaxified component");
	}
}
