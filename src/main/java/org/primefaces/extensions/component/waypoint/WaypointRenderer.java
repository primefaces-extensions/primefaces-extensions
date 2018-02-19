/**
 * Copyright 2011-2018 PrimeFaces Extensions
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
 */
package org.primefaces.extensions.component.waypoint;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.expression.SearchExpressionHint;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Waypoint} component.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @since 0.6
 */
public class WaypointRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final Waypoint waypoint = (Waypoint) component;
        encodeScript(fc, waypoint);
    }

    private void encodeScript(final FacesContext fc, final Waypoint waypoint) throws IOException {
        final String context = SearchExpressionFacade.resolveClientIds(fc, waypoint, waypoint.getForContext());
        final String target = SearchExpressionFacade.resolveClientIds(fc, waypoint, waypoint.getFor(),
                    SearchExpressionHint.PARENT_FALLBACK);

        final WidgetBuilder wb = RequestContext.getCurrentInstance().getWidgetBuilder();
        wb.initWithDomReady("ExtWaypoint", waypoint.resolveWidgetVar(), waypoint.getClientId(fc));
        wb.attr("target", target);
        wb.attr("continuous", waypoint.isContinuous());
        wb.attr("enabled", waypoint.isEnabled());
        wb.attr("horizontal", waypoint.isHorizontal());
        wb.attr("triggerOnce", waypoint.isTriggerOnce());

        if (context != null) {
            wb.attr("context", context);
        }

        if (waypoint.getOffset() != null) {
            wb.nativeAttr("offset", waypoint.getOffset());
        }

        encodeClientBehaviors(fc, waypoint);

        wb.finish();
    }
}
