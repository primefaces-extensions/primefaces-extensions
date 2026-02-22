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
package org.primefaces.extensions.component.timeago;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;

import org.primefaces.cdk.api.FacesComponentInfo;
import org.primefaces.extensions.util.Constants;
import org.primefaces.util.LocaleUtils;

/**
 * <code>TimeAgo</code> component.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 * @since 7.0.1
 */
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = Constants.LIBRARY, name = "primefaces-extensions.js")
@ResourceDependency(library = Constants.LIBRARY, name = "timeago/timeago.js")
@FacesComponent(value = TimeAgo.COMPONENT_TYPE, namespace = TimeAgo.COMPONENT_FAMILY)
@FacesComponentInfo(description = "TimeAgo displays relative time for a given date/time value.")
public class TimeAgo extends TimeAgoBaseImpl {

    public static final String STYLE_CLASS = "ui-timeago ui-widget";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private Locale appropriateLocale;

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
        return format(getTitlePattern(), getValueZoneId());
    }

    protected final ZoneId getValueZoneId() {
        final Object value = getValue();
        if (value instanceof ZonedDateTime) {
            return ((ZonedDateTime) value).getZone();
        }
        if (value instanceof OffsetDateTime) {
            return ((OffsetDateTime) value).getOffset();
        }
        return ZoneId.systemDefault();
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
        if (value instanceof OffsetDateTime) {
            return format((OffsetDateTime) value, pattern, zone);
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

    protected String format(final OffsetDateTime dateTime, final String pattern, final ZoneId zone) {
        return dateTime.atZoneSameInstant(zone)
                    .format(DateTimeFormatter.ofPattern(pattern));
    }

    @Override
    public Object saveState(FacesContext context) {
        // reset component for MyFaces view pooling
        appropriateLocale = null;

        return super.saveState(context);
    }
}
