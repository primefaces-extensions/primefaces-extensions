/*
 * Copyright (c) 2011-2022 PrimeFaces Extensions
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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
public class CounterRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Counter counter = (Counter) component;

        encodeMarkup(context, counter);
        encodeScript(context, counter);
    }

    private void encodeScript(final FacesContext context, final Counter counter) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        final Locale locale = counter.calculateLocale();
        final DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance(locale);

        String groupingSeparator = counter.getSeparator();
        String decimalSeparator = counter.getDecimal();
        if (LangUtils.isBlank(groupingSeparator)) {
            groupingSeparator = String.valueOf(formatter.getDecimalFormatSymbols().getGroupingSeparator());
        }
        if (LangUtils.isBlank(decimalSeparator)) {
            decimalSeparator = String.valueOf(formatter.getDecimalFormatSymbols().getDecimalSeparator());
        }

        wb.init("ExtCounter", counter)
                    .attr("start", counter.getStart())
                    .attr("end", counter.getEnd())
                    .attr("decimals", counter.getDecimals())
                    .attr("duration", counter.getDuration())
                    .attr("useGrouping", counter.isUseGrouping())
                    .attr("useEasing", counter.isUseEasing())
                    .attr("smartEasingThreshold", counter.getSmartEasingThreshold())
                    .attr("smartEasingAmount", counter.getSmartEasingAmount())
                    .attr("separator", groupingSeparator)
                    .attr("decimal", decimalSeparator)
                    .attr("prefix", counter.getPrefix())
                    .attr("suffix", counter.getSuffix())
                    .attr("autoStart", counter.isAutoStart());

        if (!LangUtils.isBlank(counter.getOnstart())) {
            wb.callback("onstart", "function()", counter.getOnstart());
        }
        if (!LangUtils.isBlank(counter.getOnend())) {
            wb.callback("onend", "function()", counter.getOnend());
        }

        encodeClientBehaviors(context, counter);

        wb.finish();
    }

    private void encodeMarkup(final FacesContext context, final Counter counter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String styleClass = getStyleClassBuilder(context)
                    .add(Counter.STYLE_CLASS)
                    .add(counter.getStyleClass())
                    .build();

        writer.startElement("span", counter);
        writer.writeAttribute("id", counter.getClientId(context), "id");
        writer.writeAttribute(Attrs.CLASS, styleClass, "styleClass");
        writer.writeAttribute(Attrs.STYLE,
                    (!counter.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + counter.getStyle(),
                    Attrs.STYLE);
        writer.endElement("span");
    }

}
