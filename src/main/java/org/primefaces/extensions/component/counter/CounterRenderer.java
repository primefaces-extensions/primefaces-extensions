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

public class CounterRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("No context defined!");
        }

        Counter counter = (Counter) component;

        encodeMarkup(context, counter);
        encodeScript(context, counter);
    }

    private void encodeScript(FacesContext context, Counter counter) throws IOException {
        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.init("ExtCounter", counter.resolveWidgetVar(), counter.getClientId(context))
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

        if (!LangUtils.isValueBlank(counter.getOncountercomplete())) {
            wb.callback("oncountercomplete", "function()", counter.getOncountercomplete());
        }

        wb.finish();
    }

    private void encodeMarkup(FacesContext context, Counter counter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", counter);
        writer.writeAttribute("id", counter.getClientId(context), "id");
        writer.writeAttribute(Attrs.CLASS, Counter.STYLE_CLASS + " " + counter.getStyleClass(), "styleClass");
        writer.writeAttribute(Attrs.STYLE, (!counter.isVisible() ? "display:none;" : Constants.EMPTY_STRING) + counter.getStyle(), Attrs.STYLE);
        writer.endElement("span");
    }

}
