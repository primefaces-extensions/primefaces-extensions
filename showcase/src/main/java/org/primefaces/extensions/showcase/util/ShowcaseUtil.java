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
package org.primefaces.extensions.showcase.util;

import java.io.InputStream;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

/**
 * ShowcaseUtil
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 */
public class ShowcaseUtil {
    public static String getFileContent(final String fullPathToFile) {
        try {
            // Finding in WEB ...
            final FacesContext fc = FacesContext.getCurrentInstance();
            InputStream is = fc.getExternalContext().getResourceAsStream(fullPathToFile);
            if (is != null) {
                return FileContentMarkerUtil.readFileContent(fullPathToFile, is);
            }

            // Finding in ClassPath ...
            is = ShowcaseUtil.class.getResourceAsStream(fullPathToFile);
            if (is != null) {
                return FileContentMarkerUtil.readFileContent(fullPathToFile, is);
            }
        }
        catch (final Exception e) {
            throw new IllegalStateException("Internal error: file " + fullPathToFile + " could not be read", e);
        }

        return StringUtils.EMPTY;
    }
}
