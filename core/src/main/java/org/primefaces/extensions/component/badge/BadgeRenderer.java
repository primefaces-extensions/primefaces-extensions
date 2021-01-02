/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
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
