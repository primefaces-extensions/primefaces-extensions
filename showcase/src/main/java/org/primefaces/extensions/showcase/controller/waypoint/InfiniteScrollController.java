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
package org.primefaces.extensions.showcase.controller.waypoint;

import java.io.*;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * InfiniteScrollController
 *
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
@Named
@ViewScoped
public class InfiniteScrollController implements Serializable {

    private static final long serialVersionUID = 20120810L;

    private static final String CONTENT_PATH_DUMMY = "/sections/waypoint/examples/remoteContentDummy.xhtml";
    private static final String CONTENT_PATH_FIRST = "/sections/waypoint/examples/remoteContentOne.xhtml";
    private static final String CONTENT_PATH_SECOND = "/sections/waypoint/examples/remoteContentSecond.xhtml";

    private String src;

    @PostConstruct
    public void initialize() {
        src = CONTENT_PATH_DUMMY;
    }

    public void toggleSrc(final ActionEvent evt) {
        try {
            // simulate a long running request
            Thread.sleep(1500);
        }
        catch (final Exception e) {
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        if (CONTENT_PATH_DUMMY.equals(src) || CONTENT_PATH_SECOND.equals(src)) {
            src = CONTENT_PATH_FIRST;
        }
        else if (CONTENT_PATH_FIRST.equals(src)) {
            src = CONTENT_PATH_SECOND;
        }
    }

    public String getSrc() {
        return src;
    }
}
