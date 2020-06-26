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
package org.primefaces.extensions.util;

public class ExtLangUtils {

    public static <T> boolean contains(T[] array, T value) {
        for (T entry : array) {
            if (entry == value || value != null && value.equals(entry)) {
                return true;
            }
        }

        return false;
    }


    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    public static int countMatches(String str, char c) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (char current : str.toCharArray()) {
            if (current == c) {
                count++;
            }
        }
        return count;
    }

    public static String defaultString(String str) {
        return str == null ? "" : str;
    }

    public static String defaultString(String str, String def) {
        return str == null ? def : str;
    }

    public static String deleteWhitespace(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }

        if (count == sz) {
            return str;
        }

        return new String(chs, 0, count);
    }

    public static String[] subarray(final String[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new String[0];
        }

        final String[] subarray = new String[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    public static String normalizeSpace(String s) {
        return s.replaceAll("\\s+", " ").trim();
    }
}
