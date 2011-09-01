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
 */
package org.primefaces.extensions.component.head;

import java.io.IOException;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.util.Constants;

/**
 * Renderer for the {@link Head} component.
 *
 * @author Thomas Andraschko
 * @author Oleg Varaksin
 * @since 0.2
 */
public class HeadRenderer extends org.primefaces.renderkit.HeadRenderer {

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement("head", component);

		final UIComponent first = component.getFacet("before");
		if (first != null) {
			first.encodeAll(context);
		}

		// Theme
		String theme = null;
		final String themeParamValue = context.getExternalContext().getInitParameter(Constants.THEME_PARAM);

		if (themeParamValue != null) {
			final ELContext elContext = context.getELContext();
			final ExpressionFactory expressionFactory =
				context.getApplication().getExpressionFactory();
			final ValueExpression ve =
				expressionFactory.createValueExpression(elContext, themeParamValue, String.class);

			theme = (String) ve.getValue(elContext);
		}

		if (theme == null || theme.equalsIgnoreCase("sam")) {
			encodeTheme(context, "primefaces", "themes/sam/theme.css");
		} else if (!theme.equalsIgnoreCase("none")) {
			encodeTheme(context, "primefaces-" + theme, "theme.css");
		}

		// Resources
		final UIViewRoot viewRoot = context.getViewRoot();
		final List<UIComponent> resources = viewRoot.getComponentResources(context, "head");
		for (UIComponent resource : resources) {
			writer.write("\n");
			resource.encodeAll(context);
		}
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final Head head = (Head) component;

		final UIComponent last = component.getFacet("after");
		if (last != null) {
			last.encodeAll(context);
		}

		if (head.getTitle() != null) {
			writer.startElement("title", null);
			writer.write(head.getTitle());
			writer.endElement("title");
		}

		if (head.getShortcutIcon() != null) {
			writer.startElement("link", null);
			writer.writeAttribute("rel", "shortcut icon", null);
			writer.writeAttribute("href", head.getShortcutIcon(), null);
			writer.endElement("link");
		}

		writer.endElement("head");
	}
}
