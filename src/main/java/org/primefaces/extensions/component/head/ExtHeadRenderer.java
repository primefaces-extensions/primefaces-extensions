/**
 * Copyright 2011-2017 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * Renderer for the {@link ExtHead} component which extends PF
 * {@link org.primefaces.renderkit.HeadRenderer}.
 * <p>
 * Ordering of rendered resources:
 * </p>
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
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class ExtHeadRenderer extends org.primefaces.renderkit.HeadRenderer {

   @Override
   public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
      final ResponseWriter writer = context.getResponseWriter();
      final ExtHead extHead = (ExtHead) component;

      // encode title and shortcut icon
      encodeTitle(extHead, writer);
      encodeShortcutIcon(extHead, writer);

      super.encodeEnd(context, component);
   }

   private void encodeTitle(final ExtHead extHead, final ResponseWriter writer) throws IOException {
      if (extHead.getTitle() != null) {
         writer.startElement("title", null);
         writer.write(extHead.getTitle());
         writer.endElement("title");
      }
   }

   private void encodeShortcutIcon(final ExtHead extHead, final ResponseWriter writer) throws IOException {
      if (extHead.getShortcutIcon() != null) {
         writer.startElement("link", null);
         writer.writeAttribute("rel", "shortcut icon", null);
         writer.writeAttribute("href", extHead.getShortcutIcon(), null);
         writer.endElement("link");
      }
   }
}
