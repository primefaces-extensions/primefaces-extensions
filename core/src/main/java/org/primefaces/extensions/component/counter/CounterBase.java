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

import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesBehaviorEvent;
import org.primefaces.cdk.api.FacesBehaviorEvents;
import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;
import org.primefaces.event.SelectEvent;

/**
 * <code>Counter</code> component base class.
 *
 * @author https://github.com/aripddev
 * @since 8.0.1
 */
@FacesComponentBase
@FacesBehaviorEvents({
            @FacesBehaviorEvent(name = "start", event = SelectEvent.class, description = "Fires when the counter animation starts."),
            @FacesBehaviorEvent(name = "end", event = SelectEvent.class, description = "Fires when the counter animation ends.", defaultEvent = true)
})
public abstract class CounterBase extends UIComponentBase implements Widget, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Counter";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CounterRenderer";

    public CounterBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "Starting value for the counter.", defaultValue = "0.0")
    public abstract Double getStart();

    @Property(description = "Ending value for the counter.")
    public abstract Double getEnd();

    @Property(description = "Number of decimal places.", defaultValue = "0")
    public abstract Integer getDecimals();

    @Property(description = "Animation duration in seconds.", defaultValue = "2")
    public abstract Integer getDuration();

    @Property(description = "Whether to use grouping separators (e.g. 1,000).", defaultValue = "true")
    public abstract boolean isUseGrouping();

    @Property(description = "Whether to use easing in the animation.", defaultValue = "true")
    public abstract boolean isUseEasing();

    @Property(description = "Threshold for smart easing.", defaultValue = "999")
    public abstract Integer getSmartEasingThreshold();

    @Property(description = "Amount for smart easing.", defaultValue = "333")
    public abstract Integer getSmartEasingAmount();

    @Property(description = "Locale for number formatting. Can be a string or java.util.Locale instance.")
    public abstract Object getLocale();

    @Property(description = "Thousands grouping separator character.")
    public abstract String getSeparator();

    @Property(description = "Decimal separator character.")
    public abstract String getDecimal();

    @Property(description = "Text to display before the number.")
    public abstract String getPrefix();

    @Property(description = "Text to display after the number.")
    public abstract String getSuffix();

    @Property(description = "Whether to start the counter automatically.", defaultValue = "true")
    public abstract boolean isAutoStart();

    @Property(description = "Whether the component is initially visible.", defaultValue = "true")
    public abstract boolean isVisible();

    @Property(description = "Client-side callback when the counter animation starts.")
    public abstract String getOnstart();

    @Property(description = "Client-side callback when the counter animation ends.")
    public abstract String getOnend();
}
