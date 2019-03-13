/**
 * Copyright 2011-2019 PrimeFaces Extensions
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
package org.primefaces.extensions.component.clipboard;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Clipboard} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
public class ClipboardRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Clipboard clipboard = (Clipboard) component;
        encodeScript(context, clipboard);
    }

    private void encodeScript(final FacesContext context, final Clipboard clipboard) throws IOException {
        String trigger = SearchExpressionFacade.resolveClientIds(context, clipboard, clipboard.getTrigger());
        if (isValueBlank(trigger)) {
            trigger = clipboard.getParent().getClientId(context);
        }

        final String target = SearchExpressionFacade.resolveClientIds(context, clipboard, clipboard.getTarget());

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtClipboard", clipboard.resolveWidgetVar(), clipboard.getClientId(context));
        wb.attr("action", StringUtils.lowerCase(clipboard.getAction()));
        wb.attr("trigger", trigger);
        wb.attr("target", target);
        wb.attr("text", clipboard.getText());

        if (clipboard.getOnsuccess() != null) {
            // Define a callback function if cut/copy succeeds
            wb.callback("onSuccess", "function(e)", clipboard.getOnsuccess());
        }
        if (clipboard.getOnerror() != null) {
            // Define a callback function if cut/copy fails
            wb.callback("onError", "function(e)", clipboard.getOnerror());
        }

        encodeClientBehaviors(context, clipboard);

        wb.finish();
    }

}
