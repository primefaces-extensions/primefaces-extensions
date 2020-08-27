package org.primefaces.extensions.showcase.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

/**
 * TimeZoneUtil. Taken from http://www.codereye.com/2009/05/getting-time-zone-list-in-java.html
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class TimeZoneUtil {

	private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific)/.*";

	private static List<TimeZone> TIME_ZONES;

	public static List<TimeZone> getTimeZones() {
		if (TIME_ZONES == null) {
			TIME_ZONES = new ArrayList<TimeZone>();

			final String[] timeZoneIds = TimeZone.getAvailableIDs();

			for (final String id : timeZoneIds) {
				if (id.matches(TIMEZONE_ID_PREFIXES)) {
					TIME_ZONES.add(TimeZone.getTimeZone(id));
				}
			}

			Collections.sort(TIME_ZONES, new Comparator<TimeZone>() {

					public int compare(final TimeZone t1, final TimeZone t2) {
						return t1.getID().compareTo(t2.getID());
					}
				});
		}

		return TIME_ZONES;
	}

	public static String getName(TimeZone timeZone) {
		return timeZone.getID().replaceAll("_", " ") + " - " + timeZone.getDisplayName();
	}
}
