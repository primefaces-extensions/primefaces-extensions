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
package org.primefaces.extensions.renderer;

import java.io.IOException;
import javax.faces.context.FacesContext;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandbutton.CommandButtonRenderer;

/**
 * {@link CommandButton} renderer disabling the button while action is processed for buttons that are using Ajax.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 8.0
 */
public class CommandButtonSingleClickRenderer extends CommandButtonRenderer {

    @Override
    protected void encodeMarkup(FacesContext context, CommandButton button) throws IOException {
        if (isToggleable(button)) {
            String widgetVar = button.resolveWidgetVar(context);
            button.setOnclick(prefix(button.getOnclick(), toggle(widgetVar, false)));
            button.setOncomplete(prefix(button.getOncomplete(), toggle(widgetVar, true)));
        }
        super.encodeMarkup(context, button);
    }

    protected boolean isToggleable(final CommandButton button) {
        if (!button.isRendered() || button.isDisabled() || isConfirmation(button)) {
            return false;
        }
        return button.isAjax();
    }

    protected boolean isConfirmation(final CommandButton button) {
        String styleClass = button.getStyleClass();
        return styleClass != null
               && (styleClass.contains("ui-confirmdialog-yes")
                   || styleClass.contains("ui-confirmdialog-no"));
    }

    protected String toggle(final String widgetVar, final boolean enabled) {
        return String.format("PF('%s').%sable();", widgetVar, enabled ? "en" : "dis");
    }

    protected String prefix(final String base, final String prefix) {
        return base == null ? prefix : prefix + base;
    }

}
