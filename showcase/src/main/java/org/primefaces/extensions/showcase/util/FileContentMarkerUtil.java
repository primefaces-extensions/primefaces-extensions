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
package org.primefaces.extensions.showcase.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

/**
 * FileContentMarkerUtil
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class FileContentMarkerUtil {

    private static final String LINE_SEPARATOR_WINDOWS = "\r\n";

    private static final FileContentSettings javaFileSettings = new FileContentSettings()
                .setStartMarkers("@Named", "@RequestScoped", "@ViewScoped", "@SessionScoped",
                            "@FacesConverter", " class ", " enum ")
                .setShowLineWithMarker(true);

    private static final FileContentSettings xhtmlFileSettings = new FileContentSettings().setStartMarkers("EXAMPLE_SOURCE_START", "EXAMPLE-SOURCE-START")
                .setEndMarkers("EXAMPLE_SOURCE_END", "EXAMPLE-SOURCE-END").setShowLineWithMarker(false);

    public static String readFileContent(final String fileName, final InputStream is) {
        try {
            if (StringUtils.endsWithIgnoreCase(fileName, ".java")) {
                return readFileContent(is, javaFileSettings);
            }

            if (StringUtils.endsWithIgnoreCase(fileName, ".xhtml")) {
                return readFileContent(is, xhtmlFileSettings);
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
