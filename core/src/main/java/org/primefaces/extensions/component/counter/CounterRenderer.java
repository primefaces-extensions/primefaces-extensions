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
package org.primefaces.extensions.component.counter;

import java.io.IOException;

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

        wb.init("ExtCounter", counter)
                    .attr("start", counter.getStart())
                    .attr("end", counter.getEnd())
                    .attr("decimals", counter.getDecimals())
                    .attr("duration", counter.getDuration())
                    .attr("useGrouping", counter.isUseGrouping())
                    .attr("useEasing", counter.isUseEasing())
                    .attr("smartEasingThreshold", counter.getSmartEasingThreshold())
                    .attr("smartEasingAmount", counter.getSmartEasingAmount())
                    .attr("separator", counter.getSeparator())
                    .attr("decimal", counter.getDecimal())
                    .attr("prefix", counter.getPrefix())
                    .attr("suffix", counter.getSuffix())
                    .attr("autoStart", counter.isAutoStart());

        if (!LangUtils.isValueBlank(counter.getOnstart())) {
            wb.callback("onstart", "function()", counter.getOnstart());
        }
        if (!LangUtils.isValueBlank(counter.getOnend())) {
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
        writer.writeAttribute(Attrs.STYLE, (!counter.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + counter.getStyle(), Attrs.STYLE);
        writer.endElement("span");
    }

}
