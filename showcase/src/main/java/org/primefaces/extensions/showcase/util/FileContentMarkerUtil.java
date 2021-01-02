/*
 * Copyright (c) 2011-2021 PrimeFaces Extensions
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

import java.io.*;
import java.nio.charset.*;

import org.apache.commons.lang3.*;

/**
 * FileContentMarkerUtil
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class FileContentMarkerUtil {

    private static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    private static final FileContentSettings JAVA_SETTINGS = new FileContentSettings()
                .setStartMarkers("@Named", "@RequestScoped", "@ViewScoped", "@SessionScoped",
                            "@FacesConverter", " class ", " enum ")
                .setShowLineWithMarker(true);

    private static final FileContentSettings XHTML_SETTINGS = new FileContentSettings().setStartMarkers("EXAMPLE_SOURCE_START", "EXAMPLE-SOURCE-START")
                .setEndMarkers("EXAMPLE_SOURCE_END", "EXAMPLE-SOURCE-END").setShowLineWithMarker(false);

    public static String readFileContent(final String fileName, final InputStream is) {
        try {
            if (StringUtils.endsWithIgnoreCase(fileName, ".java")) {
                return readFileContent(is, JAVA_SETTINGS);
            }

            if (StringUtils.endsWithIgnoreCase(fileName, ".xhtml")) {
                return readFileContent(is, XHTML_SETTINGS);
            }

            // Show all files
            return readFileContent(is, new FileContentSettings());
        }
        catch (final Exception e) {
            throw new IllegalStateException("Internal error: file " + fileName + " could not be read", e);
        }
    }

    private static String readFileContent(final InputStream inputStream, final FileContentSettings settings) throws IOException {
        if (inputStream == null) {
            return null;
        }

        final StringBuilder sbBeforeStartMarker = new StringBuilder(5000);
        final StringBuilder sbBeforeEndMarker = new StringBuilder(5000);
        String markerLineStart = null;
        String markerLineEnd = null;

        final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;

        StringBuilder sb = sbBeforeStartMarker;
        while ((line = br.readLine()) != null) {
            // if is before first start marker
            if (markerLineStart == null && containMarker(line, settings.getStartMarkers())) {
                markerLineStart = LINE_SEPARATOR_WINDOWS + line;
                sb = sbBeforeEndMarker;

                continue;
            }

            // if is before first end marker
            if (containMarker(line, settings.getEndMarkers())) {
                markerLineEnd = LINE_SEPARATOR_WINDOWS + line;

                break; // other content file is ignored
            }

            sb.append(LINE_SEPARATOR_WINDOWS);
            sb.append(line);
        }

        // if both (START and END) markers are in file
        if (markerLineStart != null && markerLineEnd != null) {
            if (settings.isShowLineWithMarker()) {
                sbBeforeEndMarker.append(markerLineEnd);
                sbBeforeEndMarker.insert(0, markerLineStart);
            }

            return sbBeforeEndMarker.toString().trim();
        }

        // if only START marker is in file
        if (markerLineStart != null) {
            if (settings.isShowLineWithMarker()) {
                sbBeforeEndMarker.insert(0, markerLineStart);
            }

            return sbBeforeEndMarker.toString().trim();
        }

        // if only END marker is in file
        if (settings.isShowLineWithMarker()) {
            sbBeforeStartMarker.append(markerLineEnd);
        }

        return sbBeforeStartMarker.toString().trim();
    }

    private static boolean containMarker(final String line, final String[] markers) {
        for (final String marker : markers) {
            if (StringUtils.isEmpty(marker)) {
                continue;
            }

            if (StringUtils.contains(line, marker)) {
                return true;
            }
        }

        return false;
    }
}
