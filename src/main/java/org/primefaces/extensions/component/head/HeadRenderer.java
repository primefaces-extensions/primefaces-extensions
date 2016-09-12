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

package org.primefaces.extensions.component.head;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final Head head = (Head) component;

		// encode title and shortcut icon
		encodeTitle(head, writer);
		encodeShortcutIcon(head, writer);
        
        super.encodeEnd(context, component);
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
}
