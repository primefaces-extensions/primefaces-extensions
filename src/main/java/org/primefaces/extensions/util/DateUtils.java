/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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

package org.primefaces.extensions.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for date / time operations.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7
 */
public class DateUtils {

	// convert from local date to UTC
	public static Date toUtcDate(Calendar calendar, TimeZone localTimeZone, String localTime) {
		if (localTime == null) {
			return null;
		}

		return toUtcDate(calendar, localTimeZone, Long.valueOf(localTime));
	}

	// convert from local date to UTC
	public static Date toUtcDate(Calendar calendar, TimeZone localTimeZone, long localTime) {
		return toUtcDate(calendar, localTimeZone, new Date(localTime));
	}

	// convert from local date to UTC
	public static Date toUtcDate(Calendar calendar, TimeZone localTimeZone, Date localDate) {
		int offsetFromUTC = localTimeZone.getOffset(localDate.getTime()) * (-1);
		calendar.setTime(localDate);
		//calendar.add(Calendar.MILLISECOND, offsetFromUTC);

		return calendar.getTime();
	}

	// convert from UTC to local date
	public static long toLocalDate(Calendar calendar, TimeZone localTimeZone, Date utcDate) {
		int offsetFromUTC = localTimeZone.getOffset(utcDate.getTime());
		calendar.setTime(utcDate);
		//calendar.add(Calendar.MILLISECOND, offsetFromUTC);

		return calendar.getTimeInMillis();
	}
}
