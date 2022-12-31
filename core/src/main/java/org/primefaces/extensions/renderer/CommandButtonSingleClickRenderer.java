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
package org.primefaces.extensions.renderer;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionListener;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandbutton.CommandButtonRenderer;
import org.primefaces.util.Constants;

/**
 * {@link CommandButton} renderer disabling the button while action is processed for buttons that are using Ajax.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0
 * @deprecated PF12 has a disableOnAjax attribute
 */
@Deprecated
public class CommandButtonSingleClickRenderer extends CommandButtonRenderer {

    @Override
    protected void encodeMarkup(FacesContext context, CommandButton button) throws IOException {
        if (isEligible(button)) {
            final String widgetVar = button.resolveWidgetVar(context);
            final String onClick = getAttributeValue(context, button, "onclick");
            final String onComplete = getAttributeValue(context, button, "oncomplete");
            button.setOnclick(prefix(onClick, getToggleJS(widgetVar, false)));
            button.setOncomplete(prefix(onComplete, getToggleJS(widgetVar, true)));
        }
        super.encodeMarkup(context, button);
    }

    protected boolean isEligible(final CommandButton button) {
        final ActionListener[] listeners = button.getActionListeners();
        return button.isAjax()
                    && button.isRendered()
                    && (button.getActionExpression() != null || (listeners != null && listeners.length > 0))
                    && !button.isDisabled()
                    && !isConfirmation(button);
    }

    /**
     * Check for ConfirmDialog or ConfirmPopup buttons.
     *
     * @param button the button to inspect
     * @return true if confirmation button
     */
    protected boolean isConfirmation(final CommandButton button) {
        final String styleClass = button.getStyleClass();
        return styleClass != null && styleClass.contains("ui-confirm");
    }

    protected String getToggleJS(final String widgetVar, final boolean enabled) {
        return String.format("var w=PrimeFaces.widgets['%s'];if(w){w.%sable();};", widgetVar, enabled ? "en" : "dis");
    }

    protected String getAttributeValue(final FacesContext context, final CommandButton button, final String attribute) {
        final ValueExpression ve = button.getValueExpression(attribute);
        if (ve != null) {
            return (String) ve.getValue(context.getELContext());
        }
        String key = attribute + "CommandButtonSingleClickRenderer";
        String value = (String) button.getAttributes().get(key);
        if (value == null) {
            value = (String) button.getAttributes().get(attribute);
            button.getAttributes().put(key, value == null ? Constants.EMPTY_STRING : value);
        }
        return value;
    }

    protected String prefix(final String base, final String prefix) {
        return base == null ? prefix : prefix + base;
    }

}
