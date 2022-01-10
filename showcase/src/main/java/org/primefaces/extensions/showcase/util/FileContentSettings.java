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
        if (startMarkers == null) {
            return new String[0];
        }
        return startMarkers;
    }

    public String[] getEndMarkers() {
        if (endMarkers == null) {
            return new String[0];
        }
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
