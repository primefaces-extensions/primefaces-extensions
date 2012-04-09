/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.common;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * {@link TagHandler} for the <code>MethodSignature</code> tag.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class MethodSignatureTagHandler extends TagHandler {

	private final String parameters;

	public MethodSignatureTagHandler(final TagConfig config) {
		super(config);

		final TagAttribute parametersTag = this.getRequiredAttribute("parameters");
		parameters = parametersTag.getValue();
	}

	public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
		//do nothing
	}

	public String getParameters() {
		return parameters;
	}
}
