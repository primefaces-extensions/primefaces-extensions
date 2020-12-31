/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
