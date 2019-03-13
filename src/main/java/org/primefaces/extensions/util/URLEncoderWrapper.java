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
     * Encodes the given string with the {@link URLEncoder#encode(String)} and UTF-8.
     *
     * @param string The value which should be encoded.
     * @return The encoded value.
     * @throws UnsupportedEncodingException If UTF-8 is not available.
     */
    public static String encode(final String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8");
    }
}
