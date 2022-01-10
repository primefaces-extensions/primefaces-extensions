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
package org.primefaces.extensions.util;

import java.util.Objects;

public class ExtLangUtils {

    public static final int INDEX_NOT_FOUND = -1;

    private ExtLangUtils() {
        // prevent instantiation
    }

    public static <T> boolean contains(final T[] array, final T value) {
        if (array == null || value == null) {
            return false;
        }

        for (final T entry : array) {
            if (Objects.equals(value, entry)) {
                return true;
            }
        }

        return false;
    }

    public static String lowerCase(final String str) {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    public static int countMatches(final String str, final char c) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (final char current : str.toCharArray()) {
            if (current == c) {
                count++;
            }
        }
        return count;
    }

    public static String defaultString(final String str) {
        return str == null ? "" : str;
    }

    public static String defaultString(final String str, final String def) {
        return str == null ? def : str;
    }

    public static String deleteWhitespace(final String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        final int sz = str.length();
        final char[] chs = new char[sz];
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
            return new String[0];
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

    public static String normalizeSpace(final String s) {
        return s.replaceAll("\\s+", " ").trim();
    }

    public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) {
            return INDEX_NOT_FOUND;
        }
        return seq.toString().indexOf(searchSeq.toString(), startPos);
    }

    public static String unescapeXml(final String text) {
        final int n = text.length();
        final StringBuilder result = new StringBuilder(n);
        int i = 0;
        while (i < n) {
            final char charAt = text.charAt(i);
            if (charAt != '&') {
                result.append(charAt);
                i++;
            }
            else {
                if (text.startsWith("&amp;", i)) {
                    result.append('&');
                    i += 5;
                }
                else if (text.startsWith("&apos;", i)) {
                    result.append('\'');
                    i += 6;
                }
                else if (text.startsWith("&quot;", i)) {
                    result.append('"');
                    i += 6;
                }
                else if (text.startsWith("&lt;", i)) {
                    result.append('<');
                    i += 4;
                }
                else if (text.startsWith("&gt;", i)) {
                    result.append('>');
                    i += 4;
                }
                else {
                    i++;
                }
            }
        }
        return result.toString();
    }
}
