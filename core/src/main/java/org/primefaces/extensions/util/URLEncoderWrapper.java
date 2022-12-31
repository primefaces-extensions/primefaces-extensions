/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Wrapper for the {@link URLEncoder} which always use UTF-8.
 *
 * @author Thomas Andraschko / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6.2
 */
public class URLEncoderWrapper {

    /**
     * Prevent instantiation.
     */
    private URLEncoderWrapper() {

    }

    /**
     * Encodes the given string with the {@link URLEncoder#encode(String, String)} and UTF-8.
     *
     * @param string The value which should be encoded.
     * @return The encoded value.
     * @throws UnsupportedEncodingException If UTF-8 is not available.
     */
    public static String encode(final String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8");
    }
}
