/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
package org.primefaces.extensions.component.badge;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Badge} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 7.1
 */
public class BadgeRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Badge badge = (Badge) component;
        encodeScript(context, badge);
    }

    protected void encodeScript(FacesContext context, Badge badge) throws IOException {
        String target = isValueBlank(badge.getFor())
                    ? badge.getParent().getClientId(context)
                    : resolveTarget(context, badge);

        final UIComponent targetComponent = SearchExpressionFacade.resolveComponent(context, badge, target);
        if (!targetComponent.isRendered()) {
            return;
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtBadge", badge);
        wb.attr("target", target);
        wb.attr("color", badge.getColor());
        wb.attr("position", badge.getPosition());
        if (badge.getContent() != null) {
            wb.attr("content", badge.getContent());
        }

        wb.finish();
    }

    protected String resolveTarget(FacesContext context, Badge badge) {
        String target = SearchExpressionFacade.resolveClientIds(context, badge, badge.getFor());
        if (isValueBlank(target)) {
            throw new FacesException("Badge for=\"target\" resolved to null");
        }
        return target;
    }

}
