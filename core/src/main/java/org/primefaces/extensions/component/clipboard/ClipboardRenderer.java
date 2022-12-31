/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.component.clipboard;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.ExtLangUtils;
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
        wb.init("ExtClipboard", clipboard);
        wb.attr("action", ExtLangUtils.lowerCase(clipboard.getAction()));
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
