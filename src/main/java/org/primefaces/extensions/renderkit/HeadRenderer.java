/*
 * Copyright 2011 Thomas Andraschko.
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
package org.primefaces.extensions.renderkit;

import java.io.IOException;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.extensions.application.PrimeFacesExtensionsResourceHandler;

public class HeadRenderer extends org.primefaces.renderkit.HeadRenderer {

	@Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        String theme = null;
        String themeParamValue = context.getExternalContext().getInitParameter("primefaces.extensions.THEME");

        if (themeParamValue != null) {
            ELContext elContext = context.getELContext();
            ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
            ValueExpression ve = expressionFactory.createValueExpression(elContext, themeParamValue, String.class);

            theme = (String) ve.getValue(elContext);
        }

        if (theme == null || theme.equalsIgnoreCase("default")) {
            encodeTheme(context, PrimeFacesExtensionsResourceHandler.LIBRARY, "themes/default/theme.css");
        }
        else if (!theme.equalsIgnoreCase("none")) {
            encodeTheme(context, "primefaces-extensions-" + theme, "theme.css");
        }
    }
}
