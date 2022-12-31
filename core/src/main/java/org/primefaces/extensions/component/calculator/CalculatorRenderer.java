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
        wb.init("ExtCalculator", calculator);
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
