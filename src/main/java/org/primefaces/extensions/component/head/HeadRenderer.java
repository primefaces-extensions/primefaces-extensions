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

package org.primefaces.extensions.component.head;

import java.io.IOException;
import java.util.ArrayList;
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
 * <p>Ordering of rendered resources:</p>
 *
 * <pre>
   - first facet if defined
   - Theme CSS
   - JSF, PF, PF Extensions CSS resources
   - middle facet if defined
   - JSF, PF, PF Extensions JS resources
   - title
   - shortcut icon
   - h:head content (encoded by super class at encodeChildren)
   - last facet if defined
 * </pre>
 *
 * @author  Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since   0.2
 */
public class HeadRenderer extends org.primefaces.renderkit.HeadRenderer {

	private static final String FACET_FIRST = "first";
	private static final String FACET_MIDDLE = "middle";
	private static final String FACET_LAST = "last";
	private static final String PREFIX_PRIMEFACES = "primefaces-";
	private static final String THEME_DEFAULT = "aristo";

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final Head head = (Head) component;

		writer.startElement("head", component);

		final List<UIComponent> styles = new ArrayList<UIComponent>();
		final List<UIComponent> scripts = new ArrayList<UIComponent>();
		fillScriptsAndStyles(context, styles, scripts);

		// encode first facet
		encodeFacet(context, component, FACET_FIRST);

		// encode themes
		encodeTheme(context);

		// encode CSS resources
		for (final UIComponent style : styles) {
			style.encodeAll(context);
		}

		// encode middle facet
		encodeFacet(context, component, FACET_MIDDLE);

		// encode JS resources
		for (final UIComponent script : scripts) {
			script.encodeAll(context);
		}

		// encode title and shortcut icon
		encodeTitle(head, writer);
		encodeShortcutIcon(head, writer);
	}

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();

		// encode last facet
		encodeFacet(context, component, FACET_LAST);
		writer.endElement("head");
	}

	private void encodeTheme(final FacesContext context) throws IOException {
		String theme = null;

		final String themeParamValue = context.getExternalContext().getInitParameter(Constants.THEME_PARAM);
		if (themeParamValue == null) {
			theme = THEME_DEFAULT;
		} else {
			final ELContext elContext = context.getELContext();
			final ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
			final ValueExpression ve =
				expressionFactory.createValueExpression(elContext, themeParamValue, String.class);

			theme = (String) ve.getValue(elContext);
		}

		if (theme != null && !theme.equalsIgnoreCase("none")) {
			encodeTheme(context, PREFIX_PRIMEFACES + theme, "theme.css");
		}
	}

	private void encodeFacet(final FacesContext context, final UIComponent component, final String name) throws IOException {
		final UIComponent facet = component.getFacet(name);
		if (facet != null) {
			facet.encodeAll(context);
		}
	}

	private void encodeTitle(final Head head, final ResponseWriter writer) throws IOException {
		if (head.getTitle() != null) {
			writer.startElement("title", null);
			writer.write(head.getTitle());
			writer.endElement("title");
		}
	}

	private void encodeShortcutIcon(final Head head, final ResponseWriter writer) throws IOException {
		if (head.getShortcutIcon() != null) {
			writer.startElement("link", null);
			writer.writeAttribute("rel", "shortcut icon", null);
			writer.writeAttribute("href", head.getShortcutIcon(), null);
			writer.endElement("link");
		}
	}

	private void fillScriptsAndStyles(final FacesContext context, final List<UIComponent> styles,
	                                  final List<UIComponent> scripts) {
		final UIViewRoot viewRoot = context.getViewRoot();
		final List<UIComponent> resources = viewRoot.getComponentResources(context, "head");

		for (final UIComponent resource : resources) {
			final String name = (String) resource.getAttributes().get("name");

			if (name.endsWith(org.primefaces.extensions.util.Constants.EXTENSION_CSS)) {
				styles.add(resource);
			} else if (name.endsWith(org.primefaces.extensions.util.Constants.EXTENSION_JS)) {
				scripts.add(resource);
			}
		}
	}
}
