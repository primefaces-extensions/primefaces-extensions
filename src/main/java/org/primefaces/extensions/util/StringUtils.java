/*
 * Copyright 2011 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.util;

/**
 * Common <code>String</code> manipulation routines.
 *
 * @author  Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public class StringUtils {

	/**
	 * <p>Checks if a String is whitespace, empty ("") or null.</p>
	 *
	 * <pre>
	   StringUtils.isBlank(null)      = true
	   StringUtils.isBlank("")        = true
	   StringUtils.isBlank(" ")       = true
	   StringUtils.isBlank("bob")     = false
	   StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param  str the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}

		for (int i = 0; i < strLen; i++) {
			if (!(Character.isWhitespace(str.charAt(i)))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
	 *
	 * <pre>
	   StringUtils.isNotBlank(null)      = false
	   StringUtils.isNotBlank("")        = false
	   StringUtils.isNotBlank(" ")       = false
	   StringUtils.isNotBlank("bob")     = true
	   StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param  str the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and not whitespace
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}
}
