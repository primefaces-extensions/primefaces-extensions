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
package org.primefaces.extensions.showcase.util;

import java.util.*;

/**
 * TimeZoneUtil. Taken from http://www.codereye.com/2009/05/getting-time-zone-list-in-java.html
 *
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class TimeZoneUtil {

    private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";

    private static List<TimeZone> timeZones;

    public static List<TimeZone> getTimeZones() {
        if (timeZones == null) {
            timeZones = new ArrayList<>();

            final String[] timeZoneIds = TimeZone.getAvailableIDs();

            for (final String id : timeZoneIds) {
                if (id.matches(TIMEZONE_ID_PREFIXES)) {
                    timeZones.add(TimeZone.getTimeZone(id));
                }
            }

            Collections.sort(timeZones, new Comparator<TimeZone>() {

                @Override
                public int compare(final TimeZone t1, final TimeZone t2) {
                    return t1.getID().compareTo(t2.getID());
                }
            });
        }

        return timeZones;
    }

    public static String getName(TimeZone timeZone) {
        return timeZone.getID().replaceAll("_", " ") + " - " + timeZone.getDisplayName();
    }
}
