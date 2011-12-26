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

package org.primefaces.extensions.behavior.blockui;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;

import org.primefaces.extensions.util.ComponentUtils;

/**
 * {@link ClientBehaviorRenderer} implementation for the {@link BlockUIBehavior}.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class BlockUIBehaviorRenderer extends ClientBehaviorRenderer {

	@Override
	public String getScript(final ClientBehaviorContext behaviorContext, final ClientBehavior behavior) {
		final BlockUIBehavior blockUIBehavior = (BlockUIBehavior) behavior;
		if (blockUIBehavior.isDisabled()) {
			return null;
		}

		final FacesContext context = behaviorContext.getFacesContext();
		final UIComponent component = behaviorContext.getComponent();
		final String source = ComponentUtils.escapeJQueryId(component.getParent().getClientId(context));
		String content = null;
		if (blockUIBehavior.getContent() != null) {
			final UIComponent contentComponent = component.findComponent(blockUIBehavior.getContent());
			if (contentComponent == null) {
				throw new FacesException("Cannot find component '" + blockUIBehavior.getContent() + "'.");
			} else {
				content = ComponentUtils.escapeJQueryId(contentComponent.getClientId(context));
			}
		}

		final StringBuilder script = new StringBuilder();
		script.append("return PrimeFacesExt.behavior.BlockUI('");
		script.append(source).append("','");
		script.append(ComponentUtils.findTarget(blockUIBehavior, context)).append("',");
		if (content != null) {
			script.append("'").append(content).append("',");
		} else {
			script.append("null,");
		}

		// TODO

		script.append(");");

		return script.toString();
	}
}
