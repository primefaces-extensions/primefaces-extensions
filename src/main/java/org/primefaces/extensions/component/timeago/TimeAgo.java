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
package org.primefaces.extensions.component.timeago;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;
import org.primefaces.util.LocaleUtils;

/**
 * <code>TimeAgo</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 */
@ResourceDependencies({
            @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
            @ResourceDependency(library = "primefaces", name = "core.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"),
            @ResourceDependency(library = "primefaces-extensions", name = "timeago/timeago.js"),
})
public class TimeAgo extends UIComponentBase implements Widget {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TimeAgo";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimeAgoRenderer";

    public static final String STYLE_CLASS = "ui-timeago ui-widget";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private Locale appropriateLocale;

    // @formatter:off
    public enum PropertyKeys {
        value,
        widgetVar,
        style,
        styleClass,
        locale,
        titlePattern
    }
    // @formatter:on

    public TimeAgo() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(String value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public void setLocale(final Object locale) {
        getStateHelper().put(PropertyKeys.locale, locale);
    }

    public String getTitlePattern() {
        return (String) getStateHelper().eval(PropertyKeys.titlePattern, null);
    }

    public void setTitlePattern(String titlePattern) {
        getStateHelper().put(PropertyKeys.titlePattern, titlePattern);
    }

    public Locale calculateLocale() {
        if (appropriateLocale == null) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            appropriateLocale = LocaleUtils.resolveLocale(fc, getLocale(), getClientId(fc));
        }
        return appropriateLocale;
    }

    public final String formattedForJs() {
        return format(DATE_FORMAT, ZoneId.of("UTC"));
    }

    public final String formattedForTitle() {
        return format(getTitlePattern(), ZoneId.systemDefault());
    }

    protected String format(final String pattern, final ZoneId zone) {
        final Object value = getValue();
        if (value instanceof Date) {
            return format((Date) value, pattern, zone);
        }
        if (value instanceof ZonedDateTime) {
            return format((ZonedDateTime) value, pattern, zone);
        }
        if (value instanceof LocalDateTime) {
            return format((LocalDateTime) value, pattern, zone);
        }
        throw new IllegalArgumentException("Unsupported type");
    }

    protected String format(final Date value, final String pattern, final ZoneId zone) {
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern, calculateLocale());
        sdf.setTimeZone(TimeZone.getTimeZone(zone));
        return sdf.format(value);
    }

    protected String format(final ZonedDateTime dateTime, final String pattern, final ZoneId zone) {
        return dateTime.withZoneSameInstant(zone)
                    .format(DateTimeFormatter.ofPattern(pattern));
    }

    protected String format(final LocalDateTime dateTime, final String pattern, final ZoneId zone) {
        return format(dateTime.atZone(ZoneId.systemDefault()), pattern, zone);
    }

}
