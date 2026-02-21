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
package org.primefaces.extensions.component.timer;

import jakarta.el.MethodExpression;
import jakarta.faces.component.UIComponentBase;

import org.primefaces.cdk.api.FacesComponentBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.component.api.AjaxSource;
import org.primefaces.component.api.StyleAware;
import org.primefaces.component.api.Widget;

/**
 * <code>Timer</code> component base class.
 *
 * @author f.strazzullo
 */
@FacesComponentBase
public abstract class TimerBase extends UIComponentBase implements Widget, AjaxSource, StyleAware {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Timer";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimerRenderer";
    public static final String STYLE_CLASS = "ui-timer ui-widget ui-widget-header ui-corner-all";

    public TimerBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Property(description = "When true, timer executes once and stops.", defaultValue = "true")
    public abstract boolean isSingleRun();

    @Property(description = "Timeout in seconds.", defaultValue = "10")
    public abstract int getTimeout();

    @Property(description = "Interval in milliseconds.", defaultValue = "1000")
    public abstract int getInterval();

    @Property(description = "Component(s) to be updated with ajax.")
    public abstract String getUpdate();

    @Property(description = "A method expression to invoke by polling.")
    public abstract MethodExpression getListener();

    @Property(description = "Determines the phaseId, true means APPLY_REQUEST_VALUES, false means INVOKE_APPLICATION.", defaultValue = "false")
    public abstract boolean isImmediate();

    @Property(description = "JavaScript callback before timer completion.")
    public abstract String getOntimercomplete();

    @Property(description = "JavaScript callback on each timer step.")
    public abstract String getOntimerstep();

    @Property(description = "JavaScript callback before ajax request begins.")
    public abstract String getOnstart();

    @Property(description = "JavaScript callback when ajax request is completed.")
    public abstract String getOncomplete();

    @Property(description = "Component id(s) to process partially instead of whole view.")
    public abstract String getProcess();

    @Property(description = "JavaScript callback when ajax request fails.")
    public abstract String getOnerror();

    @Property(description = "JavaScript callback when ajax request succeeds.")
    public abstract String getOnsuccess();

    @Property(description = "Global ajax requests are listened by ajaxStatus.", defaultValue = "true")
    public abstract boolean isGlobal();

    @Property(description = "Delays ajax requests.")
    public abstract String getDelay();

    @Property(description = "When true, ajax requests are not queued.", defaultValue = "false")
    public abstract boolean isAsync();

    @Property(description = "If true, timer starts automatically.", defaultValue = "true")
    public abstract boolean isAutoStart();

    @Property(description = "Enables partial submit.", defaultValue = "false")
    public abstract boolean isPartialSubmit();

    @Property(description = "Reset values during ajax update.", defaultValue = "false")
    public abstract boolean isResetValues();

    @Property(description = "Display format.", defaultValue = "")
    public abstract String getFormat();

    @Property(description = "Skips auto update when true.", defaultValue = "false")
    public abstract boolean isIgnoreAutoUpdate();

    @Property(description = "Controls visibility.", defaultValue = "true")
    public abstract boolean isVisible();

    @Property(description = "Timer direction, true is forward.", defaultValue = "false")
    public abstract boolean isForward();

    @Property(description = "String or Locale representing user locale.")
    public abstract Object getLocale();

    @Property(description = "Client-side function used to format the remaining duration.", defaultValue = "")
    public abstract String getFormatFunction();

    @Property(description = "Partial submit filter selector.")
    public abstract String getPartialSubmitFilter();

    @Property(description = "Form to serialize for ajax requests.")
    public abstract String getForm();

    @Property(description = "Title rendered on the timer element.", defaultValue = "")
    public abstract String getTitle();

    @Property(description = "Ignore unresolved update targets.", defaultValue = "false")
    public abstract boolean isIgnoreComponentNotFound();
}
