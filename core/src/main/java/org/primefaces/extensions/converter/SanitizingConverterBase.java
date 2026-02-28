/*
 * Copyright (c) 2011-2026 PrimeFaces Extensions
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

import org.owasp.html.PolicyFactory;
import org.primefaces.cdk.api.FacesConverterBase;
import org.primefaces.cdk.api.Property;
import org.primefaces.cdk.api.converter.PrimeConverter;

@FacesConverterBase
public abstract class SanitizingConverterBase extends PrimeConverter<Object> {

    /**
     * Default policy blocks all HTML.
     */
    private static final org.owasp.html.PolicyFactory DEFAULT_POLICY = new org.owasp.html.HtmlPolicyBuilder().toFactory();

    @Property(description = "Run input through OWASP HTML Decoder.", defaultValue = "true")
    public abstract boolean isDecodeHtml();

    @Property(description = "An instance of OWASP PolicyFactory which declares how to sanitize the input.", implicitDefaultValue = "strict")
    public abstract PolicyFactory getPolicy();

    public org.owasp.html.PolicyFactory getPolicyWithDefault() {
        if (getPolicy() == null) {
            return DEFAULT_POLICY;
        }
        return getPolicy();
    }

}
