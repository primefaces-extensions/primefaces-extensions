/*
 * Copyright 2011-2015 PrimeFaces Extensions
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

package org.primefaces.extensions.behavior.javascript;

import javax.faces.application.Application;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagHandler;

import org.primefaces.behavior.base.AbstractBehaviorHandler;

/**
 * {@link BehaviorHolderAttachedObjectHandler} and {@link TagHandler} implementation for the {@link JavascriptBehavior}.
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class JavascriptBehaviorHandler extends AbstractBehaviorHandler<JavascriptBehavior> {

	private final TagAttribute execute;
	private final TagAttribute disabled;

	public JavascriptBehaviorHandler(final BehaviorConfig config) {
		super(config);

		this.execute = this.getAttribute(JavascriptBehavior.PropertyKeys.execute.name());
		this.disabled = this.getAttribute(JavascriptBehavior.PropertyKeys.disabled.name());
	}

	protected JavascriptBehavior createBehavior(final FaceletContext faceletContext, final String eventName) {
		final Application application = faceletContext.getFacesContext().getApplication();
		final JavascriptBehavior behavior = (JavascriptBehavior) application.createBehavior(JavascriptBehavior.BEHAVIOR_ID);

		setBehaviorAttribute(faceletContext, behavior, this.disabled, Boolean.class);
		setBehaviorAttribute(faceletContext, behavior, this.execute, String.class);

		return behavior;
	}
}
