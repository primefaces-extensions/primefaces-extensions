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
package org.primefaces.extensions.component.head;

import javax.faces.component.UIOutput;

/**
 * Component class for the <code>ExtHead</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
public class ExtHead extends UIOutput {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Head";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.HeadRenderer";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {

        title, shortcutIcon;

        private String toString;

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        PropertyKeys() {
        }

        @Override
        public String toString() {
            return toString != null ? toString : super.toString();
        }
    }

    public ExtHead() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(final String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    public String getShortcutIcon() {
        return (String) getStateHelper().eval(PropertyKeys.shortcutIcon, null);
    }

    public void setShortcutIcon(final String shortcutIcon) {
        getStateHelper().put(PropertyKeys.shortcutIcon, shortcutIcon);
    }

}
