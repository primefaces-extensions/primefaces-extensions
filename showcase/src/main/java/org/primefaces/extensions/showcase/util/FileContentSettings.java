/*
 * Copyright 2011-2015 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.util;

/**
 * FileContentSettings
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class FileContentSettings {
    private String[] startMarkers = null;
    private String[] endMarkers = null;
    private boolean showLineWithMarker = false;

    public String[] getStartMarkers() {
        if (startMarkers == null) return new String[0];
        return startMarkers;
    }

    public String[] getEndMarkers() {
        if (endMarkers == null) return new String[0];
        return endMarkers;
    }

    public boolean isShowLineWithMarker() {
        return showLineWithMarker;
    }

    public FileContentSettings setStartMarkers(String... startMarkers) {
        this.startMarkers = startMarkers;
        return this;
    }

    public FileContentSettings setEndMarkers(String... endMarkers) {
        this.endMarkers = endMarkers;
        return this;
    }

    public FileContentSettings setShowLineWithMarker(boolean showLineWithMarker) {
        this.showLineWithMarker = showLineWithMarker;
        return this;
    }
}
