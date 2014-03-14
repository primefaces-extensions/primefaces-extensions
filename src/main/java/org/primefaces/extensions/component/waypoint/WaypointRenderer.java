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

package org.primefaces.extensions.component.waypoint;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * WaypointRenderer.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class WaypointRenderer extends CoreRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		decodeBehaviors(context, component);
	}

	@Override
	public void encodeEnd(FacesContext fc, UIComponent component) throws IOException {
		ResponseWriter writer = fc.getResponseWriter();
		Waypoint waypoint = (Waypoint) component;
		final String clientId = waypoint.getClientId(fc);

		// try to get context (which scrollable element the waypoint belongs to and acts within)
		String context = SearchExpressionFacade.resolveComponentsForClient(fc, waypoint, waypoint.getForContext());

		String target = SearchExpressionFacade.resolveComponentsForClient(fc, waypoint, waypoint.getFor(), SearchExpressionFacade.PARENT_FALLBACK);

		final String widget = waypoint.resolveWidgetVar();

		startScript(writer, clientId);
		writer.write("$(function(){");

		writer.write("PrimeFacesExt.cw('Waypoint', '" + widget + "',{");
		writer.write("id:'" + clientId + "'");
		writer.write(",target:'" + target + "'");

		if (context != null) {
			writer.write(",context:'" + context + "'");
		}

		if (waypoint.getOffset() != null) {
			writer.write(",offset:" + waypoint.getOffset());
		}

		writer.write(",continuous:" + waypoint.isContinuous());
		writer.write(",enabled:" + waypoint.isEnabled());
        writer.write(",horizontal:" + waypoint.isHorizontal());
		writer.write(",triggerOnce:" + waypoint.isTriggerOnce());

		encodeClientBehaviors(fc, waypoint);

		writer.write("})});");
		endScript(writer);
	}
}
