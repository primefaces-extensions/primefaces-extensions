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

package org.primefaces.extensions.component.dynaform;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for {@link DynaForm} component.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since   0.5
 */
public class DynaFormRenderer extends CoreRenderer {

	private static final String FACET_PRE_INCLUDE_REGULAR = "preIncludeRegular";
	private static final String FACET_POST_INCLUDE_REGULAR = "postIncludeRegular";
	private static final String FACET_PRE_INCLUDE_EXTENDED = "preIncludeExtended";
	private static final String FACET_POST_INCLUDE_EXTENDED = "postIncludeExtended";

	@Override
	public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
		encodeMarkup(fc, component);
		encodeScript(fc, component);
	}

	protected void encodeMarkup(FacesContext fc, UIComponent component) throws IOException {
		DynaForm dynaForm = (DynaForm) component;

		// TODO
	}

	protected void encodeScript(FacesContext fc, UIComponent component) throws IOException {
		DynaForm dynaForm = (DynaForm) component;
		ResponseWriter writer = fc.getResponseWriter();
		String clientId = dynaForm.getClientId(fc);

		startScript(writer, clientId);

		writer.write("$(function() {");
		writer.write("PrimeFacesExt.cw('DynaForm','" + dynaForm.resolveWidgetVar() + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",autoSubmit:" + dynaForm.isAutoSubmit());
		writer.write("});});");
		endScript(writer);
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		//Rendering happens on encodeEnd
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
