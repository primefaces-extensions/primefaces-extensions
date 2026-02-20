/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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
package org.primefaces.extensions.component.counter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;

import org.primefaces.extensions.util.Attrs;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;
import org.primefaces.util.WidgetBuilder;

/**
 * <code>Counter</code> component.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
@FacesRenderer(rendererType = Counter.DEFAULT_RENDERER, componentFamily = Counter.COMPONENT_FAMILY)
public class CounterRenderer extends CoreRenderer<Counter> {

    @Override
    public void decode(final FacesContext context, final Counter component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final Counter component) throws IOException {
        encodeMarkup(context, component);
        encodeScript(context, component);
    }

    private void encodeScript(final FacesContext context, final Counter component) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final Locale locale = component.calculateLocale();
        final DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(locale);

        String groupingSeparator = component.getSeparator();
        String decimalSeparator = component.getDecimal();
        if (LangUtils.isBlank(groupingSeparator)) {
            groupingSeparator = String.valueOf(formatter.getDecimalFormatSymbols().getGroupingSeparator());
        }
        if (LangUtils.isBlank(decimalSeparator)) {
            decimalSeparator = String.valueOf(formatter.getDecimalFormatSymbols().getDecimalSeparator());
        }

        wb.init("ExtCounter", component)
                    .attr("start", component.getStart())
                    .attr("end", component.getEnd())
                    .attr("decimals", component.getDecimals())
                    .attr("duration", component.getDuration())
                    .attr("useGrouping", component.isUseGrouping())
                    .attr("useEasing", component.isUseEasing())
                    .attr("smartEasingThreshold", component.getSmartEasingThreshold())
                    .attr("smartEasingAmount", component.getSmartEasingAmount())
                    .attr("separator", groupingSeparator)
                    .attr("decimal", decimalSeparator)
                    .attr("prefix", component.getPrefix())
                    .attr("suffix", component.getSuffix())
                    .attr("autoStart", component.isAutoStart());

        if (!LangUtils.isBlank(component.getOnstart())) {
            wb.callback("onstart", "function()", component.getOnstart());
        }
        if (!LangUtils.isBlank(component.getOnend())) {
            wb.callback("onend", "function()", component.getOnend());
        }

        encodeClientBehaviors(context, component);

        wb.finish();
    }

    private void encodeMarkup(final FacesContext context, final Counter component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String styleClass = getStyleClassBuilder(context)
                    .add(Counter.STYLE_CLASS)
                    .add(component.getStyleClass())
                    .build();

        writer.startElement("span", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        writer.writeAttribute(Attrs.STYLE,
                    (!component.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + component.getStyle(),
                    Attrs.STYLE);
        writer.endElement("span");
    }
}
