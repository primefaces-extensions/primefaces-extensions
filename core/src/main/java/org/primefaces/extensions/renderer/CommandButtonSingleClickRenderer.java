/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.renderer;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandbutton.CommandButtonRenderer;
import org.primefaces.util.Constants;

/**
 * {@link CommandButton} renderer disabling the button while action is processed for buttons that are using Ajax.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0
 */
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
        return button.isAjax()
                    && button.isRendered()
                    && button.getActionExpression() != null
                    && !button.isDisabled()
                    && !isConfirmation(button);
    }

    protected boolean isConfirmation(final CommandButton button) {
        final String styleClass = button.getStyleClass();
        return styleClass != null && styleClass.contains("ui-confirmdialog");
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
