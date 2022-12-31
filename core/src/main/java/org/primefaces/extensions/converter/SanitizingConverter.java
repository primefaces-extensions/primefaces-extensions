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
package org.primefaces.extensions.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.util.Constants;
import org.primefaces.util.LangUtils;

/**
 * {@link Converter} which sanitizes any input using an OWASP Java HTML Sanitizer PolicyFactory. Useful for cleansing input if going to be displayed in
 * outputText with escape="false".
 *
 * @since 10.0.5
 */
@FacesConverter(value = "primefaces.SanitizingConverter")
public class SanitizingConverter implements Converter<Object>, Serializable {

    private static final long serialVersionUID = 20121214L;

    /**
     * Default policy blocks all HTML.
     */
    private static final org.owasp.html.PolicyFactory DEFAULT_POLICY = new org.owasp.html.HtmlPolicyBuilder().toFactory();

    /**
     * Custom policy provided by user.
     */
    private org.owasp.html.PolicyFactory policy;

    /**
     * If true use the OWASP HTML Decode to remove
     */
    private boolean decodeHtml = true;

    /**
     * Method to facilitate "mis-using" this class to sanitize data coming over the network
     *
     * @param value the value to sanitize
     * @return sanitized string
     */
    public String sanitize(final String value) {
        if (LangUtils.isBlank(value)) {
            return value;
        }
        String result = getPolicy().sanitize(value);
        if (isDecodeHtml()) {
            result = org.owasp.html.Encoding.decodeHtml(result, false);
        }
        return result.trim();
    }

    @Override
    public Object getAsObject(final FacesContext fc, final UIComponent uic, final String value) {
        return value == null ? null : sanitize(value);
    }

    @Override
    public String getAsString(final FacesContext fc, final UIComponent uic, final Object o) {
        return o == null ? Constants.EMPTY_STRING : sanitize(o.toString());
    }

    public org.owasp.html.PolicyFactory getPolicy() {
        if (policy == null) {
            policy = DEFAULT_POLICY;
        }
        return policy;
    }

    public void setPolicy(final org.owasp.html.PolicyFactory policy) {
        this.policy = policy;
    }

    public boolean isDecodeHtml() {
        return decodeHtml;
    }

    public void setDecodeHtml(final boolean decodeHtml) {
        this.decodeHtml = decodeHtml;
    }

}
