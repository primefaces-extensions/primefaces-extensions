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
package org.primefaces.extensions.component.switchcase;

/**
 * Component class for the <code>Case</code> component.
 *
 * @author Michael Gmeiner / last modified by $Author: $
 * @version $Revision: $
 * @since 0.6
 */
public class Case extends DefaultCase {

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Case";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    /**
     * Properties that are tracked by state saving.
     *
     * @author Michael Gmeiner / last modified by $Author: $
     * @version $Revision: $
     */
    @SuppressWarnings("java:S115")
    protected enum PropertyKeys {
        value
    }

    public Case() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public void setValue(final Object value) {
        getStateHelper().put(PropertyKeys.value, value);
    }
}
