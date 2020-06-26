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
package org.primefaces.extensions.component.calculator;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.primefaces.expression.SearchExpressionFacade;
import org.primefaces.extensions.util.ExtLangUtils;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for the {@link Calculator} component.
 *
 * @author Melloware mellowaredev@gmail.com
 * @since 6.1
 */
public class CalculatorRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Calculator calculator = (Calculator) component;
        encodeScript(context, calculator);
    }

    private void encodeScript(final FacesContext context, final Calculator calculator) throws IOException {
        String target = SearchExpressionFacade.resolveClientIds(context, calculator, calculator.getFor());
        if (isValueBlank(target)) {
            target = calculator.getParent().getClientId(context);
        }

        final UIComponent targetComponent = SearchExpressionFacade.resolveComponent(context, calculator, target);
        if (!(targetComponent instanceof UIInput)) {
            throw new FacesException("Calculator must use for=\"target\" or be nested inside an input!");
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("ExtCalculator", calculator.resolveWidgetVar(), calculator.getClientId(context));
        wb.attr("target", target);
        wb.attr("showOn", ExtLangUtils.lowerCase(calculator.getShowOn()));
        wb.attr("layout", ExtLangUtils.lowerCase(calculator.getLayout()));
        wb.attr("precision", calculator.getPrecision());
        wb.attr("locale", calculator.calculateLocale().toString());
        wb.attr("isRTL", ComponentUtils.isRTL(context, calculator));
        wb.attr("calculatorClass", calculator.getStyleClass());

        if (calculator.getOnopen() != null) {
            // Define a callback function before the panel is opened
            wb.callback("onOpen", "function(value, inst)", calculator.getOnopen());
        }
        if (calculator.getOnclose() != null) {
            // Define a callback function when the panel is closed
            wb.callback("onClose", "function(value, inst)", calculator.getOnclose());
        }
        if (calculator.getOnbutton() != null) {
            // Define a callback function when a button is activated
            wb.callback("onButton", "function(label, value, inst)", calculator.getOnbutton());
        }

        encodeClientBehaviors(context, calculator);

        wb.finish();
    }

}
