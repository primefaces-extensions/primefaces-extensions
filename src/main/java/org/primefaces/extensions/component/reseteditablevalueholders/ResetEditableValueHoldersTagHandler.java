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

package org.primefaces.extensions.component.reseteditablevalueholders;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PostValidateEvent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * {@link TagHandler} for the <code>ResetEditableValueHolders</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class ResetEditableValueHoldersTagHandler extends TagHandler {

	private final TagAttribute value;

    public ResetEditableValueHoldersTagHandler(final TagConfig config) {
        super(config);
        this.value = super.getRequiredAttribute("value");
    }

	@Override
	public void apply(final FaceletContext context, final UIComponent parent) throws IOException {
		if (!(parent instanceof UICommand)) {
			throw new FacesException("ResetEditableValueHolders must be inside an UICommand.");
		}

		if (ComponentHandler.isNew(parent)) {
			final UICommand source = (UICommand) parent;
			final ComponentSystemEventListener listener =
				new ResetEditableValueHoldersListener(source, value.getValue(context));

			parent.subscribeToEvent(PostValidateEvent.class, listener);
		}
	}
}
